/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.earth.phobos.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import me.earth.phobos.Oyvey;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.BlockUtill;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.EntityUtill;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Surround
extends Module {
    public static boolean isPlacing = false;
    private final Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("BPT", 12, 1, 20));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 250));
    private final Setting<Boolean> noGhost = this.register(new Setting<Boolean>("Packet", false));
    private final Setting<Boolean> center = this.register(new Setting<Boolean>("TPCenter", false));
    private final Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", true));
    private final Setting<Boolean> helpingBlocks = this.register(new Setting<Boolean>("HelpingBlocks", true));
    private final Setting<Boolean> antiPedo = this.register(new Setting<Boolean>("Always Help", false));
    private final Setting<Boolean> floor = this.register(new Setting<Boolean>("Floor", false));
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private int obbySlot = -1;
    private boolean offHand = false;

    public Surround() {
        super("Surround", "surround", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (Surround.fullNullCheck()) {
            this.disable();
        }
        this.lastHotbarSlot = Surround.mc.field_71439_g.field_71071_by.field_70461_c;
        this.startPos = EntityUtil.getRoundedBlockPos((Entity)Surround.mc.field_71439_g);
        if (this.center.getValue().booleanValue()) {
            Oyvey.positionManager.setPositionPacket((double)this.startPos.func_177958_n() + 0.5, this.startPos.func_177956_o(), (double)this.startPos.func_177952_p() + 0.5, true, true, true);
        }
        this.retries.clear();
        this.retryTimer.reset();
    }

    @Override
    public void onTick() {
        this.doFeetPlace();
    }

    @Override
    public void onDisable() {
        if (Surround.nullCheck()) {
            return;
        }
        isPlacing = false;
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
    }

    private void doFeetPlace() {
        boolean onEChest;
        if (this.check()) {
            return;
        }
        if (Surround.mc.field_71439_g.field_70163_u < Surround.mc.field_71439_g.field_70163_u) {
            this.setEnabled(false);
            return;
        }
        boolean bl = onEChest = Surround.mc.field_71441_e.func_180495_p(new BlockPos(Surround.mc.field_71439_g.func_174791_d())).func_177230_c() == Blocks.field_150477_bB;
        if (Surround.mc.field_71439_g.field_70163_u - (double)((int)Surround.mc.field_71439_g.field_70163_u) < 0.7) {
            onEChest = false;
        }
        if (!BlockUtill.isSafe((Entity)Surround.mc.field_71439_g, onEChest ? 1 : 0, this.floor.getValue())) {
            this.placeBlocks(Surround.mc.field_71439_g.func_174791_d(), BlockUtill.getUnsafeBlockArray((Entity)Surround.mc.field_71439_g, onEChest ? 1 : 0, this.floor.getValue()), this.helpingBlocks.getValue(), false);
        } else if (!BlockUtill.isSafe((Entity)Surround.mc.field_71439_g, onEChest ? 0 : -1, false) && this.antiPedo.getValue().booleanValue()) {
            this.placeBlocks(Surround.mc.field_71439_g.func_174791_d(), BlockUtill.getUnsafeBlockArray((Entity)Surround.mc.field_71439_g, onEChest ? 0 : -1, false), false, false);
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < 1) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
            while (iterator.hasNext()) {
                Vec3d vec3d;
                array[i] = vec3d = iterator.next();
                ++i;
            }
            int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), EntityUtill.getUnsafeBlockArrayFromVec3d(this.areClose(array), 0, true), true, false);
            }
            if (placementsBefore < this.placements) {
                this.extendingBlocks.clear();
            }
        } else if (this.extendingBlocks.size() > 2 || this.extenders >= 1) {
            this.extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtill.getUnsafeBlockArray((Entity)Surround.mc.field_71439_g, 0, true)) {
                if (!vec3d.equals((Object)pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return Surround.mc.field_71439_g.func_174791_d().func_178787_e(vec3ds[0].func_178787_e(vec3ds[1]));
        }
        return null;
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping) {
        boolean gotHelp = true;
        block5: for (Vec3d vec3d : vec3ds) {
            gotHelp = true;
            BlockPos position = new BlockPos(pos).func_177963_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (this.retries.get((Object)position) == null || this.retries.get((Object)position) < 4) {
                        this.placeBlock(position);
                        this.retries.put(position, this.retries.get((Object)position) == null ? 1 : this.retries.get((Object)position) + 1);
                        this.retryTimer.reset();
                        continue block5;
                    }
                }
                case 2: {
                    if (!hasHelpingBlocks) continue block5;
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (!isHelping) continue block5;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check() {
        if (Surround.nullCheck()) {
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.field_71439_g.func_184592_cb().func_77973_b(), BlockObsidian.class);
        isPlacing = false;
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (obbySlot == -1 && !this.offHand && echestSlot == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + (Object)ChatFormatting.RED + "No Obsidian in hotbar disabling...");
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtil.stopSneaking(this.isSneaking);
        if (Surround.mc.field_71439_g.field_71071_by.field_70461_c != this.lastHotbarSlot && Surround.mc.field_71439_g.field_71071_by.field_70461_c != obbySlot && Surround.mc.field_71439_g.field_71071_by.field_70461_c != echestSlot) {
            this.lastHotbarSlot = Surround.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        if (!this.startPos.equals((Object)EntityUtil.getRoundedBlockPos((Entity)Surround.mc.field_71439_g))) {
            this.disable();
            return true;
        }
        return !this.timer.passedMs(this.delay.getValue().intValue());
    }

    private void placeBlock(BlockPos pos) {
        if (this.placements < this.blocksPerTick.getValue()) {
            int originalSlot = Surround.mc.field_71439_g.field_71071_by.field_70461_c;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                this.toggle();
            }
            isPlacing = true;
            Surround.mc.field_71439_g.field_71071_by.field_70461_c = obbySlot == -1 ? eChestSot : obbySlot;
            Surround.mc.field_71442_b.func_78765_e();
            this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
            Surround.mc.field_71439_g.field_71071_by.field_70461_c = originalSlot;
            Surround.mc.field_71442_b.func_78765_e();
            this.didPlace = true;
            ++this.placements;
        }
    }
}

