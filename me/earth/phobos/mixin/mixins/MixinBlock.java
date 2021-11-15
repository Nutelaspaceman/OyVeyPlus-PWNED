/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 */
package me.earth.phobos.mixin.mixins;

import java.util.List;
import javax.annotation.Nullable;
import me.earth.phobos.features.modules.movement.Flight;
import me.earth.phobos.features.modules.movement.Phase;
import me.earth.phobos.features.modules.player.Freecam;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Block.class})
public abstract class MixinBlock {
    @Shadow
    @Deprecated
    public abstract float func_176195_g(IBlockState var1, World var2, BlockPos var3);

    @Inject(method={"addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void addCollisionBoxToListHook(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState, CallbackInfo info) {
        if (entityIn != null && Util.mc.field_71439_g != null && (entityIn.equals((Object)Util.mc.field_71439_g) || Util.mc.field_71439_g.func_184187_bx() != null && entityIn.equals((Object)Util.mc.field_71439_g.func_184187_bx())) && (Flight.getInstance().isOn() && (Flight.getInstance().mode.getValue() == Flight.Mode.PACKET && Flight.getInstance().better.getValue() != false && Flight.getInstance().phase.getValue() != false || Flight.getInstance().mode.getValue() == Flight.Mode.DAMAGE && Flight.getInstance().noClip.getValue() != false) || Phase.getInstance().isOn() && Phase.getInstance().mode.getValue() == Phase.Mode.PACKETFLY && Phase.getInstance().type.getValue() == Phase.PacketFlyMode.SETBACK && Phase.getInstance().boundingBox.getValue().booleanValue())) {
            info.cancel();
        }
        try {
            if (Freecam.getInstance().isOff() && Util.mc.field_71439_g != null && state != null && state.func_177230_c() instanceof BlockLiquid && !(entityIn instanceof EntityBoat) && !Util.mc.field_71439_g.func_70093_af() && Util.mc.field_71439_g.field_70143_R < 3.0f && !EntityUtil.isAboveLiquid((Entity)Util.mc.field_71439_g) && EntityUtil.checkForLiquid((Entity)Util.mc.field_71439_g, false) || EntityUtil.checkForLiquid((Entity)Util.mc.field_71439_g, false) && Util.mc.field_71439_g.func_184187_bx() != null && Util.mc.field_71439_g.func_184187_bx().field_70143_R < 3.0f && EntityUtil.isAboveBlock((Entity)Util.mc.field_71439_g, pos)) {
                info.cancel();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

