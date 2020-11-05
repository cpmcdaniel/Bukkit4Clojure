(ns bukkitclj.task
  (:import [org.bukkit.scheduler BukkitRunnable BukkitTask]
           [org.bukkit.plugin Plugin]))

(defn bukkit-runnable
  "Creates a BukkitRunnable instance that calls the 
   given clojure function. Takes an optional callback
   fuction that gets called when the task is cancelled."
  ^BukkitRunnable [f]
  (proxy [BukkitRunnable] []
    (run [] (f this))))

(defn task
  "Shedule a given no-args fn on the main UI thread"
  (^BukkitTask
   [^Plugin plugin f {:keys [async?]}]
   (if async?
     (.runTaskAsynchronously (bukkit-runnable f) plugin)
     (.runTask (bukkit-runnable f) plugin)))
  (^BukkitTask
   [^Plugin plugin f]
   (task plugin f nil)))

(defn delayed-task
  "Schedule a given function on the main UI thread after a delay in 
   server ticks (1 tick = 1/20 second), will return a task object you 
   can use to cancel the task - if you specify async?, 
   take care not to directly call any Bukkit API and, by extension, 
   any bukkitclj functions that use the Bukkit API within this function."
  (^BukkitTask
   [^Plugin plugin f delay {:keys [async?]}]
   (if async?
     (.runTaskLaterAsynchronously (bukkit-runnable f) plugin delay)
     (.runTaskLater (bukkit-runnable f) plugin delay)))
  (^BukkitTask
   [^Plugin plugin f delay]
   (delayed-task plugin f delay nil)))

(defn repeating-task
  "Schedule a repeating task that calls the given clojure function
   repeatedly until cancelled."
  (^BukkitTask
   [^Plugin plugin f delay period {:keys [async?]}]
   (if async?
     (.runTaskTimerAsynchronously
      (bukkit-runnable f) plugin delay period)
     (.runTaskTimer (bukkit-runnable f)
                    plugin delay period)))
  (^BukkitTask
   [^Plugin plugin f delay period]
   (repeating-task plugin f delay period nil)))

(comment
  (set! *warn-on-reflection* true)
  (let [^bukkitclj.ClojurePlugin plugin (deref bukkitclj.repl/plugin-ref)
        hits (atom 0)
        t (repeating-task plugin
                          (fn [^BukkitRunnable this]
                            (swap! hits inc)
                            (.info plugin (str "Hits: " @hits))
                            (when (<= 5 @hits)
                              (.cancel this)))
                          10 10)]
    (delayed-task plugin (fn [_] (when-not (.isCancelled t) (.cancel t))) 60)))
  