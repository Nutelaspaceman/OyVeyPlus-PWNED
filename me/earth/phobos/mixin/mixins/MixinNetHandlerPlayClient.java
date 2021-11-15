/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.server.SPacketEntityMetadata
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package me.earth.phobos.mixin.mixins;

import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.DeathEvent;
import me.earth.phobos.util.Util;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient {
    @Inject(method={"handleEntityMetadata"}, at={@At(value="RETURN")}, cancellable=true)
    private void handleEntityMetadataHook(SPacketEntityMetadata packetIn, CallbackInfo info) {
        Entity entity;
        if (Util.mc.field_71441_e != null && (entity = Util.mc.field_71441_e.func_73045_a(packetIn.func_149375_d())) instanceof EntityPlayer) {
            EntityPlayer entityPlayer;
            EntityPlayer player = (EntityPlayer)entity;
            if (entityPlayer.func_110143_aJ() <= 0.0f) {
                MinecraftForge.EVENT_BUS.post((Event)new DeathEvent(player));
                if (Oyvey.totemPopManager != null) {
                    Oyvey.totemPopManager.onDeath(player);
                }
            }
        }
    }
}

