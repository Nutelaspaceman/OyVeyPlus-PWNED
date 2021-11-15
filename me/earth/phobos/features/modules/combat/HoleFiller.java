/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockWeb
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.lwjgl.input.Keyboard
 */
package me.earth.phobos.features.modules.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.ClickGui;
import me.earth.phobos.features.modules.client.ServerModule;
import me.earth.phobos.features.modules.player.Freecam;
import me.earth.phobos.features.setting.Bind;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class HoleFiller
extends Module {
    private static HoleFiller INSTANCE = new HoleFiller();
    private final Setting<Boolean> server = this.register(new Setting<Boolean>("Server", false));
    private final Setting<Double> range = this.register(new Setting<Double>("PlaceRange", 6.0, 0.0, 10.0));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay/Place", 50, 0, 250));
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("Block/Place", 8, 1, 20));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> raytrace = this.register(new Setting<Boolean>("Raytrace", false));
    private final Setting<Boolean> disable = this.register(new Setting<Boolean>("Disable", true));
    private final Setting<Integer> disableTime = this.register(new Setting<Integer>("Ms/Disable", 200, 1, 250));
    private final Setting<InventoryUtil.Switch> switchMode = this.register(new Setting<InventoryUtil.Switch>("Switch", InventoryUtil.Switch.NORMAL));
    private final Setting<Boolean> webSelf = this.register(new Setting<Boolean>("SelfWeb", false));
    private final Setting<Boolean> highWeb = this.register(new Setting<Boolean>("HighWeb", false));
    private final Setting<Boolean> freecam = this.register(new Setting<Boolean>("Freecam", false));
    private final Setting<Boolean> midSafeHoles = this.register(new Setting<Boolean>("MidSafe", false));
    private final Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", false));
    private final Setting<Boolean> onGroundCheck = this.register(new Setting<Boolean>("OnGroundCheck", false));
    private final Timer offTimer = new Timer();
    private final Timer timer = new Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timer retryTimer = new Timer();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.OBSIDIAN));
    public Setting<PlaceMode> placeMode = this.register(new Setting<PlaceMode>("PlaceMode", PlaceMode.ALL));
    private final Setting<Double> smartRange = this.register(new Setting<Object>("SmartRange", Double.valueOf(6.0), Double.valueOf(0.0), Double.valueOf(10.0), v -> this.placeMode.getValue() == PlaceMode.SMART));
    public Setting<Bind> obbyBind = this.register(new Setting<Bind>("Obsidian", new Bind(-1)));
    public Setting<Bind> webBind = this.register(new Setting<Bind>("Webs", new Bind(-1)));
    public Mode currentMode = Mode.OBSIDIAN;
    private boolean accessedViaBind = false;
    private int targetSlot = -1;
    private int blocksThisTick = 0;
    private boolean isSneaking;
    private boolean hasOffhand = false;
    private boolean placeHighWeb = false;
    private int lastHotbarSlot = -1;
    private boolean switchedItem = false;

    public HoleFiller() {
        super("HoleFiller", "Fills holes around you.", Module.Category.COMBAT, true, false, true);
        this.setInstance();
    }

    public static HoleFiller getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleFiller();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private boolean shouldServer() {
        return ServerModule.getInstance().isConnected() && this.server.getValue() != false;
    }

    @Override
    public void onEnable() {
        if (HoleFiller.fullNullCheck()) {
            this.disable();
        }
        if (!HoleFiller.mc.field_71439_g.field_70122_E && this.onGroundCheck.getValue().booleanValue()) {
            return;
        }
        if (this.shouldServer()) {
            HoleFiller.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            HoleFiller.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module HoleFiller set Enabled true"));
            return;
        }
        this.lastHotbarSlot = HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c;
        if (!this.accessedViaBind) {
            this.currentMode = this.mode.getValue();
        }
        Oyvey.holeManager.update();
        this.offTimer.reset();
    }

    @Override
    public void onTick() {
        if (this.isOn() && (this.blocksPerTick.getValue() != 1 || !this.rotate.getValue().booleanValue())) {
            this.doHoleFill();
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.isOn() && event.getStage() == 0 && this.blocksPerTick.getValue() == 1 && this.rotate.getValue().booleanValue()) {
            this.doHoleFill();
        }
    }

    @Override
    public void onDisable() {
        this.switchItem(true);
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        this.retries.clear();
        this.accessedViaBind = false;
        this.hasOffhand = false;
    }

    @SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (this.obbyBind.getValue().getBind() == Keyboard.getEventKey()) {
                this.accessedViaBind = true;
                this.currentMode = Mode.OBSIDIAN;
                this.toggle();
            }
            if (this.webBind.getValue().getBind() == Keyboard.getEventKey()) {
                this.accessedViaBind = true;
                this.currentMode = Mode.WEBS;
                this.toggle();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void doHoleFill() {
        ArrayList<BlockPos> targets;
        Object object;
        List<BlockPos> object2;
        if (this.check()) {
            return;
        }
        if (this.placeHighWeb) {
            BlockPos pos = new BlockPos(HoleFiller.mc.field_71439_g.field_70165_t, HoleFiller.mc.field_71439_g.field_70163_u + 1.0, HoleFiller.mc.field_71439_g.field_70161_v);
            this.placeBlock(pos);
            this.placeHighWeb = false;
        }
        if (this.midSafeHoles.getValue().booleanValue()) {
            object2 = Oyvey.holeManager.getMidSafety();
            object = object2;
            synchronized (object) {
                targets = new ArrayList<BlockPos>(Oyvey.holeManager.getMidSafety());
            }
        }
        object2 = Oyvey.holeManager.getHoles();
        object = object2;
        synchronized (object) {
            targets = new ArrayList<BlockPos>(Oyvey.holeManager.getHoles());
        }
        for (BlockPos position : targets) {
            int placeability;
            if (HoleFiller.mc.field_71439_g.func_174818_b(position) > MathUtil.square(this.range.getValue()) || this.placeMode.getValue() == PlaceMode.SMART && !this.isPlayerInRange(position)) continue;
            if (position.equals((Object)new BlockPos(HoleFiller.mc.field_71439_g.func_174791_d()))) {
                if (this.currentMode != Mode.WEBS || !this.webSelf.getValue().booleanValue()) continue;
                if (this.highWeb.getValue().booleanValue()) {
                    this.placeHighWeb = true;
                }
            }
            if ((placeability = BlockUtil.isPositionPlaceable(position, this.raytrace.getValue())) == 1 && (this.currentMode == Mode.WEBS || this.switchMode.getValue() == InventoryUtil.Switch.SILENT || this.retries.get((Object)position) == null || this.retries.get((Object)position) < 4)) {
                this.placeBlock(position);
                if (this.currentMode == Mode.WEBS) continue;
                this.retries.put(position, this.retries.get((Object)position) == null ? 1 : this.retries.get((Object)position) + 1);
                continue;
            }
            if (placeability != 3) continue;
            this.placeBlock(position);
        }
    }

    private void placeBlock(BlockPos pos) {
        if (this.blocksThisTick < this.blocksPerTick.getValue() && this.switchItem(false)) {
            boolean smartRotate = this.blocksPerTick.getValue() == 1 && this.rotate.getValue() != false;
            boolean bl = smartRotate;
            this.isSneaking = smartRotate ? BlockUtil.placeBlockSmartRotate(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, this.packet.getValue(), this.isSneaking) : BlockUtil.placeBlock(pos, this.hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue(), this.isSneaking);
            this.timer.reset();
            ++this.blocksThisTick;
        }
    }

    private boolean isPlayerInRange(BlockPos pos) {
        for (EntityPlayer player : HoleFiller.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.isntValid((Entity)player, this.smartRange.getValue())) continue;
            return true;
        }
        return false;
    }

    private boolean check() {
        if (HoleFiller.fullNullCheck() || this.disable.getValue().booleanValue() && this.offTimer.passedMs(this.disableTime.getValue().intValue())) {
            this.disable();
            return true;
        }
        if (HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c != InventoryUtil.findHotbarBlock(this.currentMode == Mode.WEBS ? BlockWeb.class : BlockObsidian.class)) {
            this.lastHotbarSlot = HoleFiller.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        this.switchItem(true);
        if (!this.freecam.getValue().booleanValue() && Oyvey.moduleManager.isModuleEnabled(Freecam.class)) {
            return true;
        }
        this.blocksThisTick = 0;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (this.retryTimer.passedMs(2000L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        switch (this.currentMode) {
            case WEBS: {
                this.hasOffhand = InventoryUtil.isBlock(HoleFiller.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockWeb.class);
                this.targetSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
                break;
            }
            case OBSIDIAN: {
                this.hasOffhand = InventoryUtil.isBlock(HoleFiller.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
                this.targetSlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            }
        }
        return !this.timer.passedMs(this.delay.getValue().intValue());
    }

    private boolean switchItem(boolean back) {
        boolean[] value = InventoryUtil.switchItem(back, this.lastHotbarSlot, this.switchedItem, this.switchMode.getValue(), this.currentMode == Mode.WEBS ? BlockWeb.class : BlockObsidian.class);
        this.switchedItem = value[0];
        return value[1];
    }

    public static enum Mode {
        WEBS,
        OBSIDIAN;

    }

    public static enum PlaceMode {
        SMART,
        ALL;

    }
}

