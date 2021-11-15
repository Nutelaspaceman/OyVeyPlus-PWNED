/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;

public class Troll
extends Module {
    public Troll() {
        super("Troll", "a", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        Troll.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("/kill"));
        this.disable();
    }
}

