/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockFence
 *  net.minecraft.block.BlockFenceGate
 *  net.minecraft.block.BlockWall
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.util.EnumFacing$Axis
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package me.earth.phobos.mixin.mixins;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.earth.phobos.event.events.PushEvent;
import me.earth.phobos.event.events.StepEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={Entity.class})
public abstract class MixinEntity {
    @Shadow
    public double field_70165_t;
    @Shadow
    public double field_70163_u;
    @Shadow
    public double field_70161_v;
    @Shadow
    public double field_70159_w;
    @Shadow
    public double field_70181_x;
    @Shadow
    public double field_70179_y;
    @Shadow
    public float field_70177_z;
    @Shadow
    public float field_70125_A;
    @Shadow
    public boolean field_70122_E;
    @Shadow
    public boolean field_70145_X;
    @Shadow
    public float field_70141_P;
    @Shadow
    public World field_70170_p;
    @Shadow
    @Final
    private double[] field_191505_aI;
    @Shadow
    private long field_191506_aJ;
    @Shadow
    protected boolean field_70134_J;
    @Shadow
    public float field_70138_W;
    @Shadow
    public boolean field_70123_F;
    @Shadow
    public boolean field_70124_G;
    @Shadow
    public boolean field_70132_H;
    @Shadow
    public float field_70140_Q;
    @Shadow
    public float field_82151_R;
    @Shadow
    private int field_190534_ay;
    @Shadow
    private int field_70150_b;
    @Shadow
    private float field_191959_ay;
    @Shadow
    protected Random field_70146_Z;

    @Shadow
    public abstract boolean func_70051_ag();

    @Shadow
    public abstract boolean func_184218_aH();

    @Shadow
    public abstract boolean func_70093_af();

    @Shadow
    public abstract void func_174826_a(AxisAlignedBB var1);

    @Shadow
    public abstract AxisAlignedBB func_174813_aQ();

    @Shadow
    public abstract void func_174829_m();

    @Shadow
    protected abstract void func_184231_a(double var1, boolean var3, IBlockState var4, BlockPos var5);

    @Shadow
    protected abstract boolean func_70041_e_();

    @Shadow
    public abstract boolean func_70090_H();

    @Shadow
    public abstract boolean func_184207_aI();

    @Shadow
    public abstract Entity func_184179_bs();

    @Shadow
    public abstract void func_184185_a(SoundEvent var1, float var2, float var3);

    @Shadow
    protected abstract void func_145775_I();

    @Shadow
    public abstract boolean func_70026_G();

    @Shadow
    protected abstract void func_180429_a(BlockPos var1, Block var2);

    @Shadow
    protected abstract SoundEvent func_184184_Z();

    @Shadow
    protected abstract float func_191954_d(float var1);

    @Shadow
    protected abstract boolean func_191957_ae();

    @Shadow
    public abstract void func_85029_a(CrashReportCategory var1);

    @Shadow
    protected abstract void func_70081_e(int var1);

    @Shadow
    public abstract void func_70015_d(int var1);

    @Shadow
    protected abstract int func_190531_bD();

    @Shadow
    public abstract boolean func_70027_ad();

    @Shadow
    public abstract int func_82145_z();

    @Overwrite
    public void func_70091_d(MoverType type, double x, double y, double z) {
        Entity _this = (Entity)this;
        if (this.field_70145_X) {
            this.func_174826_a(this.func_174813_aQ().func_72317_d(x, y, z));
            this.func_174829_m();
        } else {
            BlockPos blockpos1;
            IBlockState iblockstate1;
            Block block1;
            if (type == MoverType.PISTON) {
                long i = this.field_70170_p.func_82737_E();
                if (i != this.field_191506_aJ) {
                    Arrays.fill(this.field_191505_aI, 0.0);
                    this.field_191506_aJ = i;
                }
                if (x != 0.0) {
                    int j = EnumFacing.Axis.X.ordinal();
                    double d0 = MathHelper.func_151237_a((double)(x + this.field_191505_aI[j]), (double)-0.51, (double)0.51);
                    x = d0 - this.field_191505_aI[j];
                    this.field_191505_aI[j] = d0;
                    if (Math.abs(x) <= (double)1.0E-5f) {
                        return;
                    }
                } else if (y != 0.0) {
                    int l4 = EnumFacing.Axis.Y.ordinal();
                    double d12 = MathHelper.func_151237_a((double)(y + this.field_191505_aI[l4]), (double)-0.51, (double)0.51);
                    y = d12 - this.field_191505_aI[l4];
                    this.field_191505_aI[l4] = d12;
                    if (Math.abs(y) <= (double)1.0E-5f) {
                        return;
                    }
                } else {
                    if (z == 0.0) {
                        return;
                    }
                    int i5 = EnumFacing.Axis.Z.ordinal();
                    double d13 = MathHelper.func_151237_a((double)(z + this.field_191505_aI[i5]), (double)-0.51, (double)0.51);
                    z = d13 - this.field_191505_aI[i5];
                    this.field_191505_aI[i5] = d13;
                    if (Math.abs(z) <= (double)1.0E-5f) {
                        return;
                    }
                }
            }
            this.field_70170_p.field_72984_F.func_76320_a("move");
            double d10 = this.field_70165_t;
            double d11 = this.field_70163_u;
            double d1 = this.field_70161_v;
            if (this.field_70134_J) {
                this.field_70134_J = false;
                x *= 0.25;
                y *= (double)0.05f;
                z *= 0.25;
                this.field_70159_w = 0.0;
                this.field_70181_x = 0.0;
                this.field_70179_y = 0.0;
            }
            double d2 = x;
            double d3 = y;
            double d4 = z;
            if ((type == MoverType.SELF || type == MoverType.PLAYER) && this.field_70122_E && this.func_70093_af() && _this instanceof EntityPlayer) {
                double d5 = 0.05;
                while (x != 0.0 && this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72317_d(x, (double)(-this.field_70138_W), 0.0)).isEmpty()) {
                    d2 = x = x < 0.05 && x >= -0.05 ? 0.0 : (x > 0.0 ? (x = x - 0.05) : (x = x + 0.05));
                }
                while (z != 0.0 && this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72317_d(0.0, (double)(-this.field_70138_W), z)).isEmpty()) {
                    d4 = z = z < 0.05 && z >= -0.05 ? 0.0 : (z > 0.0 ? (z = z - 0.05) : (z = z + 0.05));
                }
                while (x != 0.0 && z != 0.0 && this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72317_d(x, (double)(-this.field_70138_W), z)).isEmpty()) {
                    d2 = x = x < 0.05 && x >= -0.05 ? 0.0 : (x > 0.0 ? (x = x - 0.05) : (x = x + 0.05));
                    d4 = z = z < 0.05 && z >= -0.05 ? 0.0 : (z > 0.0 ? (z = z - 0.05) : (z = z + 0.05));
                }
            }
            List list1 = this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72321_a(x, y, z));
            AxisAlignedBB axisalignedbb = this.func_174813_aQ();
            if (y != 0.0) {
                int l = list1.size();
                for (int k = 0; k < l; ++k) {
                    y = ((AxisAlignedBB)list1.get(k)).func_72323_b(this.func_174813_aQ(), y);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
            }
            if (x != 0.0) {
                int l5 = list1.size();
                for (int j5 = 0; j5 < l5; ++j5) {
                    x = ((AxisAlignedBB)list1.get(j5)).func_72316_a(this.func_174813_aQ(), x);
                }
                if (x != 0.0) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(x, 0.0, 0.0));
                }
            }
            if (z != 0.0) {
                int i6 = list1.size();
                for (int k5 = 0; k5 < i6; ++k5) {
                    z = ((AxisAlignedBB)list1.get(k5)).func_72322_c(this.func_174813_aQ(), z);
                }
                if (z != 0.0) {
                    this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 0.0, z));
                }
            }
            boolean flag = this.field_70122_E || d3 != y && d3 < 0.0;
            boolean bl = flag;
            if (this.field_70138_W > 0.0f && flag && (d2 != x || d4 != z)) {
                StepEvent preEvent = new StepEvent(0, _this);
                MinecraftForge.EVENT_BUS.post((Event)preEvent);
                double d14 = x;
                double d6 = y;
                double d7 = z;
                AxisAlignedBB axisalignedbb1 = this.func_174813_aQ();
                this.func_174826_a(axisalignedbb);
                y = preEvent.getHeight();
                List list = this.field_70170_p.func_184144_a(_this, this.func_174813_aQ().func_72321_a(d2, y, d4));
                AxisAlignedBB axisalignedbb2 = this.func_174813_aQ();
                AxisAlignedBB axisalignedbb3 = axisalignedbb2.func_72321_a(d2, 0.0, d4);
                double d8 = y;
                int k1 = list.size();
                for (int j1 = 0; j1 < k1; ++j1) {
                    d8 = ((AxisAlignedBB)list.get(j1)).func_72323_b(axisalignedbb3, d8);
                }
                axisalignedbb2 = axisalignedbb2.func_72317_d(0.0, d8, 0.0);
                double d18 = d2;
                int i2 = list.size();
                for (int l1 = 0; l1 < i2; ++l1) {
                    d18 = ((AxisAlignedBB)list.get(l1)).func_72316_a(axisalignedbb2, d18);
                }
                axisalignedbb2 = axisalignedbb2.func_72317_d(d18, 0.0, 0.0);
                double d19 = d4;
                int k2 = list.size();
                for (int j2 = 0; j2 < k2; ++j2) {
                    d19 = ((AxisAlignedBB)list.get(j2)).func_72322_c(axisalignedbb2, d19);
                }
                axisalignedbb2 = axisalignedbb2.func_72317_d(0.0, 0.0, d19);
                AxisAlignedBB axisalignedbb4 = this.func_174813_aQ();
                double d20 = y;
                int i3 = list.size();
                for (int l2 = 0; l2 < i3; ++l2) {
                    d20 = ((AxisAlignedBB)list.get(l2)).func_72323_b(axisalignedbb4, d20);
                }
                axisalignedbb4 = axisalignedbb4.func_72317_d(0.0, d20, 0.0);
                double d21 = d2;
                int k3 = list.size();
                for (int j3 = 0; j3 < k3; ++j3) {
                    d21 = ((AxisAlignedBB)list.get(j3)).func_72316_a(axisalignedbb4, d21);
                }
                axisalignedbb4 = axisalignedbb4.func_72317_d(d21, 0.0, 0.0);
                double d22 = d4;
                int i4 = list.size();
                for (int l3 = 0; l3 < i4; ++l3) {
                    d22 = ((AxisAlignedBB)list.get(l3)).func_72322_c(axisalignedbb4, d22);
                }
                axisalignedbb4 = axisalignedbb4.func_72317_d(0.0, 0.0, d22);
                double d23 = d18 * d18 + d19 * d19;
                double d9 = d21 * d21 + d22 * d22;
                if (d23 > d9) {
                    x = d18;
                    z = d19;
                    y = -d8;
                    this.func_174826_a(axisalignedbb2);
                } else {
                    x = d21;
                    z = d22;
                    y = -d20;
                    this.func_174826_a(axisalignedbb4);
                }
                int k4 = list.size();
                for (int j4 = 0; j4 < k4; ++j4) {
                    y = ((AxisAlignedBB)list.get(j4)).func_72323_b(this.func_174813_aQ(), y);
                }
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
                if (d14 * d14 + d7 * d7 >= x * x + z * z) {
                    x = d14;
                    y = d6;
                    z = d7;
                    this.func_174826_a(axisalignedbb1);
                } else {
                    StepEvent postEvent = new StepEvent(1, _this);
                    MinecraftForge.EVENT_BUS.post((Event)postEvent);
                }
            }
            this.field_70170_p.field_72984_F.func_76319_b();
            this.field_70170_p.field_72984_F.func_76320_a("rest");
            this.func_174829_m();
            this.field_70123_F = d2 != x || d4 != z;
            this.field_70124_G = d3 != y;
            this.field_70122_E = this.field_70124_G && d3 < 0.0;
            this.field_70132_H = this.field_70123_F || this.field_70124_G;
            int j6 = MathHelper.func_76128_c((double)this.field_70165_t);
            int i1 = MathHelper.func_76128_c((double)(this.field_70163_u - (double)0.2f));
            int k6 = MathHelper.func_76128_c((double)this.field_70161_v);
            BlockPos blockpos = new BlockPos(j6, i1, k6);
            IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
            if (iblockstate.func_185904_a() == Material.field_151579_a && ((block1 = (iblockstate1 = this.field_70170_p.func_180495_p(blockpos1 = blockpos.func_177977_b())).func_177230_c()) instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)) {
                iblockstate = iblockstate1;
                blockpos = blockpos1;
            }
            this.func_184231_a(y, this.field_70122_E, iblockstate, blockpos);
            if (d2 != x) {
                this.field_70159_w = 0.0;
            }
            if (d4 != z) {
                this.field_70179_y = 0.0;
            }
            Block block = iblockstate.func_177230_c();
            if (d3 != y) {
                block.func_176216_a(this.field_70170_p, _this);
            }
            if (!(!this.func_70041_e_() || this.field_70122_E && this.func_70093_af() && _this instanceof EntityPlayer || this.func_184218_aH())) {
                double d15 = this.field_70165_t - d10;
                double d16 = this.field_70163_u - d11;
                double d17 = this.field_70161_v - d1;
                if (block != Blocks.field_150468_ap) {
                    d16 = 0.0;
                }
                if (block != null && this.field_70122_E) {
                    block.func_176199_a(this.field_70170_p, blockpos, _this);
                }
                this.field_70140_Q = (float)((double)this.field_70140_Q + (double)MathHelper.func_76133_a((double)(d15 * d15 + d17 * d17)) * 0.6);
                this.field_82151_R = (float)((double)this.field_82151_R + (double)MathHelper.func_76133_a((double)(d15 * d15 + d16 * d16 + d17 * d17)) * 0.6);
                if (this.field_82151_R > (float)this.field_70150_b && iblockstate.func_185904_a() != Material.field_151579_a) {
                    this.field_70150_b = (int)this.field_82151_R + 1;
                    if (this.func_70090_H()) {
                        Entity entity = this.func_184207_aI() && this.func_184179_bs() != null ? this.func_184179_bs() : _this;
                        float f = entity == _this ? 0.35f : 0.4f;
                        float f1 = MathHelper.func_76133_a((double)(entity.field_70159_w * entity.field_70159_w * (double)0.2f + entity.field_70181_x * entity.field_70181_x + entity.field_70179_y * entity.field_70179_y * (double)0.2f)) * f;
                        if (f1 > 1.0f) {
                            f1 = 1.0f;
                        }
                        this.func_184185_a(this.func_184184_Z(), f1, 1.0f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                    } else {
                        this.func_180429_a(blockpos, block);
                    }
                } else if (this.field_82151_R > this.field_191959_ay && this.func_191957_ae() && iblockstate.func_185904_a() == Material.field_151579_a) {
                    this.field_191959_ay = this.func_191954_d(this.field_82151_R);
                }
            }
            try {
                this.func_145775_I();
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.func_85055_a((Throwable)throwable, (String)"Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
                this.func_85029_a(crashreportcategory);
                throw new ReportedException(crashreport);
            }
            boolean flag1 = this.func_70026_G();
            if (this.field_70170_p.func_147470_e(this.func_174813_aQ().func_186664_h(0.001))) {
                this.func_70081_e(1);
                if (!flag1) {
                    ++this.field_190534_ay;
                    if (this.field_190534_ay == 0) {
                        this.func_70015_d(8);
                    }
                }
            } else if (this.field_190534_ay <= 0) {
                this.field_190534_ay = -this.func_190531_bD();
            }
            if (flag1 && this.func_70027_ad()) {
                this.func_184185_a(SoundEvents.field_187541_bC, 0.7f, 1.6f + (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.4f);
                this.field_190534_ay = -this.func_190531_bD();
            }
            this.field_70170_p.field_72984_F.func_76319_b();
        }
    }

    @Redirect(method={"applyEntityCollision"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocityHook(Entity entity, double x, double y, double z) {
        PushEvent event = new PushEvent(entity, x, y, z, true);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            entity.field_70159_w += event.x;
            entity.field_70181_x += event.y;
            entity.field_70179_y += event.z;
            entity.field_70160_al = event.airbone;
        }
    }
}

