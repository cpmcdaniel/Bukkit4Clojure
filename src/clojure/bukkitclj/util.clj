(ns bukkitclj.util
  "Utility functions that don't have a natural home in any other namespace.
   
   This code is heavily borrowed from the cljminecraft project:
   https://github.com/CmdrDats/clj-minecraft
   ")

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
  (find-subclasses "org.bukkit.event" org.bukkit.event.Event))