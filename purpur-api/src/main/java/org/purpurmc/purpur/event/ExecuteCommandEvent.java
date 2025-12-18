package org.purpurmc.purpur.event;

import com.google.common.base.Preconditions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This event is called whenever someone runs a command
 */
@NullMarked
public class ExecuteCommandEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private CommandSender sender;
    private Command command;
    private String label;
    private @Nullable String[] args;

    @ApiStatus.Internal
    public ExecuteCommandEvent(CommandSender sender, Command command, String label, @Nullable String[] args) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
    }

    /**
     * Gets the command that the player is attempting to execute.
     *
     * @return Command the player is attempting to execute
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Sets the command that the player will execute.
     *
     * @param command New command that the player will execute
     * @throws IllegalArgumentException if command is null or empty
     */
    public void setCommand(Command command) throws IllegalArgumentException {
        Preconditions.checkArgument(command != null, "Command cannot be null");
        this.command = command;
    }

    /**
     * Gets the sender that this command will be executed as.
     *
     * @return Sender this command will be executed as
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * Sets the sender that this command will be executed as.
     *
     * @param sender New sender which this event will execute as
     * @throws IllegalArgumentException if the sender provided is null
     */
    public void setSender(final CommandSender sender) throws IllegalArgumentException {
        Preconditions.checkArgument(sender != null, "Sender cannot be null");
        this.sender = sender;
    }

    /**
     * Get the label used to execute this command
     *
     * @return Label used to execute this command
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the label used to execute this command
     *
     * @param label Label used
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the args passed to the command
     *
     * @return Args passed to the command
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Set the args passed to the command
     *
     * @param args Args passed to the command
     */
    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
