/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.command.commands;

import me.earth.phobos.Oyvey;
import me.earth.phobos.features.command.Command;

public class HelpCommand
extends Command {
    public HelpCommand() {
        super("commands");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("You can use following commands: ");
        for (Command command : Oyvey.commandManager.getCommands()) {
            HelpCommand.sendMessage(Oyvey.commandManager.getPrefix() + command.getName());
        }
    }
}

