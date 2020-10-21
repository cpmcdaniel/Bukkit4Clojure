package bukkitclj;

import clojure.lang.IFn;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

/**
 * Extend this abstract Plugin in your own Clojure plugin projects.
 * Then, make sure you have a dependency in your plugin.yml like so:
 * <p>
 * <code>
 * depend: [Bukkit4Clojure]
 * </code>
 * 
 * This will force the Clojure runtime to load before your plugin is
 * loaded, allowing you to use gen-class to extend this class and 
 * create your plugin implementation in a Clojure source file.
 */
public abstract class ClojurePlugin extends JavaPlugin {

  public ClojureExecutor registerEvent(final Class<? extends Event> eventClass, final IFn handlerFn) {
    return registerEvent(eventClass, EventPriority.NORMAL, handlerFn);
  }

  public ClojureExecutor registerEvent(final Class<? extends Event> eventClass, final EventPriority priority, final IFn handlerFn) {
    return registerEvent(eventClass, priority, handlerFn, false);
  }

  /**
   * Note, more than one handler can be registered for the same Event type. 
   * 
   * @param eventClass The type of event that will fire the given handler.
   * @param priority The event priority.
   * @param handlerFn Clojure fn that must take an event parameter. See {@link ClojureExecutor}.
   * @param ignoreCancelled 
   * @return The ClojureHandler wrapper for the handlerFn.
   */
  public ClojureExecutor registerEvent(final Class<? extends Event> eventClass, final EventPriority priority, final IFn handler, final boolean ignoreCancelled) {
    ClojureExecutor executor = new ClojureExecutor(handler);
    return registerEvent(eventClass, priority, executor, ignoreCancelled);
  }

  public ClojureExecutor registerEvent(final Class<? extends Event> eventClass, final EventPriority priority, final ClojureExecutor handler, final boolean ignoreCancelled) {
    getServer().getPluginManager().registerEvent(eventClass, handler, priority, handler, this, ignoreCancelled);
    return handler;
  }

  public void unregisterHandler(final ClojureExecutor handler) {
    HandlerList.unregisterAll(handler);
  }

  public boolean registerCommand(final String name, final CommandExecutor handler) {
    PluginCommand pc = getCommand(name);
    if (pc == null) {
      return false;
    }
    pc.setExecutor(handler);
    return true;
  }
  
  public ClojureExecutor registerCommand(final String name, final IFn handler) {
    ClojureExecutor executor = new ClojureExecutor(handler);
    if (registerCommand(name, executor)) {
      return executor;
    }
    return null;
  }

  public boolean registerTabCompleter(final String name, final TabCompleter handler) {
    PluginCommand pc = getCommand(name);
    if (pc == null) {
      return false;
    }
    pc.setTabCompleter(handler);
    return true;
  }

  public ClojureExecutor registerTabCompleter(final String name, final IFn handler) {
    ClojureExecutor executor = new ClojureExecutor(handler);
    if (registerTabCompleter(name, executor)) {
      return executor;
    }
    return null;
  }

  public static final void info(Class cls, String msg) {
    String className = cls.getName();
    tellConsole(ChatColor.DARK_AQUA+"["+className+"]"+ChatColor.RESET+" "+msg);
  }

  public static final void tellConsole(String msg) {
    ConsoleCommandSender cons = Bukkit.getConsoleSender();
    cons.sendMessage(msg);
  }

  public void info(String msg) {
    getLogger().info(msg);
  }

  public void warn(String msg) {
    getLogger().warning(msg);
  }

  public void error(String msg) {
    getLogger().severe(msg);
  }

  public void debug(String msg) {
    getLogger().fine(msg);
  }
}
