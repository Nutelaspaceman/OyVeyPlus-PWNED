/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.Module;

public class ReverseStep
extends Module {
    public ReverseStep() {
        super("ReverseStep", "Screams chinese words and teleports you", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (ReverseStep.mc.field_71439_g.field_70122_E) {
            ReverseStep.mc.field_71439_g.field_70181_x -= 1.0;
        }
    }
}

