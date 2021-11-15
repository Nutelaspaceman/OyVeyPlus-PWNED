/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.combat;

import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.combat.AutoCrystall;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.InventoryUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Offhand
extends Module {
    private static Offhand instance;
    public final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NORMAL));
    public final Setting<AltItem> alternateItem = this.register(new Setting<AltItem>("Item", AltItem.CRYSTALS, v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Float> switchHealth = this.register(new Setting<Float>("Health", Float.valueOf(16.0f), Float.valueOf(0.0f), Float.valueOf(20.0f), v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Float> holeSwitchHealth = this.register(new Setting<Float>("Hole HP", Float.valueOf(16.0f), Float.valueOf(0.0f), Float.valueOf(20.0f), v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Boolean> swordGap = this.register(new Setting<Boolean>("SwordGap", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Boolean> pickaxeGap = this.register(new Setting<Boolean>("PickaxeGap", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Boolean> switchWhenFalling = this.register(new Setting<Boolean>("Fall Switch", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Float> maxFallDistance = this.register(new Setting<Float>("Fall Distance", Float.valueOf(20.0f), Float.valueOf(2.0f), Float.valueOf(150.0f), v -> this.switchWhenFalling.getValue() != false && this.mode.getValue() == Mode.HEALTH));
    public final Setting<Boolean> crystalAuraCheck = this.register(new Setting<Boolean>("CA Check", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Boolean> bowCheck = this.register(new Setting<Boolean>("Bow Check", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Integer> bowDistance = this.register(new Setting<Integer>("Bow Distance", Integer.valueOf(14), Integer.valueOf(1), Integer.valueOf(60), v -> this.bowCheck.getValue() != false && this.mode.getValue() == Mode.HEALTH));
    private final Setting<Boolean> updateSwitch = this.register(new Setting<Boolean>("Update Switch", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.HEALTH));
    private final Setting<Integer> updateSwitchDelay = this.register(new Setting<Integer>("Update Switch Delay", Integer.valueOf(10), Integer.valueOf(0), Integer.valueOf(100), v -> this.updateSwitch.getValue() != false && this.mode.getValue() == Mode.HEALTH));
    public final Setting<Boolean> absorptionCheck = this.register(new Setting<Boolean>("Absorption Check", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.HEALTH));
    public final Setting<Boolean> cancelMovement = this.register(new Setting<Boolean>("Cancel Motion", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.NORMAL));
    public final Setting<Boolean> hotbar = this.register(new Setting<Boolean>("Hotbar", true));
    public final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 0, 0, 40));
    private int tickTimer = 0;
    private Timer updateTimer = new Timer();
    private boolean switching = false;
    private boolean cancelMotionEvents = false;

    public Offhand() {
        super("Offhand", "Allows you to switch up your Offhand.", Module.Category.COMBAT, true, false, false);
        instance = this;
    }

    @Override
    public void onUpdate() {
        if (Offhand.mc.field_71462_r instanceof GuiContainer && !(Offhand.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        if (Offhand.nullCheck()) {
            return;
        }
        if (!this.updateSwitch.getValue().booleanValue()) {
            return;
        }
        if (!this.updateTimer.passedMs(this.updateSwitchDelay.getValue().intValue())) {
            return;
        }
        this.updateTimer.reset();
        if (this.mode.getValue() == Mode.HEALTH) {
            ItemStack heldItem = Offhand.mc.field_71439_g.func_184592_cb();
            if (this.getPlayerHealth() < this.switchHealth.getValue().floatValue() && heldItem.func_77973_b() != Items.field_190929_cY) {
                this.switchToItem(Items.field_190929_cY);
            }
        }
    }

    @Override
    public void onTick() {
        if (Offhand.mc.field_71462_r instanceof GuiContainer && !(Offhand.mc.field_71462_r instanceof GuiInventory)) {
            return;
        }
        if (Offhand.nullCheck()) {
            return;
        }
        this.doOffhand();
    }

    @SubscribeEvent(priority=EventPriority.HIGH)
    public void onMove(MoveEvent event) {
        event.setCanceled(this.cancelMotionEvents);
    }

    private void doOffhand() {
        ItemStack heldItem = Offhand.mc.field_71439_g.func_184592_cb();
        if (this.tickTimer < this.delay.getValue()) {
            return;
        }
        if (this.mode.getValue() == Mode.NORMAL) {
            if (heldItem.func_190926_b() || heldItem.func_77973_b() != Items.field_190929_cY) {
                this.switchToItem(Items.field_190929_cY);
            }
        } else if (!this.shouldSwitchToTotem()) {
            AltItem altItem = this.getItemToPutInOffhand();
            if (heldItem.func_190926_b() || heldItem.func_77973_b() != altItem.getItemClass()) {
                this.switchToItem(altItem.getItemClass());
            }
        } else if (heldItem.func_190926_b() || heldItem.func_77973_b() != Items.field_190929_cY) {
            this.switchToItem(Items.field_190929_cY);
        }
    }

    private void switchToItem(Item item) {
        this.switching = true;
        this.tickTimer = 0;
        int itemSlot = this.getItemSlot(item);
        if (itemSlot == -1) {
            return;
        }
        if (this.cancelMovement.getValue().booleanValue() && this.mode.getValue() == Mode.NORMAL) {
            this.cancelMotionEvents = true;
        }
        Offhand.mc.field_71442_b.func_187098_a(0, itemSlot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
        Offhand.mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
        Offhand.mc.field_71442_b.func_187098_a(0, itemSlot, 0, ClickType.PICKUP, (EntityPlayer)Offhand.mc.field_71439_g);
        Offhand.mc.field_71442_b.func_78765_e();
        this.cancelMotionEvents = false;
        this.switching = false;
    }

    private AltItem getItemToPutInOffhand() {
        Item mainHandItem = Offhand.mc.field_71439_g.func_184614_ca().func_77973_b();
        Item offHandItem = Offhand.mc.field_71439_g.func_184592_cb().func_77973_b();
        if (this.getItemSlot(Items.field_151153_ao) != -1 || offHandItem == Items.field_151153_ao) {
            if (this.swordGap.getValue().booleanValue() && mainHandItem == Items.field_151048_u) {
                return AltItem.GAPPLES;
            }
            if (this.pickaxeGap.getValue().booleanValue() && mainHandItem == Items.field_151046_w) {
                return AltItem.GAPPLES;
            }
        }
        return this.alternateItem.getValue();
    }

    private int getItemSlot(Item item) {
        return InventoryUtil.findStackInventory(item, this.hotbar.getValue());
    }

    private boolean shouldSwitchToTotem() {
        if (this.getPlayerHealth() < this.getSwitchHealth()) {
            return true;
        }
        if (this.crystalAuraCheck.getValue().booleanValue() && AutoCrystall.getInstance().isOff()) {
            return true;
        }
        if (this.switchWhenFalling.getValue().booleanValue() && Offhand.mc.field_71439_g.field_70143_R >= this.maxFallDistance.getValue().floatValue()) {
            return true;
        }
        return this.bowCheck.getValue() != false && this.isAnyPlayerHoldingABow();
    }

    private boolean isAnyPlayerHoldingABow() {
        for (Entity entity : Offhand.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer)entity;
            if (player.field_145783_c == Offhand.mc.field_71439_g.field_145783_c || Oyvey.friendManager.isFriend(player.func_70005_c_()) || player.func_184614_ca().func_77973_b() != Items.field_151031_f || !(Offhand.mc.field_71439_g.func_70032_d((Entity)player) <= (float)this.bowDistance.getValue().intValue())) continue;
            return true;
        }
        return false;
    }

    private float getPlayerHealth() {
        if (this.absorptionCheck.getValue().booleanValue()) {
            return Offhand.mc.field_71439_g.func_110143_aJ() + Offhand.mc.field_71439_g.func_110139_bj();
        }
        return Offhand.mc.field_71439_g.func_110143_aJ();
    }

    private float getSwitchHealth() {
        if (BlockUtil.isInHole(Offhand.mc.field_71439_g)) {
            return this.holeSwitchHealth.getValue().floatValue();
        }
        return this.switchHealth.getValue().floatValue();
    }

    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.NORMAL) {
            return "Totem";
        }
        switch (this.alternateItem.getValue()) {
            case CRYSTALS: {
                return "Crystals";
            }
            case GAPPLES: {
                return "Gapples";
            }
            case SHIELD: {
                return "Shield";
            }
        }
        return "nigger";
    }

    public Offhand getInstance() {
        if (instance == null) {
            instance = new Offhand();
        }
        return instance;
    }

    public static enum AltItem {
        CRYSTALS(Items.field_185158_cP),
        GAPPLES(Items.field_151153_ao),
        SHIELD(Items.field_185159_cQ);

        private final Item item;

        private AltItem(Item item) {
            this.item = item;
        }

        public Item getItemClass() {
            return this.item;
        }
    }

    public static enum Mode {
        NORMAL,
        HEALTH;

    }
}

