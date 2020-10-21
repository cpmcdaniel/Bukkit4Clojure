# Bukkit4Clojure

This wrapper is for any developer who wishes to develop Bukkit using the LISP-based language Clojure.

## Installation

Place the bukkit4clj.jar file in your server plugins directory.

TODO: Provide link to project on dev.bukkit.org where they can download the jar.

## Usage

#### Start a REPL

Run this from your server console:

```/repl start [port]```

The default port, if left unspecified, is 7071.

#### Stop the REPL

```/repl stop```

The plugin will remember whatever state you left the repl in (running or stopped). If 
it was running when the server was stopped, it will be started again when the Bukkit server
starts. By default, the REPL is disabled until you start it. 

## Building your own Clojure Plugin

Just doing the above will be enough to give you a REPL where you can experiment interactively
with the Bukkit API. If you wish to create your own plugin using Clojure... 

TODO

#### Project Setup/Configuration

TODO

#### Extend AbstractClojurePlugin

TODO

#### Configure plugin.yml

TODO

#### Register Event Handlers

TODO

#### Register Command Handlers

TODO

#### Register Tab Completers

TODO

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