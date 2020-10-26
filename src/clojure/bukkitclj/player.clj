(ns bukkitclj.player
  (:require [bukkitclj.bukkit :as bk]
            [bukkitclj.util :as util])
  (:import  [java.util UUID]))

(defprotocol HasPlayer
  (get-player [this]))

(extend-protocol HasPlayer
  nil
  (get-player [this] nil)
  Object
  (get-player [this] nil)
  String
  (get-player [this] (bk/get-player this)))

