package me.rownox.rowmissiles.objects;

import org.bukkit.Location;

public class PlayerValues {

    private boolean readyToLaunch;
    private boolean settingLocation;
    private Location targetLoc;

    public PlayerValues() {
        this.readyToLaunch = false;
        this.settingLocation = false;
        this.targetLoc = null;
    }

    public boolean isReadyToLaunch() { return readyToLaunch; }
    public void setReadyToLaunch(boolean bool) { readyToLaunch = bool; }
    public boolean isSettingLocation() { return settingLocation; }
    public void setSettingLocation(boolean bool) { settingLocation = bool; }
    public Location getTargetLoc() { return targetLoc; }
    public void setTargetLoc(Location loc) { targetLoc = loc; }
}
