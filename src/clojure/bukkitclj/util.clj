(ns bukkitclj.util
  "Utility functions that don't have a natural home in any other namespace.
   
   This code is heavily borrowed from the cljminecraft project:
   https://github.com/CmdrDats/clj-minecraft
   "
  (:require [camel-snake-kebab.core :as csk]))

(defmacro map-enums
  "Transforms an Enum class into a map. Keywords are converted to :kebab-case
   and given a namespace prefix (can be nil)."
  [ns-sym enum-class]
  `(into {} (for [enum-val# (~(symbol (apply str (.getName enum-class) "/values")))]
              [(keyword (when ~ns-sym (name ~ns-sym))
                        (csk/->kebab-case (.name enum-val#))) 
               enum-val#])))

(defn find-subclasses
  "Use Reflections library to find all subclasses of a given class in a 
   given package. This is useful, say, for getting all Bukkit Event types."
  [package-name class]
  (remove nil?
          (seq (.getSubTypesOf
                (org.reflections.Reflections.
                 (into-array java.lang.Object 
                             [package-name
                              (.getClassLoader class)]))
                class))))

(comment 
  (name org.bukkit.Material)
  (take 5 (map-enums 'priority org.bukkit.event.EventPriority))
  (find-subclasses "org.bukkit.event" org.bukkit.event.Event))