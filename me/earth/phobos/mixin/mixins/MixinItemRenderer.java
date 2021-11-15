/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.EnumHandSide
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package me.earth.phobos.mixin.mixins;

import me.earth.phobos.event.events.RenderItemEvent;
import me.earth.phobos.features.modules.render.NoRender;
import me.earth.phobos.features.modules.render.ViewModel;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    private boolean injection = true;
    RenderUtil renderUtil;

    @Shadow
    public abstract void func_187457_a(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);

    @Inject(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            this.injection = false;
            this.func_187457_a(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            this.injection = true;
        }
    }

    @Inject(method={"transformSideFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void transformSideFirstPerson(EnumHandSide hand, float p_187459_2_, CallbackInfo cancel) {
        RenderItemEvent event = new RenderItemEvent(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (ViewModel.getInstance().isEnabled()) {
            boolean bob = ViewModel.getInstance().isDisabled() || ViewModel.getInstance().doBob.getValue() != false;
            int i = hand == EnumHandSide.RIGHT ? 1 : -1;
            GlStateManager.func_179109_b((float)((float)i * 0.56f), (float)(-0.52f + (bob ? p_187459_2_ : 0.0f) * -0.6f), (float)-0.72f);
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.func_179137_b((double)event.getMainX(), (double)event.getMainY(), (double)event.getMainZ());
                RenderUtil.rotationHelper((float)event.getMainRotX(), (float)event.getMainRotY(), (float)event.getMainRotZ());
            } else {
                GlStateManager.func_179137_b((double)event.getOffX(), (double)event.getOffY(), (double)event.getOffZ());
                RenderUtil.rotationHelper((float)event.getOffRotX(), (float)event.getOffRotY(), (float)event.getOffRotZ());
            }
            cancel.cancel();
        }
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fire.getValue().booleanValue()) {
            info.cancel();
        }
    }

    @Inject(method={"transformEatFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo cancel) {
        if (ViewModel.getInstance().isEnabled()) {
            if (!ViewModel.getInstance().noEatAnimation.getValue().booleanValue()) {
                float f3;
                float f = (float)Minecraft.func_71410_x().field_71439_g.func_184605_cv() - p_187454_1_ + 1.0f;
                float f1 = f / (float)stack.func_77988_m();
                if (f1 < 0.8f) {
                    f3 = MathHelper.func_76135_e((float)(MathHelper.func_76134_b((float)(f / 4.0f * (float)Math.PI)) * 0.1f));
                    GlStateManager.func_179109_b((float)0.0f, (float)f3, (float)0.0f);
                }
                f3 = 1.0f - (float)Math.pow(f1, 27.0);
                int i = hand == EnumHandSide.RIGHT ? 1 : -1;
                GlStateManager.func_179137_b((double)((double)(f3 * 0.6f * (float)i) * ViewModel.getInstance().eatX.getValue()), (double)((double)(f3 * 0.5f) * -ViewModel.getInstance().eatY.getValue().doubleValue()), (double)0.0);
                GlStateManager.func_179114_b((float)((float)i * f3 * 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
                GlStateManager.func_179114_b((float)(f3 * 10.0f), (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.func_179114_b((float)((float)i * f3 * 30.0f), (float)0.0f, (float)0.0f, (float)1.0f);
            }
            cancel.cancel();
        }
    }

    @Inject(method={"renderSuffocationOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().blocks.getValue().booleanValue()) {
            ci.cancel();
        }
    }
}

