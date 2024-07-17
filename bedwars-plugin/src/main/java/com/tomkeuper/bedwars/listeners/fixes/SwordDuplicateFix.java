package com.tomkeuper.bedwars.listeners.fixes;

import com.tomkeuper.bedwars.api.arena.GameState;
import com.tomkeuper.bedwars.api.arena.IArena;
import com.tomkeuper.bedwars.arena.Arena;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import static com.tomkeuper.bedwars.BedWars.nms;

public class SwordDuplicateFix implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){

        Player player = e.getPlayer();

        IArena a = Arena.getArenaByPlayer(player);

        if (!Arena.isInArena(player) || a.getStatus() != GameState.playing) return;

        if (e.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD && nms.isSword(e.getItemDrop().getItemStack())){
            e.setCancelled(true);

            // This avoids the duplication of swords on cancel the event
            if (player.getInventory().containsAtLeast(e.getItemDrop().getItemStack(), 1)){
                player.getInventory().remove(e.getItemDrop().getItemStack());
            }
        }
    }

}
