/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.vecmath.Tuple2f
 *  javax.vecmath.Vector2f
 *  net.minecraft.client.gui.ScaledResolution
 */
package me.earth.phobos.features.gui.effect.Particle;

import javax.vecmath.Tuple2f;
import javax.vecmath.Vector2f;
import me.earth.phobos.features.gui.effect.Particle.Particle;
import me.earth.phobos.features.modules.client.ClickGui;
import me.earth.phobos.util.ColorUtil;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;

public final class ParticleSystem {
    private final int PARTS = 200;
    private final Particle[] particles = new Particle[200];
    private ScaledResolution scaledResolution;

    public ParticleSystem(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
        for (int i = 0; i < 200; ++i) {
            this.particles[i] = new Particle(new Vector2f((float)(Math.random() * (double)scaledResolution.func_78326_a()), (float)(Math.random() * (double)scaledResolution.func_78328_b())));
        }
    }

    public void update() {
        for (int i = 0; i < 200; ++i) {
            Particle particle = this.particles[i];
            if (this.scaledResolution != null) {
                boolean isOffScreenY;
                boolean isOffScreenX = particle.getPos().x > (float)this.scaledResolution.func_78326_a() || particle.getPos().x < 0.0f;
                boolean bl = isOffScreenY = particle.getPos().y > (float)this.scaledResolution.func_78328_b() || particle.getPos().y < 0.0f;
                if (isOffScreenX || isOffScreenY) {
                    particle.respawn(this.scaledResolution);
                }
            }
            particle.update();
        }
    }

    public void render(int mouseX, int mouseY) {
        if (!ClickGui.getInstance().particles.getValue().booleanValue()) {
            return;
        }
        for (int i = 0; i < 200; ++i) {
            Particle particle = this.particles[i];
            for (int j = 1; j < 200; ++j) {
                int lineAlpha;
                if (i == j) continue;
                Particle otherParticle = this.particles[j];
                Vector2f diffPos = new Vector2f(particle.getPos());
                diffPos.sub((Tuple2f)otherParticle.getPos());
                float diff = diffPos.length();
                float distance = ClickGui.getInstance().particlelength.getValue().floatValue() / (float)(this.scaledResolution.func_78325_e() <= 1 ? 3 : this.scaledResolution.func_78325_e());
                if (!(diff < distance) || (lineAlpha = (int)ParticleSystem.map(diff, distance, 0.0, 0.0, 127.0)) <= 8) continue;
                RenderUtil.drawLine(particle.getPos().x + particle.getSize() / 2.0f, particle.getPos().y + particle.getSize() / 2.0f, otherParticle.getPos().x + otherParticle.getSize() / 2.0f, otherParticle.getPos().y + otherParticle.getSize() / 2.0f, 1.0f, Particle.changeAlpha(ColorUtil.toRGBA(ClickGui.getInstance().particlered.getValue(), ClickGui.getInstance().particlegreen.getValue(), ClickGui.getInstance().particleblue.getValue()), lineAlpha));
            }
            particle.render(mouseX, mouseY);
        }
    }

    public static double map(double value, double a, double b, double c, double d) {
        value = (value - a) / (b - a);
        return c + value * (d - c);
    }

    public ScaledResolution getScaledResolution() {
        return this.scaledResolution;
    }

    public void setScaledResolution(ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }
}

