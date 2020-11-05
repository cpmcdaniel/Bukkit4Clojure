(ns bukkitclj.logging)

(defmacro log [plugin level fmt & args]
  (let [logger (gensym)]
    `(let [~(with-meta logger {:tag "java.util.logging.Logger"}) 
           (.getLogger ~(with-meta plugin {:tag "org.bukkit.plugin.Plugin"}))]
       (when (.isLoggable ~logger ~level)
         (.log ~logger ~level
               ~(if (seq args)
                  `(format ~fmt ~@args)
                  fmt))))))

(defmacro info [plugin fmt & args]
  `(log ~plugin java.util.logging.Level/INFO ~fmt ~@args))

(defmacro warning [plugin fmt & args]
  `(log ~plugin java.util.logging.Level/WARNING ~fmt ~@args))

(defmacro severe [plugin fmt & args]
  `(log ~plugin java.util.logging.Level/SEVERE ~fmt ~@args))

(comment
  ;; Are we ever gonna need to log through Bukkit.getLogger()?
  
  (macroexpand '(info p "%s:%s" "one" "two"))
  (macroexpand '(info p "noargs"))
  (macroexpand '(severe p "ERROR!"))
  
  (let [plugin (deref bukkitclj.repl/plugin-ref)]
    (info plugin "This is info message")
    (warning plugin "This is warn message")
    (severe plugin "Error: %s!!!" "some error value")))

