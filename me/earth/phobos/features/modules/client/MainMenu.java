/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;

public class MainMenu
extends Module {
    private static MainMenu INSTANCE = new MainMenu();
    public Setting<Boolean> mainScreen = this.register(new Setting<Boolean>("MainScreen", true));
    public Setting<Boolean> particles = this.register(new Setting<Boolean>("Particles", true));

    public MainMenu() {
        super("MainMenuScreen", "Toggles MainMenuScreen", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static MainMenu getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MainMenu();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

