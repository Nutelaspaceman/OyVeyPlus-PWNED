/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemEgg
 *  net.minecraft.item.ItemEnderPearl
 *  net.minecraft.item.ItemSnowball
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.misc;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Bowbomb
extends Module {
    private boolean shooting;
    private long lastShootTime;
    public Setting<Boolean> Bows = this.register(new Setting<Boolean>("Bows", true));
    public Setting<Boolean> pearls = this.register(new Setting<Boolean>("Pearls", true));
    public Setting<Boolean> eggs = this.register(new Setting<Boolean>("Eggs", true));
    public Setting<Boolean> snowballs = this.register(new Setting<Boolean>("SnowBallz", true));
    public Setting<Integer> Timeout = this.register(new Setting<Integer>("Timeout", 5000, 100, 20000));
    public Setting<Integer> spoofs = this.register(new Setting<Integer>("Spoofs", 10, 1, 300));
    public Setting<Boolean> bypass = this.register(new Setting<Boolean>("Bypass", false));
    public Setting<Boolean> debug = this.register(new Setting<Boolean>("Debug", false));

    public Bowbomb() {
        super("BowoBomb", "Uno hitter w bows", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (this.isEnabled()) {
            this.shooting = false;
            this.lastShootTime = System.currentTimeMillis();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        ItemStack handStack;
        CPacketPlayerTryUseItem packet2;
        if (event.getStage() != 0) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            ItemStack handStack2;
            CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            if (packet.func_180762_c() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && !(handStack2 = Bowbomb.mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND)).func_190926_b() && handStack2.func_77973_b() != null && handStack2.func_77973_b() instanceof ItemBow && this.Bows.getValue().booleanValue()) {
                this.doSpoofs();
                if (this.debug.getValue().booleanValue()) {
                    Command.sendMessage("trying to spoof");
                }
            }
        } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet2 = (CPacketPlayerTryUseItem)event.getPacket()).func_187028_a() == EnumHand.MAIN_HAND && !(handStack = Bowbomb.mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND)).func_190926_b() && handStack.func_77973_b() != null) {
            if (handStack.func_77973_b() instanceof ItemEgg && this.eggs.getValue().booleanValue()) {
                this.doSpoofs();
            } else if (handStack.func_77973_b() instanceof ItemEnderPearl && this.pearls.getValue().booleanValue()) {
                this.doSpoofs();
            } else if (handStack.func_77973_b() instanceof ItemSnowball && this.snowballs.getValue().booleanValue()) {
                this.doSpoofs();
            }
        }
    }

    private void doSpoofs() {
        if (System.currentTimeMillis() - this.lastShootTime >= (long)this.Timeout.getValue().intValue()) {
            this.shooting = true;
            this.lastShootTime = System.currentTimeMillis();
            Bowbomb.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Bowbomb.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
            for (int index = 0; index < this.spoofs.getValue(); ++index) {
                if (this.bypass.getValue().booleanValue()) {
                    Bowbomb.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Bowbomb.mc.field_71439_g.field_70165_t, Bowbomb.mc.field_71439_g.field_70163_u + 1.0E-10, Bowbomb.mc.field_71439_g.field_70161_v, false));
                    Bowbomb.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Bowbomb.mc.field_71439_g.field_70165_t, Bowbomb.mc.field_71439_g.field_70163_u - 1.0E-10, Bowbomb.mc.field_71439_g.field_70161_v, true));
                    continue;
                }
                Bowbomb.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Bowbomb.mc.field_71439_g.field_70165_t, Bowbomb.mc.field_71439_g.field_70163_u - 1.0E-10, Bowbomb.mc.field_71439_g.field_70161_v, true));
                Bowbomb.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Bowbomb.mc.field_71439_g.field_70165_t, Bowbomb.mc.field_71439_g.field_70163_u + 1.0E-10, Bowbomb.mc.field_71439_g.field_70161_v, false));
            }
            if (this.debug.getValue().booleanValue()) {
                Command.sendMessage("Spoofed");
            }
            this.shooting = false;
        }
    }
}

