/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.ScaledResolution
 *  org.lwjgl.input.Mouse
 */
package me.earth.phobos.features.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import me.earth.phobos.Oyvey;
import me.earth.phobos.features.gui.components.Component;
import me.earth.phobos.features.gui.components.items.Item;
import me.earth.phobos.features.gui.components.items.buttons.ModuleButton;
import me.earth.phobos.features.gui.effect.Particle.ParticleSystem;
import me.earth.phobos.features.gui.effect.Snow;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.ClickGui;
import me.earth.phobos.features.modules.client.Colors;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class OyVeyGui
extends GuiScreen {
    private static OyVeyGui oyVeyGui;
    private static OyVeyGui INSTANCE;
    private final ArrayList<Component> components = new ArrayList();
    private ArrayList<Snow> _snowList = new ArrayList();
    public ParticleSystem particleSystem;

    public OyVeyGui() {
        this.setInstance();
        this.load();
    }

    public static OyVeyGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OyVeyGui();
        }
        return INSTANCE;
    }

    public static OyVeyGui getClickGui() {
        return OyVeyGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
        Random random = new Random();
        for (int i = 0; i < 100; ++i) {
            for (int y = 0; y < 3; ++y) {
                Snow snow = new Snow(25 * i, y * -50, random.nextInt(3) + 1, random.nextInt(2) + 1);
                this._snowList.add(snow);
            }
        }
        int x = -80;
        for (final Module.Category category : Oyvey.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 100, 8, true){

                @Override
                public void setupItems() {
                    Oyvey.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton((Module)module));
                        }
                    });
                }
            });
        }
        this.components.forEach(components -> components.getItems().sort((item1, item2) -> item1.getName().compareTo(item2.getName())));
    }

    public void updateModule(Module module) {
        block0: for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton)item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
                continue block0;
            }
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        if (ClickGui.getInstance().dark.getValue().booleanValue()) {
            this.func_146276_q_();
        }
        ScaledResolution res = new ScaledResolution(this.field_146297_k);
        this.checkMouseWheel();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        if (ClickGui.getInstance().gradiant.getValue().booleanValue()) {
            this.func_73733_a(0, 0, sr.func_78326_a(), sr.func_78328_b(), 0, ClickGui.getInstance().colorSync.getValue() != false ? Colors.INSTANCE.getCurrentColor().getRGB() : new Color(ClickGui.getInstance().red.getValue(), ClickGui.getInstance().green.getValue(), ClickGui.getInstance().blue.getValue(), ClickGui.getInstance().alpha.getValue() / 2).getRGB());
        }
        if (!this._snowList.isEmpty() && ClickGui.getInstance().snowing.getValue().booleanValue()) {
            this._snowList.forEach(snow -> snow.Update(res));
        }
        if (this.particleSystem != null && ClickGui.getInstance().particles.getValue().booleanValue()) {
            this.particleSystem.render(mouseX, mouseY);
        } else {
            this.particleSystem = new ParticleSystem(new ScaledResolution(this.field_146297_k));
        }
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
    }

    public void func_73864_a(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    public void func_146286_b(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public boolean func_73868_f() {
        return false;
    }

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void func_73869_a(char typedChar, int keyCode) throws IOException {
        super.func_73869_a(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }

    public void func_73876_c() {
        if (this.particleSystem != null) {
            this.particleSystem.update();
        }
    }

    static {
        INSTANCE = new OyVeyGui();
    }
}

