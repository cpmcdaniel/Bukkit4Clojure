(ns example1.plugin
   (:import [org.kowboy.bukkit ClojurePlugin]))

(defn on-enable [^ClojurePlugin plugin]
  (.info (.getLogger plugin) "example1 enabled"))

(defn on-disable [^ClojurePlugin plugin]
  (.info (.getLogger plugin) "example1 disabled"))

;; Create the 'main' plugin class. See plugin.yml
(gen-class
 :name "example1.Example1Plugin"
 :extends org.kowboy.bukkit.AbstractClojurePlugin
 :prefix "plugin-")

(defn plugin-onEnable [this]
  (.. this (getLogger) (info "Example1 IS AWESOME!!!!")))

(defn plugin-onDisable [this]
  (on-disable this))