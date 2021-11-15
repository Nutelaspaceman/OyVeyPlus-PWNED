/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Converter
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonPrimitive
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package me.earth.phobos.features.setting;

import com.google.common.base.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.earth.phobos.Oyvey;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Bind {
    private int bind;
    private BindType type;

    public Bind(int key) {
        this.bind = key;
        this.type = BindType.KEY;
    }

    public Bind(int bind, BindType type) {
        this.bind = bind;
        this.type = type;
    }

    public static Bind none() {
        return new Bind(-1);
    }

    public int getBind() {
        return this.bind;
    }

    public BindType getType() {
        return this.type;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public boolean isEmpty() {
        return this.bind < 0;
    }

    public String toString() {
        if (this.isEmpty()) {
            return "None";
        }
        if (this.type == BindType.KEY) {
            String keyName = Keyboard.getKeyName((int)this.bind);
            return this.capitalise(keyName);
        }
        return "MOUSE" + String.valueOf(this.bind);
    }

    public boolean isDown() {
        if (this.isEmpty()) {
            return false;
        }
        if (this.type == BindType.KEY) {
            return Keyboard.isKeyDown((int)this.bind);
        }
        return Mouse.isButtonDown((int)this.bind);
    }

    private String capitalise(String str) {
        if (str.isEmpty()) {
            return "";
        }
        return Character.toUpperCase(str.charAt(0)) + (str.length() != 1 ? str.substring(1).toLowerCase() : "");
    }

    public static class BindConverter
    extends Converter<Bind, JsonElement> {
        public JsonElement doForward(Bind bind) {
            return new JsonPrimitive(bind.toString());
        }

        public Bind doBackward(JsonElement jsonElement) {
            String s = jsonElement.getAsString();
            BindType type = BindType.KEY;
            if (s.equalsIgnoreCase("None")) {
                return Bind.none();
            }
            if (s.startsWith("MOUSE")) {
                Oyvey.LOGGER.info("mouse");
                type = BindType.MOUSE;
                s = s.replace("MOUSE", "");
            }
            int bind = -1;
            try {
                if (type == BindType.KEY) {
                    bind = Keyboard.getKeyIndex((String)s.toUpperCase());
                } else {
                    Oyvey.LOGGER.info((Object)Integer.valueOf(s));
                    bind = Integer.valueOf(s);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (bind == 0 && type == BindType.KEY) {
                return Bind.none();
            }
            return new Bind(bind, type);
        }
    }

    public static enum BindType {
        KEY,
        MOUSE;

    }
}

