package com.tomkeuper.bedwars.listeners.fixes;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class IdenticalWoolFix implements Listener {

    @EventHandler
    public void woolPickUpEvent(PlayerPickupItemEvent event) {
        //the problem is we get 2 different wool blocks, but we want to make sure they stack

        //check if pickup is wool
        Material material = event.getItem().getItemStack().getType();
        if (material != Material.WOOL) {
            return;
        }

        //check if player has wool in inventory
        if (event.getPlayer().getInventory().contains(Material.WOOL)) {
            //check if wool is the same color
            if (event.getPlayer().hasPermission("bedwarsdebug.test")) {
                System.out.println(event.getItem().getItemStack().getData().toString());
            }

        }

    }

}
