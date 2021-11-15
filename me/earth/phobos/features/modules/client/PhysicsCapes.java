/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelRenderer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.client.event.RenderPlayerEvent$Post
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.features.modules.client;

import java.util.HashMap;
import me.earth.phobos.features.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class PhysicsCapes
extends Module {
    private final ResourceLocation capeTexture = new ResourceLocation("textures/cape.png");
    public ModelPhyscisCapes cape = new ModelPhyscisCapes();

    public PhysicsCapes() {
        super("PhysicsCapes", "Capes with superior physics", Module.Category.CLIENT, true, false, false);
    }

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Post event) {
        GlStateManager.func_179094_E();
        float f11 = (float)System.currentTimeMillis() / 1000.0f;
        HashMap<ModelRenderer, Float> waveMap = new HashMap<ModelRenderer, Float>();
        float fuck = f11;
        for (ModelRenderer renderer : this.cape.field_78092_r) {
            waveMap.put(renderer, Float.valueOf((float)Math.sin((double)fuck / 0.5) * 4.0f));
            fuck += 1.0f;
        }
        double rotate = this.interpolate(event.getEntityPlayer().field_70760_ar, event.getEntityPlayer().field_70761_aq, event.getPartialRenderTick());
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.125f);
        double d0 = event.getEntityPlayer().field_71091_bM + (event.getEntityPlayer().field_71094_bP - event.getEntityPlayer().field_71091_bM) * (double)event.getPartialRenderTick() - (event.getEntityPlayer().field_70169_q + (event.getEntityPlayer().field_70165_t - event.getEntityPlayer().field_70169_q) * (double)event.getPartialRenderTick());
        double d1 = event.getEntityPlayer().field_71096_bN + (event.getEntityPlayer().field_71095_bQ - event.getEntityPlayer().field_71096_bN) * (double)event.getPartialRenderTick() - (event.getEntityPlayer().field_70167_r + (event.getEntityPlayer().field_70163_u - event.getEntityPlayer().field_70167_r) * (double)event.getPartialRenderTick());
        double d2 = event.getEntityPlayer().field_71097_bO + (event.getEntityPlayer().field_71085_bR - event.getEntityPlayer().field_71097_bO) * (double)event.getPartialRenderTick() - (event.getEntityPlayer().field_70166_s + (event.getEntityPlayer().field_70161_v - event.getEntityPlayer().field_70166_s) * (double)event.getPartialRenderTick());
        float f = event.getEntityPlayer().field_70760_ar + (event.getEntityPlayer().field_70761_aq - event.getEntityPlayer().field_70760_ar) * event.getPartialRenderTick();
        double d3 = MathHelper.func_76126_a((float)(f * ((float)Math.PI / 180)));
        double d4 = -MathHelper.func_76134_b((float)(f * ((float)Math.PI / 180)));
        float f1 = (float)d1 * 10.0f;
        f1 = MathHelper.func_76131_a((float)f1, (float)-6.0f, (float)32.0f);
        float f2 = (float)(d0 * d3 + d2 * d4) * 100.0f;
        float f3 = (float)(d0 * d4 - d2 * d3) * 100.0f;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        float f4 = event.getEntityPlayer().field_71107_bF + (event.getEntityPlayer().field_71109_bG - event.getEntityPlayer().field_71107_bF) * event.getPartialRenderTick();
        f1 += MathHelper.func_76126_a((float)((event.getEntityPlayer().field_70141_P + (event.getEntityPlayer().field_70140_Q - event.getEntityPlayer().field_70141_P) * event.getPartialRenderTick()) * 6.0f)) * 32.0f * f4;
        if (event.getEntityPlayer().func_70093_af()) {
            f1 += 25.0f;
        }
        GL11.glRotated((double)(-rotate), (double)0.0, (double)1.0, (double)0.0);
        GlStateManager.func_179114_b((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glTranslated((double)0.0, (double)(-((double)event.getEntityPlayer().field_70131_O - (event.getEntityPlayer().func_70093_af() ? 0.25 : 0.0) - 0.38)), (double)0.0);
        GlStateManager.func_179114_b((float)(6.0f + f2 / 2.0f + f1), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f3 / 2.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(-f3 / 2.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        if (PhysicsCapes.mc.field_71439_g.field_191988_bg != 0.0f || PhysicsCapes.mc.field_71439_g.field_70702_br != 0.0f) {
            for (ModelRenderer renderer : this.cape.field_78092_r) {
                renderer.field_78795_f = ((Float)waveMap.get((Object)renderer)).floatValue();
            }
        } else {
            for (ModelRenderer renderer : this.cape.field_78092_r) {
                renderer.field_78795_f = 0.0f;
            }
        }
        Minecraft.func_71410_x().func_110434_K().func_110577_a(this.capeTexture);
        this.cape.func_78088_a(event.getEntity(), 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        Minecraft.func_71410_x().func_110434_K().func_147645_c(this.capeTexture);
        GlStateManager.func_179121_F();
    }

    public float interpolate(float yaw1, float yaw2, float percent) {
        float rotation = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
        if (rotation < 0.0f) {
            rotation += 360.0f;
        }
        return rotation;
    }

    public class ModelPhyscisCapes
    extends ModelBase {
        public ModelRenderer shape1;
        public ModelRenderer shape2;
        public ModelRenderer shape3;
        public ModelRenderer shape4;
        public ModelRenderer shape5;
        public ModelRenderer shape6;
        public ModelRenderer shape7;
        public ModelRenderer shape8;
        public ModelRenderer shape9;
        public ModelRenderer shape10;
        public ModelRenderer shape11;
        public ModelRenderer shape12;
        public ModelRenderer shape13;
        public ModelRenderer shape14;
        public ModelRenderer shape15;
        public ModelRenderer shape16;

        public ModelPhyscisCapes() {
            this.field_78090_t = 64;
            this.field_78089_u = 32;
            this.shape9 = new ModelRenderer((ModelBase)this, 0, 8);
            this.shape9.func_78793_a(-5.0f, 8.0f, -1.0f);
            this.shape9.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape15 = new ModelRenderer((ModelBase)this, 0, 14);
            this.shape15.func_78793_a(-5.0f, 14.0f, -1.0f);
            this.shape15.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape3 = new ModelRenderer((ModelBase)this, 0, 2);
            this.shape3.func_78793_a(-5.0f, 2.0f, -1.0f);
            this.shape3.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape7 = new ModelRenderer((ModelBase)this, 0, 6);
            this.shape7.func_78793_a(-5.0f, 6.0f, -1.0f);
            this.shape7.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape1 = new ModelRenderer((ModelBase)this, 0, 0);
            this.shape1.func_78793_a(-5.0f, 0.0f, -1.0f);
            this.shape1.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape6 = new ModelRenderer((ModelBase)this, 0, 5);
            this.shape6.func_78793_a(-5.0f, 5.0f, -1.0f);
            this.shape6.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape14 = new ModelRenderer((ModelBase)this, 0, 13);
            this.shape14.func_78793_a(-5.0f, 13.0f, -1.0f);
            this.shape14.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape10 = new ModelRenderer((ModelBase)this, 0, 9);
            this.shape10.func_78793_a(-5.0f, 9.0f, -1.0f);
            this.shape10.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape13 = new ModelRenderer((ModelBase)this, 0, 12);
            this.shape13.func_78793_a(-5.0f, 12.0f, -1.0f);
            this.shape13.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape4 = new ModelRenderer((ModelBase)this, 0, 3);
            this.shape4.func_78793_a(-5.0f, 3.0f, -1.0f);
            this.shape4.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape8 = new ModelRenderer((ModelBase)this, 0, 7);
            this.shape8.func_78793_a(-5.0f, 7.0f, -1.0f);
            this.shape8.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape16 = new ModelRenderer((ModelBase)this, 0, 15);
            this.shape16.func_78793_a(-5.0f, 15.0f, -1.0f);
            this.shape16.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape12 = new ModelRenderer((ModelBase)this, 0, 11);
            this.shape12.func_78793_a(-5.0f, 11.0f, -1.0f);
            this.shape12.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape5 = new ModelRenderer((ModelBase)this, 0, 4);
            this.shape5.func_78793_a(-5.0f, 4.0f, -1.0f);
            this.shape5.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape11 = new ModelRenderer((ModelBase)this, 0, 10);
            this.shape11.func_78793_a(-5.0f, 10.0f, -1.0f);
            this.shape11.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.shape2 = new ModelRenderer((ModelBase)this, 0, 1);
            this.shape2.func_78793_a(-5.0f, 1.0f, -1.0f);
            this.shape2.func_78790_a(0.0f, 0.0f, 0.0f, 10, 1, 1, 0.0f);
            this.field_78092_r.add(this.shape1);
            this.field_78092_r.add(this.shape2);
            this.field_78092_r.add(this.shape3);
            this.field_78092_r.add(this.shape4);
            this.field_78092_r.add(this.shape5);
            this.field_78092_r.add(this.shape6);
            this.field_78092_r.add(this.shape7);
            this.field_78092_r.add(this.shape8);
            this.field_78092_r.add(this.shape9);
            this.field_78092_r.add(this.shape10);
            this.field_78092_r.add(this.shape11);
            this.field_78092_r.add(this.shape12);
            this.field_78092_r.add(this.shape13);
            this.field_78092_r.add(this.shape14);
            this.field_78092_r.add(this.shape15);
            this.field_78092_r.add(this.shape16);
        }

        public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            this.shape9.func_78785_a(f5);
            this.shape15.func_78785_a(f5);
            this.shape3.func_78785_a(f5);
            this.shape7.func_78785_a(f5);
            this.shape1.func_78785_a(f5);
            this.shape6.func_78785_a(f5);
            this.shape14.func_78785_a(f5);
            this.shape10.func_78785_a(f5);
            this.shape13.func_78785_a(f5);
            this.shape4.func_78785_a(f5);
            this.shape8.func_78785_a(f5);
            this.shape16.func_78785_a(f5);
            this.shape12.func_78785_a(f5);
            this.shape5.func_78785_a(f5);
            this.shape11.func_78785_a(f5);
            this.shape2.func_78785_a(f5);
        }

        public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
            modelRenderer.field_78795_f = x;
            modelRenderer.field_78796_g = y;
            modelRenderer.field_78808_h = z;
        }
    }
}

