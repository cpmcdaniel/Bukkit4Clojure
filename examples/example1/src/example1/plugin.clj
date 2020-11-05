(ns example1.plugin
  ;; Create the 'main' plugin class. See plugin.yml
  (:gen-class
   :name example1.Example1Plugin
   :extends bukkitclj.ClojurePlugin))

(defn -onEnable [this]
  (.info this "Example1 IS AWESOME!!!!")
  (.info this
         (str "config.option: "
              (.. this (getConfig) (getString "config.option"))))
  (.info this 
         (str "config.example1: "
              (.. this (getConfig) (getString "config.example1")))))

(defn -onDisable [this]
  (.info this "Example1 disabled"))
