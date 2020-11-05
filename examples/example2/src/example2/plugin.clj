(ns example2.plugin
  (:require [example1.foo])
  (:import [bukkitclj ClojurePlugin]))

(defn on-enable [^ClojurePlugin this]
  (.info this "example2 enabled")
  (.info this (str "foo" example1.foo/foo)))

(defn on-disable [^ClojurePlugin this]
  (.info this "example2 disabled"))

(gen-class
 :name "example2.Example2Plugin"
 :extends bukkitclj.ClojurePlugin
 :prefix "plugin-")

(defn plugin-onEnable [this]
  (.info this "Example2 IS AWESOME!!!!"))

(defn plugin-onDisable [this]
  (on-disable this))