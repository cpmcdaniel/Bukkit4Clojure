(defproject bukkitclj/bukkit-for-clojure "1.0.8"
  :description "A Clojure wrapper for Bukkit."
  :url "https://github.com/cpmcdaniel/Bukkit4Clojure"
  :license {:name "MIT License"
            :url "https://github.com/cpmcdaniel/Bukkit4Clojure/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [nrepl/nrepl "0.8.2"]
                 [org.reflections/reflections "0.9.12"]
                 [camel-snake-kebab/camel-snake-kebab "0.4.2"]]
  :repositories {"spigot-repo" "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
                 "releases" {:url "https://repo.clojars.org"
                             :creds :gpg}}
  :java-source-paths ["src/java"]
  :source-paths ["src/clojure"]
  :target-path "target/%s"
  :javac-options ["-target" "1.8" "-source" "1.8" "-Xlint:-options"]
  :pom-plugins [[org.apache.maven.plugins/maven-compiler-plugin "3.7.0"
                 {:configuration ([:source "1.8"] [:target "1.8"])}]]
  :uberjar-name "Bukkit4Clojure.jar"
  :profiles {:provided {:dependencies [[org.spigotmc/spigot-api "1.16.3-R0.1-SNAPSHOT"]]}
             :uberjar {:aot :all}}
  )
