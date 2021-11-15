/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.material.Material
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.earth.phobos.features.modules.movement;

import me.earth.phobos.event.events.StepEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Step
extends Module {
    private static Step instance;
    public Setting<Boolean> vanilla = this.register(new Setting<Boolean>("Vanilla", false));
    public Setting<Integer> stepHeight = this.register(new Setting<Integer>("Height", 2, 1, 2));
    public Setting<Boolean> turnOff = this.register(new Setting<Boolean>("Disable", false));

    public Step() {
        super("Step", "Allows you to step up blocks", Module.Category.MOVEMENT, true, false, false);
        instance = this;
    }

    public static Step getInstance() {
        if (instance == null) {
            instance = new Step();
        }
        return instance;
    }

    @SubscribeEvent
    public void onStep(StepEvent event) {
        if (Step.mc.field_71439_g.field_70122_E && !Step.mc.field_71439_g.func_70055_a(Material.field_151586_h) && !Step.mc.field_71439_g.func_70055_a(Material.field_151587_i) && Step.mc.field_71439_g.field_70124_G && Step.mc.field_71439_g.field_70143_R == 0.0f && !Step.mc.field_71474_y.field_74314_A.field_74513_e && !Step.mc.field_71439_g.func_70617_f_()) {
            event.setHeight(this.stepHeight.getValue().intValue());
            double rheight = Step.mc.field_71439_g.func_174813_aQ().field_72338_b - Step.mc.field_71439_g.field_70163_u;
            if (rheight >= 0.625) {
                if (!this.vanilla.getValue().booleanValue()) {
                    this.ncpStep(rheight);
                }
                if (this.turnOff.getValue().booleanValue()) {
                    this.disable();
                }
            }
        } else {
            event.setHeight(0.6f);
        }
    }

    private void ncpStep(double height) {
        block12: {
            double y;
            double posZ;
            double posX;
            block11: {
                posX = Step.mc.field_71439_g.field_70165_t;
                posZ = Step.mc.field_71439_g.field_70161_v;
                y = Step.mc.field_71439_g.field_70163_u;
                if (!(height < 1.1)) break block11;
                double first = 0.42;
                double second = 0.75;
                if (height != 1.0) {
                    first *= height;
                    second *= height;
                    if (first > 0.425) {
                        first = 0.425;
                    }
                    if (second > 0.78) {
                        second = 0.78;
                    }
                    if (second < 0.49) {
                        second = 0.49;
                    }
                }
                Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + first, posZ, false));
                if (!(y + second < y + height)) break block12;
                Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + second, posZ, false));
                break block12;
            }
            if (height < 1.6) {
                double[] offset = new double[]{0.42, 0.33, 0.24, 0.083, -0.078};
                for (double off : offset) {
                    Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y += off, posZ, false));
                }
            } else if (height < 2.1) {
                double[] heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
                for (double off : heights) {
                    Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + off, posZ, false));
                }
            } else {
                double[] heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
                for (double off : heights) {
                    Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(posX, y + off, posZ, false));
                }
            }
        }
    }
}

