/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiOptions
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiWorldSelection
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.features.gui.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import me.earth.phobos.Oyvey;
import me.earth.phobos.features.gui.effect.Particle.ParticleSystem;
import me.earth.phobos.features.modules.client.MainMenu;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCustomMainScreen
extends GuiScreen {
    private final String backgroundURL = "https://i.imgur.com/GCJRhiA.png";
    private final ResourceLocation resourceLocation = new ResourceLocation("textures/backgroundimg.png");
    private int y;
    private int x;
    private int singleplayerWidth;
    private int multiplayerWidth;
    private int settingsWidth;
    private int exitWidth;
    private int textHeight;
    private float xOffset;
    private float yOffset;
    private ParticleSystem particleSystem;

    public static void drawCompleteImage(float posX, float posY, float width, float height) {
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

    public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }

    public void func_73866_w_() {
        this.x = this.field_146294_l / 2;
        this.y = this.field_146295_m / 4 + 48;
        this.field_146292_n.add(new TextButton(0, this.x, this.y + 20, "Singleplayer"));
        this.field_146292_n.add(new TextButton(1, this.x, this.y + 44, "Multiplayer"));
        this.field_146292_n.add(new TextButton(2, this.x, this.y + 66, "Settings"));
        this.field_146292_n.add(new TextButton(2, this.x, this.y + 88, "Exit"));
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179103_j((int)7425);
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }

    public void func_73876_c() {
        if (this.particleSystem != null) {
            this.particleSystem.update();
        }
        super.func_73876_c();
    }

    public void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        if (GuiCustomMainScreen.isHovered(this.x - Oyvey.textManager.getStringWidth("Singleplayer") / 2, this.y + 20, Oyvey.textManager.getStringWidth("Singleplayer"), Oyvey.textManager.getFontHeight(), mouseX, mouseY)) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldSelection((GuiScreen)this));
        } else if (GuiCustomMainScreen.isHovered(this.x - Oyvey.textManager.getStringWidth("Multiplayer") / 2, this.y + 44, Oyvey.textManager.getStringWidth("Multiplayer"), Oyvey.textManager.getFontHeight(), mouseX, mouseY)) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        } else if (GuiCustomMainScreen.isHovered(this.x - Oyvey.textManager.getStringWidth("Settings") / 2, this.y + 66, Oyvey.textManager.getStringWidth("Settings"), Oyvey.textManager.getFontHeight(), mouseX, mouseY)) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiOptions((GuiScreen)this, this.field_146297_k.field_71474_y));
        } else if (GuiCustomMainScreen.isHovered(this.x - Oyvey.textManager.getStringWidth("Exit") / 2, this.y + 88, Oyvey.textManager.getStringWidth("Exit"), Oyvey.textManager.getFontHeight(), mouseX, mouseY)) {
            this.field_146297_k.func_71400_g();
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.xOffset = -1.0f * (((float)mouseX - (float)this.field_146294_l / 2.0f) / ((float)this.field_146294_l / 32.0f));
        this.yOffset = -1.0f * (((float)mouseY - (float)this.field_146295_m / 2.0f) / ((float)this.field_146295_m / 18.0f));
        this.x = this.field_146294_l / 2;
        this.y = this.field_146295_m / 4 + 48;
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        this.field_146297_k.func_110434_K().func_110577_a(this.resourceLocation);
        GuiCustomMainScreen.drawCompleteImage(-16.0f + this.xOffset, -9.0f + this.yOffset, this.field_146294_l + 32, this.field_146295_m + 18);
        super.func_73863_a(mouseX, mouseY, partialTicks);
        if (this.particleSystem != null && MainMenu.getInstance().particles.getValue().booleanValue()) {
            this.particleSystem.render(mouseX, mouseY);
        } else {
            this.particleSystem = new ParticleSystem(new ScaledResolution(this.field_146297_k));
        }
    }

    public BufferedImage parseBackground(BufferedImage background) {
        int height;
        int width = 1920;
        int srcWidth = background.getWidth();
        int srcHeight = background.getHeight();
        for (height = 1080; width < srcWidth || height < srcHeight; width *= 2, height *= 2) {
        }
        BufferedImage imgNew = new BufferedImage(width, height, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.dispose();
        return imgNew;
    }

    private static class TextButton
    extends GuiButton {
        public TextButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, Oyvey.textManager.getStringWidth(buttonText), Oyvey.textManager.getFontHeight(), buttonText);
        }

        public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.field_146125_m) {
                this.field_146124_l = true;
                this.field_146123_n = (float)mouseX >= (float)this.field_146128_h - (float)Oyvey.textManager.getStringWidth(this.field_146126_j) / 2.0f && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
                Oyvey.textManager.drawStringWithShadow(this.field_146126_j, (float)this.field_146128_h - (float)Oyvey.textManager.getStringWidth(this.field_146126_j) / 2.0f, this.field_146129_i, Color.WHITE.getRGB());
                if (this.field_146123_n) {
                    RenderUtil.drawLine((float)(this.field_146128_h - 1) - (float)Oyvey.textManager.getStringWidth(this.field_146126_j) / 2.0f, this.field_146129_i + 2 + Oyvey.textManager.getFontHeight(), (float)this.field_146128_h + (float)Oyvey.textManager.getStringWidth(this.field_146126_j) / 2.0f + 1.0f, this.field_146129_i + 2 + Oyvey.textManager.getFontHeight(), 1.0f, Color.WHITE.getRGB());
                }
            }
        }

        public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
            return this.field_146124_l && this.field_146125_m && (float)mouseX >= (float)this.field_146128_h - (float)Oyvey.textManager.getStringWidth(this.field_146126_j) / 2.0f && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
        }
    }
}

