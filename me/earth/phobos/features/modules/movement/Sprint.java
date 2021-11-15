/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.movement;

import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Sprint
extends Module {
    private static Sprint INSTANCE = new Sprint();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.LEGIT));

    public Sprint() {
        super("Sprint", "Modifies sprinting", Module.Category.MOVEMENT, false, false, false);
        this.setInstance();
    }

    public static Sprint getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Sprint();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSprint(MoveEvent event) {
        if (event.getStage() == 1 && this.mode.getValue() == Mode.RAGE && (Sprint.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || Sprint.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f)) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RAGE: {
                if (!Sprint.mc.field_71474_y.field_74351_w.func_151470_d() && !Sprint.mc.field_71474_y.field_74368_y.func_151470_d() && !Sprint.mc.field_71474_y.field_74370_x.func_151470_d() && !Sprint.mc.field_71474_y.field_74366_z.func_151470_d() || Sprint.mc.field_71439_g.func_70093_af() || Sprint.mc.field_71439_g.field_70123_F || (float)Sprint.mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0f) break;
                Sprint.mc.field_71439_g.func_70031_b(true);
                break;
            }
            case LEGIT: {
                if (!Sprint.mc.field_71474_y.field_74351_w.func_151470_d() || Sprint.mc.field_71439_g.func_70093_af() || Sprint.mc.field_71439_g.func_184587_cr() || Sprint.mc.field_71439_g.field_70123_F || (float)Sprint.mc.field_71439_g.func_71024_bL().func_75116_a() <= 6.0f || Sprint.mc.field_71462_r != null) break;
                Sprint.mc.field_71439_g.func_70031_b(true);
            }
        }
    }

    @Override
    public void onDisable() {
        if (!Sprint.nullCheck()) {
            Sprint.mc.field_71439_g.func_70031_b(false);
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    public static enum Mode {
        LEGIT,
        RAGE;

    }
}

