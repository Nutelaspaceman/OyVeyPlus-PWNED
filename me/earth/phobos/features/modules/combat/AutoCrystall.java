/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  io.netty.util.internal.ConcurrentSet
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityEnderCrystal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.client.CPacketUseEntity$Action
 *  net.minecraft.network.play.server.SPacketDestroyEntities
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraft.network.play.server.SPacketSoundEffect
 *  net.minecraft.network.play.server.SPacketSpawnObject
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.SoundCategory
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package me.earth.phobos.features.modules.combat;

import com.mojang.authlib.GameProfile;
import io.netty.util.internal.ConcurrentSet;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.gui.OyVeyGui;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.modules.combat.AutoTrap;
import me.earth.phobos.features.modules.combat.Offhand;
import me.earth.phobos.features.modules.combat.Surround;
import me.earth.phobos.features.setting.Bind;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.DamageUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.RenderUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class AutoCrystall
extends Module {
    public static EntityPlayer target = null;
    public static Set<BlockPos> lowDmgPos = new ConcurrentSet();
    public static Set<BlockPos> placedPos = new HashSet<BlockPos>();
    public static Set<BlockPos> brokenPos = new HashSet<BlockPos>();
    private static AutoCrystall instance;
    public final Timer threadTimer = new Timer();
    private final Setting<Settings> setting = this.register(new Setting<Settings>("Settings", Settings.PLACE));
    public final Setting<Boolean> attackOppositeHand = this.register(new Setting<Object>("OppositeHand", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV));
    public final Setting<Boolean> removeAfterAttack = this.register(new Setting<Object>("AttackRemove", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV));
    public final Setting<Boolean> antiBlock = this.register(new Setting<Object>("AntiFeetPlace", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV));
    private final Setting<Integer> switchCooldown = this.register(new Setting<Object>("Cooldown", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000), v -> this.setting.getValue() == Settings.MISC));
    private final Setting<Integer> eventMode = this.register(new Setting<Object>("Updates", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(3), v -> this.setting.getValue() == Settings.DEV));
    private final Timer switchTimer = new Timer();
    private final Timer manualTimer = new Timer();
    private final Timer breakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Timer syncTimer = new Timer();
    private final Timer predictTimer = new Timer();
    private final Timer renderTimer = new Timer();
    private final AtomicBoolean shouldInterrupt = new AtomicBoolean(false);
    private final Timer syncroTimer = new Timer();
    private final Map<EntityPlayer, Timer> totemPops = new ConcurrentHashMap<EntityPlayer, Timer>();
    private final Queue<CPacketUseEntity> packetUseEntities = new LinkedList<CPacketUseEntity>();
    private final AtomicBoolean threadOngoing = new AtomicBoolean(false);
    public Setting<Raytrace> raytrace = this.register(new Setting<Object>("Raytrace", (Object)Raytrace.NONE, v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> place = this.register(new Setting<Object>("Place", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.PLACE));
    public Setting<Integer> placeDelay = this.register(new Setting<Object>("PlaceDelay", Integer.valueOf(25), Integer.valueOf(0), Integer.valueOf(500), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Float> placeRange = this.register(new Setting<Object>("PlaceRange", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Float> minDamage = this.register(new Setting<Object>("MinDamage", Float.valueOf(7.0f), Float.valueOf(0.1f), Float.valueOf(20.0f), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Float> maxSelfPlace = this.register(new Setting<Object>("MaxSelfPlace", Float.valueOf(10.0f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Integer> wasteAmount = this.register(new Setting<Object>("WasteAmount", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(5), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Boolean> wasteMinDmgCount = this.register(new Setting<Object>("CountMinDmg", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Float> facePlace = this.register(new Setting<Object>("FacePlace", Float.valueOf(8.0f), Float.valueOf(0.1f), Float.valueOf(20.0f), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Float> placetrace = this.register(new Setting<Object>("Placetrace", Float.valueOf(4.5f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.BREAK));
    public Setting<Boolean> antiSurround = this.register(new Setting<Object>("AntiSurround", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Boolean> limitFacePlace = this.register(new Setting<Object>("LimitFacePlace", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Boolean> oneDot15 = this.register(new Setting<Object>("1.15", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Boolean> doublePop = this.register(new Setting<Object>("AntiTotem", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false));
    public Setting<Double> popHealth = this.register(new Setting<Object>("PopHealth", Double.valueOf(1.0), Double.valueOf(0.0), Double.valueOf(3.0), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false && this.doublePop.getValue() != false));
    public Setting<Float> popDamage = this.register(new Setting<Object>("PopDamage", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(6.0f), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false && this.doublePop.getValue() != false));
    public Setting<Integer> popTime = this.register(new Setting<Object>("PopTime", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(1000), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false && this.doublePop.getValue() != false));
    public Setting<Boolean> explode = this.register(new Setting<Object>("Break", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK));
    public Setting<Switch> switchMode = this.register(new Setting<Object>("Attack", (Object)Switch.BREAKSLOT, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false));
    public Setting<Integer> breakDelay = this.register(new Setting<Object>("BreakDelay", Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(500), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false));
    public Setting<Float> breakRange = this.register(new Setting<Object>("BreakRange", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false));
    public Setting<Integer> packets = this.register(new Setting<Object>("Packets", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(6), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false));
    public Setting<Float> maxSelfBreak = this.register(new Setting<Object>("MaxSelfBreak", Float.valueOf(10.0f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false));
    public Setting<Float> breaktrace = this.register(new Setting<Object>("Breaktrace", Float.valueOf(4.5f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false && this.raytrace.getValue() != Raytrace.NONE && this.raytrace.getValue() != Raytrace.PLACE));
    public Setting<Boolean> manual = this.register(new Setting<Object>("Manual", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK));
    public Setting<Boolean> manualMinDmg = this.register(new Setting<Object>("ManMinDmg", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK && this.manual.getValue() != false));
    public Setting<Integer> manualBreak = this.register(new Setting<Object>("ManualDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> this.setting.getValue() == Settings.BREAK && this.manual.getValue() != false));
    public Setting<Boolean> sync = this.register(new Setting<Object>("Sync", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK && (this.explode.getValue() != false || this.manual.getValue() != false)));
    public Setting<Boolean> instant = this.register(new Setting<Object>("Predict", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false && this.place.getValue() != false));
    public Setting<PredictTimer> instantTimer = this.register(new Setting<Object>("PredictTimer", (Object)PredictTimer.NONE, v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false && this.place.getValue() != false && this.instant.getValue() != false));
    public Setting<Boolean> resetBreakTimer = this.register(new Setting<Object>("ResetBreakTimer", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false && this.place.getValue() != false && this.instant.getValue() != false));
    public Setting<Integer> predictDelay = this.register(new Setting<Object>("PredictDelay", Integer.valueOf(12), Integer.valueOf(0), Integer.valueOf(500), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false && this.place.getValue() != false && this.instant.getValue() != false && this.instantTimer.getValue() == PredictTimer.PREDICT));
    public Setting<Boolean> predictCalc = this.register(new Setting<Object>("PredictCalc", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false && this.place.getValue() != false && this.instant.getValue() != false));
    public Setting<Boolean> superSafe = this.register(new Setting<Object>("SuperSafe", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false && this.place.getValue() != false && this.instant.getValue() != false));
    public Setting<Boolean> antiCommit = this.register(new Setting<Object>("AntiOverCommit", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.BREAK && this.explode.getValue() != false && this.place.getValue() != false && this.instant.getValue() != false));
    public Setting<Boolean> render = this.register(new Setting<Object>("Render", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.RENDER));
    private final Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false));
    private final Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false));
    private final Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false));
    private final Setting<Integer> alpha = this.register(new Setting<Object>("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false));
    public Setting<Boolean> colorSync = this.register(new Setting<Object>("CSync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.RENDER));
    public Setting<Boolean> box = this.register(new Setting<Object>("Box", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<Object>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false && this.box.getValue() != false));
    public Setting<Boolean> outline = this.register(new Setting<Object>("Outline", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false));
    private final Setting<Float> lineWidth = this.register(new Setting<Object>("LineWidth", Float.valueOf(1.5f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false && this.outline.getValue() != false));
    public Setting<Boolean> text = this.register(new Setting<Object>("Text", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false));
    public Setting<Boolean> customOutline = this.register(new Setting<Object>("CustomLine", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cRed = this.register(new Setting<Object>("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cGreen = this.register(new Setting<Object>("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cBlue = this.register(new Setting<Object>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cAlpha = this.register(new Setting<Object>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.setting.getValue() == Settings.RENDER && this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    public Setting<Boolean> holdFacePlace = this.register(new Setting<Object>("HoldFacePlace", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> holdFaceBreak = this.register(new Setting<Object>("HoldSlowBreak", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC && this.holdFacePlace.getValue() != false));
    public Setting<Boolean> slowFaceBreak = this.register(new Setting<Object>("SlowFaceBreak", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> actualSlowBreak = this.register(new Setting<Object>("ActuallySlow", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Integer> facePlaceSpeed = this.register(new Setting<Object>("FaceSpeed", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> antiNaked = this.register(new Setting<Object>("AntiNaked", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Float> range = this.register(new Setting<Object>("Range", Float.valueOf(12.0f), Float.valueOf(0.1f), Float.valueOf(20.0f), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Target> targetMode = this.register(new Setting<Object>("Target", (Object)Target.CLOSEST, v -> this.setting.getValue() == Settings.MISC));
    public Setting<Integer> minArmor = this.register(new Setting<Object>("MinArmor", Integer.valueOf(5), Integer.valueOf(0), Integer.valueOf(125), v -> this.setting.getValue() == Settings.MISC));
    public Setting<AutoSwitch> autoSwitch = this.register(new Setting<Object>("Switch", (Object)AutoSwitch.TOGGLE, v -> this.setting.getValue() == Settings.MISC));
    public Setting<Bind> switchBind = this.register(new Setting<Object>("SwitchBind", new Bind(-1), v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() == AutoSwitch.TOGGLE));
    public Setting<Boolean> offhandSwitch = this.register(new Setting<Object>("Offhand", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
    public Setting<Boolean> switchBack = this.register(new Setting<Object>("Switchback", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE && this.offhandSwitch.getValue() != false));
    public Setting<Boolean> lethalSwitch = this.register(new Setting<Object>("LethalSwitch", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
    public Setting<Boolean> mineSwitch = this.register(new Setting<Object>("MineSwitch", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.MISC && this.autoSwitch.getValue() != AutoSwitch.NONE));
    public Setting<Rotate> rotate = this.register(new Setting<Object>("Rotate", (Object)Rotate.OFF, v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> suicide = this.register(new Setting<Object>("Suicide", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> webAttack = this.register(new Setting<Object>("WebAttack", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.MISC && this.targetMode.getValue() != Target.DAMAGE));
    public Setting<Boolean> fullCalc = this.register(new Setting<Object>("ExtraCalc", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> sound = this.register(new Setting<Object>("Sound", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Float> soundRange = this.register(new Setting<Object>("SoundRange", Float.valueOf(12.0f), Float.valueOf(0.0f), Float.valueOf(12.0f), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Float> soundPlayer = this.register(new Setting<Object>("SoundPlayer", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(12.0f), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> soundConfirm = this.register(new Setting<Object>("SoundConfirm", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> extraSelfCalc = this.register(new Setting<Object>("MinSelfDmg", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC));
    public Setting<AntiFriendPop> antiFriendPop = this.register(new Setting<Object>("FriendPop", (Object)AntiFriendPop.NONE, v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> noCount = this.register(new Setting<Object>("AntiCount", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK)));
    public Setting<Boolean> calcEvenIfNoDamage = this.register(new Setting<Object>("BigFriendCalc", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK) && this.targetMode.getValue() != Target.DAMAGE));
    public Setting<Boolean> predictFriendDmg = this.register(new Setting<Object>("PredictFriend", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.BREAK) && this.instant.getValue() != false));
    public Setting<Float> minMinDmg = this.register(new Setting<Object>("MinMinDmg", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(3.0f), v -> this.setting.getValue() == Settings.DEV && this.place.getValue() != false));
    public Setting<Boolean> breakSwing = this.register(new Setting<Object>("BreakSwing", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.DEV));
    public Setting<Boolean> placeSwing = this.register(new Setting<Object>("PlaceSwing", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV));
    public Setting<Boolean> exactHand = this.register(new Setting<Object>("ExactHand", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.placeSwing.getValue() != false));
    public Setting<Boolean> justRender = this.register(new Setting<Object>("JustRender", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV));
    public Setting<Boolean> fakeSwing = this.register(new Setting<Object>("FakeSwing", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.justRender.getValue() != false));
    public Setting<Logic> logic = this.register(new Setting<Object>("Logic", (Object)Logic.BREAKPLACE, v -> this.setting.getValue() == Settings.DEV));
    public Setting<DamageSync> damageSync = this.register(new Setting<Object>("DamageSync", (Object)DamageSync.NONE, v -> this.setting.getValue() == Settings.DEV));
    public Setting<Integer> damageSyncTime = this.register(new Setting<Object>("SyncDelay", Integer.valueOf(500), Integer.valueOf(0), Integer.valueOf(500), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
    public Setting<Float> dropOff = this.register(new Setting<Object>("DropOff", Float.valueOf(5.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() == DamageSync.BREAK));
    public Setting<Integer> confirm = this.register(new Setting<Object>("Confirm", Integer.valueOf(250), Integer.valueOf(0), Integer.valueOf(1000), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
    public Setting<Boolean> syncedFeetPlace = this.register(new Setting<Object>("FeetSync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE));
    public Setting<Boolean> fullSync = this.register(new Setting<Object>("FullSync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue() != false));
    public Setting<Boolean> syncCount = this.register(new Setting<Object>("SyncCount", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue() != false));
    public Setting<Boolean> hyperSync = this.register(new Setting<Object>("HyperSync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue() != false));
    public Setting<Boolean> gigaSync = this.register(new Setting<Object>("GigaSync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue() != false));
    public Setting<Boolean> syncySync = this.register(new Setting<Object>("SyncySync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue() != false));
    public Setting<Boolean> enormousSync = this.register(new Setting<Object>("EnormousSync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue() != false));
    public Setting<Boolean> holySync = this.register(new Setting<Object>("UnbelievableSync", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.damageSync.getValue() != DamageSync.NONE && this.syncedFeetPlace.getValue() != false));
    public Setting<Boolean> rotateFirst = this.register(new Setting<Object>("FirstRotation", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.rotate.getValue() != Rotate.OFF && this.eventMode.getValue() == 2));
    public Setting<ThreadMode> threadMode = this.register(new Setting<Object>("Thread", (Object)ThreadMode.NONE, v -> this.setting.getValue() == Settings.DEV));
    public Setting<Integer> threadDelay = this.register(new Setting<Object>("ThreadDelay", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(1000), v -> this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE));
    public Setting<Boolean> syncThreadBool = this.register(new Setting<Object>("ThreadSync", Boolean.valueOf(true), v -> this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE));
    public Setting<Integer> syncThreads = this.register(new Setting<Object>("SyncThreads", Integer.valueOf(1000), Integer.valueOf(1), Integer.valueOf(10000), v -> this.setting.getValue() == Settings.DEV && this.threadMode.getValue() != ThreadMode.NONE && this.syncThreadBool.getValue() != false));
    public Setting<Boolean> predictPos = this.register(new Setting<Object>("PredictPos", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV));
    public Setting<Boolean> renderExtrapolation = this.register(new Setting<Object>("RenderExtrapolation", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV && this.predictPos.getValue() != false));
    public Setting<Integer> predictTicks = this.register(new Setting<Object>("ExtrapolationTicks", Integer.valueOf(2), Integer.valueOf(1), Integer.valueOf(20), v -> this.setting.getValue() == Settings.DEV && this.predictPos.getValue() != false));
    public Setting<Integer> rotations = this.register(new Setting<Object>("Spoofs", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(20), v -> this.setting.getValue() == Settings.DEV));
    public Setting<Boolean> predictRotate = this.register(new Setting<Object>("PredictRotate", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.DEV));
    public Setting<Float> predictOffset = this.register(new Setting<Object>("PredictOffset", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(4.0f), v -> this.setting.getValue() == Settings.DEV));
    public Setting<Boolean> brownZombie = this.register(new Setting<Object>("BrownZombieMode", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.MISC));
    public Setting<Boolean> doublePopOnDamage = this.register(new Setting<Object>("DamagePop", Boolean.valueOf(false), v -> this.setting.getValue() == Settings.PLACE && this.place.getValue() != false && this.doublePop.getValue() != false && this.targetMode.getValue() == Target.DAMAGE));
    public boolean rotating = false;
    private Queue<Entity> attackList = new ConcurrentLinkedQueue<Entity>();
    private Map<Entity, Float> crystalMap = new HashMap<Entity, Float>();
    private Entity efficientTarget = null;
    private double currentDamage = 0.0;
    private double renderDamage = 0.0;
    private double lastDamage = 0.0;
    private boolean didRotation = false;
    private boolean switching = false;
    private BlockPos placePos = null;
    private BlockPos renderPos = null;
    private boolean mainHand = false;
    private boolean offHand = false;
    private int crystalCount = 0;
    private int minDmgCount = 0;
    private int lastSlot = -1;
    private float yaw = 0.0f;
    private float pitch = 0.0f;
    private BlockPos webPos = null;
    private BlockPos lastPos = null;
    private boolean posConfirmed = false;
    private boolean foundDoublePop = false;
    private int rotationPacketsSpoofed = 0;
    private ScheduledExecutorService executor;
    private Thread thread;
    private EntityPlayer currentSyncTarget;
    private BlockPos syncedPlayerPos;
    private BlockPos syncedCrystalPos;
    private PlaceInfo placeInfo;
    private boolean addTolowDmg;

    public AutoCrystall() {
        super("AutoCrystal", "Best CA on the market", Module.Category.COMBAT, true, false, false);
        instance = this;
    }

    public static AutoCrystall getInstance() {
        if (instance == null) {
            instance = new AutoCrystall();
        }
        return instance;
    }

    @Override
    public void onTick() {
        if (this.threadMode.getValue() == ThreadMode.NONE && this.eventMode.getValue() == 3) {
            this.doAutoCrystal();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            this.postProcessing();
        }
        if (event.getStage() != 0) {
            return;
        }
        if (this.eventMode.getValue() == 2) {
            this.doAutoCrystal();
        }
    }

    public void postTick() {
        if (this.threadMode.getValue() != ThreadMode.NONE) {
            this.processMultiThreading();
        }
    }

    @Override
    public void onUpdate() {
        if (this.threadMode.getValue() == ThreadMode.NONE && this.eventMode.getValue() == 1) {
            this.doAutoCrystal();
        }
    }

    @Override
    public void onToggle() {
        brokenPos.clear();
        placedPos.clear();
        this.totemPops.clear();
        this.rotating = false;
    }

    @Override
    public void onDisable() {
        if (this.thread != null) {
            this.shouldInterrupt.set(true);
        }
        if (this.executor != null) {
            this.executor.shutdown();
        }
    }

    @Override
    public void onEnable() {
        if (this.threadMode.getValue() != ThreadMode.NONE) {
            this.processMultiThreading();
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.switching) {
            return "\u00a7aSwitch";
        }
        if (target != null) {
            return target.func_70005_c_();
        }
        return null;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getStage() == 0 && this.rotate.getValue() != Rotate.OFF && this.rotating && this.eventMode.getValue() != 2 && event.getPacket() instanceof CPacketPlayer) {
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
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet.func_149564_a((World)AutoCrystall.mc.field_71441_e) instanceof EntityEnderCrystal) {
            pos = packet.func_149564_a((World)AutoCrystall.mc.field_71441_e).func_180425_c();
            if (this.removeAfterAttack.getValue().booleanValue()) {
                Objects.requireNonNull(packet.func_149564_a((World)AutoCrystall.mc.field_71441_e)).func_70106_y();
                AutoCrystall.mc.field_71441_e.func_73028_b(packet.field_149567_a);
            }
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK && packet.func_149564_a((World)AutoCrystall.mc.field_71441_e) instanceof EntityEnderCrystal) {
            EntityEnderCrystal crystal = (EntityEnderCrystal)packet.func_149564_a((World)AutoCrystall.mc.field_71441_e);
            if (this.antiBlock.getValue().booleanValue() && EntityUtil.isCrystalAtFeet(crystal, this.range.getValue().floatValue()) && pos != null) {
                this.rotateToPos(pos);
                BlockUtil.placeCrystalOnBlock(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.placeSwing.getValue(), this.exactHand.getValue());
            }
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGH, receiveCanceled=true)
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketSoundEffect packet;
        if (AutoCrystall.fullNullCheck()) {
            return;
        }
        if (!this.justRender.getValue().booleanValue() && this.switchTimer.passedMs(this.switchCooldown.getValue().intValue()) && this.explode.getValue().booleanValue() && this.instant.getValue().booleanValue() && event.getPacket() instanceof SPacketSpawnObject && (this.syncedCrystalPos == null || !this.syncedFeetPlace.getValue().booleanValue() || this.damageSync.getValue() == DamageSync.NONE)) {
            SPacketSpawnObject packet2 = (SPacketSpawnObject)event.getPacket();
            if (packet2.func_148993_l() == 51) {
                BlockPos blockPos;
                BlockPos pos = new BlockPos(packet2.func_186880_c(), packet2.func_186882_d(), packet2.func_186881_e());
                if (AutoCrystall.mc.field_71439_g.func_174818_b(blockPos) + (double)this.predictOffset.getValue().floatValue() <= MathUtil.square(this.breakRange.getValue().floatValue()) && (this.instantTimer.getValue() == PredictTimer.NONE || this.instantTimer.getValue() == PredictTimer.BREAK && this.breakTimer.passedMs(this.breakDelay.getValue().intValue()) || this.instantTimer.getValue() == PredictTimer.PREDICT && this.predictTimer.passedMs(this.predictDelay.getValue().intValue()))) {
                    if (this.predictSlowBreak(pos.func_177977_b())) {
                        return;
                    }
                    if (this.predictFriendDmg.getValue().booleanValue() && (this.antiFriendPop.getValue() == AntiFriendPop.BREAK || this.antiFriendPop.getValue() == AntiFriendPop.ALL) && this.isRightThread()) {
                        for (EntityPlayer friend : AutoCrystall.mc.field_71441_e.field_73010_i) {
                            if (friend == null || AutoCrystall.mc.field_71439_g.equals((Object)friend) || friend.func_174818_b(pos) > MathUtil.square(this.range.getValue().floatValue() + this.placeRange.getValue().floatValue()) || !Oyvey.friendManager.isFriend(friend) || !((double)DamageUtil.calculateDamage(pos, (Entity)friend) > (double)EntityUtil.getHealth((Entity)friend) + 0.5)) continue;
                            return;
                        }
                    }
                    if (placedPos.contains((Object)pos.func_177977_b())) {
                        float selfDamage;
                        if (this.isRightThread() && this.superSafe.getValue() != false ? DamageUtil.canTakeDamage(this.suicide.getValue()) && ((double)(selfDamage = DamageUtil.calculateDamage(pos, (Entity)AutoCrystall.mc.field_71439_g)) - 0.5 > (double)EntityUtil.getHealth((Entity)AutoCrystall.mc.field_71439_g) || selfDamage > this.maxSelfBreak.getValue().floatValue()) : this.superSafe.getValue() != false) {
                            return;
                        }
                        this.attackCrystalPredict(packet2.func_149001_c(), pos);
                    } else if (this.predictCalc.getValue().booleanValue() && this.isRightThread()) {
                        float selfDamage = -1.0f;
                        if (DamageUtil.canTakeDamage(this.suicide.getValue())) {
                            selfDamage = DamageUtil.calculateDamage(pos, (Entity)AutoCrystall.mc.field_71439_g);
                        }
                        if ((double)selfDamage + 0.5 < (double)EntityUtil.getHealth((Entity)AutoCrystall.mc.field_71439_g) && selfDamage <= this.maxSelfBreak.getValue().floatValue()) {
                            for (EntityPlayer player : AutoCrystall.mc.field_71441_e.field_73010_i) {
                                float damage;
                                if (!(player.func_174818_b(pos) <= MathUtil.square(this.range.getValue().floatValue())) || !EntityUtil.isValid((Entity)player, this.range.getValue().floatValue() + this.breakRange.getValue().floatValue()) || this.antiNaked.getValue().booleanValue() && DamageUtil.isNaked(player) || !((damage = DamageUtil.calculateDamage(pos, (Entity)player)) > selfDamage || damage > this.minDamage.getValue().floatValue() && !DamageUtil.canTakeDamage(this.suicide.getValue())) && !(damage > EntityUtil.getHealth((Entity)player))) continue;
                                if (this.predictRotate.getValue().booleanValue() && this.eventMode.getValue() != 2 && (this.rotate.getValue() == Rotate.BREAK || this.rotate.getValue() == Rotate.ALL)) {
                                    this.rotateToPos(pos);
                                }
                                this.attackCrystalPredict(packet2.func_149001_c(), pos);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (!this.soundConfirm.getValue().booleanValue() && event.getPacket() instanceof SPacketExplosion) {
            SPacketExplosion packet3 = (SPacketExplosion)event.getPacket();
            BlockPos pos = new BlockPos(packet3.func_149148_f(), packet3.func_149143_g(), packet3.func_149145_h()).func_177977_b();
            this.removePos(pos);
        } else if (event.getPacket() instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities packet4 = (SPacketDestroyEntities)event.getPacket();
            for (int id : packet4.func_149098_c()) {
                Entity entity = AutoCrystall.mc.field_71441_e.func_73045_a(id);
                if (!(entity instanceof EntityEnderCrystal)) continue;
                brokenPos.remove((Object)new BlockPos(entity.func_174791_d()).func_177977_b());
                placedPos.remove((Object)new BlockPos(entity.func_174791_d()).func_177977_b());
            }
        } else if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet5 = (SPacketEntityStatus)event.getPacket();
            if (packet5.func_149160_c() == 35 && packet5.func_149161_a((World)AutoCrystall.mc.field_71441_e) instanceof EntityPlayer) {
                this.totemPops.put((EntityPlayer)packet5.func_149161_a((World)AutoCrystall.mc.field_71441_e), new Timer().reset());
            }
        } else if (event.getPacket() instanceof SPacketSoundEffect && (packet = (SPacketSoundEffect)event.getPacket()).func_186977_b() == SoundCategory.BLOCKS && packet.func_186978_a() == SoundEvents.field_187539_bB) {
            BlockPos pos = new BlockPos(packet.func_149207_d(), packet.func_149211_e(), packet.func_149210_f());
            if (this.soundConfirm.getValue().booleanValue()) {
                this.removePos(pos);
            }
            if (this.threadMode.getValue() == ThreadMode.SOUND && this.isRightThread() && AutoCrystall.mc.field_71439_g != null && AutoCrystall.mc.field_71439_g.func_174818_b(pos) < MathUtil.square(this.soundPlayer.getValue().floatValue())) {
                this.handlePool(true);
            }
        }
    }

    private boolean predictSlowBreak(BlockPos pos) {
        if (this.antiCommit.getValue().booleanValue() && lowDmgPos.remove((Object)pos)) {
            return this.shouldSlowBreak(false);
        }
        return false;
    }

    private boolean isRightThread() {
        return mc.func_152345_ab() || !Oyvey.eventManager.ticksOngoing() && !this.threadOngoing.get();
    }

    private void attackCrystalPredict(int entityID, BlockPos pos) {
        if (!(!this.predictRotate.getValue().booleanValue() || this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE || this.rotate.getValue() != Rotate.BREAK && this.rotate.getValue() != Rotate.ALL)) {
            this.rotateToPos(pos);
        }
        CPacketUseEntity attackPacket = new CPacketUseEntity();
        attackPacket.field_149567_a = entityID;
        attackPacket.field_149566_b = CPacketUseEntity.Action.ATTACK;
        AutoCrystall.mc.field_71439_g.field_71174_a.func_147297_a((Packet)attackPacket);
        if (this.breakSwing.getValue().booleanValue()) {
            AutoCrystall.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
        if (this.resetBreakTimer.getValue().booleanValue()) {
            this.breakTimer.reset();
        }
        this.predictTimer.reset();
    }

    private void removePos(BlockPos pos) {
        if (this.damageSync.getValue() == DamageSync.PLACE) {
            if (placedPos.remove((Object)pos)) {
                this.posConfirmed = true;
            }
        } else if (this.damageSync.getValue() == DamageSync.BREAK && brokenPos.remove((Object)pos)) {
            this.posConfirmed = true;
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if ((this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC) && this.renderPos != null && this.render.getValue().booleanValue() && (this.box.getValue().booleanValue() || this.text.getValue().booleanValue() || this.outline.getValue().booleanValue())) {
            RenderUtil.drawBoxESP(this.renderPos, this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), this.customOutline.getValue(), this.colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.lineWidth.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
            if (this.text.getValue().booleanValue()) {
                RenderUtil.drawText(this.renderPos, (Math.floor(this.renderDamage) == this.renderDamage ? Integer.valueOf((int)this.renderDamage) : String.format("%.1f", this.renderDamage)) + "");
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && !(AutoCrystall.mc.field_71462_r instanceof OyVeyGui) && this.switchBind.getValue().getBind() == Keyboard.getEventKey()) {
            if (this.switchBack.getValue().booleanValue() && this.offhandSwitch.getValue().booleanValue() && this.offHand) {
                Offhand module = Oyvey.moduleManager.getModuleByClass(Offhand.class);
                if (module.isOff()) {
                    Command.sendMessage("<" + this.getDisplayName() + "> \u00a7cSwitch failed. Enable the Offhand module.");
                }
                return;
            }
            this.switching = !this.switching;
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting() != null && event.getSetting().getFeature() != null && event.getSetting().getFeature().equals(this) && this.isEnabled() && (event.getSetting().equals(this.threadDelay) || event.getSetting().equals(this.threadMode))) {
            if (this.executor != null) {
                this.executor.shutdown();
            }
            if (this.thread != null) {
                this.shouldInterrupt.set(true);
            }
        }
    }

    private void postProcessing() {
        if (this.threadMode.getValue() != ThreadMode.NONE || this.eventMode.getValue() != 2 || this.rotate.getValue() == Rotate.OFF || !this.rotateFirst.getValue().booleanValue()) {
            return;
        }
        switch (this.logic.getValue()) {
            case BREAKPLACE: {
                this.postProcessBreak();
                this.postProcessPlace();
                break;
            }
            case PLACEBREAK: {
                this.postProcessPlace();
                this.postProcessBreak();
            }
        }
    }

    private void postProcessBreak() {
        while (!this.packetUseEntities.isEmpty()) {
            CPacketUseEntity packet = this.packetUseEntities.poll();
            AutoCrystall.mc.field_71439_g.field_71174_a.func_147297_a((Packet)packet);
            if (this.breakSwing.getValue().booleanValue()) {
                AutoCrystall.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            }
            this.breakTimer.reset();
        }
    }

    private void postProcessPlace() {
        if (this.placeInfo != null) {
            this.placeInfo.runPlace();
            this.placeTimer.reset();
            this.placeInfo = null;
        }
    }

    private void processMultiThreading() {
        if (this.isOff()) {
            return;
        }
        if (this.threadMode.getValue() == ThreadMode.WHILE) {
            this.handleWhile();
        } else if (this.threadMode.getValue() != ThreadMode.NONE) {
            this.handlePool(false);
        }
    }

    private void handlePool(boolean justDoIt) {
        if (justDoIt || this.executor == null || this.executor.isTerminated() || this.executor.isShutdown() || this.syncroTimer.passedMs(this.syncThreads.getValue().intValue()) && this.syncThreadBool.getValue().booleanValue()) {
            if (this.executor != null) {
                this.executor.shutdown();
            }
            this.executor = this.getExecutor();
            this.syncroTimer.reset();
        }
    }

    private void handleWhile() {
        if (this.thread == null || this.thread.isInterrupted() || !this.thread.isAlive() || this.syncroTimer.passedMs(this.syncThreads.getValue().intValue()) && this.syncThreadBool.getValue().booleanValue()) {
            if (this.thread == null) {
                this.thread = new Thread(RAutoCrystal.getInstance(this));
            } else if (this.syncroTimer.passedMs(this.syncThreads.getValue().intValue()) && !this.shouldInterrupt.get() && this.syncThreadBool.getValue().booleanValue()) {
                this.shouldInterrupt.set(true);
                this.syncroTimer.reset();
                return;
            }
            if (this.thread != null && (this.thread.isInterrupted() || !this.thread.isAlive())) {
                this.thread = new Thread(RAutoCrystal.getInstance(this));
            }
            if (this.thread != null && this.thread.getState() == Thread.State.NEW) {
                try {
                    this.thread.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                this.syncroTimer.reset();
            }
        }
    }

    private ScheduledExecutorService getExecutor() {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(RAutoCrystal.getInstance(this), 0L, this.threadDelay.getValue().intValue(), TimeUnit.MILLISECONDS);
        return service;
    }

    public void doAutoCrystal() {
        if (this.brownZombie.getValue().booleanValue()) {
            return;
        }
        if (this.check()) {
            switch (this.logic.getValue()) {
                case PLACEBREAK: {
                    this.placeCrystal();
                    this.breakCrystal();
                    break;
                }
                case BREAKPLACE: {
                    this.breakCrystal();
                    this.placeCrystal();
                }
            }
            this.manualBreaker();
        }
    }

    private boolean check() {
        if (AutoCrystall.fullNullCheck()) {
            return false;
        }
        if (this.syncTimer.passedMs(this.damageSyncTime.getValue().intValue())) {
            this.currentSyncTarget = null;
            this.syncedCrystalPos = null;
            this.syncedPlayerPos = null;
        } else if (this.syncySync.getValue().booleanValue() && this.syncedCrystalPos != null) {
            this.posConfirmed = true;
        }
        this.foundDoublePop = false;
        if (this.renderTimer.passedMs(500L)) {
            this.renderPos = null;
            this.renderTimer.reset();
        }
        this.mainHand = AutoCrystall.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP;
        this.offHand = AutoCrystall.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
        this.currentDamage = 0.0;
        this.placePos = null;
        if (this.lastSlot != AutoCrystall.mc.field_71439_g.field_71071_by.field_70461_c || AutoTrap.isPlacing || Surround.isPlacing) {
            this.lastSlot = AutoCrystall.mc.field_71439_g.field_71071_by.field_70461_c;
            this.switchTimer.reset();
        }
        if (!this.offHand && !this.mainHand) {
            this.placeInfo = null;
            this.packetUseEntities.clear();
        }
        if (this.offHand || this.mainHand) {
            this.switching = false;
        }
        if (!((this.offHand || this.mainHand || this.switchMode.getValue() != Switch.BREAKSLOT || this.switching) && DamageUtil.canBreakWeakness((EntityPlayer)AutoCrystall.mc.field_71439_g) && this.switchTimer.passedMs(this.switchCooldown.getValue().intValue()))) {
            this.renderPos = null;
            target = null;
            this.rotating = false;
            return false;
        }
        if (this.mineSwitch.getValue().booleanValue() && Mouse.isButtonDown((int)0) && (this.switching || this.autoSwitch.getValue() == AutoSwitch.ALWAYS) && Mouse.isButtonDown((int)1) && AutoCrystall.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemPickaxe) {
            this.switchItem();
        }
        this.mapCrystals();
        if (!this.posConfirmed && this.damageSync.getValue() != DamageSync.NONE && this.syncTimer.passedMs(this.confirm.getValue().intValue())) {
            this.syncTimer.setMs(this.damageSyncTime.getValue() + 1);
        }
        return true;
    }

    private void mapCrystals() {
        this.efficientTarget = null;
        if (this.packets.getValue() != 1) {
            this.attackList = new ConcurrentLinkedQueue<Entity>();
            this.crystalMap = new HashMap<Entity, Float>();
        }
        this.crystalCount = 0;
        this.minDmgCount = 0;
        Entity maxCrystal = null;
        float maxDamage = 0.5f;
        for (Entity entity : AutoCrystall.mc.field_71441_e.field_72996_f) {
            if (entity.field_70128_L || !(entity instanceof EntityEnderCrystal) || !this.isValid(entity)) continue;
            if (this.syncedFeetPlace.getValue().booleanValue() && entity.func_180425_c().func_177977_b().equals((Object)this.syncedCrystalPos) && this.damageSync.getValue() != DamageSync.NONE) {
                ++this.minDmgCount;
                ++this.crystalCount;
                if (this.syncCount.getValue().booleanValue()) {
                    this.minDmgCount = this.wasteAmount.getValue() + 1;
                    this.crystalCount = this.wasteAmount.getValue() + 1;
                }
                if (!this.hyperSync.getValue().booleanValue()) continue;
                maxCrystal = null;
                break;
            }
            boolean count = false;
            boolean countMin = false;
            float selfDamage = -1.0f;
            if (DamageUtil.canTakeDamage(this.suicide.getValue())) {
                selfDamage = DamageUtil.calculateDamage(entity, (Entity)AutoCrystall.mc.field_71439_g);
            }
            if ((double)selfDamage + 0.5 < (double)EntityUtil.getHealth((Entity)AutoCrystall.mc.field_71439_g) && selfDamage <= this.maxSelfBreak.getValue().floatValue()) {
                Entity beforeCrystal = maxCrystal;
                float beforeDamage = maxDamage;
                for (EntityPlayer player : AutoCrystall.mc.field_71441_e.field_73010_i) {
                    float damage;
                    if (!(player.func_70068_e(entity) <= MathUtil.square(this.range.getValue().floatValue()))) continue;
                    if (EntityUtil.isValid((Entity)player, this.range.getValue().floatValue() + this.breakRange.getValue().floatValue())) {
                        if (this.antiNaked.getValue().booleanValue() && DamageUtil.isNaked(player) || !((damage = DamageUtil.calculateDamage(entity, (Entity)player)) > selfDamage || damage > this.minDamage.getValue().floatValue() && !DamageUtil.canTakeDamage(this.suicide.getValue())) && !(damage > EntityUtil.getHealth((Entity)player))) continue;
                        if (damage > maxDamage) {
                            maxDamage = damage;
                            maxCrystal = entity;
                        }
                        if (this.packets.getValue() == 1) {
                            if (damage >= this.minDamage.getValue().floatValue() || !this.wasteMinDmgCount.getValue().booleanValue()) {
                                count = true;
                            }
                            countMin = true;
                            continue;
                        }
                        if (this.crystalMap.get((Object)entity) != null && !(this.crystalMap.get((Object)entity).floatValue() < damage)) continue;
                        this.crystalMap.put(entity, Float.valueOf(damage));
                        continue;
                    }
                    if (this.antiFriendPop.getValue() != AntiFriendPop.BREAK && this.antiFriendPop.getValue() != AntiFriendPop.ALL || !Oyvey.friendManager.isFriend(player.func_70005_c_()) || !((double)(damage = DamageUtil.calculateDamage(entity, (Entity)player)) > (double)EntityUtil.getHealth((Entity)player) + 0.5)) continue;
                    maxCrystal = beforeCrystal;
                    maxDamage = beforeDamage;
                    this.crystalMap.remove((Object)entity);
                    if (!this.noCount.getValue().booleanValue()) break;
                    count = false;
                    countMin = false;
                    break;
                }
            }
            if (!countMin) continue;
            ++this.minDmgCount;
            if (!count) continue;
            ++this.crystalCount;
        }
        if (this.damageSync.getValue() == DamageSync.BREAK && ((double)maxDamage > this.lastDamage || this.syncTimer.passedMs(this.damageSyncTime.getValue().intValue()) || this.damageSync.getValue() == DamageSync.NONE)) {
            this.lastDamage = maxDamage;
        }
        if (this.enormousSync.getValue().booleanValue() && this.syncedFeetPlace.getValue().booleanValue() && this.damageSync.getValue() != DamageSync.NONE && this.syncedCrystalPos != null) {
            if (this.syncCount.getValue().booleanValue()) {
                this.minDmgCount = this.wasteAmount.getValue() + 1;
                this.crystalCount = this.wasteAmount.getValue() + 1;
            }
            return;
        }
        if (this.webAttack.getValue().booleanValue() && this.webPos != null) {
            if (AutoCrystall.mc.field_71439_g.func_174818_b(this.webPos.func_177984_a()) > MathUtil.square(this.breakRange.getValue().floatValue())) {
                this.webPos = null;
            } else {
                for (Entity entity : AutoCrystall.mc.field_71441_e.func_72872_a(Entity.class, new AxisAlignedBB(this.webPos.func_177984_a()))) {
                    if (!(entity instanceof EntityEnderCrystal)) continue;
                    this.attackList.add(entity);
                    this.efficientTarget = entity;
                    this.webPos = null;
                    this.lastDamage = 0.5;
                    return;
                }
            }
        }
        if (this.shouldSlowBreak(true) && maxDamage < this.minDamage.getValue().floatValue() && (target == null || !(EntityUtil.getHealth((Entity)target) <= this.facePlace.getValue().floatValue()) || !this.breakTimer.passedMs(this.facePlaceSpeed.getValue().intValue()) && this.slowFaceBreak.getValue().booleanValue() && Mouse.isButtonDown((int)0) && this.holdFacePlace.getValue().booleanValue() && this.holdFaceBreak.getValue().booleanValue())) {
            this.efficientTarget = null;
            return;
        }
        if (this.packets.getValue() == 1) {
            this.efficientTarget = maxCrystal;
        } else {
            this.crystalMap = MathUtil.sortByValue(this.crystalMap, true);
            for (Map.Entry entry : this.crystalMap.entrySet()) {
                Entity crystal = (Entity)entry.getKey();
                float damage = ((Float)entry.getValue()).floatValue();
                if (damage >= this.minDamage.getValue().floatValue() || !this.wasteMinDmgCount.getValue().booleanValue()) {
                    ++this.crystalCount;
                }
                this.attackList.add(crystal);
                ++this.minDmgCount;
            }
        }
    }

    private boolean shouldSlowBreak(boolean withManual) {
        return withManual && this.manual.getValue() != false && this.manualMinDmg.getValue() != false && Mouse.isButtonDown((int)1) && (!Mouse.isButtonDown((int)0) || this.holdFacePlace.getValue() == false) || this.holdFacePlace.getValue() != false && this.holdFaceBreak.getValue() != false && Mouse.isButtonDown((int)0) && !this.breakTimer.passedMs(this.facePlaceSpeed.getValue().intValue()) || this.slowFaceBreak.getValue() != false && !this.breakTimer.passedMs(this.facePlaceSpeed.getValue().intValue());
    }

    private void placeCrystal() {
        int crystalLimit = this.wasteAmount.getValue();
        if (this.placeTimer.passedMs(this.placeDelay.getValue().intValue()) && this.place.getValue().booleanValue() && (this.offHand || this.mainHand || this.switchMode.getValue() == Switch.CALC || this.switchMode.getValue() == Switch.BREAKSLOT && this.switching)) {
            if (!(!this.offHand && !this.mainHand && (this.switchMode.getValue() == Switch.ALWAYS || this.switching) || this.crystalCount < crystalLimit || this.antiSurround.getValue().booleanValue() && this.lastPos != null && this.lastPos.equals((Object)this.placePos))) {
                return;
            }
            this.calculateDamage(this.getTarget(this.targetMode.getValue() == Target.UNSAFE));
            if (target != null && this.placePos != null) {
                if (!this.offHand && !this.mainHand && this.autoSwitch.getValue() != AutoSwitch.NONE && (this.currentDamage > (double)this.minDamage.getValue().floatValue() || this.lethalSwitch.getValue().booleanValue() && EntityUtil.getHealth((Entity)target) <= this.facePlace.getValue().floatValue()) && !this.switchItem()) {
                    return;
                }
                if (this.currentDamage < (double)this.minDamage.getValue().floatValue() && this.limitFacePlace.getValue().booleanValue()) {
                    crystalLimit = 1;
                }
                if (this.currentDamage >= (double)this.minMinDmg.getValue().floatValue() && (this.offHand || this.mainHand || this.autoSwitch.getValue() != AutoSwitch.NONE) && (this.crystalCount < crystalLimit || this.antiSurround.getValue().booleanValue() && this.lastPos != null && this.lastPos.equals((Object)this.placePos)) && (this.currentDamage > (double)this.minDamage.getValue().floatValue() || this.minDmgCount < crystalLimit) && this.currentDamage >= 1.0 && (DamageUtil.isArmorLow(target, this.minArmor.getValue()) || EntityUtil.getHealth((Entity)target) <= this.facePlace.getValue().floatValue() || this.currentDamage > (double)this.minDamage.getValue().floatValue() || this.shouldHoldFacePlace())) {
                    float damageOffset = this.damageSync.getValue() == DamageSync.BREAK ? this.dropOff.getValue().floatValue() - 5.0f : 0.0f;
                    boolean syncflag = false;
                    if (this.syncedFeetPlace.getValue().booleanValue() && this.placePos.equals((Object)this.lastPos) && this.isEligableForFeetSync(target, this.placePos) && !this.syncTimer.passedMs(this.damageSyncTime.getValue().intValue()) && target.equals((Object)this.currentSyncTarget) && target.func_180425_c().equals((Object)this.syncedPlayerPos) && this.damageSync.getValue() != DamageSync.NONE) {
                        this.syncedCrystalPos = this.placePos;
                        this.lastDamage = this.currentDamage;
                        if (this.fullSync.getValue().booleanValue()) {
                            this.lastDamage = 100.0;
                        }
                        syncflag = true;
                    }
                    if (syncflag || this.currentDamage - (double)damageOffset > this.lastDamage || this.syncTimer.passedMs(this.damageSyncTime.getValue().intValue()) || this.damageSync.getValue() == DamageSync.NONE) {
                        if (!syncflag && this.damageSync.getValue() != DamageSync.BREAK) {
                            this.lastDamage = this.currentDamage;
                        }
                        this.renderPos = this.placePos;
                        this.renderDamage = this.currentDamage;
                        if (this.switchItem()) {
                            this.currentSyncTarget = target;
                            this.syncedPlayerPos = target.func_180425_c();
                            if (this.foundDoublePop) {
                                this.totemPops.put(target, new Timer().reset());
                            }
                            this.rotateToPos(this.placePos);
                            if (this.addTolowDmg || this.actualSlowBreak.getValue().booleanValue() && this.currentDamage < (double)this.minDamage.getValue().floatValue()) {
                                lowDmgPos.add(this.placePos);
                            }
                            placedPos.add(this.placePos);
                            if (!this.justRender.getValue().booleanValue()) {
                                if (this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE && this.rotateFirst.getValue().booleanValue() && this.rotate.getValue() != Rotate.OFF) {
                                    this.placeInfo = new PlaceInfo(this.placePos, this.offHand, this.placeSwing.getValue(), this.exactHand.getValue());
                                } else {
                                    BlockUtil.placeCrystalOnBlock(this.placePos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.placeSwing.getValue(), this.exactHand.getValue());
                                }
                            }
                            this.lastPos = this.placePos;
                            this.placeTimer.reset();
                            this.posConfirmed = false;
                            if (this.syncTimer.passedMs(this.damageSyncTime.getValue().intValue())) {
                                this.syncedCrystalPos = null;
                                this.syncTimer.reset();
                            }
                        }
                    }
                }
            } else {
                this.renderPos = null;
            }
        }
    }

    private boolean shouldHoldFacePlace() {
        this.addTolowDmg = false;
        if (this.holdFacePlace.getValue().booleanValue() && Mouse.isButtonDown((int)0)) {
            this.addTolowDmg = true;
            return true;
        }
        return false;
    }

    private boolean switchItem() {
        if (this.offHand || this.mainHand) {
            return true;
        }
        switch (this.autoSwitch.getValue()) {
            case NONE: {
                return false;
            }
            case TOGGLE: {
                if (!this.switching) {
                    return false;
                }
            }
            case ALWAYS: {
                if (!this.doSwitch()) break;
                return true;
            }
        }
        return false;
    }

    private boolean doSwitch() {
        if (this.offhandSwitch.getValue().booleanValue()) {
            Offhand module = Oyvey.moduleManager.getModuleByClass(Offhand.class);
            if (module.isOff()) {
                Command.sendMessage("<" + this.getDisplayName() + "> \u00a7cSwitch failed. Enable the Offhand module.");
                this.switching = false;
                return false;
            }
            this.switching = false;
            return true;
        }
        if (AutoCrystall.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
            this.mainHand = false;
        } else {
            InventoryUtil.switchToHotbarSlot(ItemEndCrystal.class, false);
            this.mainHand = true;
        }
        this.switching = false;
        return true;
    }

    private void calculateDamage(EntityPlayer targettedPlayer) {
        BlockPos playerPos;
        Block web;
        if (targettedPlayer == null && this.targetMode.getValue() != Target.DAMAGE && !this.fullCalc.getValue().booleanValue()) {
            return;
        }
        float maxDamage = 0.5f;
        EntityPlayer currentTarget = null;
        BlockPos currentPos = null;
        float maxSelfDamage = 0.0f;
        this.foundDoublePop = false;
        BlockPos setToAir = null;
        IBlockState state = null;
        if (this.webAttack.getValue().booleanValue() && targettedPlayer != null && (web = AutoCrystall.mc.field_71441_e.func_180495_p(playerPos = new BlockPos(targettedPlayer.func_174791_d())).func_177230_c()) == Blocks.field_150321_G) {
            setToAir = playerPos;
            state = AutoCrystall.mc.field_71441_e.func_180495_p(playerPos);
            AutoCrystall.mc.field_71441_e.func_175698_g(playerPos);
        }
        block0: for (BlockPos pos : BlockUtil.possiblePlacePositions(this.placeRange.getValue().floatValue(), this.antiSurround.getValue(), this.oneDot15.getValue())) {
            if (!BlockUtil.rayTracePlaceCheck(pos, (this.raytrace.getValue() == Raytrace.PLACE || this.raytrace.getValue() == Raytrace.FULL) && AutoCrystall.mc.field_71439_g.func_174818_b(pos) > MathUtil.square(this.placetrace.getValue().floatValue()), 1.0f)) continue;
            float selfDamage = -1.0f;
            if (DamageUtil.canTakeDamage(this.suicide.getValue())) {
                selfDamage = DamageUtil.calculateDamage(pos, (Entity)AutoCrystall.mc.field_71439_g);
            }
            if (!((double)selfDamage + 0.5 < (double)EntityUtil.getHealth((Entity)AutoCrystall.mc.field_71439_g)) || !(selfDamage <= this.maxSelfPlace.getValue().floatValue())) continue;
            if (targettedPlayer != null) {
                float playerDamage = DamageUtil.calculateDamage(pos, (Entity)targettedPlayer);
                if (this.calcEvenIfNoDamage.getValue().booleanValue() && (this.antiFriendPop.getValue() == AntiFriendPop.ALL || this.antiFriendPop.getValue() == AntiFriendPop.PLACE)) {
                    boolean friendPop = false;
                    for (EntityPlayer friend : AutoCrystall.mc.field_71441_e.field_73010_i) {
                        float friendDamage;
                        if (friend == null || AutoCrystall.mc.field_71439_g.equals((Object)friend) || friend.func_174818_b(pos) > MathUtil.square(this.range.getValue().floatValue() + this.placeRange.getValue().floatValue()) || !Oyvey.friendManager.isFriend(friend) || !((double)(friendDamage = DamageUtil.calculateDamage(pos, (Entity)friend)) > (double)EntityUtil.getHealth((Entity)friend) + 0.5)) continue;
                        friendPop = true;
                        break;
                    }
                    if (friendPop) continue;
                }
                if (this.isDoublePoppable(targettedPlayer, playerDamage) && (currentPos == null || targettedPlayer.func_174818_b(pos) < targettedPlayer.func_174818_b(currentPos))) {
                    currentTarget = targettedPlayer;
                    maxDamage = playerDamage;
                    currentPos = pos;
                    this.foundDoublePop = true;
                    continue;
                }
                if (!(!this.foundDoublePop && (playerDamage > maxDamage || this.extraSelfCalc.getValue() != false && playerDamage >= maxDamage && selfDamage < maxSelfDamage) && (playerDamage > selfDamage || playerDamage > this.minDamage.getValue().floatValue() && !DamageUtil.canTakeDamage(this.suicide.getValue()) || playerDamage > EntityUtil.getHealth((Entity)targettedPlayer)))) continue;
                maxDamage = playerDamage;
                currentTarget = targettedPlayer;
                currentPos = pos;
                maxSelfDamage = selfDamage;
                continue;
            }
            float maxDamageBefore = maxDamage;
            EntityPlayer currentTargetBefore = currentTarget;
            BlockPos currentPosBefore = currentPos;
            float maxSelfDamageBefore = maxSelfDamage;
            for (EntityPlayer player : AutoCrystall.mc.field_71441_e.field_73010_i) {
                float friendDamage;
                if (EntityUtil.isValid((Entity)player, this.placeRange.getValue().floatValue() + this.range.getValue().floatValue())) {
                    if (this.antiNaked.getValue().booleanValue() && DamageUtil.isNaked(player)) continue;
                    float playerDamage = DamageUtil.calculateDamage(pos, (Entity)player);
                    if (this.doublePopOnDamage.getValue().booleanValue() && this.isDoublePoppable(player, playerDamage) && (currentPos == null || player.func_174818_b(pos) < player.func_174818_b(currentPos))) {
                        currentTarget = player;
                        maxDamage = playerDamage;
                        currentPos = pos;
                        maxSelfDamage = selfDamage;
                        this.foundDoublePop = true;
                        if (this.antiFriendPop.getValue() == AntiFriendPop.BREAK || this.antiFriendPop.getValue() == AntiFriendPop.PLACE) continue block0;
                        continue;
                    }
                    if (!(!this.foundDoublePop && (playerDamage > maxDamage || this.extraSelfCalc.getValue() != false && playerDamage >= maxDamage && selfDamage < maxSelfDamage) && (playerDamage > selfDamage || playerDamage > this.minDamage.getValue().floatValue() && !DamageUtil.canTakeDamage(this.suicide.getValue()) || playerDamage > EntityUtil.getHealth((Entity)player)))) continue;
                    maxDamage = playerDamage;
                    currentTarget = player;
                    currentPos = pos;
                    maxSelfDamage = selfDamage;
                    continue;
                }
                if (this.antiFriendPop.getValue() != AntiFriendPop.ALL && this.antiFriendPop.getValue() != AntiFriendPop.PLACE || player == null || !(player.func_174818_b(pos) <= MathUtil.square(this.range.getValue().floatValue() + this.placeRange.getValue().floatValue())) || !Oyvey.friendManager.isFriend(player) || !((double)(friendDamage = DamageUtil.calculateDamage(pos, (Entity)player)) > (double)EntityUtil.getHealth((Entity)player) + 0.5)) continue;
                maxDamage = maxDamageBefore;
                currentTarget = currentTargetBefore;
                currentPos = currentPosBefore;
                maxSelfDamage = maxSelfDamageBefore;
                continue block0;
            }
        }
        if (setToAir != null) {
            AutoCrystall.mc.field_71441_e.func_175656_a(setToAir, state);
            this.webPos = currentPos;
        }
        target = currentTarget;
        this.currentDamage = maxDamage;
        this.placePos = currentPos;
    }

    private EntityPlayer getTarget(boolean unsafe) {
        if (this.targetMode.getValue() == Target.DAMAGE) {
            return null;
        }
        EntityPlayer currentTarget = null;
        for (EntityPlayer player : AutoCrystall.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.isntValid((Entity)player, this.placeRange.getValue().floatValue() + this.range.getValue().floatValue()) || this.antiNaked.getValue().booleanValue() && DamageUtil.isNaked(player) || unsafe && EntityUtil.isSafe((Entity)player)) continue;
            if (this.minArmor.getValue() > 0 && DamageUtil.isArmorLow(player, this.minArmor.getValue())) {
                currentTarget = player;
                break;
            }
            if (currentTarget == null) {
                currentTarget = player;
                continue;
            }
            if (!(AutoCrystall.mc.field_71439_g.func_70068_e((Entity)player) < AutoCrystall.mc.field_71439_g.func_70068_e((Entity)currentTarget))) continue;
            currentTarget = player;
        }
        if (unsafe && currentTarget == null) {
            return this.getTarget(false);
        }
        if (this.predictPos.getValue().booleanValue() && currentTarget != null) {
            GameProfile profile = new GameProfile(currentTarget.func_110124_au() == null ? UUID.fromString("8af022c8-b926-41a0-8b79-2b544ff00fcf") : currentTarget.func_110124_au(), currentTarget.func_70005_c_());
            EntityOtherPlayerMP newTarget = new EntityOtherPlayerMP((World)AutoCrystall.mc.field_71441_e, profile);
            Vec3d extrapolatePosition = MathUtil.extrapolatePlayerPosition(currentTarget, this.predictTicks.getValue());
            newTarget.func_82149_j((Entity)currentTarget);
            newTarget.field_70165_t = extrapolatePosition.field_72450_a;
            newTarget.field_70163_u = extrapolatePosition.field_72448_b;
            newTarget.field_70161_v = extrapolatePosition.field_72449_c;
            newTarget.func_70606_j(EntityUtil.getHealth((Entity)currentTarget));
            newTarget.field_71071_by.func_70455_b(currentTarget.field_71071_by);
            currentTarget = newTarget;
        }
        return currentTarget;
    }

    private void breakCrystal() {
        if (this.explode.getValue().booleanValue() && this.breakTimer.passedMs(this.breakDelay.getValue().intValue()) && (this.switchMode.getValue() == Switch.ALWAYS || this.mainHand || this.offHand)) {
            if (this.packets.getValue() == 1 && this.efficientTarget != null) {
                if (this.justRender.getValue().booleanValue()) {
                    this.doFakeSwing();
                    return;
                }
                if (this.syncedFeetPlace.getValue().booleanValue() && this.gigaSync.getValue().booleanValue() && this.syncedCrystalPos != null && this.damageSync.getValue() != DamageSync.NONE) {
                    return;
                }
                this.rotateTo(this.efficientTarget);
                this.attackEntity(this.efficientTarget);
                this.breakTimer.reset();
            } else if (!this.attackList.isEmpty()) {
                if (this.justRender.getValue().booleanValue()) {
                    this.doFakeSwing();
                    return;
                }
                if (this.syncedFeetPlace.getValue().booleanValue() && this.gigaSync.getValue().booleanValue() && this.syncedCrystalPos != null && this.damageSync.getValue() != DamageSync.NONE) {
                    return;
                }
                for (int i = 0; i < this.packets.getValue(); ++i) {
                    Entity entity = this.attackList.poll();
                    if (entity == null) continue;
                    this.rotateTo(entity);
                    this.attackEntity(entity);
                }
                this.breakTimer.reset();
            }
        }
    }

    private void attackEntity(Entity entity) {
        if (entity != null) {
            if (this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE && this.rotateFirst.getValue().booleanValue() && this.rotate.getValue() != Rotate.OFF) {
                this.packetUseEntities.add(new CPacketUseEntity(entity));
            } else {
                EntityUtil.attackEntity(entity, this.sync.getValue(), this.breakSwing.getValue());
                brokenPos.add(new BlockPos(entity.func_174791_d()).func_177977_b());
            }
        }
    }

    private void doFakeSwing() {
        if (this.fakeSwing.getValue().booleanValue()) {
            EntityUtil.swingArmNoPacket(EnumHand.MAIN_HAND, (EntityLivingBase)AutoCrystall.mc.field_71439_g);
        }
    }

    private void manualBreaker() {
        RayTraceResult result;
        if (this.rotate.getValue() != Rotate.OFF && this.eventMode.getValue() != 2 && this.rotating) {
            if (this.didRotation) {
                AutoCrystall.mc.field_71439_g.field_70125_A = (float)((double)AutoCrystall.mc.field_71439_g.field_70125_A + 4.0E-4);
                this.didRotation = false;
            } else {
                AutoCrystall.mc.field_71439_g.field_70125_A = (float)((double)AutoCrystall.mc.field_71439_g.field_70125_A - 4.0E-4);
                this.didRotation = true;
            }
        }
        if ((this.offHand || this.mainHand) && this.manual.getValue().booleanValue() && this.manualTimer.passedMs(this.manualBreak.getValue().intValue()) && Mouse.isButtonDown((int)1) && AutoCrystall.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151153_ao && AutoCrystall.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151153_ao && AutoCrystall.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151031_f && AutoCrystall.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151062_by && (result = AutoCrystall.mc.field_71476_x) != null) {
            switch (result.field_72313_a) {
                case ENTITY: {
                    Entity entity = result.field_72308_g;
                    if (!(entity instanceof EntityEnderCrystal)) break;
                    EntityUtil.attackEntity(entity, this.sync.getValue(), this.breakSwing.getValue());
                    this.manualTimer.reset();
                    break;
                }
                case BLOCK: {
                    BlockPos mousePos = AutoCrystall.mc.field_71476_x.func_178782_a().func_177984_a();
                    for (Entity target : AutoCrystall.mc.field_71441_e.func_72839_b(null, new AxisAlignedBB(mousePos))) {
                        if (!(target instanceof EntityEnderCrystal)) continue;
                        EntityUtil.attackEntity(target, this.sync.getValue(), this.breakSwing.getValue());
                        this.manualTimer.reset();
                    }
                    break;
                }
            }
        }
    }

    private void rotateTo(Entity entity) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case PLACE: {
                break;
            }
            case BREAK: 
            case ALL: {
                float[] angle = MathUtil.calcAngle(AutoCrystall.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), entity.func_174791_d());
                if (this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE) {
                    Oyvey.rotationManager.setPlayerRotations(angle[0], angle[1]);
                    break;
                }
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
            }
        }
    }

    private void rotateToPos(BlockPos pos) {
        switch (this.rotate.getValue()) {
            case OFF: {
                this.rotating = false;
            }
            case BREAK: {
                break;
            }
            case PLACE: 
            case ALL: {
                float[] angle = MathUtil.calcAngle(AutoCrystall.mc.field_71439_g.func_174824_e(mc.func_184121_ak()), new Vec3d((double)((float)pos.func_177958_n() + 0.5f), (double)((float)pos.func_177956_o() - 0.5f), (double)((float)pos.func_177952_p() + 0.5f)));
                if (this.eventMode.getValue() == 2 && this.threadMode.getValue() == ThreadMode.NONE) {
                    Oyvey.rotationManager.setPlayerRotations(angle[0], angle[1]);
                    break;
                }
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
            }
        }
    }

    private boolean isDoublePoppable(EntityPlayer player, float damage) {
        if (this.doublePop.getValue().booleanValue()) {
            float f;
            float health = EntityUtil.getHealth((Entity)player);
            if ((double)f <= this.popHealth.getValue() && (double)damage > (double)health + 0.5 && damage <= this.popDamage.getValue().floatValue()) {
                Timer timer = this.totemPops.get((Object)player);
                return timer == null || timer.passedMs(this.popTime.getValue().intValue());
            }
        }
        return false;
    }

    private boolean isValid(Entity entity) {
        return entity != null && AutoCrystall.mc.field_71439_g.func_70068_e(entity) <= MathUtil.square(this.breakRange.getValue().floatValue()) && (this.raytrace.getValue() == Raytrace.NONE || this.raytrace.getValue() == Raytrace.PLACE || AutoCrystall.mc.field_71439_g.func_70685_l(entity) || !AutoCrystall.mc.field_71439_g.func_70685_l(entity) && AutoCrystall.mc.field_71439_g.func_70068_e(entity) <= MathUtil.square(this.breaktrace.getValue().floatValue()));
    }

    private boolean isEligableForFeetSync(EntityPlayer player, BlockPos pos) {
        if (this.holySync.getValue().booleanValue()) {
            BlockPos playerPos = new BlockPos(player.func_174791_d());
            for (EnumFacing facing : EnumFacing.values()) {
                BlockPos holyPos;
                if (facing == EnumFacing.DOWN || facing == EnumFacing.UP || !pos.equals((Object)(holyPos = playerPos.func_177977_b().func_177972_a(facing)))) continue;
                return true;
            }
            return false;
        }
        return true;
    }

    private static class RAutoCrystal
    implements Runnable {
        private static RAutoCrystal instance;
        private AutoCrystall autoCrystal;

        private RAutoCrystal() {
        }

        public static RAutoCrystal getInstance(AutoCrystall autoCrystal) {
            if (instance == null) {
                instance = new RAutoCrystal();
                RAutoCrystal.instance.autoCrystal = autoCrystal;
            }
            return instance;
        }

        @Override
        public void run() {
            if (this.autoCrystal.threadMode.getValue() == ThreadMode.WHILE) {
                while (this.autoCrystal.isOn() && this.autoCrystal.threadMode.getValue() == ThreadMode.WHILE) {
                    while (Oyvey.eventManager.ticksOngoing()) {
                    }
                    if (this.autoCrystal.shouldInterrupt.get()) {
                        this.autoCrystal.shouldInterrupt.set(false);
                        this.autoCrystal.syncroTimer.reset();
                        this.autoCrystal.thread.interrupt();
                        break;
                    }
                    this.autoCrystal.threadOngoing.set(true);
                    Oyvey.safetyManager.doSafetyCheck();
                    this.autoCrystal.doAutoCrystal();
                    this.autoCrystal.threadOngoing.set(false);
                    try {
                        Thread.sleep(this.autoCrystal.threadDelay.getValue().intValue());
                    }
                    catch (InterruptedException e) {
                        this.autoCrystal.thread.interrupt();
                        e.printStackTrace();
                    }
                }
            } else if (this.autoCrystal.threadMode.getValue() != ThreadMode.NONE && this.autoCrystal.isOn()) {
                while (Oyvey.eventManager.ticksOngoing()) {
                }
                this.autoCrystal.threadOngoing.set(true);
                Oyvey.safetyManager.doSafetyCheck();
                this.autoCrystal.doAutoCrystal();
                this.autoCrystal.threadOngoing.set(false);
            }
        }
    }

    public static class PlaceInfo {
        private final BlockPos pos;
        private final boolean offhand;
        private final boolean placeSwing;
        private final boolean exactHand;

        public PlaceInfo(BlockPos pos, boolean offhand, boolean placeSwing, boolean exactHand) {
            this.pos = pos;
            this.offhand = offhand;
            this.placeSwing = placeSwing;
            this.exactHand = exactHand;
        }

        public void runPlace() {
            BlockUtil.placeCrystalOnBlock(this.pos, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.placeSwing, this.exactHand);
        }
    }

    public static enum Settings {
        PLACE,
        BREAK,
        RENDER,
        MISC,
        DEV;

    }

    public static enum DamageSync {
        NONE,
        PLACE,
        BREAK;

    }

    public static enum Rotate {
        OFF,
        PLACE,
        BREAK,
        ALL;

    }

    public static enum Target {
        CLOSEST,
        UNSAFE,
        DAMAGE;

    }

    public static enum Logic {
        BREAKPLACE,
        PLACEBREAK;

    }

    public static enum Switch {
        ALWAYS,
        BREAKSLOT,
        CALC;

    }

    public static enum Raytrace {
        NONE,
        PLACE,
        BREAK,
        FULL;

    }

    public static enum AutoSwitch {
        NONE,
        TOGGLE,
        ALWAYS;

    }

    public static enum ThreadMode {
        NONE,
        POOL,
        SOUND,
        WHILE;

    }

    public static enum AntiFriendPop {
        NONE,
        PLACE,
        BREAK,
        ALL;

    }

    public static enum PredictTimer {
        NONE,
        BREAK,
        PREDICT;

    }
}

