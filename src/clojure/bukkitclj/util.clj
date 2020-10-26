(ns bukkitclj.util
  "Utility functions that don't have a natural home in any other namespace.

   This code is heavily borrowed from the cljminecraft project:
   https://github.com/CmdrDats/clj-minecraft
   "
  (:require [camel-snake-kebab.core :as csk]
            [clojure.string :as st])
  (:import  [org.reflections Reflections]
            [org.reflections.util ConfigurationBuilder FilterBuilder]
            [org.reflections.scanners Scanner SubTypesScanner MethodParameterScanner]))

(defmacro map-enums
  "Transforms an Enum class into a map. Keywords are converted to :kebab-case
   and given a namespace prefix (can be nil)."
  [ns-sym enum-class]
  `(into {} (for [enum-val# (~(symbol (apply str (.getName enum-class) "/values")))]
              [(keyword (when ~ns-sym (name ~ns-sym))
                        (csk/->kebab-case (.name enum-val#)))
               enum-val#])))

(defn class->kebab-case
  "Takes a class and returns the fully-qualified name, but with each segment
   converted from CamelCase to kebab-case.
   org.bukkit.plugin.JavaPlugin -> 'org.bukkit.plugin.java-plugin'"
  ^String
  [^Class cls]
  (let [package-name (.. cls (getPackage) (getName))]
    (str package-name "." (csk/->kebab-case (.getSimpleName cls)))))

(defn kebab-case->class
  "Does the reverse of the above fn.
   'org.bukkit.plugin.java-plugin' -> org.bukkit.plugin.JavaPlugin"
  ^Class
  [^String class-name]
  (let [segments (seq (.split class-name "\\."))
        simple-name (csk/->PascalCase (last segments))
        package-name (st/join "." (butlast segments))]
    (resolve (symbol (str package-name "." simple-name)))))

(defn reflector []
  (Reflections.
   (into-array java.lang.Object
               ["org.bukkit"
                (.getClassLoader org.bukkit.Bukkit)
                (MethodParameterScanner.)
                (SubTypesScanner.)])))

(defn find-has-player-classes
  "Use Reflections library to find all bukkit classes with a method called
   getPlayer() that returns org.bukkit.entity.Player and has 0 parameters."
  []
  (->> (.getMethodsReturn (reflector) org.bukkit.entity.Player)
       (seq)
       (remove nil?)
       (filter (fn [^java.lang.reflect.Method m]
                 (and (= "getPlayer" (.getName m))
                      (empty? (.getParameterTypes m)))))
       (map #(.getDeclaringClass ^java.lang.reflect.Method %))
       (filter #(.startsWith (.getName ^java.lang.Class %) "org.bukkit.event"))
       (into #{})))

(defn find-has-command-sender
  "Use Reflections library to find all bukkit classes with a nargs
  getCommandSender() function."
  []
  (->> (.getMethodsReturn (reflector) org.bukkit.command.CommandSender)
       (seq)
       (remove nil?)
       (filter #(= "getSender" (.getName %)))
       (map #(.getDeclaringClass ^java.lang.reflect.Method %))
       (into #{})))


(defn find-subclasses
  "Use Reflections library to find all subclasses of a given class in a
   given package. This is useful, say, for getting all Bukkit Event types."
  [^Class class ^String package-name]
  (->> (.getSubTypesOf (reflector) class)
       seq
       (remove nil?)
       (filter #(.startsWith (.getName ^Class %) package-name))))

(comment
  (name org.bukkit.Material)
  (take 5 (map-enums 'priority org.bukkit.event.EventPriority))

  (find-has-player-classes)

  (doseq [cls (find-has-command-sender)]
    (clojure.pprint/pprint cls))

  (.getMethodsReturn (reflector) org.bukkit.entity.Player)
  (->> (find-subclasses org.bukkit.event.Event "org.bukkit.event")
       (filter #(.contains (.toLowerCase (.getName %)) "block"))))
