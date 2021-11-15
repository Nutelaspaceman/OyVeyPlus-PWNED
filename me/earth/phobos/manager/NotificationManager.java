/*
 * Decompiled with CFR 0.150.
 */
package me.earth.phobos.manager;

import java.util.ArrayList;
import me.earth.phobos.Oyvey;
import me.earth.phobos.features.modules.client.HUD;
import me.earth.phobos.features.notifications.Notifications;

public class NotificationManager {
    private final ArrayList<Notifications> notifications = new ArrayList();

    public void handleNotifications(int posY) {
        for (int i = 0; i < this.getNotifications().size(); ++i) {
            this.getNotifications().get(i).onDraw(posY);
            posY -= Oyvey.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 5;
        }
    }

    public void addNotification(String text, long duration) {
        this.getNotifications().add(new Notifications(text, duration));
    }

    public ArrayList<Notifications> getNotifications() {
        return this.notifications;
    }
}

