(ns example3.plugin
  ;; Create the 'main' plugin class. See plugin.yml
  (:gen-class
   :name example3.Example3Plugin
   :extends bukkitclj.ClojurePlugin))

(defn -onEnable [this]
  (.info this "Example3 IS AWESOME!!!!")
  (.info this
         (str "config.option: "
              (.. this (getConfig) (getString "config.option"))))
(.info this
       (str "config.example3: "
            (.. this (getConfig) (getString "config.example3")))))

(defn -onDisable [this]
  (.info this "Example1 disabled"))
