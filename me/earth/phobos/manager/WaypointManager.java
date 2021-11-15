/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.manager;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import me.earth.phobos.features.Feature;
import me.earth.phobos.util.RenderUtil;
import me.earth.phobos.util.Util;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class WaypointManager
extends Feature {
    public static final ResourceLocation WAYPOINT_RESOURCE = new ResourceLocation("textures/waypoint.png");
    public Map<String, Waypoint> waypoints = new HashMap<String, Waypoint>();

    public static class Waypoint {
        public String owner;
        public String server;
        public int dimension;
        public int x;
        public int y;
        public int z;
        public int red;
        public int green;
        public int blue;
        public int alpha;

        public Waypoint(String owner, String server, int dimension, int x, int y, int z, Color color) {
            this.owner = owner;
            this.server = server;
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.red = color.getRed();
            this.green = color.getGreen();
            this.blue = color.getBlue();
            this.alpha = color.getAlpha();
        }

        public void renderBox() {
            GL11.glPushMatrix();
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            RenderUtil.drawBlockOutline(new BlockPos(this.x, this.y, this.z), new Color(this.red, this.green, this.blue, this.alpha), 1.0f, true);
            GlStateManager.func_179098_w();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)2929);
            GL11.glPopMatrix();
        }

        public void render() {
            double posX = (double)this.x - Util.mc.func_175598_ae().field_78725_b + 0.5;
            double posY = (double)this.y - Util.mc.func_175598_ae().field_78726_c - 0.5;
            double posZ = (double)this.z - Util.mc.func_175598_ae().field_78723_d + 0.5;
            float scale = (float)(Util.mc.field_71439_g.func_70011_f(posX + Util.mc.func_175598_ae().field_78725_b, posY + Util.mc.func_175598_ae().field_78726_c, posZ + Util.mc.func_175598_ae().field_78723_d) / 4.0);
            if (scale < 1.6f) {
                scale = 1.6f;
            }
            GL11.glPushMatrix();
            GL11.glTranslatef((float)((float)posX), (float)((float)posY + 1.4f), (float)((float)posZ));
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)(-Util.mc.func_175598_ae().field_78735_i), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)Util.mc.func_175598_ae().field_78732_j, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glScalef((float)(-(scale /= 25.0f)), (float)(-scale), (float)scale);
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            int width = Util.mc.field_71466_p.func_78256_a(this.owner) / 2;
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
            Util.mc.field_71466_p.func_175063_a(this.owner, (float)(-width), (float)(-(Util.mc.field_71466_p.field_78288_b + 7)), new Color(this.red, this.green, this.blue, this.alpha).getRGB());
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.func_179098_w();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)2929);
            GL11.glPopMatrix();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }
}

