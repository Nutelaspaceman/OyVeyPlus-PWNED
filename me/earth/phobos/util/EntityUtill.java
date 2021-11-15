/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityAgeable
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureType
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.item.EntityBoat
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.item.EntityMinecart
 *  net.minecraft.entity.monster.EntityEnderman
 *  net.minecraft.entity.monster.EntityIronGolem
 *  net.minecraft.entity.monster.EntityPigZombie
 *  net.minecraft.entity.passive.EntityAmbientCreature
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.passive.EntityVillager
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityFireball
 *  net.minecraft.entity.projectile.EntityShulkerBullet
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Enchantments
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketEntityAction$Action
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.MovementInput
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */
package me.earth.phobos.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.earth.phobos.Oyvey;
import me.earth.phobos.mixin.mixins.accessors.IEntityLivingBase;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityUtill
implements Util {
    public static final Vec3d[] antiDropOffsetList = new Vec3d[]{new Vec3d(0.0, -2.0, 0.0)};
    public static final Vec3d[] platformOffsetList = new Vec3d[]{new Vec3d(0.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(1.0, -1.0, 0.0)};
    public static final Vec3d[] legOffsetList = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0)};
    public static final Vec3d[] OffsetList = new Vec3d[]{new Vec3d(1.0, 1.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 2.0, 0.0)};
    public static final Vec3d[] antiStepOffsetList = new Vec3d[]{new Vec3d(-1.0, 2.0, 0.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(0.0, 2.0, -1.0)};
    public static final Vec3d[] antiScaffoldOffsetList = new Vec3d[]{new Vec3d(0.0, 3.0, 0.0)};
    public static final Vec3d[] doubleLegOffsetList = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(2.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(0.0, 0.0, 2.0)};

    public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
        if (packet) {
            EntityUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
        } else {
            EntityUtil.mc.field_71442_b.func_78764_a((EntityPlayer)EntityUtil.mc.field_71439_g, entity);
        }
        if (swingArm) {
            EntityUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        }
    }

    public static float calculate(double posX, double posY, double posZ, EntityLivingBase entity) {
        double distancedsize = entity.func_70011_f(posX, posY, posZ) / 12.0;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double v = 1.0 - distancedsize;
        float damage = (int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        return EntityUtill.getBlastReduction(entity, EntityUtill.getDamageMultiplied(damage), new Explosion((World)EntityUtill.mc.field_71441_e, null, posX, posY, posZ, 6.0f, false, true));
    }

    public static EntityPlayer getClosestPlayer(float range) {
        EntityPlayer player = null;
        double maxDistance = 999.0;
        int size = EntityUtill.mc.field_71441_e.field_73010_i.size();
        for (int i = 0; i < size; ++i) {
            EntityPlayer entityPlayer = (EntityPlayer)EntityUtill.mc.field_71441_e.field_73010_i.get(i);
            if (!EntityUtill.isPlayerValid(entityPlayer, range)) continue;
            double distanceSq = EntityUtill.mc.field_71439_g.func_70068_e((Entity)entityPlayer);
            if (maxDistance != 999.0 && !(distanceSq < maxDistance)) continue;
            maxDistance = distanceSq;
            player = entityPlayer;
        }
        return player;
    }

    public static boolean isPlayerValid(EntityPlayer player, float range) {
        return player != EntityUtill.mc.field_71439_g && EntityUtill.mc.field_71439_g.func_70032_d((Entity)player) < range && !EntityUtill.isDead((Entity)player) && !Oyvey.friendManager.isFriend(player.func_70005_c_());
    }

    private static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer)entity;
            DamageSource ds = DamageSource.func_94539_a((Explosion)explosion);
            damage = CombatRules.func_189427_a((float)damage, (float)ep.func_70658_aO(), (float)((float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
            int k = EnchantmentHelper.func_77508_a((Iterable)ep.func_184193_aE(), (DamageSource)ds);
            float f = MathHelper.func_76131_a((float)k, (float)0.0f, (float)20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.func_70644_a(MobEffects.field_76429_m)) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.func_189427_a((float)damage, (float)entity.func_70658_aO(), (float)((float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e()));
        return damage;
    }

    private static float getDamageMultiplied(float damage) {
        int diff = EntityUtill.mc.field_71441_e.func_175659_aa().func_151525_a();
        return damage * (diff == 0 ? 0.0f : (diff == 2 ? 1.0f : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static RayTraceResult rayTraceBlocks(Vec3d vec31, Vec3d vec32) {
        int j1;
        int i1;
        int i = MathHelper.func_76128_c((double)vec32.field_72450_a);
        int j = MathHelper.func_76128_c((double)vec32.field_72448_b);
        int k = MathHelper.func_76128_c((double)vec32.field_72449_c);
        int l = MathHelper.func_76128_c((double)vec31.field_72450_a);
        BlockPos blockpos = new BlockPos(l, i1 = MathHelper.func_76128_c((double)vec31.field_72448_b), j1 = MathHelper.func_76128_c((double)vec31.field_72449_c));
        IBlockState iblockstate = EntityUtill.mc.field_71441_e.func_180495_p(blockpos);
        Block block = iblockstate.func_177230_c();
        if (block.func_176209_a(iblockstate, false) && block != Blocks.field_150321_G) {
            return iblockstate.func_185910_a((World)EntityUtill.mc.field_71441_e, blockpos, vec31, vec32);
        }
        int k1 = 200;
        double d6 = vec32.field_72450_a - vec31.field_72450_a;
        double d7 = vec32.field_72448_b - vec31.field_72448_b;
        double d8 = vec32.field_72449_c - vec31.field_72449_c;
        while (k1-- >= 0) {
            EnumFacing enumfacing;
            if (l == i && i1 == j && j1 == k) {
                return null;
            }
            boolean flag2 = true;
            boolean flag = true;
            boolean flag1 = true;
            double d0 = 999.0;
            double d1 = 999.0;
            double d2 = 999.0;
            if (i > l) {
                d0 = (double)l + 1.0;
            } else if (i < l) {
                d0 = (double)l + 0.0;
            } else {
                flag2 = false;
            }
            if (j > i1) {
                d1 = (double)i1 + 1.0;
            } else if (j < i1) {
                d1 = (double)i1 + 0.0;
            } else {
                flag = false;
            }
            if (k > j1) {
                d2 = (double)j1 + 1.0;
            } else if (k < j1) {
                d2 = (double)j1 + 0.0;
            } else {
                flag1 = false;
            }
            double d3 = 999.0;
            double d4 = 999.0;
            double d5 = 999.0;
            if (flag2) {
                d3 = (d0 - vec31.field_72450_a) / d6;
            }
            if (flag) {
                d4 = (d1 - vec31.field_72448_b) / d7;
            }
            if (flag1) {
                d5 = (d2 - vec31.field_72449_c) / d8;
            }
            if (d3 == -0.0) {
                d3 = -1.0E-4;
            }
            if (d4 == -0.0) {
                d4 = -1.0E-4;
            }
            if (d5 == -0.0) {
                d5 = -1.0E-4;
            }
            if (d3 < d4 && d3 < d5) {
                enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                vec31 = new Vec3d(d0, vec31.field_72448_b + d7 * d3, vec31.field_72449_c + d8 * d3);
            } else if (d4 < d5) {
                enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                vec31 = new Vec3d(vec31.field_72450_a + d6 * d4, d1, vec31.field_72449_c + d8 * d4);
            } else {
                enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                vec31 = new Vec3d(vec31.field_72450_a + d6 * d5, vec31.field_72448_b + d7 * d5, d2);
            }
            if (!(block = (iblockstate = EntityUtill.mc.field_71441_e.func_180495_p(blockpos = new BlockPos(l = MathHelper.func_76128_c((double)vec31.field_72450_a) - (enumfacing == EnumFacing.EAST ? 1 : 0), i1 = MathHelper.func_76128_c((double)vec31.field_72448_b) - (enumfacing == EnumFacing.UP ? 1 : 0), j1 = MathHelper.func_76128_c((double)vec31.field_72449_c) - (enumfacing == EnumFacing.SOUTH ? 1 : 0)))).func_177230_c()).func_176209_a(iblockstate, false) || block == Blocks.field_150321_G) continue;
            return iblockstate.func_185910_a((World)EntityUtill.mc.field_71441_e, blockpos, vec31, vec32);
        }
        return null;
    }

    public static boolean checkForLiquid(Entity entity, boolean b) {
        if (entity == null) {
            return false;
        }
        double posY = entity.field_70163_u;
        double n = b ? 0.03 : (entity instanceof EntityPlayer ? 0.2 : 0.5);
        double n2 = posY - n;
        for (int i = MathHelper.func_76128_c((double)entity.field_70165_t); i < MathHelper.func_76143_f((double)entity.field_70165_t); ++i) {
            for (int j = MathHelper.func_76128_c((double)entity.field_70161_v); j < MathHelper.func_76143_f((double)entity.field_70161_v); ++j) {
                if (!(EntityUtil.mc.field_71441_e.func_180495_p(new BlockPos(i, MathHelper.func_76128_c((double)n2), j)).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isAboveLiquid(Entity entity) {
        if (entity == null) {
            return false;
        }
        double n = entity.field_70163_u + 0.01;
        for (int i = MathHelper.func_76128_c((double)entity.field_70165_t); i < MathHelper.func_76143_f((double)entity.field_70165_t); ++i) {
            for (int j = MathHelper.func_76128_c((double)entity.field_70161_v); j < MathHelper.func_76143_f((double)entity.field_70161_v); ++j) {
                if (!(EntityUtil.mc.field_71441_e.func_180495_p(new BlockPos(i, (int)n, j)).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isOnLiquid(double offset) {
        if (EntityUtil.mc.field_71439_g.field_70143_R >= 3.0f) {
            return false;
        }
        AxisAlignedBB bb = EntityUtil.mc.field_71439_g.func_184187_bx() != null ? EntityUtil.mc.field_71439_g.func_184187_bx().func_174813_aQ().func_191195_a(0.0, 0.0, 0.0).func_72317_d(0.0, -offset, 0.0) : EntityUtil.mc.field_71439_g.func_174813_aQ().func_191195_a(0.0, 0.0, 0.0).func_72317_d(0.0, -offset, 0.0);
        boolean onLiquid = false;
        int y = (int)bb.field_72338_b;
        for (int x = MathHelper.func_76128_c((double)bb.field_72340_a); x < MathHelper.func_76128_c((double)(bb.field_72336_d + 1.0)); ++x) {
            for (int z = MathHelper.func_76128_c((double)bb.field_72339_c); z < MathHelper.func_76128_c((double)(bb.field_72334_f + 1.0)); ++z) {
                Block block = EntityUtil.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                if (block == Blocks.field_150350_a) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                onLiquid = true;
            }
        }
        return onLiquid;
    }

    public static boolean isOnLiquid() {
        double y = EntityUtil.mc.field_71439_g.field_70163_u - 0.03;
        for (int x = MathHelper.func_76128_c((double)EntityUtil.mc.field_71439_g.field_70165_t); x < MathHelper.func_76143_f((double)EntityUtil.mc.field_71439_g.field_70165_t); ++x) {
            for (int z = MathHelper.func_76128_c((double)EntityUtil.mc.field_71439_g.field_70161_v); z < MathHelper.func_76143_f((double)EntityUtil.mc.field_71439_g.field_70161_v); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.func_76128_c((double)y), z);
                if (!(EntityUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isInLiquid() {
        if (EntityUtil.mc.field_71439_g.field_70143_R >= 3.0f) {
            return false;
        }
        boolean inLiquid = false;
        AxisAlignedBB bb = EntityUtil.mc.field_71439_g.func_184187_bx() != null ? EntityUtil.mc.field_71439_g.func_184187_bx().func_174813_aQ() : EntityUtil.mc.field_71439_g.func_174813_aQ();
        int y = (int)bb.field_72338_b;
        for (int x = MathHelper.func_76128_c((double)bb.field_72340_a); x < MathHelper.func_76128_c((double)bb.field_72336_d) + 1; ++x) {
            for (int z = MathHelper.func_76128_c((double)bb.field_72339_c); z < MathHelper.func_76128_c((double)bb.field_72334_f) + 1; ++z) {
                Block block = EntityUtil.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                if (block instanceof BlockAir) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    public static void resetTimer() {
        Minecraft.func_71410_x().field_71428_T.field_194149_e = 50.0f;
    }

    public static BlockPos getFlooredPos(Entity e) {
        return new BlockPos(Math.floor(e.field_70165_t), Math.floor(e.field_70163_u), Math.floor(e.field_70161_v));
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)time, entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)time, entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)time);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
        return new Vec3d(entity.field_70142_S, entity.field_70137_T, entity.field_70136_U).func_178787_e(EntityUtil.getInterpolatedAmount(entity, partialTicks));
    }

    public static Vec3d getInterpolatedRenderPos(Entity entity, float partialTicks) {
        return EntityUtil.getInterpolatedPos(entity, partialTicks).func_178786_a(EntityUtil.mc.func_175598_ae().field_78725_b, EntityUtil.mc.func_175598_ae().field_78726_c, EntityUtil.mc.func_175598_ae().field_78723_d);
    }

    public static Vec3d getInterpolatedRenderPos(Vec3d vec) {
        return new Vec3d(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c).func_178786_a(EntityUtil.mc.func_175598_ae().field_78725_b, EntityUtil.mc.func_175598_ae().field_78726_c, EntityUtil.mc.func_175598_ae().field_78723_d);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.field_70165_t - entity.field_70142_S) * x, (entity.field_70163_u - entity.field_70137_T) * y, (entity.field_70161_v - entity.field_70136_U) * z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
        return EntityUtil.getInterpolatedAmount(entity, vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
        return EntityUtil.getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (EntityUtill.mc.field_71439_g != null && EntityUtill.mc.field_71439_g.func_70644_a(Potion.func_188412_a((int)1))) {
            int amplifier = EntityUtill.mc.field_71439_g.func_70660_b(Potion.func_188412_a((int)1)).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static boolean isPassive(Entity entity) {
        if (entity instanceof EntityWolf && ((EntityWolf)entity).func_70919_bu()) {
            return false;
        }
        if (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid) {
            return true;
        }
        return entity instanceof EntityIronGolem && ((EntityIronGolem)entity).func_70643_av() == null;
    }

    public static boolean isSafe(Entity entity, int height, boolean floor, boolean face) {
        return EntityUtill.getUnsafeBlocks(entity, height, floor).size() == 0;
    }

    public static boolean stopSneaking(boolean isSneaking) {
        if (isSneaking && EntityUtill.mc.field_71439_g != null) {
            EntityUtill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)EntityUtil.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return false;
    }

    public static boolean isSafe(Entity entity) {
        return EntityUtill.isSafe(entity, 0, false, true);
    }

    public static BlockPos getPlayerPos(EntityPlayer player) {
        return new BlockPos(Math.floor(player.field_70165_t), Math.floor(player.field_70163_u), Math.floor(player.field_70161_v));
    }

    public static List<Vec3d> getUnsafeBlocks(Entity entity, int height, boolean floor) {
        return EntityUtill.getUnsafeBlocksFromVec3d(entity.func_174791_d(), height, floor);
    }

    public static boolean isMobAggressive(Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).func_184734_db() || ((EntityPigZombie)entity).func_175457_ck()) {
                return true;
            }
        } else {
            if (entity instanceof EntityWolf) {
                return ((EntityWolf)entity).func_70919_bu() && !EntityUtil.mc.field_71439_g.equals((Object)((EntityWolf)entity).func_70902_q());
            }
            if (entity instanceof EntityEnderman) {
                return ((EntityEnderman)entity).func_70823_r();
            }
        }
        return EntityUtil.isHostileMob(entity);
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }

    public static boolean isProjectile(Entity entity) {
        return entity instanceof EntityShulkerBullet || entity instanceof EntityFireball;
    }

    public static boolean isVehicle(Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }

    public static boolean isFriendlyMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.CREATURE, false) && !EntityUtil.isNeutralMob(entity) || entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof EntityVillager || entity instanceof EntityIronGolem || EntityUtil.isNeutralMob(entity) && !EntityUtil.isMobAggressive(entity);
    }

    public static boolean isHostileMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !EntityUtil.isNeutralMob(entity);
    }

    public static List<Vec3d> getUnsafeBlocksFromVec3d(Vec3d pos, int height, boolean floor) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        for (Vec3d vector : EntityUtill.getOffsets(height, floor)) {
            BlockPos targetPos = new BlockPos(pos).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
            Block block = EntityUtil.mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static boolean isInHole(Entity entity) {
        return EntityUtil.isBlockValid(new BlockPos(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
    }

    public static boolean isBlockValid(BlockPos blockPos) {
        return EntityUtil.isBedrockHole(blockPos) || EntityUtil.isObbyHole(blockPos) || EntityUtil.isBothHole(blockPos);
    }

    public static boolean isObbyHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = EntityUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_177230_c() == Blocks.field_150343_Z) continue;
            return false;
        }
        return true;
    }

    public static boolean isBedrockHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = EntityUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && touchingState.func_177230_c() == Blocks.field_150357_h) continue;
            return false;
        }
        return true;
    }

    public static boolean isBothHole(BlockPos blockPos) {
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e(), blockPos.func_177977_b()};
        for (BlockPos pos : touchingBlocks) {
            IBlockState touchingState = EntityUtil.mc.field_71441_e.func_180495_p(pos);
            if (touchingState.func_177230_c() != Blocks.field_150350_a && (touchingState.func_177230_c() == Blocks.field_150357_h || touchingState.func_177230_c() == Blocks.field_150343_Z)) continue;
            return false;
        }
        return true;
    }

    public static Vec3d[] getUnsafeBlockArray(Entity entity, int height, boolean floor) {
        List<Vec3d> list = EntityUtill.getUnsafeBlocks(entity, height, floor);
        Vec3d[] array = new Vec3d[list.size()];
        return list.toArray((T[])array);
    }

    public static Vec3d[] getUnsafeBlockArrayFromVec3d(Vec3d pos, int height, boolean floor) {
        List<Vec3d> list = EntityUtill.getUnsafeBlocksFromVec3d(pos, height, floor);
        Vec3d[] array = new Vec3d[list.size()];
        return list.toArray((T[])array);
    }

    public static double getDst(Vec3d vec) {
        return EntityUtill.mc.field_71439_g.func_174791_d().func_72438_d(vec);
    }

    public static boolean isTrapped(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        return EntityUtill.getUntrappedBlocks(player, antiScaffold, antiStep, legs, platform, antiDrop).size() == 0;
    }

    public static boolean isTrappedExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
        return EntityUtill.getUntrappedBlocksExtended(extension, player, antiScaffold, antiStep, legs, platform, antiDrop, raytrace).size() == 0;
    }

    public static List<Vec3d> getUntrappedBlocks(EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        ArrayList<Vec3d> vec3ds = new ArrayList<Vec3d>();
        if (!antiStep && EntityUtill.getUnsafeBlocks((Entity)player, 2, false).size() == 4) {
            vec3ds.addAll(EntityUtill.getUnsafeBlocks((Entity)player, 2, false));
        }
        for (int i = 0; i < EntityUtill.getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop).length; ++i) {
            Vec3d vector = EntityUtill.getTrapOffsets(antiScaffold, antiStep, legs, platform, antiDrop)[i];
            BlockPos targetPos = new BlockPos(player.func_174791_d()).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
            Block block = EntityUtill.mc.field_71441_e.func_180495_p(targetPos).func_177230_c();
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            vec3ds.add(vector);
        }
        return vec3ds;
    }

    public static boolean isInWater(Entity entity) {
        if (entity == null) {
            return false;
        }
        double y = entity.field_70163_u + 0.01;
        for (int x = MathHelper.func_76128_c((double)entity.field_70165_t); x < MathHelper.func_76143_f((double)entity.field_70165_t); ++x) {
            for (int z = MathHelper.func_76128_c((double)entity.field_70161_v); z < MathHelper.func_76143_f((double)entity.field_70161_v); ++z) {
                BlockPos pos = new BlockPos(x, (int)y, z);
                if (!(EntityUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean isDrivenByPlayer(Entity entityIn) {
        return EntityUtil.mc.field_71439_g != null && entityIn != null && entityIn.equals((Object)EntityUtil.mc.field_71439_g.func_184187_bx());
    }

    public static boolean isPlayer(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    public static boolean isAboveWater(Entity entity) {
        return EntityUtil.isAboveWater(entity, false);
    }

    public static boolean isAboveWater(Entity entity, boolean packet) {
        if (entity == null) {
            return false;
        }
        double y = entity.field_70163_u - (packet ? 0.03 : (EntityUtil.isPlayer(entity) ? 0.2 : 0.5));
        for (int x = MathHelper.func_76128_c((double)entity.field_70165_t); x < MathHelper.func_76143_f((double)entity.field_70165_t); ++x) {
            for (int z = MathHelper.func_76128_c((double)entity.field_70161_v); z < MathHelper.func_76143_f((double)entity.field_70161_v); ++z) {
                BlockPos pos = new BlockPos(x, MathHelper.func_76128_c((double)y), z);
                if (!(EntityUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid)) continue;
                return true;
            }
        }
        return false;
    }

    public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.field_70165_t - px;
        double diry = me.field_70163_u - py;
        double dirz = me.field_70161_v - pz;
        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        double yaw = Math.atan2(dirz /= len, dirx /= len);
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;
        return new double[]{yaw += 90.0, pitch};
    }

    public static List<Vec3d> getUntrappedBlocksExtended(int extension, EntityPlayer player, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (extension == 1) {
            placeTargets.addAll(EntityUtill.targets(player.func_174791_d(), antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
        } else {
            int extend = 1;
            for (Vec3d vec3d : MathUtil.getBlockBlocks((Entity)player)) {
                if (extend > extension) break;
                placeTargets.addAll(EntityUtill.targets(vec3d, antiScaffold, antiStep, legs, platform, antiDrop, raytrace));
                ++extend;
            }
        }
        ArrayList<Vec3d> removeList = new ArrayList<Vec3d>();
        for (Vec3d vec3d : placeTargets) {
            BlockPos pos = new BlockPos(vec3d);
            if (BlockUtil.isPositionPlaceable(pos, raytrace) != -1) continue;
            removeList.add(vec3d);
        }
        for (Vec3d vec3d : removeList) {
            placeTargets.remove((Object)vec3d);
        }
        return placeTargets;
    }

    public static List<Vec3d> targets(Vec3d vec3d, boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop, boolean raytrace) {
        ArrayList<Vec3d> placeTargets = new ArrayList<Vec3d>();
        if (antiDrop) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiDropOffsetList));
        }
        if (platform) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, platformOffsetList));
        }
        if (legs) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, legOffsetList));
        }
        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, OffsetList));
        if (antiStep) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiStepOffsetList));
        } else {
            List<Vec3d> vec3ds = EntityUtill.getUnsafeBlocksFromVec3d(vec3d, 2, false);
            if (vec3ds.size() == 4) {
                block5: for (Vec3d vector : vec3ds) {
                    BlockPos position = new BlockPos(vec3d).func_177963_a(vector.field_72450_a, vector.field_72448_b, vector.field_72449_c);
                    switch (BlockUtil.isPositionPlaceable(position, raytrace)) {
                        case 0: {
                            break;
                        }
                        case -1: 
                        case 1: 
                        case 2: {
                            continue block5;
                        }
                        case 3: {
                            placeTargets.add(vec3d.func_178787_e(vector));
                        }
                    }
                    if (antiScaffold) {
                        Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
                    }
                    return placeTargets;
                }
            }
        }
        if (antiScaffold) {
            Collections.addAll(placeTargets, BlockUtil.convertVec3ds(vec3d, antiScaffoldOffsetList));
        }
        return placeTargets;
    }

    public static List<Vec3d> getOffsetList(int y, boolean floor) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        offsets.add(new Vec3d(-1.0, (double)y, 0.0));
        offsets.add(new Vec3d(1.0, (double)y, 0.0));
        offsets.add(new Vec3d(0.0, (double)y, -1.0));
        offsets.add(new Vec3d(0.0, (double)y, 1.0));
        if (floor) {
            offsets.add(new Vec3d(0.0, (double)(y - 1), 0.0));
        }
        return offsets;
    }

    public static Vec3d[] getOffsets(int y, boolean floor) {
        List<Vec3d> offsets = EntityUtill.getOffsetList(y, floor);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static Vec3d[] getTrapOffsets(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        List<Vec3d> offsets = EntityUtill.getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop);
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static List<Vec3d> getTrapOffsetsList(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>(EntityUtill.getOffsetList(1, false));
        offsets.add(new Vec3d(0.0, 2.0, 0.0));
        if (antiScaffold) {
            offsets.add(new Vec3d(0.0, 3.0, 0.0));
        }
        if (antiStep) {
            offsets.addAll(EntityUtill.getOffsetList(2, false));
        }
        if (legs) {
            offsets.addAll(EntityUtill.getOffsetList(0, false));
        }
        if (platform) {
            offsets.addAll(EntityUtill.getOffsetList(-1, false));
            offsets.add(new Vec3d(0.0, -1.0, 0.0));
        }
        if (antiDrop) {
            offsets.add(new Vec3d(0.0, -2.0, 0.0));
        }
        return offsets;
    }

    public static Vec3d[] getHeightOffsets(int min, int max) {
        ArrayList<Vec3d> offsets = new ArrayList<Vec3d>();
        for (int i = min; i <= max; ++i) {
            offsets.add(new Vec3d(0.0, (double)i, 0.0));
        }
        Vec3d[] array = new Vec3d[offsets.size()];
        return offsets.toArray((T[])array);
    }

    public static BlockPos getRoundedBlockPos(Entity entity) {
        return new BlockPos(MathUtil.roundVec(entity.func_174791_d(), 0));
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isAlive(Entity entity) {
        return EntityUtil.isLiving(entity) && !entity.field_70128_L && ((EntityLivingBase)entity).func_110143_aJ() > 0.0f;
    }

    public static boolean isDead(Entity entity) {
        return !EntityUtil.isAlive(entity);
    }

    public static float getHealth(Entity entity) {
        if (EntityUtil.isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.func_110143_aJ() + livingBase.func_110139_bj();
        }
        return 0.0f;
    }

    public static float getHealth(Entity entity, boolean absorption) {
        if (EntityUtil.isLiving(entity)) {
            EntityLivingBase livingBase = (EntityLivingBase)entity;
            return livingBase.func_110143_aJ() + (absorption ? livingBase.func_110139_bj() : 0.0f);
        }
        return 0.0f;
    }

    public static boolean canEntityFeetBeSeen(Entity entityIn) {
        return EntityUtil.mc.field_71441_e.func_147447_a(new Vec3d(EntityUtil.mc.field_71439_g.field_70165_t, EntityUtil.mc.field_71439_g.field_70165_t + (double)EntityUtil.mc.field_71439_g.func_70047_e(), EntityUtil.mc.field_71439_g.field_70161_v), new Vec3d(entityIn.field_70165_t, entityIn.field_70163_u, entityIn.field_70161_v), false, true, false) == null;
    }

    public static boolean isntValid(Entity entity, double range) {
        return entity == null || EntityUtil.isDead(entity) || entity.equals((Object)EntityUtil.mc.field_71439_g) || entity instanceof EntityPlayer && Oyvey.friendManager.isFriend(entity.func_70005_c_()) || EntityUtil.mc.field_71439_g.func_70068_e(entity) > MathUtil.square(range);
    }

    public static boolean isValid(Entity entity, double range) {
        return !EntityUtil.isntValid(entity, range);
    }

    public static boolean holdingWeapon(EntityPlayer player) {
        return player.func_184614_ca().func_77973_b() instanceof ItemSword || player.func_184614_ca().func_77973_b() instanceof ItemAxe;
    }

    public static double getMaxSpeed() {
        double maxModifier = 0.2873;
        if (EntityUtil.mc.field_71439_g.func_70644_a(Objects.requireNonNull(Potion.func_188412_a((int)1)))) {
            maxModifier *= 1.0 + 0.2 * (double)(Objects.requireNonNull(EntityUtil.mc.field_71439_g.func_70660_b(Objects.requireNonNull(Potion.func_188412_a((int)1)))).func_76458_c() + 1);
        }
        return maxModifier;
    }

    public static void mutliplyEntitySpeed(Entity entity, double multiplier) {
        if (entity != null) {
            entity.field_70159_w *= multiplier;
            entity.field_70179_y *= multiplier;
        }
    }

    public static boolean isEntityMoving(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return EntityUtil.mc.field_71474_y.field_74351_w.func_151470_d() || EntityUtil.mc.field_71474_y.field_74368_y.func_151470_d() || EntityUtil.mc.field_71474_y.field_74370_x.func_151470_d() || EntityUtil.mc.field_71474_y.field_74366_z.func_151470_d();
        }
        return entity.field_70159_w != 0.0 || entity.field_70181_x != 0.0 || entity.field_70179_y != 0.0;
    }

    public static double getEntitySpeed(Entity entity) {
        if (entity != null) {
            double distTraveledLastTickX = entity.field_70165_t - entity.field_70169_q;
            double distTraveledLastTickZ = entity.field_70161_v - entity.field_70166_s;
            double speed = MathHelper.func_76133_a((double)(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ));
            return speed * 20.0;
        }
        return 0.0;
    }

    public static boolean is32k(ItemStack stack) {
        return EnchantmentHelper.func_77506_a((Enchantment)Enchantments.field_185302_k, (ItemStack)stack) >= 1000;
    }

    public static void moveEntityStrafe(double speed, Entity entity) {
        if (entity != null) {
            MovementInput movementInput = EntityUtil.mc.field_71439_g.field_71158_b;
            double forward = movementInput.field_192832_b;
            double strafe = movementInput.field_78902_a;
            float yaw = EntityUtil.mc.field_71439_g.field_70177_z;
            if (forward == 0.0 && strafe == 0.0) {
                entity.field_70159_w = 0.0;
                entity.field_70179_y = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                entity.field_70159_w = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                entity.field_70179_y = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        }
    }

    public static boolean rayTraceHitCheck(Entity entity, boolean shouldCheck) {
        return !shouldCheck || EntityUtil.mc.field_71439_g.func_70685_l(entity);
    }

    public static Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
        Color color = new Color((float)red / 255.0f, (float)green / 255.0f, (float)blue / 255.0f, (float)alpha / 255.0f);
        if (entity instanceof EntityPlayer && colorFriends && Oyvey.friendManager.isFriend((EntityPlayer)entity)) {
            color = new Color(0.33333334f, 1.0f, 1.0f, (float)alpha / 255.0f);
        }
        return color;
    }

    public static boolean isMoving() {
        return (double)EntityUtil.mc.field_71439_g.field_191988_bg != 0.0 || (double)EntityUtil.mc.field_71439_g.field_70702_br != 0.0;
    }

    public static boolean isMoving(EntityLivingBase entity) {
        return entity.field_191988_bg != 0.0f || entity.field_70702_br != 0.0f;
    }

    public static EntityPlayer getClosestEnemy(double distance) {
        EntityPlayer closest = null;
        for (EntityPlayer player : EntityUtil.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.isntValid((Entity)player, distance)) continue;
            if (closest == null) {
                closest = player;
                continue;
            }
            if (!(EntityUtil.mc.field_71439_g.func_70068_e((Entity)player) < EntityUtil.mc.field_71439_g.func_70068_e((Entity)closest))) continue;
            closest = player;
        }
        return closest;
    }

    public static boolean checkCollide() {
        if (EntityUtil.mc.field_71439_g.func_70093_af()) {
            return false;
        }
        if (EntityUtil.mc.field_71439_g.func_184187_bx() != null && EntityUtil.mc.field_71439_g.func_184187_bx().field_70143_R >= 3.0f) {
            return false;
        }
        return EntityUtil.mc.field_71439_g.field_70143_R < 3.0f;
    }

    public static BlockPos getPlayerPosWithEntity() {
        return new BlockPos(EntityUtil.mc.field_71439_g.func_184187_bx() != null ? EntityUtil.mc.field_71439_g.func_184187_bx().field_70165_t : EntityUtil.mc.field_71439_g.field_70165_t, EntityUtil.mc.field_71439_g.func_184187_bx() != null ? EntityUtil.mc.field_71439_g.func_184187_bx().field_70163_u : EntityUtil.mc.field_71439_g.field_70163_u, EntityUtil.mc.field_71439_g.func_184187_bx() != null ? EntityUtil.mc.field_71439_g.func_184187_bx().field_70161_v : EntityUtil.mc.field_71439_g.field_70161_v);
    }

    public static boolean isCrystalAtFeet(EntityEnderCrystal crystal, double range) {
        for (EntityPlayer player : EntityUtil.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.mc.field_71439_g.func_70068_e((Entity)player) > range * range || Oyvey.friendManager.isFriend(player)) continue;
            for (Vec3d vec : EntityUtil.doubleLegOffsetList) {
                if (new BlockPos(player.func_174791_d()).func_177963_a(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c) != crystal.func_180425_c()) continue;
                return true;
            }
        }
        return false;
    }

    public static double[] forward(double speed) {
        float forward = EntityUtil.mc.field_71439_g.field_71158_b.field_192832_b;
        float side = EntityUtil.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = EntityUtil.mc.field_71439_g.field_70126_B + (EntityUtil.mc.field_71439_g.field_70177_z - EntityUtil.mc.field_71439_g.field_70126_B) * mc.func_184121_ak();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += (float)(forward > 0.0f ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += (float)(forward > 0.0f ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        double posX = (double)forward * speed * cos + (double)side * speed * sin;
        double posZ = (double)forward * speed * sin - (double)side * speed * cos;
        return new double[]{posX, posZ};
    }

    public static void swingArmNoPacket(EnumHand hand, EntityLivingBase entity) {
        ItemStack stack = entity.func_184586_b(hand);
        if (!stack.func_190926_b() && stack.func_77973_b().onEntitySwing(entity, stack)) {
            return;
        }
        if (!entity.field_82175_bq || entity.field_110158_av >= ((IEntityLivingBase)entity).getArmSwingAnimationEnd() / 2 || entity.field_110158_av < 0) {
            entity.field_110158_av = -1;
            entity.field_82175_bq = true;
            entity.field_184622_au = hand;
        }
    }

    public static Map<String, Integer> getTextRadarPlayers() {
        Map<String, Integer> output = new HashMap<String, Integer>();
        DecimalFormat dfHealth = new DecimalFormat("#.#");
        dfHealth.setRoundingMode(RoundingMode.CEILING);
        DecimalFormat dfDistance = new DecimalFormat("#.#");
        dfDistance.setRoundingMode(RoundingMode.CEILING);
        StringBuilder healthSB = new StringBuilder();
        StringBuilder distanceSB = new StringBuilder();
        for (EntityPlayer player : EntityUtil.mc.field_71441_e.field_73010_i) {
            if (player.func_82150_aj() || player.func_70005_c_().equals(EntityUtil.mc.field_71439_g.func_70005_c_())) continue;
            int hpRaw = (int)EntityUtil.getHealth((Entity)player);
            String hp = dfHealth.format(hpRaw);
            healthSB.append("\u00c2\u00a7");
            if (hpRaw >= 20) {
                healthSB.append("a");
            } else if (hpRaw >= 10) {
                healthSB.append("e");
            } else if (hpRaw >= 5) {
                healthSB.append("6");
            } else {
                healthSB.append("c");
            }
            healthSB.append(hp);
            int distanceInt = (int)EntityUtil.mc.field_71439_g.func_70032_d((Entity)player);
            String distance = dfDistance.format(distanceInt);
            distanceSB.append("\u00c2\u00a7");
            if (distanceInt >= 25) {
                distanceSB.append("a");
            } else if (distanceInt > 10) {
                distanceSB.append("6");
            } else {
                distanceSB.append("c");
            }
            distanceSB.append(distance);
            output.put(healthSB.toString() + " " + (Object)(Oyvey.friendManager.isFriend(player) ? ChatFormatting.AQUA : ChatFormatting.RED) + player.func_70005_c_() + " " + distanceSB.toString() + " \u00c2\u00a7f0", (int)EntityUtil.mc.field_71439_g.func_70032_d((Entity)player));
            healthSB.setLength(0);
            distanceSB.setLength(0);
        }
        if (!output.isEmpty()) {
            output = MathUtil.sortByValue(output, false);
        }
        return output;
    }

    public static void setTimer(float speed) {
        Minecraft.func_71410_x().field_71428_T.field_194149_e = 50.0f / speed;
    }

    public static Block isColliding(double posX, double posY, double posZ) {
        Block block = null;
        if (EntityUtill.mc.field_71439_g != null) {
            AxisAlignedBB bb = EntityUtill.mc.field_71439_g.func_184187_bx() != null ? EntityUtill.mc.field_71439_g.func_184187_bx().func_174813_aQ().func_191195_a(0.0, 0.0, 0.0).func_72317_d(posX, posY, posZ) : EntityUtill.mc.field_71439_g.func_174813_aQ().func_191195_a(0.0, 0.0, 0.0).func_72317_d(posX, posY, posZ);
            int y = (int)bb.field_72338_b;
            for (int x = MathHelper.func_76128_c((double)bb.field_72340_a); x < MathHelper.func_76128_c((double)bb.field_72336_d) + 1; ++x) {
                for (int z = MathHelper.func_76128_c((double)bb.field_72339_c); z < MathHelper.func_76128_c((double)bb.field_72334_f) + 1; ++z) {
                    block = EntityUtill.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                }
            }
        }
        return block;
    }

    public static boolean isAboveBlock(Entity entity, BlockPos blockPos) {
        return entity.field_70163_u >= (double)blockPos.func_177956_o();
    }
}

