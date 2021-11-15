/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.client.renderer.DestroyBlockProgress
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.MobEffects
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.features.modules.client;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.Render2DEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.combat.AutoCrystall;
import me.earth.phobos.features.modules.combat.Killaura;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.ColorUtil;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Components
extends Module {
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final double HALF_PI = 1.5707963267948966;
    public static ResourceLocation logo = new ResourceLocation("textures/phobos.png");
    public Setting<Boolean> inventory = this.register(new Setting<Boolean>("Inventory", false));
    public Setting<Integer> invX = this.register(new Setting<Object>("InvX", Integer.valueOf(564), Integer.valueOf(0), Integer.valueOf(1000), v -> this.inventory.getValue()));
    public Setting<Integer> invY = this.register(new Setting<Object>("InvY", Integer.valueOf(467), Integer.valueOf(0), Integer.valueOf(1000), v -> this.inventory.getValue()));
    public Setting<Integer> fineinvX = this.register(new Setting<Object>("InvFineX", Integer.valueOf(0), v -> this.inventory.getValue()));
    public Setting<Integer> fineinvY = this.register(new Setting<Object>("InvFineY", Integer.valueOf(0), v -> this.inventory.getValue()));
    public Setting<Boolean> renderXCarry = this.register(new Setting<Object>("RenderXCarry", Boolean.valueOf(false), v -> this.inventory.getValue()));
    public Setting<Integer> invH = this.register(new Setting<Object>("InvH", Integer.valueOf(3), v -> this.inventory.getValue()));
    public Setting<Boolean> holeHud = this.register(new Setting<Boolean>("HoleHUD", false));
    public Setting<Integer> holeX = this.register(new Setting<Object>("HoleX", Integer.valueOf(279), Integer.valueOf(0), Integer.valueOf(1000), v -> this.holeHud.getValue()));
    public Setting<Integer> holeY = this.register(new Setting<Object>("HoleY", Integer.valueOf(485), Integer.valueOf(0), Integer.valueOf(1000), v -> this.holeHud.getValue()));
    public Setting<Compass> compass = this.register(new Setting<Compass>("Compass", Compass.NONE));
    public Setting<Integer> compassX = this.register(new Setting<Object>("CompX", Integer.valueOf(472), Integer.valueOf(0), Integer.valueOf(1000), v -> this.compass.getValue() != Compass.NONE));
    public Setting<Integer> compassY = this.register(new Setting<Object>("CompY", Integer.valueOf(424), Integer.valueOf(0), Integer.valueOf(1000), v -> this.compass.getValue() != Compass.NONE));
    public Setting<Integer> scale = this.register(new Setting<Object>("Scale", Integer.valueOf(3), Integer.valueOf(0), Integer.valueOf(10), v -> this.compass.getValue() != Compass.NONE));
    public Setting<Boolean> playerViewer = this.register(new Setting<Boolean>("PlayerViewer", false));
    public Setting<Integer> playerViewerX = this.register(new Setting<Object>("PlayerX", Integer.valueOf(752), Integer.valueOf(0), Integer.valueOf(1000), v -> this.playerViewer.getValue()));
    public Setting<Integer> playerViewerY = this.register(new Setting<Object>("PlayerY", Integer.valueOf(497), Integer.valueOf(0), Integer.valueOf(1000), v -> this.playerViewer.getValue()));
    public Setting<Float> playerScale = this.register(new Setting<Object>("PlayerScale", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(2.0f), v -> this.playerViewer.getValue()));
    public Setting<Boolean> imageLogo = this.register(new Setting<Boolean>("ImageLogo", false));
    public Setting<Integer> imageX = this.register(new Setting<Object>("ImageX", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> this.imageLogo.getValue()));
    public Setting<Integer> imageY = this.register(new Setting<Object>("ImageY", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> this.imageLogo.getValue()));
    public Setting<Integer> imageWidth = this.register(new Setting<Object>("ImageWidth", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000), v -> this.imageLogo.getValue()));
    public Setting<Integer> imageHeight = this.register(new Setting<Object>("ImageHeight", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(1000), v -> this.imageLogo.getValue()));
    public Setting<Boolean> targetHud = this.register(new Setting<Boolean>("TargetHud", false));
    public Setting<Boolean> targetHudBackground = this.register(new Setting<Object>("TargetHudBackground", Boolean.valueOf(true), v -> this.targetHud.getValue()));
    public Setting<Integer> targetHudX = this.register(new Setting<Object>("TargetHudX", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> this.targetHud.getValue()));
    public Setting<Integer> targetHudY = this.register(new Setting<Object>("TargetHudY", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(1000), v -> this.targetHud.getValue()));
    public Setting<TargetHudDesign> design = this.register(new Setting<Object>("Design", (Object)TargetHudDesign.NORMAL, v -> this.targetHud.getValue()));
    public Setting<Boolean> clock = this.register(new Setting<Boolean>("Clock", true));
    public Setting<Boolean> clockFill = this.register(new Setting<Boolean>("ClockFill", true));
    public Setting<Float> clockX = this.register(new Setting<Object>("ClockX", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(1000.0f), v -> this.clock.getValue()));
    public Setting<Float> clockY = this.register(new Setting<Object>("ClockY", Float.valueOf(2.0f), Float.valueOf(0.0f), Float.valueOf(1000.0f), v -> this.clock.getValue()));
    public Setting<Float> clockRadius = this.register(new Setting<Object>("ClockRadius", Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(100.0f), v -> this.clock.getValue()));
    public Setting<Float> clockLineWidth = this.register(new Setting<Object>("ClockLineWidth", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), v -> this.clock.getValue()));
    public Setting<Integer> clockSlices = this.register(new Setting<Object>("ClockSlices", Integer.valueOf(360), Integer.valueOf(1), Integer.valueOf(720), v -> this.clock.getValue()));
    public Setting<Integer> clockLoops = this.register(new Setting<Object>("ClockLoops", Integer.valueOf(1), Integer.valueOf(1), Integer.valueOf(720), v -> this.clock.getValue()));
    private final Map<EntityPlayer, Map<Integer, ItemStack>> hotbarMap = new HashMap<EntityPlayer, Map<Integer, ItemStack>>();

    public Components() {
        super("Components", "HudComponents", Module.Category.CLIENT, false, false, true);
    }

    public static EntityPlayer getClosestEnemy() {
        EntityPlayer closestPlayer = null;
        for (EntityPlayer player : Components.mc.field_71441_e.field_73010_i) {
            if (player == Components.mc.field_71439_g || Oyvey.friendManager.isFriend(player)) continue;
            if (closestPlayer == null) {
                closestPlayer = player;
                continue;
            }
            if (!(Components.mc.field_71439_g.func_70068_e((Entity)player) < Components.mc.field_71439_g.func_70068_e((Entity)closestPlayer))) continue;
            closestPlayer = player;
        }
        return closestPlayer;
    }

    private static double getPosOnCompass(Direction dir) {
        double yaw = Math.toRadians(MathHelper.func_76142_g((float)Components.mc.field_71439_g.field_70177_z));
        int index = dir.ordinal();
        return yaw + (double)index * 1.5707963267948966;
    }

    private static void preboxrender() {
        GL11.glPushMatrix();
        GlStateManager.func_179094_E();
        GlStateManager.func_179118_c();
        GlStateManager.func_179086_m((int)256);
        GlStateManager.func_179147_l();
        GlStateManager.func_179131_c((float)255.0f, (float)255.0f, (float)255.0f, (float)255.0f);
    }

    private static void postboxrender() {
        GlStateManager.func_179084_k();
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        GlStateManager.func_179126_j();
        GlStateManager.func_179141_d();
        GlStateManager.func_179121_F();
        GL11.glPopMatrix();
    }

    private static void preitemrender() {
        GL11.glPushMatrix();
        GL11.glDepthMask((boolean)true);
        GlStateManager.func_179086_m((int)256);
        GlStateManager.func_179097_i();
        GlStateManager.func_179126_j();
        RenderHelper.func_74519_b();
        GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)0.01f);
    }

    private static void postitemrender() {
        GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.func_74518_a();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179140_f();
        GlStateManager.func_179139_a((double)0.5, (double)0.5, (double)0.5);
        GlStateManager.func_179097_i();
        GlStateManager.func_179126_j();
        GlStateManager.func_179152_a((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glPopMatrix();
    }

    public static void drawCompleteImage(int posX, int posY, int width, int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)posX, (float)posY, (float)0.0f);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3f((float)0.0f, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3f((float)width, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3f((float)width, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (Components.fullNullCheck()) {
            return;
        }
        if (this.playerViewer.getValue().booleanValue()) {
            this.drawPlayer();
        }
        if (this.compass.getValue() != Compass.NONE) {
            this.drawCompass();
        }
        if (this.holeHud.getValue().booleanValue()) {
            this.drawOverlay(event.partialTicks);
        }
        if (this.inventory.getValue().booleanValue()) {
            this.renderInventory();
        }
        if (this.imageLogo.getValue().booleanValue()) {
            this.drawImageLogo();
        }
        if (this.targetHud.getValue().booleanValue()) {
            this.drawTargetHud(event.partialTicks);
        }
        if (this.clock.getValue().booleanValue()) {
            RenderUtil.drawClock(this.clockX.getValue().floatValue(), this.clockY.getValue().floatValue(), this.clockRadius.getValue().floatValue(), this.clockSlices.getValue(), this.clockLoops.getValue(), this.clockLineWidth.getValue().floatValue(), this.clockFill.getValue(), new Color(255, 0, 0, 255));
        }
    }

    public void drawTargetHud(float partialTicks) {
        if (this.design.getValue() == TargetHudDesign.NORMAL) {
            EntityPlayer target;
            EntityPlayer entityPlayer = AutoCrystall.target != null ? AutoCrystall.target : (target = Killaura.target instanceof EntityPlayer ? (EntityPlayer)Killaura.target : Components.getClosestEnemy());
            if (target == null) {
                return;
            }
            if (this.targetHudBackground.getValue().booleanValue()) {
                RenderUtil.drawRectangleCorrectly(this.targetHudX.getValue(), this.targetHudY.getValue(), 210, 100, ColorUtil.toRGBA(20, 20, 20, 160));
            }
            GlStateManager.func_179101_C();
            GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
            GlStateManager.func_179090_x();
            GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            try {
                GuiInventory.func_147046_a((int)(this.targetHudX.getValue() + 30), (int)(this.targetHudY.getValue() + 90), (int)45, (float)0.0f, (float)0.0f, (EntityLivingBase)target);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            GlStateManager.func_179091_B();
            GlStateManager.func_179098_w();
            GlStateManager.func_179147_l();
            GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
            this.renderer.drawStringWithShadow(target.func_70005_c_(), this.targetHudX.getValue() + 60, this.targetHudY.getValue() + 10, ColorUtil.toRGBA(255, 0, 0, 255));
            float health = target.func_110143_aJ() + target.func_110139_bj();
            int healthColor = health >= 16.0f ? ColorUtil.toRGBA(0, 255, 0, 255) : (health >= 10.0f ? ColorUtil.toRGBA(255, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
            DecimalFormat df = new DecimalFormat("##.#");
            this.renderer.drawStringWithShadow(df.format(target.func_110143_aJ() + target.func_110139_bj()), this.targetHudX.getValue() + 60 + this.renderer.getStringWidth(target.func_70005_c_() + "  "), this.targetHudY.getValue() + 10, healthColor);
            Integer ping = EntityUtil.isFakePlayer(target) ? 0 : (mc.func_147114_u().func_175102_a(target.func_110124_au()) == null ? 0 : mc.func_147114_u().func_175102_a(target.func_110124_au()).func_178853_c());
            int color = ping >= 100 ? ColorUtil.toRGBA(0, 255, 0, 255) : (ping > 50 ? ColorUtil.toRGBA(255, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
            this.renderer.drawStringWithShadow("Ping: " + (ping == null ? 0 : ping), this.targetHudX.getValue() + 60, this.targetHudY.getValue() + this.renderer.getFontHeight() + 20, color);
            this.renderer.drawStringWithShadow("Pops: " + Oyvey.totemPopManager.getTotemPops(target), this.targetHudX.getValue() + 60, this.targetHudY.getValue() + this.renderer.getFontHeight() * 2 + 30, ColorUtil.toRGBA(255, 0, 0, 255));
            GlStateManager.func_179098_w();
            int iteration = 0;
            int i = this.targetHudX.getValue() + 50;
            int y = this.targetHudY.getValue() + this.renderer.getFontHeight() * 3 + 44;
            for (ItemStack is : target.field_71071_by.field_70460_b) {
                ++iteration;
                if (is.func_190926_b()) continue;
                int x = i - 90 + (9 - iteration) * 20 + 2;
                GlStateManager.func_179126_j();
                RenderUtil.itemRender.field_77023_b = 200.0f;
                RenderUtil.itemRender.func_180450_b(is, x, y);
                RenderUtil.itemRender.func_180453_a(Components.mc.field_71466_p, is, x, y, "");
                RenderUtil.itemRender.field_77023_b = 0.0f;
                GlStateManager.func_179098_w();
                GlStateManager.func_179140_f();
                GlStateManager.func_179097_i();
                String s = is.func_190916_E() > 1 ? is.func_190916_E() + "" : "";
                this.renderer.drawStringWithShadow(s, x + 19 - 2 - this.renderer.getStringWidth(s), y + 9, 0xFFFFFF);
                int dmg = 0;
                int itemDurability = is.func_77958_k() - is.func_77952_i();
                float green = ((float)is.func_77958_k() - (float)is.func_77952_i()) / (float)is.func_77958_k();
                float red = 1.0f - green;
                dmg = 100 - (int)(red * 100.0f);
                this.renderer.drawStringWithShadow(dmg + "", (float)(x + 8) - (float)this.renderer.getStringWidth(dmg + "") / 2.0f, y - 5, ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
            }
            this.drawOverlay(partialTicks, (Entity)target, this.targetHudX.getValue() + 150, this.targetHudY.getValue() + 6);
            this.renderer.drawStringWithShadow("Strength", this.targetHudX.getValue() + 150, this.targetHudY.getValue() + 60, target.func_70644_a(MobEffects.field_76420_g) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
            this.renderer.drawStringWithShadow("Weakness", this.targetHudX.getValue() + 150, this.targetHudY.getValue() + this.renderer.getFontHeight() + 70, target.func_70644_a(MobEffects.field_76437_t) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
        } else if (this.design.getValue() == TargetHudDesign.COMPACT) {
            // empty if block
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive event) {
    }

    public void drawImageLogo() {
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        mc.func_110434_K().func_110577_a(logo);
        Components.drawCompleteImage(this.imageX.getValue(), this.imageY.getValue(), this.imageWidth.getValue(), this.imageHeight.getValue());
        mc.func_110434_K().func_147645_c(logo);
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
    }

    public void drawCompass() {
        ScaledResolution sr = new ScaledResolution(mc);
        if (this.compass.getValue() == Compass.LINE) {
            float playerYaw = Components.mc.field_71439_g.field_70177_z;
            float rotationYaw = MathUtil.wrap(playerYaw);
            RenderUtil.drawRect(this.compassX.getValue().intValue(), this.compassY.getValue().intValue(), this.compassX.getValue() + 100, this.compassY.getValue() + this.renderer.getFontHeight(), 1963986960);
            RenderUtil.glScissor(this.compassX.getValue().intValue(), this.compassY.getValue().intValue(), this.compassX.getValue() + 100, this.compassY.getValue() + this.renderer.getFontHeight(), sr);
            GL11.glEnable((int)3089);
            float zeroZeroYaw = MathUtil.wrap((float)(Math.atan2(0.0 - Components.mc.field_71439_g.field_70161_v, 0.0 - Components.mc.field_71439_g.field_70165_t) * 180.0 / Math.PI) - 90.0f);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + zeroZeroYaw, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + zeroZeroYaw, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -61424);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 45.0f, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 45.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 45.0f, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 45.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 135.0f, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 135.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            RenderUtil.drawLine((float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 135.0f, this.compassY.getValue() + 2, (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 135.0f, this.compassY.getValue() + this.renderer.getFontHeight() - 2, 2.0f, -1);
            this.renderer.drawStringWithShadow("n", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 180.0f - (float)this.renderer.getStringWidth("n") / 2.0f, this.compassY.getValue().intValue(), -1);
            this.renderer.drawStringWithShadow("n", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 180.0f - (float)this.renderer.getStringWidth("n") / 2.0f, this.compassY.getValue().intValue(), -1);
            this.renderer.drawStringWithShadow("e", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - 90.0f - (float)this.renderer.getStringWidth("e") / 2.0f, this.compassY.getValue().intValue(), -1);
            this.renderer.drawStringWithShadow("s", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f - (float)this.renderer.getStringWidth("s") / 2.0f, this.compassY.getValue().intValue(), -1);
            this.renderer.drawStringWithShadow("w", (float)this.compassX.getValue().intValue() - rotationYaw + 50.0f + 90.0f - (float)this.renderer.getStringWidth("w") / 2.0f, this.compassY.getValue().intValue(), -1);
            RenderUtil.drawLine(this.compassX.getValue() + 50, this.compassY.getValue() + 1, this.compassX.getValue() + 50, this.compassY.getValue() + this.renderer.getFontHeight() - 1, 2.0f, -7303024);
            GL11.glDisable((int)3089);
        } else {
            double centerX = this.compassX.getValue().intValue();
            double centerY = this.compassY.getValue().intValue();
            for (Direction dir : Direction.values()) {
                double rad = Components.getPosOnCompass(dir);
                this.renderer.drawStringWithShadow(dir.name(), (float)(centerX + this.getX(rad)), (float)(centerY + this.getY(rad)), dir == Direction.N ? -65536 : -1);
            }
        }
    }

    public void drawPlayer(EntityPlayer player, int x, int y) {
        EntityPlayer ent = player;
        GlStateManager.func_179094_E();
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179141_d();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179141_d();
        GlStateManager.func_179126_j();
        GlStateManager.func_179114_b((float)0.0f, (float)0.0f, (float)5.0f, (float)0.0f);
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), (float)50.0f);
        GlStateManager.func_179152_a((float)(-50.0f * this.playerScale.getValue().floatValue()), (float)(50.0f * this.playerScale.getValue().floatValue()), (float)(50.0f * this.playerScale.getValue().floatValue()));
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-((float)Math.atan((float)this.playerViewerY.getValue().intValue() / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager rendermanager = mc.func_175598_ae();
        rendermanager.func_178631_a(180.0f);
        rendermanager.func_178633_a(false);
        try {
            rendermanager.func_188391_a((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception exception) {
            // empty catch block
        }
        rendermanager.func_178633_a(true);
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
        GlStateManager.func_179143_c((int)515);
        GlStateManager.func_179117_G();
        GlStateManager.func_179097_i();
        GlStateManager.func_179121_F();
    }

    public void drawPlayer() {
        EntityPlayerSP ent = Components.mc.field_71439_g;
        GlStateManager.func_179094_E();
        GlStateManager.func_179124_c((float)1.0f, (float)1.0f, (float)1.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179141_d();
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179141_d();
        GlStateManager.func_179126_j();
        GlStateManager.func_179114_b((float)0.0f, (float)0.0f, (float)5.0f, (float)0.0f);
        GlStateManager.func_179142_g();
        GlStateManager.func_179094_E();
        GlStateManager.func_179109_b((float)(this.playerViewerX.getValue() + 25), (float)(this.playerViewerY.getValue() + 25), (float)50.0f);
        GlStateManager.func_179152_a((float)(-50.0f * this.playerScale.getValue().floatValue()), (float)(50.0f * this.playerScale.getValue().floatValue()), (float)(50.0f * this.playerScale.getValue().floatValue()));
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GlStateManager.func_179114_b((float)-135.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(-((float)Math.atan((float)this.playerViewerY.getValue().intValue() / 40.0f)) * 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        RenderManager rendermanager = mc.func_175598_ae();
        rendermanager.func_178631_a(180.0f);
        rendermanager.func_178633_a(false);
        try {
            rendermanager.func_188391_a((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception exception) {
            // empty catch block
        }
        rendermanager.func_178633_a(true);
        GlStateManager.func_179121_F();
        RenderHelper.func_74518_a();
        GlStateManager.func_179101_C();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179090_x();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
        GlStateManager.func_179143_c((int)515);
        GlStateManager.func_179117_G();
        GlStateManager.func_179097_i();
        GlStateManager.func_179121_F();
    }

    private double getX(double rad) {
        return Math.sin(rad) * (double)(this.scale.getValue() * 10);
    }

    private double getY(double rad) {
        double epicPitch = MathHelper.func_76131_a((float)(Components.mc.field_71439_g.field_70125_A + 30.0f), (float)-90.0f, (float)90.0f);
        double pitchRadians = Math.toRadians(epicPitch);
        return Math.cos(rad) * Math.sin(pitchRadians) * (double)(this.scale.getValue() * 10);
    }

    public void drawOverlay(float partialTicks) {
        BlockPos westPos;
        Block west;
        BlockPos eastPos;
        Block east;
        BlockPos southPos;
        Block south;
        int damage;
        float yaw = 0.0f;
        int dir = MathHelper.func_76128_c((double)((double)(Components.mc.field_71439_g.field_70177_z * 4.0f / 360.0f) + 0.5)) & 3;
        switch (dir) {
            case 1: {
                yaw = 90.0f;
                break;
            }
            case 2: {
                yaw = -180.0f;
                break;
            }
            case 3: {
                yaw = -90.0f;
            }
        }
        BlockPos northPos = this.traceToBlock(partialTicks, yaw);
        Block north = this.getBlock(northPos);
        if (north != null && north != Blocks.field_150350_a) {
            damage = this.getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect(this.holeX.getValue() + 16, this.holeY.getValue().intValue(), this.holeX.getValue() + 32, this.holeY.getValue() + 16, 0x60FF0000);
            }
            this.drawBlock(north, this.holeX.getValue() + 16, this.holeY.getValue().intValue());
        }
        if ((south = this.getBlock(southPos = this.traceToBlock(partialTicks, yaw - 180.0f))) != null && south != Blocks.field_150350_a) {
            damage = this.getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect(this.holeX.getValue() + 16, this.holeY.getValue() + 32, this.holeX.getValue() + 32, this.holeY.getValue() + 48, 0x60FF0000);
            }
            this.drawBlock(south, this.holeX.getValue() + 16, this.holeY.getValue() + 32);
        }
        if ((east = this.getBlock(eastPos = this.traceToBlock(partialTicks, yaw + 90.0f))) != null && east != Blocks.field_150350_a) {
            damage = this.getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect(this.holeX.getValue() + 32, this.holeY.getValue() + 16, this.holeX.getValue() + 48, this.holeY.getValue() + 32, 0x60FF0000);
            }
            this.drawBlock(east, this.holeX.getValue() + 32, this.holeY.getValue() + 16);
        }
        if ((west = this.getBlock(westPos = this.traceToBlock(partialTicks, yaw - 90.0f))) != null && west != Blocks.field_150350_a) {
            damage = this.getBlockDamage(westPos);
            if (damage != 0) {
                RenderUtil.drawRect(this.holeX.getValue().intValue(), this.holeY.getValue() + 16, this.holeX.getValue() + 16, this.holeY.getValue() + 32, 0x60FF0000);
            }
            this.drawBlock(west, this.holeX.getValue().intValue(), this.holeY.getValue() + 16);
        }
    }

    public void drawOverlay(float partialTicks, Entity player, int x, int y) {
        BlockPos westPos;
        Block west;
        BlockPos eastPos;
        Block east;
        BlockPos southPos;
        Block south;
        int damage;
        float yaw = 0.0f;
        int dir = MathHelper.func_76128_c((double)((double)(player.field_70177_z * 4.0f / 360.0f) + 0.5)) & 3;
        switch (dir) {
            case 1: {
                yaw = 90.0f;
                break;
            }
            case 2: {
                yaw = -180.0f;
                break;
            }
            case 3: {
                yaw = -90.0f;
            }
        }
        BlockPos northPos = this.traceToBlock(partialTicks, yaw, player);
        Block north = this.getBlock(northPos);
        if (north != null && north != Blocks.field_150350_a) {
            damage = this.getBlockDamage(northPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 16, y, x + 32, y + 16, 0x60FF0000);
            }
            this.drawBlock(north, x + 16, y);
        }
        if ((south = this.getBlock(southPos = this.traceToBlock(partialTicks, yaw - 180.0f, player))) != null && south != Blocks.field_150350_a) {
            damage = this.getBlockDamage(southPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 16, y + 32, x + 32, y + 48, 0x60FF0000);
            }
            this.drawBlock(south, x + 16, y + 32);
        }
        if ((east = this.getBlock(eastPos = this.traceToBlock(partialTicks, yaw + 90.0f, player))) != null && east != Blocks.field_150350_a) {
            damage = this.getBlockDamage(eastPos);
            if (damage != 0) {
                RenderUtil.drawRect(x + 32, y + 16, x + 48, y + 32, 0x60FF0000);
            }
            this.drawBlock(east, x + 32, y + 16);
        }
        if ((west = this.getBlock(westPos = this.traceToBlock(partialTicks, yaw - 90.0f, player))) != null && west != Blocks.field_150350_a) {
            damage = this.getBlockDamage(westPos);
            if (damage != 0) {
                RenderUtil.drawRect(x, y + 16, x + 16, y + 32, 0x60FF0000);
            }
            this.drawBlock(west, x, y + 16);
        }
    }

    private int getBlockDamage(BlockPos pos) {
        for (DestroyBlockProgress destBlockProgress : Components.mc.field_71438_f.field_72738_E.values()) {
            if (destBlockProgress.func_180246_b().func_177958_n() != pos.func_177958_n() || destBlockProgress.func_180246_b().func_177956_o() != pos.func_177956_o() || destBlockProgress.func_180246_b().func_177952_p() != pos.func_177952_p()) continue;
            return destBlockProgress.func_73106_e();
        }
        return 0;
    }

    private BlockPos traceToBlock(float partialTicks, float yaw) {
        Vec3d pos = EntityUtil.interpolateEntity((Entity)Components.mc.field_71439_g, partialTicks);
        Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
    }

    private BlockPos traceToBlock(float partialTicks, float yaw, Entity player) {
        Vec3d pos = EntityUtil.interpolateEntity(player, partialTicks);
        Vec3d dir = MathUtil.direction(yaw);
        return new BlockPos(pos.field_72450_a + dir.field_72450_a, pos.field_72448_b, pos.field_72449_c + dir.field_72449_c);
    }

    private Block getBlock(BlockPos pos) {
        Block block = Components.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (block == Blocks.field_150357_h || block == Blocks.field_150343_Z) {
            return block;
        }
        return Blocks.field_150350_a;
    }

    private void drawBlock(Block block, float x, float y) {
        ItemStack stack = new ItemStack(block);
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        RenderHelper.func_74520_c();
        GlStateManager.func_179109_b((float)x, (float)y, (float)0.0f);
        Components.mc.func_175599_af().field_77023_b = 501.0f;
        mc.func_175599_af().func_180450_b(stack, 0, 0);
        Components.mc.func_175599_af().field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        GlStateManager.func_179084_k();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179121_F();
    }

    public void renderInventory() {
        this.boxrender(this.invX.getValue() + this.fineinvX.getValue(), this.invY.getValue() + this.fineinvY.getValue());
        this.itemrender((NonNullList<ItemStack>)Components.mc.field_71439_g.field_71071_by.field_70462_a, this.invX.getValue() + this.fineinvX.getValue(), this.invY.getValue() + this.fineinvY.getValue());
    }

    private void boxrender(int x, int y) {
        Components.preboxrender();
        Components.mc.field_71446_o.func_110577_a(box);
        RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
        RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 54 + this.invH.getValue(), 500);
        RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
        Components.postboxrender();
    }

    private void itemrender(NonNullList<ItemStack> items, int x, int y) {
        int iX;
        int i;
        for (i = 0; i < items.size() - 9; ++i) {
            iX = x + i % 9 * 18 + 8;
            int iY = y + i / 9 * 18 + 18;
            ItemStack itemStack = (ItemStack)items.get(i + 9);
            Components.preitemrender();
            Components.mc.func_175599_af().field_77023_b = 501.0f;
            RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
            RenderUtil.itemRender.func_180453_a(Components.mc.field_71466_p, itemStack, iX, iY, null);
            Components.mc.func_175599_af().field_77023_b = 0.0f;
            Components.postitemrender();
        }
        if (this.renderXCarry.getValue().booleanValue()) {
            for (i = 1; i < 5; ++i) {
                iX = x + (i + 4) % 9 * 18 + 8;
                ItemStack itemStack = ((Slot)Components.mc.field_71439_g.field_71069_bz.field_75151_b.get(i)).func_75211_c();
                if (itemStack == null || itemStack.field_190928_g) continue;
                Components.preitemrender();
                Components.mc.func_175599_af().field_77023_b = 501.0f;
                RenderUtil.itemRender.func_180450_b(itemStack, iX, y + 1);
                RenderUtil.itemRender.func_180453_a(Components.mc.field_71466_p, itemStack, iX, y + 1, null);
                Components.mc.func_175599_af().field_77023_b = 0.0f;
                Components.postitemrender();
            }
        }
    }

    private static enum Direction {
        N,
        W,
        S,
        E;

    }

    public static enum Compass {
        NONE,
        CIRCLE,
        LINE;

    }

    public static enum TargetHudDesign {
        NORMAL,
        COMPACT;

    }
}

