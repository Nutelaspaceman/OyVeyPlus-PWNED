/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package me.earth.phobos.manager;

import me.earth.phobos.features.Feature;
import me.earth.phobos.features.modules.client.Managers;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.util.math.BlockPos;

public class NoStopManager
extends Feature {
    private final Timer timer = new Timer();
    private String prefix;
    private boolean running;
    private boolean sentMessage;
    private BlockPos pos;
    private BlockPos lastPos;
    private boolean stopped;

    public void onUpdateWalkingPlayer() {
        if (NoStopManager.fullNullCheck()) {
            this.stop();
            return;
        }
        if (this.running && this.pos != null) {
            BlockPos currentPos = NoStopManager.mc.field_71439_g.func_180425_c();
            if (currentPos.equals((Object)this.pos)) {
                BlockUtil.debugPos("<Baritone> Arrived at Position: ", this.pos);
                this.running = false;
                return;
            }
            if (currentPos.equals((Object)this.lastPos)) {
                if (this.stopped && this.timer.passedS(Managers.getInstance().baritoneTimeOut.getValue().intValue())) {
                    this.sendMessage();
                    this.stopped = false;
                    return;
                }
                if (!this.stopped) {
                    this.stopped = true;
                    this.timer.reset();
                }
            } else {
                this.lastPos = currentPos;
                this.stopped = false;
            }
            if (!this.sentMessage) {
                this.sendMessage();
                this.sentMessage = true;
            }
        }
    }

    public void sendMessage() {
        NoStopManager.mc.field_71439_g.func_71165_d(this.prefix + "goto " + this.pos.func_177958_n() + " " + this.pos.func_177956_o() + " " + this.pos.func_177952_p());
    }

    public void start(int x, int y, int z) {
        this.pos = new BlockPos(x, y, z);
        this.sentMessage = false;
        this.running = true;
    }

    public void stop() {
        if (this.running) {
            if (NoStopManager.mc.field_71439_g != null) {
                NoStopManager.mc.field_71439_g.func_71165_d(this.prefix + "stop");
            }
            this.running = false;
        }
    }

    public void setPrefix(String prefixIn) {
        this.prefix = prefixIn;
    }
}

