(ns bukkitclj.bukkit
  "Idiomatic Clojure wrappers around org.bukkit.Bukkit 
   functions and other utility functions specific to 
   Bukkit APIs. More general non-Bukkit-related utilities
   should go in the `bukkitclj.util` namespace.
   
   Many of these are macros for the efficiency of being inlined.
   No-args calls are the least likely to be used in higher-order
   functions."
  (:import [org.bukkit Bukkit World]))

(defmacro server []
  `(Bukkit/getServer))

(defmacro plugin-manager []
  `(.getPluginManager (server)))

(defmacro scheduler []
  `(.getScheduler (server)))

(defmacro services-manager []
  `(.getServicesManager (server)))

(defmacro worlds []
  `(.getWorlds (server)))

(defmacro online-players []
  `(seq (.getOnlinePlayers (server))))

(defmacro broadcast [fmt & args]
  `(.broadcastMessage (server) (apply format ~fmt '~args)))

(defmacro worlds-by-name []
  `(into {} (map (fn [^World w#] [(.getName w#) w#])
              (worlds))))

(defmacro world-by-name [name]
  `(.getWorld (server) ~name))

(defn seconds-to-ticks [^Number s]
  (int (* 20 s)))



(comment
  (broadcast "Foo: %s, %s" "hello" "world")
  (worlds-by-name)
  (seconds-to-ticks 5)
  
  (macroexpand '(worlds-by-name)))