(ns bukkitclj.event
  "Event handlers for Bukkit"
  (:require [bukkitclj.util :as util]
            [bukkitclj.logging :as log]
            [bukkitclj.bukkit :as bk]
            [clojure.string :as st])
  (:import  [org.bukkit.plugin Plugin]))

(defonce actions (util/map-enums 'action org.bukkit.event.block.Action))
(defonce priorities (util/map-enums 'priority org.bukkit.event.EventPriority))

(def event-package "org.bukkit.event")

(defn class->event-key [^Class cls]
  (let [segments (-> cls
                     util/class->kebab-case
                     (subs (inc (count event-package)))
                     (.replaceAll "-event$" "")
                     (.split "\\."))
        keyword-ns (st/join "." (butlast segments))]
    (keyword keyword-ns (last segments))))

(defn event-key->class [event-key]
  (util/kebab-case->class
   (str event-package
        "."
        (namespace event-key)
        "."
        (name event-key)
        "-event")))

(defonce events
  (set (map class->event-key
            (util/find-subclasses org.bukkit.event.Event event-package))))

(defn register-event
  ([plugin event-key handler-fn]
   (register-event plugin event-key handler-fn :priority/normal false))
  ([plugin event-key handler-fn priority-key]
   (register-event plugin event-key handler-fn priority-key false))
  ([^Plugin plugin
    event-key          ;; keyword, like :player/player-toggle-sneak
    handler-fn         ;; (fn [event] ...)
    priority-key       ;; :priority/normal, :priority/high, etc...
    ignore-cancelled?] ;; defaults to false
   (if-let [^Class event-class (event-key->class event-key)]
     (.registerEvent
      (bk/plugin-manager)
      event-class
      (proxy [org.bukkit.event.Listener] [])
      (get priorities priority-key :priority/normal)
      (proxy [org.bukkit.plugin.EventExecutor] []
        (execute [_ e]
          (handler-fn e)))
      plugin
      (boolean ignore-cancelled?))
     (log/warning plugin "Unrecognized event-key %s. Ignoring event registration." event-key))))

;; Discovery methods

(defn find-event [^String search]
  (filter #(.contains (str %) (.toLowerCase search)) events))

(def boring-methods #{"getClass"
                      "notify"
                      "notifyAll"
                      "isAsynchronous"
                      "toString"
                      "hashCode"
                      "equals"
                      "wait"
                      "callEvent"
                      "getEventName"
                      "getHandlers"
                      "getHandlerList"})

(defn describe-event [event-key]
  (let [cls (event-key->class event-key)]
    (set (remove boring-methods
                 (map #(.getName %) (seq (.getMethods cls)))))))

(comment
  (take 6 events)
  (find-event "player-toggle")
  (describe-event :player/player-toggle-sneak)
  ;; use a reference to your own plugin instance here...
  (let [plugin (deref bukkitclj.repl/plugin-ref)] 
    (register-event plugin
                    :player/player-toggle-sneak
                    (fn [e] (println "event triggered!"))
                    :priority/normal)))

