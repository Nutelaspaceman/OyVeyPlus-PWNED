/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 */
package me.earth.phobos.features.gui.effect;

import java.util.Random;
import me.earth.phobos.util.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;

public class Snow {
    private int _x;
    private int _y;
    private int _fallingSpeed;
    private int _size;

    public Snow(int x, int y, int fallingSpeed, int size) {
        this._x = x;
        this._y = y;
        this._fallingSpeed = fallingSpeed;
        this._size = size;
    }

    public int getX() {
        return this._x;
    }

    public void setX(int x) {
        this._x = x;
    }

    public int getY() {
        return this._y;
    }

    public void setY(int _y) {
        this._y = _y;
    }

    public void Update(ScaledResolution res) {
        RenderUtil.drawRect(this.getX(), this.getY(), this.getX() + this._size, this.getY() + this._size, -1714829883);
        this.setY(this.getY() + this._fallingSpeed);
        if (this.getY() > res.func_78328_b() + 10 || this.getY() < -10) {
            this.setY(-10);
            Random rand = new Random();
            this._fallingSpeed = rand.nextInt(5) + 1;
            this._size = rand.nextInt(4) + 1;
        }
    }
}

