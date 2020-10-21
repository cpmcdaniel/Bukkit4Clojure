(ns bukkitclj.repl
  "Provides a REPL that can be used to interact with the 
   running Bukkit server. The port defaults to 7071, but
   can be set explicitly in the `repl.port` setting in 
   config.yml"
  (:require [nrepl.server :refer [start-server stop-server]])
  (:import [bukkitclj ClojurePlugin]
           [org.bukkit.configuration Configuration]
           [org.bukkit.command TabExecutor]))

(def default-host "127.0.0.1")

(def default-port 7071)

(defonce nrepl-ref (atom nil))

(defonce ^ClojurePlugin plugin-ref (atom nil))

(def sub-commands #{"start" "stop"})

(defn start! 
  "Starts an nREPL service on the specified port, if one is not already
   running. Returns true when a service was started, false otherwise."
  [^ClojurePlugin plugin host port]
  (if-let [nrepl @nrepl-ref]
    (do (.info plugin (format "nREPL is already running on port %s" (:port nrepl)))
        false)
    (do (reset! nrepl-ref (start-server :bind host :port port))
        (.info plugin (format "nREPL running at %s:%d" host port))
        true)))

(defn stop! 
  "Stops the running nREPL service. Silently does nothing
   if the service is not running."
  [^ClojurePlugin plugin]
  (when-let [nrepl @nrepl-ref] 
    (.info plugin "Stopping nREPL")
    (stop-server nrepl)
    (reset! nrepl-ref nil)))

(defn repl-start
  "Implements the `/repl start [port]` command."
  [^ClojurePlugin plugin [_ port-arg]]
  (try
    (let [^Configuration config (.getConfig plugin)
          port (or (and port-arg (Integer/parseInt port-arg))
                   (.getInt config "repl.port" default-port))
          host (.getString config "repl.host" default-host)]
      (when (start! plugin host port)
                ;; Save the port number we are using to the config
                ;; so when the server restarts it will keep this port number.
        (.set config "repl.port" port)
                ;; Make sure nREPL is enabled in case of restart.
        (.set config "repl.enabled" true)
        (.saveConfig plugin))
      true)
    (catch NumberFormatException _
      false)))

(defn repl-stop
  "Implements the `/repl stop` command."
  [^ClojurePlugin plugin]
  (let [^Configuration config (.getConfig plugin)]
    (stop! plugin)
    ;; Remember that nREPL is disabled after server restart.
    (.set config "repl.enabled" false)
    (.saveConfig plugin)
    true))

(defn repl-executor
  [^ClojurePlugin plugin]
  (reify TabExecutor
    (onCommand
      [this sender command label args]
      (let [sub-command (first args)]
        (cond
          ;; START
          (= "start" sub-command)
          (repl-start plugin args)

          ;; STOP
          (= "stop" sub-command)
          (repl-stop plugin)

          ;; UNKNOWN sub-command
          :else false)))

    (onTabComplete
      [this sender command alias args]
      (when (= 1 (count args))
        (let [[partial-arg] args]
          (filter #(.startsWith % partial-arg) sub-commands))))))

(defn on-enable [^ClojurePlugin plugin]
  (reset! plugin-ref plugin)
  
  ;; Write out initial config.yml
  ;; Does not overwrite if it already exists!
  (.saveDefaultConfig plugin)
  
  (let [^Configuration config (.getConfig plugin)
        host (.getString config "repl.host" default-host)
        port (.getInt config "repl.port" default-port)
        enabled? (.getBoolean config "repl.enabled" false)
        executor (repl-executor plugin)]
    (.registerCommand plugin "repl" executor)
    (.registerTabCompleter plugin "repl" executor)
    (when enabled?
      (start! plugin host port))))

(defn on-disable [^ClojurePlugin plugin]
  (.saveConfig plugin)
  (stop! plugin)
  (reset! plugin-ref nil))


(comment
   )
  