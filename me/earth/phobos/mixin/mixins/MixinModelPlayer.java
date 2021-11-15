/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelPlayer
 */
package me.earth.phobos.mixin.mixins;

import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={ModelPlayer.class})
public class MixinModelPlayer {
    @Redirect(method={"renderCape"}, at=@At(value="HEAD"))
    public void renderCape(float scale) {
    }
}

