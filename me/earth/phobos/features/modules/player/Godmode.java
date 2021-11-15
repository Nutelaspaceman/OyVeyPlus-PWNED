/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketInput
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketVehicleMove
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.player;

import java.util.Objects;
import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Godmode
extends Module {
    private final Setting<Boolean> remount = this.register(new Setting<Boolean>("Remount", false));
    public Minecraft mc = Minecraft.func_71410_x();
    public Entity entity;

    public Godmode() {
        super("Godmode", "Hi there :D", Module.Category.PLAYER, true, false, false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (this.mc.field_71441_e != null && this.mc.field_71439_g.func_184187_bx() != null) {
            this.entity = this.mc.field_71439_g.func_184187_bx();
            this.mc.field_71438_f.func_72712_a();
            this.hideEntity();
            this.mc.field_71439_g.func_70107_b((double)Minecraft.func_71410_x().field_71439_g.func_180425_c().func_177958_n(), (double)(Minecraft.func_71410_x().field_71439_g.func_180425_c().func_177956_o() - 1), (double)Minecraft.func_71410_x().field_71439_g.func_180425_c().func_177952_p());
        }
        if (this.mc.field_71441_e != null && this.remount.getValue().booleanValue()) {
            this.remount.setValue(false);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.remount.getValue().booleanValue()) {
            this.remount.setValue(false);
        }
        this.mc.field_71439_g.func_184210_p();
        this.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)this.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
        this.mc.func_147114_u().func_147297_a((Packet)new CPacketEntityAction((Entity)this.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer.Position || event.getPacket() instanceof CPacketPlayer.PositionRotation) {
            event.setCanceled(true);
        }
    }

    private void hideEntity() {
        if (this.mc.field_71439_g.func_184187_bx() != null) {
            this.mc.field_71439_g.func_184210_p();
            this.mc.field_71441_e.func_72900_e(this.entity);
        }
    }

    private void showEntity(Entity entity2) {
        entity2.field_70128_L = false;
        this.mc.field_71441_e.field_72996_f.add(entity2);
        this.mc.field_71439_g.func_184205_a(entity2, true);
    }

    @SubscribeEvent
    public void onPlayerWalkingUpdate(UpdateWalkingPlayerEvent event) {
        if (this.entity == null) {
            return;
        }
        if (event.getStage() == 0) {
            if (this.remount.getValue().booleanValue() && Objects.requireNonNull(Oyvey.moduleManager.getModuleByClass(Godmode.class)).isEnabled()) {
                this.showEntity(this.entity);
            }
            this.entity.func_70080_a(Minecraft.func_71410_x().field_71439_g.field_70165_t, Minecraft.func_71410_x().field_71439_g.field_70163_u, Minecraft.func_71410_x().field_71439_g.field_70161_v, Minecraft.func_71410_x().field_71439_g.field_70177_z, Minecraft.func_71410_x().field_71439_g.field_70125_A);
            this.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(this.mc.field_71439_g.field_70177_z, this.mc.field_71439_g.field_70125_A, true));
            this.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketInput(this.mc.field_71439_g.field_71158_b.field_192832_b, this.mc.field_71439_g.field_71158_b.field_78902_a, false, false));
            this.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketVehicleMove(this.entity));
        }
    }
}

