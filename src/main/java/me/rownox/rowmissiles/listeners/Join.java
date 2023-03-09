package me.rownox.rowmissiles.listeners;

import me.rownox.rowmissiles.RowMissiles;
import me.rownox.rowmissiles.objects.PlayerValues;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        RowMissiles.playerValues.put(p.getUniqueId(), new PlayerValues());
    }
}
