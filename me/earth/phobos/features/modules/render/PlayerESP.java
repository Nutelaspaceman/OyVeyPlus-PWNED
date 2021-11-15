/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;

public class PlayerESP
extends Module {
    private static PlayerESP INSTANCE = new PlayerESP();
    public Setting<Boolean> fireside = this.register(new Setting<Boolean>("Fireside", false));
    public Setting<Boolean> bigj = this.register(new Setting<Boolean>("BigJ", false));
    public Setting<Boolean> alphaguy = this.register(new Setting<Boolean>("Alpha432", false));
    public Setting<Boolean> hause = this.register(new Setting<Boolean>("HauseMaster", false));
    public Setting<Boolean> jj20051 = this.register(new Setting<Boolean>("JJ20051", false));

    public PlayerESP() {
        super("PlayerESP", "makes ppl look like other ppl", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static PlayerESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

