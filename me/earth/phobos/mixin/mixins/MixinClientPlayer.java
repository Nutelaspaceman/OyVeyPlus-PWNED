/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.util.ResourceLocation
 */
package me.earth.phobos.mixin.mixins;

import javax.annotation.Nullable;
import me.earth.phobos.features.modules.render.PlayerESP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={AbstractClientPlayer.class}, priority=999999999)
public abstract class MixinClientPlayer {
    @Shadow
    @Nullable
    protected abstract NetworkPlayerInfo func_175155_b();

    @Inject(method={"getLocationSkin()Lnet/minecraft/util/ResourceLocation;"}, at={@At(value="HEAD")}, cancellable=true)
    public void getLocationSkin(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (PlayerESP.getInstance().bigj.getValue().booleanValue()) {
            callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/bigjmuffin.png"));
        }
        if (PlayerESP.getInstance().fireside.getValue().booleanValue()) {
            callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/fireside.png"));
        }
        if (PlayerESP.getInstance().alphaguy.getValue().booleanValue()) {
            callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/alpha432.png"));
        }
        if (PlayerESP.getInstance().jj20051.getValue().booleanValue()) {
            callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/JJ20051.png"));
        }
        if (PlayerESP.getInstance().hause.getValue().booleanValue()) {
            callbackInfoReturnable.setReturnValue(new ResourceLocation("textures/hausemaster.png"));
        }
    }
}

