/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemEndCrystal
 *  net.minecraft.item.ItemExpBottle
 *  net.minecraft.item.ItemMinecart
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.player;

import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.InventoryUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;
import net.minecraft.item.ItemMinecart;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastPlace
extends Module {
    private final Setting<Boolean> all = this.register(new Setting<Boolean>("All", false));
    private final Setting<Boolean> obby = this.register(new Setting<Object>("Obsidian", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> enderChests = this.register(new Setting<Object>("EnderChests", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> crystals = this.register(new Setting<Object>("Crystals", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> exp = this.register(new Setting<Object>("Experience", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> Minecart = this.register(new Setting<Object>("Minecarts", Boolean.valueOf(false), v -> this.all.getValue() == false));
    private final Setting<Boolean> feetExp = this.register(new Setting<Boolean>("ExpFeet", false));
    private final Setting<Boolean> fastCrystal = this.register(new Setting<Boolean>("PacketCrystal", false));
    private BlockPos mousePos = null;

    public FastPlace() {
        super("FastPlace", "Fast everything.", Module.Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && this.feetExp.getValue().booleanValue()) {
            boolean mainHand = FastPlace.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151062_by;
            boolean offHand = FastPlace.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151062_by;
            boolean bl = offHand;
            if (FastPlace.mc.field_71474_y.field_74313_G.func_151470_d() && (FastPlace.mc.field_71439_g.func_184600_cs() == EnumHand.MAIN_HAND && mainHand || FastPlace.mc.field_71439_g.func_184600_cs() == EnumHand.OFF_HAND && offHand)) {
                Oyvey.rotationManager.lookAtVec3d(FastPlace.mc.field_71439_g.func_174791_d());
            }
        }
    }

    @Override
    public void onUpdate() {
        if (FastPlace.fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem(ItemExpBottle.class) && this.exp.getValue().booleanValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(BlockObsidian.class) && this.obby.getValue().booleanValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(BlockEnderChest.class) && this.enderChests.getValue().booleanValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(ItemMinecart.class) && this.Minecart.getValue().booleanValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (this.all.getValue().booleanValue()) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (InventoryUtil.holdingItem(ItemEndCrystal.class) && (this.crystals.getValue().booleanValue() || this.all.getValue().booleanValue())) {
            FastPlace.mc.field_71467_ac = 0;
        }
        if (this.fastCrystal.getValue().booleanValue() && FastPlace.mc.field_71474_y.field_74313_G.func_151470_d()) {
            boolean offhand = FastPlace.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
            boolean bl = offhand;
            if (offhand || FastPlace.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) {
                RayTraceResult result = FastPlace.mc.field_71476_x;
                if (result == null) {
                    return;
                }
                switch (result.field_72313_a) {
                    case MISS: {
                        this.mousePos = null;
                        break;
                    }
                    case BLOCK: {
                        this.mousePos = FastPlace.mc.field_71476_x.func_178782_a();
                        break;
                    }
                    case ENTITY: {
                        Entity entity;
                        if (this.mousePos == null || (entity = result.field_72308_g) == null || !this.mousePos.equals((Object)new BlockPos(entity.field_70165_t, entity.field_70163_u - 1.0, entity.field_70161_v))) break;
                        FastPlace.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.mousePos, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                }
            }
        }
    }
}

