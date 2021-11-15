/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 */
package me.earth.phobos.features.modules.combat;

import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;

public class SilentXP
extends Module {
    Setting<Integer> lookPitch = this.register(new Setting<Integer>("LookPitch", 90, 0, 100));
    private int delay_count;
    int prvSlot;

    public SilentXP() {
        super("SilentXp", "uses exp with packets", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        this.delay_count = 0;
    }

    @Override
    public void onUpdate() {
        if (SilentXP.mc.field_71462_r == null) {
            this.usedXp();
        }
    }

    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (SilentXP.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Items.field_151062_by) continue;
            slot = i;
            break;
        }
        return slot;
    }

    private void usedXp() {
        int oldPitch = (int)SilentXP.mc.field_71439_g.field_70125_A;
        this.prvSlot = SilentXP.mc.field_71439_g.field_71071_by.field_70461_c;
        SilentXP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.findExpInHotbar()));
        SilentXP.mc.field_71439_g.field_70125_A = this.lookPitch.getValue().intValue();
        SilentXP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(SilentXP.mc.field_71439_g.field_70177_z, (float)this.lookPitch.getValue().intValue(), true));
        SilentXP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        SilentXP.mc.field_71439_g.field_70125_A = oldPitch;
        SilentXP.mc.field_71439_g.field_71071_by.field_70461_c = this.prvSlot;
        SilentXP.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(this.prvSlot));
    }
}

