package org.kowboy.bukkit;

import clojure.lang.IFn;

import org.bukkit.plugin.EventExecutor;
import org.bukkit.event.Listener;
import org.bukkit.event.Event;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

/**
 * An executor wrapper around a Clojure fn. The primary purpose of this wrapper
 * is to provide a way to re-bind handler functions at the REPL. The simple way
 * to do this is to call {@link #setHandler(IFn)} (you will need to hang on to
 * the instance of this class to do so).
 * 
 * This implements multiple <code>*Executor</code> interfaces (Tab, Command, and Event), 
 * but in reality the wrapped function will only be called through one of these 
 * interfaces. For example, when registering a handler function as a 
 * CommandExecutor, the handler fn will only be called through 
 * {@link #onCommand(CommandSender, Command, String, String[])}.
 */
public class ClojureExecutor implements TabExecutor, EventExecutor, Listener {
    private IFn handler;

    /**
     * Constructs an <code>*Executor</code> wrapper for a Clojure function.
     * 
     * @param handler The Clojure function to call when the corresponding
     *                <code>*Executor</code> method is called.
     */
    public ClojureExecutor(IFn handler) {
        this.handler = handler;
    }

    /**
     * I know, mutability stinks. Still, this is the simplest and easiest way to
     * swap handler functions at the REPL.
     * 
     * @param handler The new handler function that will be used on future
     *                invocations of the <code>*Executor</code> methods.
     */
    public void setHandler(IFn handler) {
        this.handler = handler;
    }

    /**
     * Calls the wrapped clojure function with a single {@link Event} parameter.
     * The handler function should type hint to the appropriate event type:
     * 
     * <p>
     * <code>
     * (fn [^PlayerEvent event]
     *   (.. event (getPlayer) (sendMessage "Hello, Player!")))
     * </code>
     * 
     * @param listener The listener instance the event is registered for. 
     *                 Most likely it is the same as <code>this</code> object.
     * @param event The event that triggered this execute call.
     */
    @Override
    public void execute(Listener listener, Event event) {
        this.handler.invoke(event);
    }

    /**
     * Calls the wrapped clojure function when a {@link org.bukkit.command.TabCompleteEvent} is fired for
     * a particular {@link Command}. The handler function params and return should 
     * mirror that of {@link TabExecutor#onTabComplete(CommandSender, Command, String, String[])}.
     * 
     * @param sender The sender of the TabCompleteEvent, usually a Player.
     * @param command The command that this tab completion belongs to.
     * @param alias The command alias used by the sender.
     * @param args The current args list at the time the sender pressed Tab. Will be passed along to the handler.
     * @return A list of possible command completion strings. The list may be empty if there are no completions.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Object returnVal = this.handler.invoke(sender, command, alias, args);
        if (returnVal instanceof List) {
            return (List<String>) returnVal;
        }
        return Collections.emptyList();
    }

    /**
     * Calls the wrapped clojure function when a {@link Command} has been invoked. The handler
     * function params and return should mirror that of 
     * {@link org.bukkit.command.CommandExecutor#onCommand(CommandSender, Command, String, String[])}.
     * 
     * @param sender The sender of this Command, usually a Player.
     * @param command The command that is being invoked.
     * @param label The alias used to invoke this command.
     * @param args The args list for the command. Will be passed along to the handler.
     * @return A boolean if the command was valid, false otherwise. A false return value will
     *         cause the usage message to be sent to the command sender.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Object retVal = this.handler.invoke(sender, command, label, args);
        if (retVal == null) {
            // null considered falsey
            return false;
        } else if (retVal instanceof Boolean) {
            return ((Boolean) retVal).booleanValue();
        }
        // everything else considered truthy
        return true;
    }
}
