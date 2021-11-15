/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.world.World
 */
package me.earth.phobos.features.modules.player;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.modules.client.ClickGui;
import me.earth.phobos.features.modules.client.ServerModule;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.world.World;

public class FakePlayer
extends Module {
    private static final String[] fitInfo = new String[]{"fdee323e-7f0c-4c15-8d1c-0f277442342a", "Fit"};
    private static FakePlayer INSTANCE = new FakePlayer();
    private final List<EntityOtherPlayerMP> fakeEntities = new ArrayList<EntityOtherPlayerMP>();
    public List<Integer> fakePlayerIdList = new ArrayList<Integer>();

    public FakePlayer() {
        super("FakePlayer", "Spawns in a fake player", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    public static FakePlayer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakePlayer();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        this.disable();
    }

    @Override
    public void onEnable() {
        if (FakePlayer.fullNullCheck()) {
            this.disable();
            return;
        }
        if (ServerModule.getInstance().isConnected()) {
            FakePlayer.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            FakePlayer.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module FakePlayer set Enabled true"));
        }
        this.fakePlayerIdList = new ArrayList<Integer>();
        this.addFakePlayer(fitInfo[0], fitInfo[1], -100, 0, 0);
    }

    @Override
    public void onDisable() {
        if (FakePlayer.fullNullCheck()) {
            return;
        }
        if (ServerModule.getInstance().isConnected()) {
            FakePlayer.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Serverprefix" + ClickGui.getInstance().prefix.getValue()));
            FakePlayer.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage("@Server" + ClickGui.getInstance().prefix.getValue() + "module FakePlayer set Enabled false"));
        }
        for (int id : this.fakePlayerIdList) {
            FakePlayer.mc.field_71441_e.func_73028_b(id);
        }
    }

    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }

    private void addFakePlayer(String uuid, String name, int entityId, int offsetX, int offsetZ) {
        GameProfile profile = new GameProfile(UUID.fromString(uuid), name);
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.field_71441_e, profile);
        fakePlayer.func_82149_j((Entity)FakePlayer.mc.field_71439_g);
        fakePlayer.field_70165_t += (double)offsetX;
        fakePlayer.field_70161_v += (double)offsetZ;
        fakePlayer.func_70606_j(FakePlayer.mc.field_71439_g.func_110143_aJ() + FakePlayer.mc.field_71439_g.func_110139_bj());
        this.fakeEntities.add(fakePlayer);
        FakePlayer.mc.field_71441_e.func_73027_a(entityId, (Entity)fakePlayer);
        this.fakePlayerIdList.add(entityId);
    }
}

