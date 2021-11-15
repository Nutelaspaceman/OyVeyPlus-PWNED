/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 */
package me.earth.phobos.mixin.mixins;

import me.earth.phobos.features.modules.player.TrueDurability;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemStack.class})
public abstract class MixinItemStack {
    @Shadow
    private int field_77991_e;

    @Inject(method={"<init>(Lnet/minecraft/item/Item;IILnet/minecraft/nbt/NBTTagCompound;)V"}, at={@At(value="RETURN")})
    @Dynamic
    private void initHook(Item item, int idkWhatDisIsIPastedThis, int dura, NBTTagCompound compound, CallbackInfo info) {
        this.field_77991_e = this.checkDurability((ItemStack)ItemStack.class.cast(this), this.field_77991_e, dura);
    }

    @Inject(method={"<init>(Lnet/minecraft/nbt/NBTTagCompound;)V"}, at={@At(value="RETURN")})
    private void initHook2(NBTTagCompound compound, CallbackInfo info) {
        this.field_77991_e = this.checkDurability((ItemStack)ItemStack.class.cast(this), this.field_77991_e, compound.func_74765_d("Damage"));
    }

    private int checkDurability(ItemStack item, int damage, int dura) {
        int trueDura = damage;
        if (TrueDurability.getInstance().isOn() && dura < 0) {
            trueDura = dura;
        }
        return trueDura;
    }
}

