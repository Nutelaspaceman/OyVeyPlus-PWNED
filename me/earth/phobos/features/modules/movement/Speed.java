/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.MobEffects
 *  net.minecraft.util.MovementInput
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.movement;

import java.util.Objects;
import java.util.Random;
import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speed
extends Module {
    private static Speed INSTANCE = new Speed();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.INSTANT));
    public Setting<Boolean> strafeJump = this.register(new Setting<Object>("Jump", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.INSTANT));
    public Setting<Boolean> noShake = this.register(new Setting<Object>("NoShake", Boolean.valueOf(true), v -> this.mode.getValue() != Mode.INSTANT));
    public Setting<Boolean> useTimer = this.register(new Setting<Object>("UseTimer", Boolean.valueOf(false), v -> this.mode.getValue() != Mode.INSTANT));
    public Setting<Double> zeroSpeed = this.register(new Setting<Object>("0-Speed", Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(100.0), v -> this.mode.getValue() == Mode.VANILLA));
    public Setting<Double> speed = this.register(new Setting<Object>("Speed", Double.valueOf(10.0), Double.valueOf(0.1), Double.valueOf(100.0), v -> this.mode.getValue() == Mode.VANILLA));
    public Setting<Double> blocked = this.register(new Setting<Object>("Blocked", Double.valueOf(10.0), Double.valueOf(0.0), Double.valueOf(100.0), v -> this.mode.getValue() == Mode.VANILLA));
    public Setting<Double> unblocked = this.register(new Setting<Object>("Unblocked", Double.valueOf(10.0), Double.valueOf(0.0), Double.valueOf(100.0), v -> this.mode.getValue() == Mode.VANILLA));
    public double startY = 0.0;
    public boolean antiShake = false;
    public double minY = 0.0;
    public boolean changeY = false;
    private double highChainVal = 0.0;
    private double lowChainVal = 0.0;
    private boolean oneTime = false;
    private double bounceHeight = 0.4;
    private float move = 0.26f;
    private int vanillaCounter = 0;

    public Speed() {
        super("Speed", "Makes you faster", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static Speed getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Speed();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private boolean shouldReturn() {
        return Oyvey.moduleManager.isModuleEnabled("Freecam") || Oyvey.moduleManager.isModuleEnabled("Phase") || Oyvey.moduleManager.isModuleEnabled("ElytraFlight") || Oyvey.moduleManager.isModuleEnabled("Strafe") || Oyvey.moduleManager.isModuleEnabled("Flight");
    }

    @Override
    public void onUpdate() {
        if (this.shouldReturn() || Speed.mc.field_71439_g.func_70093_af() || Speed.mc.field_71439_g.func_70090_H() || Speed.mc.field_71439_g.func_180799_ab()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                this.doBoost();
                break;
            }
            case ACCEL: {
                this.doAccel();
                break;
            }
            case ONGROUND: {
                this.doOnground();
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.mode.getValue() != Mode.VANILLA || Speed.nullCheck()) {
            return;
        }
        switch (event.getStage()) {
            case 0: {
                int n = this.vanillaCounter = this.vanilla() ? (this.vanillaCounter = this.vanillaCounter + 1) : 0;
                if (this.vanillaCounter != 4) break;
                this.changeY = true;
                this.minY = Speed.mc.field_71439_g.func_174813_aQ().field_72338_b + (Speed.mc.field_71441_e.func_180495_p(Speed.mc.field_71439_g.func_180425_c()).func_185904_a().func_76230_c() ? -this.blocked.getValue().doubleValue() / 10.0 : this.unblocked.getValue() / 10.0) + this.getJumpBoostModifier();
                return;
            }
            case 1: {
                if (this.vanillaCounter == 3) {
                    Speed.mc.field_71439_g.field_70159_w *= this.zeroSpeed.getValue() / 10.0;
                    Speed.mc.field_71439_g.field_70179_y *= this.zeroSpeed.getValue() / 10.0;
                    break;
                }
                if (this.vanillaCounter != 4) break;
                Speed.mc.field_71439_g.field_70159_w /= this.speed.getValue() / 10.0;
                Speed.mc.field_71439_g.field_70179_y /= this.speed.getValue() / 10.0;
                this.vanillaCounter = 2;
            }
        }
    }

    private double getJumpBoostModifier() {
        double boost = 0.0;
        if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
            int amplifier = Objects.requireNonNull(Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j)).func_76458_c();
            boost *= 1.0 + 0.2 * (double)amplifier;
        }
        return boost;
    }

    private boolean vanillaCheck() {
        if (Speed.mc.field_71439_g.field_70122_E) {
            // empty if block
        }
        return false;
    }

    private boolean vanilla() {
        return Speed.mc.field_71439_g.field_70122_E;
    }

    private void doBoost() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.field_71439_g.field_70122_E) {
            this.startY = Speed.mc.field_71439_g.field_70163_u;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.field_71439_g) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.field_71439_g) && !Speed.mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.field_71439_g)) {
            this.oneTime = true;
            this.antiShake = this.noShake.getValue() != false && Speed.mc.field_71439_g.func_184187_bx() == null;
            Random random = new Random();
            boolean rnd = random.nextBoolean();
            if (Speed.mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
                Speed.mc.field_71439_g.field_70181_x = -this.bounceHeight;
                this.lowChainVal += 1.0;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.15f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.2f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.225f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.25f;
                }
                if (this.lowChainVal >= 7.0) {
                    this.move = 0.27895f;
                }
                if (this.useTimer.getValue().booleanValue()) {
                    Oyvey.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.field_71439_g.field_70163_u == this.startY) {
                Speed.mc.field_71439_g.field_70181_x = this.bounceHeight;
                this.highChainVal += 1.0;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.325f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.4f;
                }
                if (this.highChainVal >= 6.0) {
                    this.move = 0.43395f;
                }
                if (this.useTimer.getValue().booleanValue()) {
                    if (rnd) {
                        Oyvey.timerManager.setTimer(1.3f);
                    } else {
                        Oyvey.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.field_71439_g);
        } else {
            if (this.oneTime) {
                Speed.mc.field_71439_g.field_70181_x = -0.1;
                this.oneTime = false;
            }
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.antiShake = false;
            this.speedOff();
        }
    }

    private void doAccel() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.field_71439_g.field_70122_E) {
            this.startY = Speed.mc.field_71439_g.field_70163_u;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.field_71439_g) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.field_71439_g) && !Speed.mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.field_71439_g)) {
            this.oneTime = true;
            this.antiShake = this.noShake.getValue() != false && Speed.mc.field_71439_g.func_184187_bx() == null;
            Random random = new Random();
            boolean rnd = random.nextBoolean();
            if (Speed.mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
                Speed.mc.field_71439_g.field_70181_x = -this.bounceHeight;
                this.lowChainVal += 1.0;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.getValue().booleanValue()) {
                    Oyvey.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.field_71439_g.field_70163_u == this.startY) {
                Speed.mc.field_71439_g.field_70181_x = this.bounceHeight;
                this.highChainVal += 1.0;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.175f;
                }
                if (this.useTimer.getValue().booleanValue()) {
                    if (rnd) {
                        Oyvey.timerManager.setTimer(1.3f);
                    } else {
                        Oyvey.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.field_71439_g);
        } else {
            if (this.oneTime) {
                Speed.mc.field_71439_g.field_70181_x = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }

    private void doOnground() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.field_71439_g.field_70122_E) {
            this.startY = Speed.mc.field_71439_g.field_70163_u;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.field_71439_g) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.field_71439_g) && !Speed.mc.field_71439_g.field_70123_F && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.field_71439_g) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.field_71439_g)) {
            this.oneTime = true;
            this.antiShake = this.noShake.getValue() != false && Speed.mc.field_71439_g.func_184187_bx() == null;
            Random random = new Random();
            boolean rnd = random.nextBoolean();
            if (Speed.mc.field_71439_g.field_70163_u >= this.startY + this.bounceHeight) {
                Speed.mc.field_71439_g.field_70181_x = -this.bounceHeight;
                this.lowChainVal += 1.0;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.getValue().booleanValue()) {
                    Oyvey.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.field_71439_g.field_70163_u == this.startY) {
                Speed.mc.field_71439_g.field_70181_x = this.bounceHeight;
                this.highChainVal += 1.0;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.2f;
                }
                if (this.useTimer.getValue().booleanValue()) {
                    if (rnd) {
                        Oyvey.timerManager.setTimer(1.3f);
                    } else {
                        Oyvey.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.field_71439_g);
        } else {
            if (this.oneTime) {
                Speed.mc.field_71439_g.field_70181_x = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue() == Mode.ONGROUND || this.mode.getValue() == Mode.BOOST) {
            Speed.mc.field_71439_g.field_70181_x = -0.1;
        }
        this.changeY = false;
        Oyvey.timerManager.setTimer(1.0f);
        this.highChainVal = 0.0;
        this.lowChainVal = 0.0;
        this.antiShake = false;
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().equals(this.mode) && this.mode.getPlannedValue() == Mode.INSTANT) {
            Speed.mc.field_71439_g.field_70181_x = -0.1;
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @SubscribeEvent
    public void onMode(MoveEvent event) {
        if (!(this.shouldReturn() || event.getStage() != 0 || this.mode.getValue() != Mode.INSTANT || Speed.nullCheck() || Speed.mc.field_71439_g.func_70093_af() || Speed.mc.field_71439_g.func_70090_H() || Speed.mc.field_71439_g.func_180799_ab() || Speed.mc.field_71439_g.field_71158_b.field_192832_b == 0.0f && Speed.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f)) {
            if (Speed.mc.field_71439_g.field_70122_E && this.strafeJump.getValue().booleanValue()) {
                Speed.mc.field_71439_g.field_70181_x = 0.4;
                event.setY(0.4);
            }
            MovementInput movementInput = Speed.mc.field_71439_g.field_71158_b;
            float moveForward = movementInput.field_192832_b;
            float moveStrafe = movementInput.field_78902_a;
            float rotationYaw = Speed.mc.field_71439_g.field_70177_z;
            if ((double)moveForward == 0.0 && (double)moveStrafe == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            } else {
                if ((double)moveForward != 0.0) {
                    float f;
                    if ((double)moveStrafe > 0.0) {
                        rotationYaw += (float)((double)moveForward > 0.0 ? -45 : 45);
                    } else if ((double)moveStrafe < 0.0) {
                        rotationYaw += (float)((double)moveForward > 0.0 ? 45 : -45);
                    }
                    moveStrafe = 0.0f;
                    float f2 = moveForward == 0.0f ? moveForward : (f = (moveForward = (double)moveForward > 0.0 ? 1.0f : -1.0f));
                }
                moveStrafe = moveStrafe == 0.0f ? moveStrafe : ((double)moveStrafe > 0.0 ? 1.0f : -1.0f);
                event.setX((double)moveForward * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + (double)moveStrafe * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
                event.setZ((double)moveForward * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - (double)moveStrafe * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
            }
        }
    }

    private void speedOff() {
        float yaw = (float)Math.toRadians(Speed.mc.field_71439_g.field_70177_z);
        if (BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.field_71439_g)) {
            if (Speed.mc.field_71474_y.field_74351_w.func_151470_d() && !Speed.mc.field_71474_y.field_74311_E.func_151470_d() && Speed.mc.field_71439_g.field_70122_E) {
                Speed.mc.field_71439_g.field_70159_w -= (double)MathUtil.sin(yaw) * 0.15;
                Speed.mc.field_71439_g.field_70179_y += (double)MathUtil.cos(yaw) * 0.15;
            }
        } else if (Speed.mc.field_71439_g.field_70123_F) {
            if (Speed.mc.field_71474_y.field_74351_w.func_151470_d() && !Speed.mc.field_71474_y.field_74311_E.func_151470_d() && Speed.mc.field_71439_g.field_70122_E) {
                Speed.mc.field_71439_g.field_70159_w -= (double)MathUtil.sin(yaw) * 0.03;
                Speed.mc.field_71439_g.field_70179_y += (double)MathUtil.cos(yaw) * 0.03;
            }
        } else if (!BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.field_71439_g)) {
            if (Speed.mc.field_71474_y.field_74351_w.func_151470_d() && !Speed.mc.field_71474_y.field_74311_E.func_151470_d() && Speed.mc.field_71439_g.field_70122_E) {
                Speed.mc.field_71439_g.field_70159_w -= (double)MathUtil.sin(yaw) * 0.03;
                Speed.mc.field_71439_g.field_70179_y += (double)MathUtil.cos(yaw) * 0.03;
            }
        } else {
            Speed.mc.field_71439_g.field_70159_w = 0.0;
            Speed.mc.field_71439_g.field_70179_y = 0.0;
        }
    }

    public static enum Mode {
        INSTANT,
        ONGROUND,
        ACCEL,
        BOOST,
        VANILLA;

    }
}

