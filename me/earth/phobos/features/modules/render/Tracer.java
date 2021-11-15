/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.features.modules.render;

import java.awt.Color;
import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.combat.AutoCrystall;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.MathUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Tracer
extends Module {
    public Setting<Boolean> players = this.register(new Setting<Boolean>("Players", true));
    public Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mobs", false));
    public Setting<Boolean> animals = this.register(new Setting<Boolean>("Animals", false));
    public Setting<Boolean> invisibles = this.register(new Setting<Boolean>("Invisibles", false));
    public Setting<Boolean> drawFromSky = this.register(new Setting<Boolean>("DrawFromSky", false));
    public Setting<Float> width = this.register(new Setting<Float>("Width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    public Setting<Integer> distance = this.register(new Setting<Integer>("Radius", 300, 0, 300));
    public Setting<Boolean> crystalCheck = this.register(new Setting<Boolean>("CrystalCheck", false));

    public Tracer() {
        super("Tracers", "Draws lines to other players.", Module.Category.RENDER, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (Tracer.fullNullCheck()) {
            return;
        }
        GlStateManager.func_179094_E();
        Tracer.mc.field_71441_e.field_72996_f.stream().filter(EntityUtil::isLiving).filter(entity -> entity instanceof EntityPlayer ? this.players.getValue().booleanValue() && Tracer.mc.field_71439_g != entity : (EntityUtil.isPassive(entity) ? this.animals.getValue().booleanValue() : this.mobs.getValue().booleanValue())).filter(entity -> Tracer.mc.field_71439_g.func_70068_e(entity) < MathUtil.square(this.distance.getValue().intValue())).filter(entity -> this.invisibles.getValue() != false || !entity.func_82150_aj()).forEach(entity -> {
            float[] colour = this.getColorByDistance((Entity)entity);
            this.drawLineToEntity((Entity)entity, colour[0], colour[1], colour[2], colour[3]);
        });
        GlStateManager.func_179121_F();
    }

    public double interpolate(double now, double then) {
        return then + (now - then) * (double)mc.func_184121_ak();
    }

    public double[] interpolate(Entity entity) {
        double posX = this.interpolate(entity.field_70165_t, entity.field_70142_S) - Tracer.mc.func_175598_ae().field_78725_b;
        double posY = this.interpolate(entity.field_70163_u, entity.field_70137_T) - Tracer.mc.func_175598_ae().field_78726_c;
        double posZ = this.interpolate(entity.field_70161_v, entity.field_70136_U) - Tracer.mc.func_175598_ae().field_78723_d;
        return new double[]{posX, posY, posZ};
    }

    public void drawLineToEntity(Entity e, float red, float green, float blue, float opacity) {
        double[] xyz = this.interpolate(e);
        this.drawLine(xyz[0], xyz[1], xyz[2], e.field_70131_O, red, green, blue, opacity);
    }

    public void drawLine(double posx, double posy, double posz, double up, float red, float green, float blue, float opacity) {
        Vec3d eyes = new Vec3d(0.0, 0.0, 1.0).func_178789_a(-((float)Math.toRadians(Tracer.mc.field_71439_g.field_70125_A))).func_178785_b(-((float)Math.toRadians(Tracer.mc.field_71439_g.field_70177_z)));
        if (!this.drawFromSky.getValue().booleanValue()) {
            this.drawLineFromPosToPos(eyes.field_72450_a, eyes.field_72448_b + (double)Tracer.mc.field_71439_g.func_70047_e(), eyes.field_72449_c, posx, posy, posz, up, red, green, blue, opacity);
        } else {
            this.drawLineFromPosToPos(posx, 256.0, posz, posx, posy, posz, up, red, green, blue, opacity);
        }
    }

    public void drawLineFromPosToPos(double posx, double posy, double posz, double posx2, double posy2, double posz2, double up, float red, float green, float blue, float opacity) {
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)this.width.getValue().floatValue());
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)opacity);
        GlStateManager.func_179140_f();
        GL11.glLoadIdentity();
        Tracer.mc.field_71460_t.func_78467_g(mc.func_184121_ak());
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)posx, (double)posy, (double)posz);
        GL11.glVertex3d((double)posx2, (double)posy2, (double)posz2);
        GL11.glVertex3d((double)posx2, (double)posy2, (double)posz2);
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
        GlStateManager.func_179145_e();
    }

    public float[] getColorByDistance(Entity entity) {
        if (entity instanceof EntityPlayer && Oyvey.friendManager.isFriend(entity.func_70005_c_())) {
            return new float[]{0.0f, 0.5f, 1.0f, 1.0f};
        }
        AutoCrystall autoCrystal = Oyvey.moduleManager.getModuleByClass(AutoCrystall.class);
        Color col = new Color(Color.HSBtoRGB((float)(Math.max(0.0, Math.min(Tracer.mc.field_71439_g.func_70068_e(entity), this.crystalCheck.getValue() != false ? (double)(autoCrystal.placeRange.getValue().floatValue() * autoCrystal.placeRange.getValue().floatValue()) : 2500.0) / (double)(this.crystalCheck.getValue() != false ? autoCrystal.placeRange.getValue().floatValue() * autoCrystal.placeRange.getValue().floatValue() : 2500.0f)) / 3.0), 1.0f, 0.8f) | 0xFF000000);
        return new float[]{(float)col.getRed() / 255.0f, (float)col.getGreen() / 255.0f, (float)col.getBlue() / 255.0f, 1.0f};
    }
}

