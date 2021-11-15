/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.util.math.Vec3i
 */
package me.earth.phobos.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class ItemUtil {
    public static final Minecraft mc = Minecraft.func_71410_x();

    public static boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = ItemUtil.getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        BlockPos neighbour = pos.func_177972_a(side);
        EnumFacing opposite = side.func_176734_d();
        Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        Block neighbourBlock = ItemUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!ItemUtil.mc.field_71439_g.func_70093_af()) {
            ItemUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)ItemUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            ItemUtil.mc.field_71439_g.func_70095_a(true);
            sneaking = true;
        }
        if (rotate) {
            ItemUtil.faceVector(hitVec, true);
        }
        ItemUtil.rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        ItemUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        ItemUtil.mc.field_71467_ac = 4;
        return sneaking || isSneaking;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (EnumFacing side : EnumFacing.values()) {
            IBlockState blockState;
            BlockPos neighbour = pos.func_177972_a(side);
            if (!ItemUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(ItemUtil.mc.field_71441_e.func_180495_p(neighbour), false) || (blockState = ItemUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) continue;
            facings.add(side);
        }
        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator<EnumFacing> iterator = ItemUtil.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            EnumFacing facing = iterator.next();
            return facing;
        }
        return null;
    }

    public static Vec3d getEyesPos() {
        return new Vec3d(ItemUtil.mc.field_71439_g.field_70165_t, ItemUtil.mc.field_71439_g.field_70163_u + (double)ItemUtil.mc.field_71439_g.func_70047_e(), ItemUtil.mc.field_71439_g.field_70161_v);
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = ItemUtil.getEyesPos();
        double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{ItemUtil.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g((float)(yaw - ItemUtil.mc.field_71439_g.field_70177_z)), ItemUtil.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g((float)(pitch - ItemUtil.mc.field_71439_g.field_70125_A))};
    }

    public static void faceVector(Vec3d vec, boolean normalizeAngle) {
        float[] rotations = ItemUtil.getLegitRotations(vec);
        ItemUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? (float)MathHelper.func_180184_b((int)((int)rotations[1]), (int)360) : rotations[1], ItemUtil.mc.field_71439_g.field_70122_E));
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.field_72450_a - (double)pos.func_177958_n());
            float f1 = (float)(vec.field_72448_b - (double)pos.func_177956_o());
            float f2 = (float)(vec.field_72449_c - (double)pos.func_177952_p());
            ItemUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            ItemUtil.mc.field_71442_b.func_187099_a(ItemUtil.mc.field_71439_g, ItemUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
        ItemUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        ItemUtil.mc.field_71467_ac = 4;
    }

    public static int findHotbarBlock(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = ItemUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack == ItemStack.field_190927_a) continue;
            if (clazz.isInstance((Object)stack.func_77973_b())) {
                return i;
            }
            if (!(stack.func_77973_b() instanceof ItemBlock) || !clazz.isInstance((Object)(block = ((ItemBlock)stack.func_77973_b()).func_179223_d()))) continue;
            return i;
        }
        return -1;
    }

    public static void switchToSlot(int slot) {
        ItemUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
        ItemUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        ItemUtil.mc.field_71442_b.func_78765_e();
    }

    public static int getBlockFromHotbar(Block block) {
        int slot = -1;
        for (int i = 8; i >= 0; --i) {
            if (ItemUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != Item.func_150898_a((Block)block)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int getItemFromHotbar(Class<?> clazz) {
        int slot = -1;
        for (int i = 8; i >= 0; --i) {
            if (ItemUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b().getClass() != clazz) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int getItemFromHotbar(Item item) {
        int slot = -1;
        for (int i = 8; i >= 0; --i) {
            if (ItemUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() != item) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static boolean isArmorUnderPercent(EntityPlayer player, float percent) {
        for (int i = 3; i >= 0; --i) {
            ItemStack stack = (ItemStack)player.field_71071_by.field_70460_b.get(i);
            if (!(ItemUtil.getDamageInPercent(stack) < percent)) continue;
            return true;
        }
        return false;
    }

    public static int getRoundedDamage(ItemStack stack) {
        return (int)ItemUtil.getDamageInPercent(stack);
    }

    public static float getDamageInPercent(ItemStack stack) {
        float green = ((float)stack.func_77958_k() - (float)stack.func_77952_i()) / (float)stack.func_77958_k();
        float red = 1.0f - green;
        return 100 - (int)(red * 100.0f);
    }
}

