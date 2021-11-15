/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.EventBus
 *  org.lwjgl.input.Keyboard
 */
package me.earth.phobos.manager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import me.earth.phobos.event.events.Render2DEvent;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.features.Feature;
import me.earth.phobos.features.gui.OyVeyGui;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.ClickGui;
import me.earth.phobos.features.modules.client.Colors;
import me.earth.phobos.features.modules.client.Components;
import me.earth.phobos.features.modules.client.FontMod;
import me.earth.phobos.features.modules.client.HUD;
import me.earth.phobos.features.modules.client.MainMenu;
import me.earth.phobos.features.modules.client.Managers;
import me.earth.phobos.features.modules.client.Notifications;
import me.earth.phobos.features.modules.client.ServerModule;
import me.earth.phobos.features.modules.combat.AutoArmor;
import me.earth.phobos.features.modules.combat.AutoCrystall;
import me.earth.phobos.features.modules.combat.AutoMuffin;
import me.earth.phobos.features.modules.combat.AutoTrap;
import me.earth.phobos.features.modules.combat.Criticals;
import me.earth.phobos.features.modules.combat.HoleFiller;
import me.earth.phobos.features.modules.combat.Killaura;
import me.earth.phobos.features.modules.combat.Offhand;
import me.earth.phobos.features.modules.combat.Selftrap;
import me.earth.phobos.features.modules.combat.SilentXP;
import me.earth.phobos.features.modules.combat.Surround;
import me.earth.phobos.features.modules.misc.AutoGG;
import me.earth.phobos.features.modules.misc.AutoRespawn;
import me.earth.phobos.features.modules.misc.Bowbomb;
import me.earth.phobos.features.modules.misc.ChatModifier;
import me.earth.phobos.features.modules.misc.ExtraTab;
import me.earth.phobos.features.modules.misc.MCF;
import me.earth.phobos.features.modules.misc.RPC;
import me.earth.phobos.features.modules.misc.Troll;
import me.earth.phobos.features.modules.movement.Flight;
import me.earth.phobos.features.modules.movement.NoSlowDown;
import me.earth.phobos.features.modules.movement.Phase;
import me.earth.phobos.features.modules.movement.ReverseStep;
import me.earth.phobos.features.modules.movement.Speed;
import me.earth.phobos.features.modules.movement.Sprint;
import me.earth.phobos.features.modules.movement.Step;
import me.earth.phobos.features.modules.movement.Strafe;
import me.earth.phobos.features.modules.movement.Velocity;
import me.earth.phobos.features.modules.player.FakePlayer;
import me.earth.phobos.features.modules.player.FastPlace;
import me.earth.phobos.features.modules.player.Freecam;
import me.earth.phobos.features.modules.player.Godmode;
import me.earth.phobos.features.modules.player.Scaffold;
import me.earth.phobos.features.modules.player.Speedmine;
import me.earth.phobos.features.modules.player.TimerSpeed;
import me.earth.phobos.features.modules.player.TrueDurability;
import me.earth.phobos.features.modules.player.XCarry;
import me.earth.phobos.features.modules.render.BlockHighlight;
import me.earth.phobos.features.modules.render.Chams;
import me.earth.phobos.features.modules.render.CrystalModifier;
import me.earth.phobos.features.modules.render.ESP;
import me.earth.phobos.features.modules.render.HandColor;
import me.earth.phobos.features.modules.render.HoleESP;
import me.earth.phobos.features.modules.render.Nametags;
import me.earth.phobos.features.modules.render.NoRender;
import me.earth.phobos.features.modules.render.PlayerESP;
import me.earth.phobos.features.modules.render.StorageESP;
import me.earth.phobos.features.modules.render.Tracer;
import me.earth.phobos.features.modules.render.ViewModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.lwjgl.input.Keyboard;

public class ModuleManager
extends Feature {
    public ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<Module> alphabeticallySortedModules = new ArrayList<Module>();
    public Map<Module, Color> moduleColorMap = new HashMap<Module, Color>();

    public void init() {
        this.modules.add(new Offhand());
        this.modules.add(new Surround());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoCrystall());
        this.modules.add(new Criticals());
        this.modules.add(new Killaura());
        this.modules.add(new HoleFiller());
        this.modules.add(new Selftrap());
        this.modules.add(new AutoArmor());
        this.modules.add(new AutoMuffin());
        this.modules.add(new SilentXP());
        this.modules.add(new Bowbomb());
        this.modules.add(new ChatModifier());
        this.modules.add(new AutoRespawn());
        this.modules.add(new MCF());
        this.modules.add(new ExtraTab());
        this.modules.add(new RPC());
        this.modules.add(new AutoGG());
        this.modules.add(new Godmode());
        this.modules.add(new ReverseStep());
        this.modules.add(new Strafe());
        this.modules.add(new Velocity());
        this.modules.add(new Speed());
        this.modules.add(new Step());
        this.modules.add(new Sprint());
        this.modules.add(new Phase());
        this.modules.add(new Flight());
        this.modules.add(new NoSlowDown());
        this.modules.add(new FakePlayer());
        this.modules.add(new TimerSpeed());
        this.modules.add(new FastPlace());
        this.modules.add(new Freecam());
        this.modules.add(new Scaffold());
        this.modules.add(new Nametags());
        this.modules.add(new ViewModel());
        this.modules.add(new Speedmine());
        this.modules.add(new XCarry());
        this.modules.add(new TrueDurability());
        this.modules.add(new StorageESP());
        this.modules.add(new NoRender());
        this.modules.add(new Chams());
        this.modules.add(new ESP());
        this.modules.add(new HoleESP());
        this.modules.add(new BlockHighlight());
        this.modules.add(new Tracer());
        this.modules.add(new Troll());
        this.modules.add(new HandColor());
        this.modules.add(new CrystalModifier());
        this.modules.add(new PlayerESP());
        this.modules.add(new Notifications());
        this.modules.add(new HUD());
        this.modules.add(new FontMod());
        this.modules.add(new ClickGui());
        this.modules.add(new Managers());
        this.modules.add(new Components());
        this.modules.add(new Colors());
        this.modules.add(new ServerModule());
        this.modules.add(new MainMenu());
        this.moduleColorMap.put(this.getModuleByClass(AutoArmor.class), new Color(74, 227, 206));
        this.moduleColorMap.put(this.getModuleByClass(AutoCrystall.class), new Color(255, 15, 43));
        this.moduleColorMap.put(this.getModuleByClass(AutoTrap.class), new Color(193, 49, 244));
        this.moduleColorMap.put(this.getModuleByClass(Criticals.class), new Color(204, 151, 184));
        this.moduleColorMap.put(this.getModuleByClass(HoleFiller.class), new Color(166, 55, 110));
        this.moduleColorMap.put(this.getModuleByClass(Killaura.class), new Color(255, 37, 0));
        this.moduleColorMap.put(this.getModuleByClass(Offhand.class), new Color(185, 212, 144));
        this.moduleColorMap.put(this.getModuleByClass(Selftrap.class), new Color(22, 127, 145));
        this.moduleColorMap.put(this.getModuleByClass(Surround.class), new Color(100, 0, 150));
        this.moduleColorMap.put(this.getModuleByClass(AutoGG.class), new Color(240, 49, 110));
        this.moduleColorMap.put(this.getModuleByClass(ChatModifier.class), new Color(255, 59, 216));
        this.moduleColorMap.put(this.getModuleByClass(ExtraTab.class), new Color(161, 113, 173));
        this.moduleColorMap.put(this.getModuleByClass(Godmode.class), new Color(1, 35, 95));
        this.moduleColorMap.put(this.getModuleByClass(MCF.class), new Color(17, 85, 255));
        this.moduleColorMap.put(this.getModuleByClass(RPC.class), new Color(0, 64, 255));
        this.moduleColorMap.put(this.getModuleByClass(BlockHighlight.class), new Color(103, 182, 224));
        this.moduleColorMap.put(this.getModuleByClass(Chams.class), new Color(34, 152, 34));
        this.moduleColorMap.put(this.getModuleByClass(ESP.class), new Color(255, 27, 155));
        this.moduleColorMap.put(this.getModuleByClass(HandColor.class), new Color(96, 138, 92));
        this.moduleColorMap.put(this.getModuleByClass(HoleESP.class), new Color(95, 83, 130));
        this.moduleColorMap.put(this.getModuleByClass(Nametags.class), new Color(98, 82, 223));
        this.moduleColorMap.put(this.getModuleByClass(NoRender.class), new Color(255, 164, 107));
        this.moduleColorMap.put(this.getModuleByClass(StorageESP.class), new Color(97, 81, 223));
        this.moduleColorMap.put(this.getModuleByClass(Tracer.class), new Color(255, 107, 107));
        this.moduleColorMap.put(this.getModuleByClass(Flight.class), new Color(186, 164, 178));
        this.moduleColorMap.put(this.getModuleByClass(NoSlowDown.class), new Color(61, 204, 78));
        this.moduleColorMap.put(this.getModuleByClass(Phase.class), new Color(186, 144, 212));
        this.moduleColorMap.put(this.getModuleByClass(Speed.class), new Color(55, 161, 196));
        this.moduleColorMap.put(this.getModuleByClass(Sprint.class), new Color(148, 184, 142));
        this.moduleColorMap.put(this.getModuleByClass(Step.class), new Color(144, 212, 203));
        this.moduleColorMap.put(this.getModuleByClass(Strafe.class), new Color(0, 204, 255));
        this.moduleColorMap.put(this.getModuleByClass(Velocity.class), new Color(115, 134, 140));
        this.moduleColorMap.put(this.getModuleByClass(ReverseStep.class), new Color(1, 134, 140));
        this.moduleColorMap.put(this.getModuleByClass(FakePlayer.class), new Color(37, 192, 170));
        this.moduleColorMap.put(this.getModuleByClass(FastPlace.class), new Color(217, 118, 37));
        this.moduleColorMap.put(this.getModuleByClass(Freecam.class), new Color(206, 232, 128));
        this.moduleColorMap.put(this.getModuleByClass(Speedmine.class), new Color(152, 166, 113));
        this.moduleColorMap.put(this.getModuleByClass(TimerSpeed.class), new Color(255, 133, 18));
        this.moduleColorMap.put(this.getModuleByClass(TrueDurability.class), new Color(254, 161, 51));
        this.moduleColorMap.put(this.getModuleByClass(XCarry.class), new Color(254, 161, 51));
        this.moduleColorMap.put(this.getModuleByClass(ClickGui.class), new Color(26, 81, 135));
        this.moduleColorMap.put(this.getModuleByClass(Colors.class), new Color(135, 133, 26));
        this.moduleColorMap.put(this.getModuleByClass(FontMod.class), new Color(135, 26, 88));
        this.moduleColorMap.put(this.getModuleByClass(HUD.class), new Color(110, 26, 135));
        this.moduleColorMap.put(this.getModuleByClass(Managers.class), new Color(26, 90, 135));
        this.moduleColorMap.put(this.getModuleByClass(Notifications.class), new Color(170, 153, 255));
        for (Module module : this.modules) {
            module.animation.start();
        }
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T)module;
        }
        return null;
    }

    public void enableModule(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module)module).enable();
        }
    }

    public void disableModule(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        if (module != null) {
            ((Module)module).disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public boolean isModuleEnabled(Class clazz) {
        Object module = this.getModuleByClass(clazz);
        return module != null && ((Module)module).isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled() && !module.isSliding()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        this.modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add((Module)module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        this.modules.stream().filter(Module::listening).forEach(((EventBus)MinecraftForge.EVENT_BUS)::register);
        this.modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onUpdate);
    }

    public void onTick() {
        this.modules.stream().filter(Feature::isEnabled).forEach(Module::onTick);
    }

    public void onRender2D(Render2DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender2D(event));
    }

    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Feature::isEnabled).forEach(module -> module.onRender3D(event));
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void alphabeticallySortModules() {
        this.alphabeticallySortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(Module::getDisplayName)).collect(Collectors.toList());
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        this.modules.forEach(((EventBus)MinecraftForge.EVENT_BUS)::unregister);
        this.modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.field_71462_r instanceof OyVeyGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getBind() == eventKey) {
                module.toggle();
            }
        });
    }

    public List<Module> getAnimationModules(Module.Category category) {
        ArrayList<Module> animationModules = new ArrayList<Module>();
        for (Module module : this.getEnabledModules()) {
            if (module.getCategory() != category || module.isDisabled() || !module.isSliding() || !module.isDrawn()) continue;
            animationModules.add(module);
        }
        return animationModules;
    }
}

