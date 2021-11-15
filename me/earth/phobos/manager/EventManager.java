/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Strings
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketHeldItemChange
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.network.play.server.SPacketPlayerListItem
 *  net.minecraft.network.play.server.SPacketPlayerListItem$Action
 *  net.minecraft.network.play.server.SPacketTimeUpdate
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.ClientChatEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Post
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$Text
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  net.minecraftforge.fml.common.eventhandler.EventPriority
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$ClientTickEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$Phase
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientConnectedToServerEvent
 *  net.minecraftforge.fml.common.network.FMLNetworkEvent$ClientDisconnectionFromServerEvent
 *  org.lwjgl.opengl.GL11
 */
package me.earth.phobos.manager;

import com.google.common.base.Strings;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import me.earth.phobos.Oyvey;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.event.events.ConnectionEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.Render2DEvent;
import me.earth.phobos.event.events.Render3DEvent;
import me.earth.phobos.event.events.TotemPopEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.Feature;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.client.Managers;
import me.earth.phobos.features.modules.client.ServerModule;
import me.earth.phobos.features.modules.combat.AutoCrystall;
import me.earth.phobos.util.GLUProjection;
import me.earth.phobos.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.opengl.GL11;

public class EventManager
extends Feature {
    private final Timer timer = new Timer();
    private final Timer logoutTimer = new Timer();
    private final Timer switchTimer = new Timer();
    private boolean keyTimeout;
    private final AtomicBoolean tickOngoing = new AtomicBoolean(false);

    public void init() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    public void onUnload() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!EventManager.fullNullCheck() && event.getEntity().func_130014_f_().field_72995_K && event.getEntityLiving().equals((Object)EventManager.mc.field_71439_g)) {
            Oyvey.potionManager.update();
            Oyvey.totemPopManager.onUpdate();
            Oyvey.inventoryManager.update();
            Oyvey.holeManager.update();
            Oyvey.safetyManager.onUpdate();
            Oyvey.moduleManager.onUpdate();
            Oyvey.timerManager.update();
            if (this.timer.passedMs(Managers.getInstance().moduleListUpdates.getValue().intValue())) {
                Oyvey.moduleManager.sortModules(true);
                Oyvey.moduleManager.alphabeticallySortModules();
                this.timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && mc.func_147114_u() != null && ServerModule.getInstance().isConnected() && EventManager.mc.field_71441_e != null) {
            String command = "@Server" + ServerModule.getInstance().getServerPrefix() + "module " + event.getSetting().getFeature().getName() + " set " + event.getSetting().getName() + " " + event.getSetting().getPlannedValue().toString();
            CPacketChatMessage cPacketChatMessage = new CPacketChatMessage(command);
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onTickHighest(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            this.tickOngoing.set(true);
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onTickLowest(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.tickOngoing.set(false);
            AutoCrystall.getInstance().postTick();
        }
    }

    public boolean ticksOngoing() {
        return this.tickOngoing.get();
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.logoutTimer.reset();
        Oyvey.moduleManager.onLogin();
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Oyvey.moduleManager.onLogout();
        Oyvey.totemPopManager.onLogout();
        Oyvey.potionManager.onLogout();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        Oyvey.moduleManager.onTick();
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (EventManager.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0) {
            Oyvey.baritoneManager.onUpdateWalkingPlayer();
            Oyvey.speedManager.updateValues();
            Oyvey.rotationManager.updateRotations();
            Oyvey.positionManager.updatePosition();
        }
        if (event.getStage() == 1) {
            Oyvey.rotationManager.restoreRotations();
            Oyvey.positionManager.restorePosition();
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketHeldItemChange) {
            this.switchTimer.reset();
        }
    }

    public boolean isOnSwitchCoolDown() {
        return !this.switchTimer.passedMs(500L);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getStage() != 0) {
            return;
        }
        Oyvey.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.func_149160_c() == 35 && packet.func_149161_a((World)EventManager.mc.field_71441_e) instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)packet.func_149161_a((World)EventManager.mc.field_71441_e);
                MinecraftForge.EVENT_BUS.post((Event)new TotemPopEvent(player));
                Oyvey.totemPopManager.onTotemPop(player);
                Oyvey.potionManager.onTotemPop(player);
            }
        } else if (event.getPacket() instanceof SPacketPlayerListItem && !EventManager.fullNullCheck() && this.logoutTimer.passedS(1.0)) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals((Object)packet.func_179768_b()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals((Object)packet.func_179768_b())) {
                return;
            }
            packet.func_179767_a().stream().filter(Objects::nonNull).filter(data -> !Strings.isNullOrEmpty((String)data.func_179962_a().getName()) || data.func_179962_a().getId() != null).forEach(data -> {
                UUID id = data.func_179962_a().getId();
                switch (packet.func_179768_b()) {
                    case ADD_PLAYER: {
                        String name = data.func_179962_a().getName();
                        MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(0, id, name));
                        break;
                    }
                    case REMOVE_PLAYER: {
                        EntityPlayer entity = EventManager.mc.field_71441_e.func_152378_a(id);
                        if (entity != null) {
                            String logoutName = entity.func_70005_c_();
                            MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(1, entity, id, logoutName));
                            break;
                        }
                        MinecraftForge.EVENT_BUS.post((Event)new ConnectionEvent(2, id, null));
                    }
                }
            });
        } else if (event.getPacket() instanceof SPacketTimeUpdate) {
            Oyvey.serverManager.update();
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        EventManager.mc.field_71424_I.func_76320_a("phobos");
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
        GlStateManager.func_179103_j((int)7425);
        GlStateManager.func_179097_i();
        GlStateManager.func_187441_d((float)1.0f);
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        GLUProjection projection = GLUProjection.getInstance();
        IntBuffer viewPort = GLAllocation.func_74527_f((int)16);
        FloatBuffer modelView = GLAllocation.func_74529_h((int)16);
        FloatBuffer projectionPort = GLAllocation.func_74529_h((int)16);
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projectionPort);
        GL11.glGetInteger((int)2978, (IntBuffer)viewPort);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        projection.updateMatrices(viewPort, modelView, projectionPort, (double)scaledResolution.func_78326_a() / (double)Minecraft.func_71410_x().field_71443_c, (double)scaledResolution.func_78328_b() / (double)Minecraft.func_71410_x().field_71440_d);
        Oyvey.moduleManager.onRender3D(render3dEvent);
        GlStateManager.func_187441_d((float)1.0f);
        GlStateManager.func_179103_j((int)7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
        GlStateManager.func_179089_o();
        GlStateManager.func_179132_a((boolean)true);
        GlStateManager.func_179098_w();
        GlStateManager.func_179147_l();
        GlStateManager.func_179126_j();
        EventManager.mc.field_71424_I.func_76319_b();
    }

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Oyvey.textManager.updateResolution();
        }
    }

    @SubscribeEvent(priority=EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals((Object)RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            Oyvey.moduleManager.onRender2D(render2DEvent);
            GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                EventManager.mc.field_71456_v.func_146158_b().func_146239_a(event.getMessage());
                if (event.getMessage().length() > 1) {
                    Oyvey.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendMessage("Please enter a command.");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage("\u00a7cAn error occurred while running this command. Check the log!");
            }
            event.setMessage("");
        }
    }
}

