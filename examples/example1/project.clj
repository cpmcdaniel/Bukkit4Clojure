(defproject org.kowboy/clojure-example1 "1.0"
  :description "An example Clojure plugin."
  :url "https://github.com/cpmcdaniel/Bukkit4Clojure"
  :license {:name "MIT License"
            :url "https://github.com/cpmcdaniel/Bukkit4Clojure/blob/master/LICENSE"}
  :dependencies []
  :repositories {"spigot-repo" "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"}
  :source-paths ["src"]
  :resource-paths ["resources"]
  :target-path "target/%s"
  :aot :all
  :profiles {:provided {:dependencies [[org.clojure/clojure "1.10.1"]
                                       [bukkitclj/bukkit-for-clojure "1.0.6"]
                                       [org.spigotmc/spigot-api "1.16.3-R0.1-SNAPSHOT"]]}})
