/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.command.commands;

import me.earth.phobos.Oyvey;
import me.earth.phobos.features.command.Command;

public class ReloadCommand
extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Oyvey.reload();
    }
}

