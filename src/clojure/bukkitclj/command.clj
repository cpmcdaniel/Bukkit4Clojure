(ns bukkitclj.command

  (:import [org.bukkit.plugin Plugin]
           [org.bukkit.command TabExecutor]))

(defn register-command
  "Registers a command function and a tab completer function
  with a given command."
  [^Plugin plugin
   ^String command-name
   handler-fn
   completer-fn] ;; can be nil if no tab-completion

  (let [executor
        (proxy [TabExecutor] []
          (onCommand [sender cmd alias args]
            (apply handler-fn sender cmd alias args))
          (onTabComplete [sender cmd alias args]
            (apply completer-fn sender cmd alias args)))]
    (doto (.getCommand plugin command-name)
      (.setExecutor executor)
      (.setTabCompleter executor))))

(comment
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
