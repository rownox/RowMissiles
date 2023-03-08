package me.rownox.rowmissles.objects;

import org.bukkit.entity.Player;

public class PlayerValues {

    private boolean readyToLaunch;
    private boolean settingLocation;

    public PlayerValues() {
        this.readyToLaunch = false;
        this.settingLocation = false;
    }

    public boolean isReadyToLaunch() { return readyToLaunch; }
    public void setReadyToLaunch(boolean bool) { readyToLaunch = bool; }
    public boolean isSettingLocation() { return settingLocation; }
    public void setSettingLocation(boolean bool) { settingLocation = bool; }
}
