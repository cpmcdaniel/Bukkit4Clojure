package org.kowboy.bukkit;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import clojure.lang.IFn;

/**
 * Provides a set of register/unregister functions designed to 
 * make it easy to use simple Clojure functions as executors
 * (including EventExecutor, CommandExecutor, and TabCompleter).
 * 
 * None of this is stricly necessary. It simply provides some convenience.
 * Handlers/executors can be registered in the standard way using the
 * Bukkit API and reifying the appropriate interfaces.
 * 
 * For more information on how to re-bind executor functions at the REPL, 
 * see {@link ClojureExecutor}.
 */
public interface ClojurePlugin {
    public ClojureExecutor registerEvent(Class<? extends Event> eventClass, IFn handlerFn);

    public ClojureExecutor registerEvent(Class<? extends Event> eventClass, EventPriority priority, IFn handlerFn);

    public ClojureExecutor registerEvent(Class<? extends Event> eventClass, EventPriority priority, IFn handler, boolean ignoreCancelled);

    public ClojureExecutor registerEvent(Class<? extends Event> eventClass, EventPriority priority, ClojureExecutor handler, boolean ignoreCancelled);

    public void unregisterHandler(ClojureExecutor handler);

    public boolean registerCommand(String name, CommandExecutor handler);

    public ClojureExecutor registerCommand(String name, IFn handler);

    public boolean registerTabCompleter(String name, TabCompleter handler);

    public ClojureExecutor registerTabCompleter(String name, IFn handler);
}
