package bukkitclj;

import org.bukkit.Bukkit;

import clojure.java.api.Clojure;
import clojure.lang.Compiler;
import clojure.lang.IFn;
import clojure.lang.RT;
import clojure.lang.Var;

/**
 * This plugin is intended to be a dependency of other Clojure 
 * plugins. It provides the Clojure runtime and library so that 
 * your own plugin doesn't need to worry about this. It also 
 * provides a REPL on a configurable port (see repl.clj). By 
 * default, the REPL is not enabled, and can be turned on via
 * <code>/repl start [port]</code>.
 * 
 * <p>
 * DO NOT use this class as your plugin's main class.
 * DO NOT extend this class (it's final, anyway).
 * The Bukkit plugin system fails when the classloader tries to 
 * load the same class into memory more than once.
 * 
 * <p>
 * To ensure the Clojure runtime is loaded before your plugin, you
 * should extend {@link AbstractClojurePlugin} and then specify 
 * this plugin as a dependency in plugin.yml:
 * 
 * <p>
 * <code>
 *  depend: [Bukkit4Clojure]
 * </code>
 * 
 * <p>
 * Doing so allows your plugin to be written in Clojure source files
 * using gen-class, and lets you avoid having to package the Clojure
 * library into your plugin jar.
 */
public final class Bukkit4Clojure extends ClojurePlugin {

  static {
    ClassLoader previous = Thread.currentThread().getContextClassLoader();
    final ClassLoader ccl = Bukkit4Clojure.class.getClassLoader();
    Thread.currentThread().setContextClassLoader(ccl);
    try {
      RT.init();
      Var.pushThreadBindings(RT.map(Compiler.LOADER, ccl));
    } finally {
      Thread.currentThread().setContextClassLoader(previous);
    }
  }

  private IFn require;
  private final String replNs = "bukkitclj.repl";

  @Override
  public void onLoad() {
    require = Clojure.var(
          "clojure.core",
          "require"
    );
    getLogger().info("Loaded Clojure Runtime");
  }

  @Override
  public void onEnable() {
    require.invoke(Clojure.read(replNs));
    IFn replOnEnable = Clojure.var(replNs, "on-enable");
    replOnEnable.invoke(this);
  }

  @Override
  public void onDisable() {
    require.invoke(Clojure.read(replNs));
    IFn replOnDisable = Clojure.var(replNs, "on-disable");
    replOnDisable.invoke(this);  
  }
}
