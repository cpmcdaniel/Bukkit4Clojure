(defproject org.kowboy/bukkit-for-clojure "1.0.4"
  :description "A Clojure wrapper for Bukkit."
  :url "https://github.com/cpmcdaniel/Bukkit4Clojure"
  :license {:name "MIT License"
            :url "https://github.com/cpmcdaniel/Bukkit4Clojure/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [nrepl/nrepl "0.8.2"]]
  :repositories {"spigot-repo" "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"}
  :java-source-paths ["src"]
  :source-paths ["src"]
  :target-path "target/%s"
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options"]
  :pom-plugins [[org.apache.maven.plugins/maven-compiler-plugin "3.7.0"
                 {:configuration ([:source "1.8"] [:target "1.8"])}]]
  :uberjar-name "bukkit-for-clojure.jar"
  :aot :all
  :profiles {:provided {:dependencies [[org.spigotmc/spigot-api "1.16.3-R0.1-SNAPSHOT"]]}}
  )
