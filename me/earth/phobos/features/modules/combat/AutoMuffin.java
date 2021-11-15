/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 */
package me.earth.phobos.features.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.ClickGui;
import me.earth.phobos.features.modules.combat.AutoCrystall;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.mixin.mixins.accessors.AccessorCPacketUseEntity;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.BlockUtill;
import me.earth.phobos.util.ColorUtill;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.EntityUtill;
import me.earth.phobos.util.ItemUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.RenderUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoMuffin
extends Module {
    private final Setting<Settings> setting = this.register(new Setting<Settings>("Settings", Settings.Place));
    private final Setting<Integer> placeDelay = this.register(new Setting<Object>("Delay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(200), v -> this.setting.getValue() == Settings.Place));
    private final Setting<Float> placeRange = this.register(new Setting<Object>("Range", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), v -> this.setting.getValue() == Settings.Place));
    private final Setting<Integer> breakDelay = this.register(new Setting<Object>("BDelay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(200), v -> this.setting.getValue() == Settings.Break));
    public Setting<Float> breakRange = this.register(new Setting<Object>("BRange", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), v -> this.setting.getValue() == Settings.Break));
    private final Setting<Boolean> cancelcrystal = this.register(new Setting<Object>("SetDead", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.Break));
    private final Setting<InfoMode> infomode = this.register(new Setting<Object>("Info", (Object)InfoMode.Target, v -> this.setting.getValue() == Settings.Render));
    private final Setting<Boolean> offhandS = this.register(new Setting<Object>("OffhandSwing", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Render));
    public Setting<Boolean> render = this.register(new Setting<Object>("Render", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Render));
    private final Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(80), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false));
    private final Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(120), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false));
    private final Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false));
    private final Setting<Integer> alpha = this.register(new Setting<Object>("Alpha", Integer.valueOf(120), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false));
    public Setting<Boolean> colorSync = this.register(new Setting<Object>("ColorSync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false));
    public Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(30), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false && this.box.getValue() != false));
    public Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(0.1f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false && this.outline.getValue() != false));
    public Setting<Boolean> text = this.register(new Setting<Object>("DamageText", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false));
    public Setting<Boolean> customOutline = this.register(new Setting<Object>("CustomLine", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cRed = this.register(new Setting<Object>("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cGreen = this.register(new Setting<Object>("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cBlue = this.register(new Setting<Object>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.Render && this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Float> range = this.register(new Setting<Object>("TargetRange", Float.valueOf(9.5f), Float.valueOf(0.0f), Float.valueOf(16.0f), v -> this.setting.getValue() == Settings.Misc));
    public Setting<Rotate> rotate = this.register(new Setting<Object>("Rotate", (Object)Rotate.OFF, v -> this.setting.getValue() == Settings.Misc));
    public Setting<Integer> rotations = this.register(new Setting<Object>("Spoofs", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(20), v -> this.setting.getValue() == Settings.Misc && this.rotate.getValue() != Rotate.OFF));
    public Setting<Boolean> rotateFirst = this.register(new Setting<Object>("FirstRotation", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.Misc && this.rotate.getValue() != Rotate.OFF));
    public Setting<Raytrace> raytrace = this.register(new Setting<Object>("Raytrace", (Object)Raytrace.None, v -> this.setting.getValue() == Settings.Misc));
    public Setting<Float> placetrace = this.register(new Setting<Object>("Placetrace", Float.valueOf(5.5f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.setting.getValue() == Settings.Misc && this.raytrace.getValue() != Raytrace.None && this.raytrace.getValue() != Raytrace.Break));
    public Setting<Float> breaktrace = this.register(new Setting<Object>("Breaktrace", Float.valueOf(5.5f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.setting.getValue() == Settings.Misc && this.raytrace.getValue() != Raytrace.None && this.raytrace.getValue() != Raytrace.Place));
    private final Setting<Float> breakWallRange = this.register(new Setting<Object>("WallRange", Float.valueOf(4.5f), Float.valueOf(0.0f), Float.valueOf(6.0f), v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Float> minDamage = this.register(new Setting<Object>("MinDamage", Float.valueOf(0.7f), Float.valueOf(0.0f), Float.valueOf(30.0f), v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Float> maxSelf = this.register(new Setting<Object>("MaxSelf", Float.valueOf(18.5f), Float.valueOf(0.0f), Float.valueOf(36.0f), v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Float> lethalMult = this.register(new Setting<Object>("LethalMult", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Float> armorScale = this.register(new Setting<Object>("ArmorBreak", Float.valueOf(100.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Boolean> second = this.register(new Setting<Object>("Second", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.Misc));
    private final Setting<Boolean> autoSwitch = this.register(new Setting<Object>("AutoSwitch", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.Misc));
    private final List<BlockPos> placedList = new ArrayList<BlockPos>();
    private final Timer breakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer renderTimer = new Timer();
    private int rotationPacketsSpoofed = 0;
    public static EntityPlayer currentTarget;
    private BlockPos renderPos = null;
    private double renderDamage = 0.0;
    private BlockPos placePos = null;
    private boolean offHand = false;
    public boolean rotating = false;
    private float pitch = 0.0f;
    private float yaw = 0.0f;
    private boolean offhand;

    public AutoMuffin() {
        super("AutoMuffin", "Based crystal aura.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onToggle() {
        this.placedList.clear();
        this.breakTimer.reset();
        this.placeTimer.reset();
        this.renderTimer.reset();
        currentTarget = null;
        this.renderPos = null;
        this.offhand = false;
        this.rotating = false;
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onTick(TickEvent.ClientTickEvent event) {
        if (AutoMuffin.fullNullCheck()) {
            return;
        }
        if (this.renderTimer.passedMs(500L)) {
            this.placedList.clear();
            this.renderPos = null;
            this.renderTimer.reset();
        }
        this.offhand = ((ItemStack)AutoMuffin.mc.field_71439_g.field_71071_by.field_184439_c.get(0)).func_77973_b() == Items.field_185158_cP;
        currentTarget = EntityUtill.getClosestPlayer(this.range.getValue().floatValue());
        if (currentTarget == null) {
            return;
        }
        this.doPlace();
        if (event.phase == TickEvent.Phase.START) {
            this.doBreak();
        }
    }

    private void doBreak() {
        Entity crystal = null;
        double maxDamage = 0.5;
        int size = AutoMuffin.mc.field_71441_e.field_72996_f.size();
        for (int i = 0; i < size; ++i) {
            float selfDamage;
            float targetDamage;
            Entity entity = (Entity)AutoMuffin.mc.field_71441_e.field_72996_f.get(i);
            if (!(entity instanceof EntityEnderCrystal)) continue;
            float f = AutoMuffin.mc.field_71439_g.func_70032_d(entity);
            Float f2 = AutoMuffin.mc.field_71439_g.func_70685_l(entity) ? this.breakRange.getValue() : this.breakWallRange.getValue();
            if (!(f < f2.floatValue()) || !((targetDamage = EntityUtill.calculate(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, (EntityLivingBase)currentTarget)) > this.minDamage.getValue().floatValue()) && !(targetDamage * this.lethalMult.getValue().floatValue() > currentTarget.func_110143_aJ() + currentTarget.func_110139_bj()) && !ItemUtil.isArmorUnderPercent(currentTarget, this.armorScale.getValue().floatValue()) || !((selfDamage = EntityUtill.calculate(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, (EntityLivingBase)AutoMuffin.mc.field_71439_g)) <= this.maxSelf.getValue().floatValue()) || !(selfDamage + 2.0f <= AutoMuffin.mc.field_71439_g.func_110143_aJ() + AutoMuffin.mc.field_71439_g.func_110139_bj()) || !(selfDamage < targetDamage) || !(maxDamage <= (double)targetDamage)) continue;
            maxDamage = targetDamage;
            crystal = entity;
        }
        if (crystal != null && this.breakTimer.passedMs(this.breakDelay.getValue().intValue())) {
            mc.func_147114_u().func_147297_a((Packet)new CPacketUseEntity(crystal));
            AutoMuffin.mc.field_71439_g.func_184609_a(this.offhandS.getValue() != false ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            this.breakTimer.reset();
        }
    }

    private void doPlace() {
        BlockPos placePos = null;
        double maxDamage = 0.5;
        List<BlockPos> sphere = BlockUtill.getSphereRealth(this.placeRange.getValue().floatValue(), true);
        int size = sphere.size();
        for (int i = 0; i < size; ++i) {
            float selfDamage;
            float targetDamage;
            BlockPos pos = sphere.get(i);
            if (!BlockUtill.canPlaceCrystal(pos, this.second.getValue()) || !((targetDamage = EntityUtill.calculate((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 1.0, (double)pos.func_177952_p() + 0.5, (EntityLivingBase)currentTarget)) > this.minDamage.getValue().floatValue()) && !(targetDamage * this.lethalMult.getValue().floatValue() > currentTarget.func_110143_aJ() + currentTarget.func_110139_bj()) && !ItemUtil.isArmorUnderPercent(currentTarget, this.armorScale.getValue().floatValue()) || !((selfDamage = EntityUtill.calculate((double)pos.func_177958_n() + 0.5, (double)pos.func_177956_o() + 1.0, (double)pos.func_177952_p() + 0.5, (EntityLivingBase)AutoMuffin.mc.field_71439_g)) <= this.maxSelf.getValue().floatValue()) || !(selfDamage + 2.0f <= AutoMuffin.mc.field_71439_g.func_110143_aJ() + AutoMuffin.mc.field_71439_g.func_110139_bj()) || !(selfDamage < targetDamage) || !(maxDamage <= (double)targetDamage)) continue;
            maxDamage = targetDamage;
            placePos = pos;
            this.renderPos = pos;
            this.renderDamage = targetDamage;
        }
        boolean flag = false;
        if (!this.offhand && AutoMuffin.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_185158_cP) {
            flag = true;
            if (!this.autoSwitch.getValue().booleanValue() || AutoMuffin.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() == Items.field_151153_ao && AutoMuffin.mc.field_71439_g.func_184587_cr()) {
                return;
            }
        }
        if (placePos != null) {
            if (this.placeTimer.passedMs(this.placeDelay.getValue().intValue())) {
                if (flag) {
                    int slot = ItemUtil.getItemFromHotbar(Items.field_185158_cP);
                    if (slot == -1) {
                        return;
                    }
                    AutoMuffin.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                }
                this.placedList.add(placePos);
                mc.func_147114_u().func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(placePos, EnumFacing.UP, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                this.placeTimer.reset();
            }
            this.renderPos = placePos;
        }
        for (BlockPos pos2 : BlockUtil.possiblePlacePositions(this.placeRange.getValue().floatValue())) {
            if (BlockUtil.rayTracePlaceCheck(pos2, (this.raytrace.getValue() == Raytrace.Place || this.raytrace.getValue() == Raytrace.Both) && AutoCrystall.mc.field_71439_g.func_174818_b(pos2) > MathUtil.square(this.placetrace.getValue().floatValue()), 1.0f)) continue;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSoundEffect packet2;
        SPacketSpawnObject packet;
        if (event.getPacket() instanceof SPacketSpawnObject && (packet = (SPacketSpawnObject)event.getPacket()).func_148993_l() == 51 && this.placedList.contains((Object)new BlockPos(packet.func_186880_c(), packet.func_186882_d() - 1.0, packet.func_186881_e()))) {
            AccessorCPacketUseEntity use = (AccessorCPacketUseEntity)new CPacketUseEntity();
            use.setEntityId(packet.func_149001_c());
            use.setAction(CPacketUseEntity.Action.ATTACK);
            mc.func_147114_u().func_147297_a((Packet)use);
            AutoMuffin.mc.field_71439_g.func_184609_a(this.offhandS.getValue() != false ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            this.breakTimer.reset();
            return;
        }
        if (event.getPacket() instanceof SPacketSoundEffect && (packet2 = (SPacketSoundEffect)event.getPacket()).func_186977_b() == SoundCategory.BLOCKS && packet2.func_186978_a() == SoundEvents.field_187539_bB) {
            SPacketSoundEffect sPacketSoundEffect = new SPacketSoundEffect();
            new ArrayList<Object>(AutoMuffin.mc.field_71441_e.field_72996_f).forEach(e -> {
                if (e instanceof EntityEnderCrystal && ((EntityEnderCrystal)e).func_70092_e(sPacketSoundEffect.func_149207_d(), sPacketSoundEffect.func_149211_e(), sPacketSoundEffect.func_149210_f()) < 36.0) {
                    ((EntityEnderCrystal)e).func_70106_y();
                }
            });
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        EntityEnderCrystal crystal;
        if (this.rotate.getValue() != Rotate.OFF && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet2 = (CPacketPlayer)event.getPacket();
            packet2.field_149476_e = this.yaw;
            packet2.field_149473_f = this.pitch;
            ++this.rotationPacketsSpoofed;
            if (this.rotationPacketsSpoofed >= this.rotations.getValue()) {
                this.rotating = false;
                this.rotationPacketsSpoofed = 0;
            }
        }
        BlockPos pos = null;
        CPacketUseEntity packet3 = null;
        if (event.getPacket() instanceof CPacketUseEntity && (packet3 = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet3.func_149564_a((World)AutoCrystall.mc.field_71441_e) instanceof EntityEnderCrystal) {
            pos = packet3.func_149564_a((World)AutoCrystall.mc.field_71441_e).func_180425_c();
        }
        if (event.getPacket() instanceof CPacketUseEntity && (packet3 = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet3.func_149564_a((World)AutoCrystall.mc.field_71441_e) instanceof EntityEnderCrystal && EntityUtil.isCrystalAtFeet(crystal = (EntityEnderCrystal)packet3.func_149564_a((World)AutoCrystall.mc.field_71441_e), this.range.getValue().floatValue()) && pos != null) {
            this.rotateToPos(pos);
            BlockUtil.placeCrystalOnBlock(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, false);
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet3 = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet3.func_149564_a((World)AutoCrystall.mc.field_71441_e) instanceof EntityEnderCrystal && this.cancelcrystal.getValue().booleanValue()) {
            Objects.requireNonNull(packet3.func_149564_a((World)AutoCrystall.mc.field_71441_e)).func_70106_y();
            AutoCrystall.mc.field_71441_e.func_73028_b(packet3.field_149567_a);
        }
    }

    private void rotateToPos(BlockPos pos) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case Place: 
            case All: {
                float[] angle = MathUtil.calcAngle(AutoCrystall.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() - 0.5f), (double)((float)pos.func_177952_p() + 0.5f)));
                if (this.rotate.getValue() != Rotate.OFF) {
                    Oyvey.rotationManager.setPlayerRotations(angle[0], angle[1]);
                    break;
                }
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
                break;
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (this.renderPos != null && this.render.getValue().booleanValue() && (this.box.getValue().booleanValue() || this.text.getValue().booleanValue() || this.outline.getValue().booleanValue())) {
            RenderUtil.drawBoxESP(this.renderPos, this.colorSync.getValue() != false ? ColorUtill.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), this.colorSync.getValue() != false ? this.getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            if (this.text.getValue().booleanValue()) {
                RenderUtil.drawText(this.renderPos, (Math.floor(this.renderDamage) == this.renderDamage ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "");
            }
        }
    }

    public Color getCurrentColor() {
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }

    @Override
    public String getDisplayInfo() {
        if (currentTarget != null) {
            if (this.infomode.getValue() == InfoMode.Target) {
                return currentTarget.func_70005_c_();
            }
            if (this.infomode.getValue() == InfoMode.Damage) {
                return (Math.floor(this.renderDamage) == this.renderDamage ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "";
            }
            if (this.infomode.getValue() == InfoMode.Both) {
                return currentTarget.func_70005_c_() + ", " + (Math.floor(this.renderDamage) == this.renderDamage ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "";
            }
        }
        return null;
    }

    private boolean isValid(Entity entity) {
        return entity != null && AutoCrystall.mc.field_71439_g.func_70068_e(entity) <= MathUtil.square(this.breakRange.getValue().floatValue()) && (this.raytrace.getValue() == Raytrace.None || this.raytrace.getValue() == Raytrace.Place || AutoCrystall.mc.field_71439_g.func_70685_l(entity) || !AutoCrystall.mc.field_71439_g.func_70685_l(entity) && AutoCrystall.mc.field_71439_g.func_70068_e(entity) <= MathUtil.square(this.breaktrace.getValue().floatValue()));
    }

    public static enum Raytrace {
        None,
        Place,
        Break,
        Both;

    }

    public static enum Rotate {
        OFF,
        Place,
        Break,
        All;

    }

    public static enum InfoMode {
        Target,
        Damage,
        Both;

    }

    public static enum Settings {
        Place,
        Break,
        Render,
        Misc;

    }
}

