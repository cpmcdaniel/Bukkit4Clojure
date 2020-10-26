(ns bukkitclj.repl
  "Provides a REPL that can be used to interact with the
   running Bukkit server. The port defaults to 7071, but
   can be set explicitly in the `repl.port` setting in
   config.yml"
  (:require [nrepl.server :refer [start-server stop-server]]
            [bukkitclj.logging :as log]
            [bukkitclj.command :as cmd])
  (:import [bukkitclj ClojurePlugin]
           [org.bukkit.configuration Configuration]))

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
    (do (log/warning plugin "nREPL is already running on port %s" (:port nrepl))
        false)
    (do (reset! nrepl-ref (start-server :bind host :port port))
        (log/info plugin "nREPL running at %s:%d" host port)
        true)))

(defn stop!
  "Stops the running nREPL service. Silently does nothing
   if the service is not running."
  [^ClojurePlugin plugin]
  (when-let [nrepl @nrepl-ref]
    (log/info plugin "Stopping nREPL")
    (stop-server nrepl)
    (reset! nrepl-ref nil)))

(defn repl-start
  "Implements the `/repl start [port]` command."
  [^ClojurePlugin plugin port]
  (try
    (let [^Configuration config (.getConfig plugin)
          port (or (and port (Integer/parseInt port))
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

(defn repl-command
  [plugin]
  (fn [sender command label sub-command & [port]]
    (cond
      ;; START
      (= "start" sub-command)
      (repl-start plugin port)

      ;; STOP
      (= "stop" sub-command)
      (repl-stop plugin)

      ;; UNKNOWN sub-command
      :else false)))

(defn on-tab-complete
  [this sender command alias & args]
  (when (= 1 (count args))
    (let [[partial-arg] args]
      (filter #(.startsWith % partial-arg) sub-commands))))

(defn on-enable [^ClojurePlugin plugin]
  (reset! plugin-ref plugin)

  ;; Write out initial config.yml
  ;; Does not overwrite if it already exists!
  (.saveDefaultConfig plugin)

  (let [^Configuration config (.getConfig plugin)
        host (.getString config "repl.host" default-host)
        port (.getInt config "repl.port" default-port)
        enabled? (.getBoolean config "repl.enabled" false)]
    (cmd/register-command plugin "repl" (repl-command plugin) on-tab-complete)
    (when enabled?
      (start! plugin host port))))

(defn on-disable [^ClojurePlugin plugin]
  (.saveConfig plugin)
  (stop! plugin)
  (reset! plugin-ref nil))


(comment
   )
