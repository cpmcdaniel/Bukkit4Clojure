(ns bukkitclj.command
  (:import [org.bukkit.plugin.java JavaPlugin]
           [org.bukkit.command TabExecutor]))

(defn register-command
  "Registers a command function and a tab completer function
  with a given command."
  ([^JavaPlugin plugin
    ^String command-name
    handler-fn
    completer-fn]
   (let [executor
         (proxy [TabExecutor] []
           (onCommand [sender cmd alias args]
             (handler-fn sender cmd alias args))
           (onTabComplete [sender cmd alias args]
             ;; completer-fn can be nil if no tab completion
             (if completer-fn
               (completer-fn sender cmd alias args)
               [])))]
     (doto (.getCommand plugin command-name)
       (.setExecutor executor)
       (.setTabCompleter executor))))
  ([^JavaPlugin plugin
    ^String command-name
    handler-fn]
   (register-command plugin command-name handler-fn nil)))

(comment
  (cmd/register-command 
   plugin
   "repl"
   (fn [sender cmd alias args])
     ;; Do command stuff here...

   (fn [sender cmd alias args]
     ;; Do tab completion here
     (when (= 1 (count args))
       (filter #(.startsWith % (first args)) #{"on" "off"}))))
  
  ;; TODO build a higher-level command registration function that
  ;;      provides convenience for parameter validation, conversion, and
  ;;      tab completion options.
  {:name "param-name"
   ;; :string, :player, :material, :long, :double, :keyword, :event, :entity...
   :type :int
   ;; If specified, overrides the default coercion.
   :coerce coerce-fn
   ;; Can be a collection or a function. If specified, overrides default
   ;; options. For example, if the arg is a :player type, you might want to
   ;; limit to online players only.
   :options []
   ;; If set to true, this param is optional. All subsequent params must also
   ;; be optional or command registration will fail.
   :optional? true})
