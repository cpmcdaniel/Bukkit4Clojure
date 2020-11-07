# Bukkit4Clojure

This wrapper is for any developer who wishes to develop [Bukkit](https://bukkit.org/) 
plugins using the LISP-based language Clojure.

## Installation

Place the Bukkit4Clojure.jar file in your server plugins directory.

## Usage

#### Start a REPL

Run this from your server console:

`/repl start [port]`

The default port, if left unspecified, is 7071.

#### Stop the REPL

`/repl stop`

The plugin will remember whatever state you left the repl in (running or stopped). If 
it was running when the server was stopped, it will be started again when the Bukkit server
starts. By default, the REPL is disabled until you start it. 

See config.yml in your plugins directory. There you can change the port and even the host/IP
where the REPL server will listen.

## Building your own Clojure Plugin

Just doing the above will be enough to give you a REPL where you can experiment interactively
with the Bukkit API. If you wish to create your own plugin using Clojure, the general steps are...

1. Create a leiningen project.
2. Specify dependencies, including Bukkit4Clojure.
3. Code your plugin by extending bukkitclj.ClojurePlugin via gen-class.
4. Configure plugin.yml
5. Create an uberjar
6. Deploy your uberjar in server plugins along side Bukkit4Clojure.

#### Project Setup/Configuration

```clojure
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
                                       [bukkitclj/bukkit-for-clojure "1.0.5"]
                                       [org.spigotmc/spigot-api "1.16.3-R0.1-SNAPSHOT"]]}})

```

A basic project.clj file should look like this. The `:provided` profile is used for these 3 dependencies
because we do not need them included in the uberjar. These are provided by the Bukkit4Clojure.jar plugin
that must also be installed on your server. This is one of the benefits of using Bukkit4Clojure vs other
Clojure plugin projects out there - smaller jar files because of the shared Clojure runtime and library.

If you wish to add other third-party dependencies, use the top-level `:dependencies`. They will be included
in your uberjar. Note, you can create your own "library" plugin (similar to Bukkit4Clojure) by using the 
`depend` setting in plugin.yml (see below). This is useful if you wish to write multiple Clojure plugins that
share a common set of third-party dependencies.

#### Extend ClojurePlugin

```clojure
(ns example1.plugin
  ;; Create the 'main' plugin class. See plugin.yml
  (:gen-class
   :name example1.Example1Plugin
   :extends bukkitclj.ClojurePlugin))

(defn -onEnable [this]
  (.info this "Example1 IS AWESOME!!!!")
  (.info this
         (str "config.option: "
              (.. this (getConfig) (getString "config.option"))))
  (.info this 
         (str "config.example1: "
              (.. this (getConfig) (getString "config.example1")))))

(defn -onDisable [this]
  (.info this "Example1 disabled"))
```

Use `gen-class` to extend bukkitclj.ClojurePlugin. Define your onEnable and onDisable functions.
Remember the classname you chose. It will become your `main` setting in plugin.yml

#### Configure plugin.yml

```yaml
name: Example1

version: 1.0

api-version: 1.16

author: Craig McDaniel

main: example1.Example1Plugin

depend: [Bukkit4Clojure]
```

The two most important things to note here are the `main` and `depend` settings. 

#### Register Event Handlers

```clojure
(require '[bukkitclj.ev :as ev])
(ev/find-event "player-toggle")
(ev/describe-event :player/player-toggle-sneak)
;; use a reference to your own plugin instance here...
(let [plugin (deref bukkitclj.repl/plugin-ref)] 
  (ev/register-event plugin
                     :player/player-toggle-sneak
                     (fn [e] (println "event triggered!"))
                     :priority/normal))
```

#### Register Command and Tab Completion Handlers

```clojure
(require '[bukkitclj.command :as cmd])

(cmd/register-command 
 plugin
 "repl"
 (fn [sender cmd alias args]
   ;; Do command stuff here...
   )
 (fn [sender cmd alias args]
   ;; Do tab completion here
   (when (= 1 (count args))
     (filter #(.startsWith % (first args)) #{"on" "off"}))))
```

#### Testing your plugin

### Links

- [Bukkt4Clojure plugin download](FIXME) - Go here to download the Bukkit4Clojure plugin JAR
  file to add to your server `plugins/` directory.
- [SpigotMC](https://www.spigotmc.org/) - A modified Minecraft server (and community) based on 
  CraftBukkit. The server APIs and docs are here. This is one option for you to run your own
  Minecraft server (and test your plugins).
- [PaperMC](https://papermc.io/) - Built on top of Spigot (compatible with Spigot plugins) with
  performance improvements and an easy-to-use installer. Bukkit API docs are also availavble here.
  This is another option for running your own server and testing your plugins.

### Bugs

Currently no bugs are known.

## TO-DO/Plans

- [ ] Create a lein project template, similar to [spigot-clj](https://github.com/JohnnyJayJay/spigot-clj-template).
- [ ] Test lein project template using [clj-new](https://github.com/seancorfield/clj-new) for generating deps.edn
- [ ] Write directly to Bukkit's CommandMap.

## [License](/LICENSE)

```
MIT License

Copyright (c) 2018 Mariell Hoversholm

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
