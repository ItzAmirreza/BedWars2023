package com.tomkeuper.bedwars.utils;

import com.tomkeuper.bedwars.api.arena.GameState;
import com.tomkeuper.bedwars.api.arena.IArena;
import com.tomkeuper.bedwars.api.events.player.PlayerGeneratorCollectEvent;
import com.tomkeuper.bedwars.api.server.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

import static com.tomkeuper.bedwars.support.version.common.VersionCommon.api;

public class MainUtils {
    public static List<Item> getItemsAround(Item item) {
        List<Item> items = item.getNearbyEntities(0.5, 0.5, 0.5).stream()
                .filter(entity -> entity instanceof Item)
                .filter(entity -> ((Item) entity).getItemStack().getType() == item.getItemStack().getType())
                .filter(entity -> entity.getLocation().distance(item.getLocation()) < 0.1)
                .map(entity -> (Item) entity)
                .collect(Collectors.toList());
        items.add(item);
        return items;
    }

    public static void manageGeneratorPickUp(PlayerPickupItemEvent e, Player player, Item item, List<Item> items) {
        int am = items.size();

        if (items.size() > 1) {
            items.remove(item);
            items.forEach(Entity::remove);
        }
        if (am == 1) return;
        player.getInventory().addItem(new ItemStack(item.getItemStack().getType(), am));
    }

    public static void manageGeneratorPickUp(EntityPickupItemEvent e, Player player, Item item, List<Item> items) {
        int am = items.size();

        if (items.size() > 1) {
            items.remove(item);
            items.forEach(Entity::remove);
        }
        if (am == 1) return;
        player.getInventory().addItem(new ItemStack(item.getItemStack().getType(), am));
    }

    /**
     * @return true if event should be cancelled
     */
    public static boolean managePickup(Item item, LivingEntity player, int amount) {
        if (!(player instanceof Player)) return false;

        if (api.getServerType() == ServerType.MULTIARENA) {
            //noinspection ConstantConditions
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(api.getLobbyWorld())) {
                return true;
            }
        }
        Player p = (Player) player;
        IArena a = api.getArenaUtil().getArenaByPlayer(p);
        ItemStack iStack = item.getItemStack();

        if (a == null) return false;
        if (!a.isPlayer(p)) return true;
        if (a.getStatus() != GameState.playing) return true;
        if (a.getRespawnSessions().containsKey(p)) return true;

        if (iStack.getType() == Material.ARROW) {
            item.setItemStack(api.getVersionSupport().createItemStack(iStack.getType().toString(), iStack.getAmount(), (short) 0));
            return false;
        }

        if (iStack.getType().toString().equals("BED")) {
            item.remove();
            return true;
        } else if (iStack.hasItemMeta()) {
            //noinspection ConstantConditions
            if (iStack.getItemMeta().hasDisplayName()) {
                if (iStack.getItemMeta().getDisplayName().contains("custom")) {
                    Material material = item.getItemStack().getType();
                    ItemMeta itemMeta = new ItemStack(material).getItemMeta();

                    //Call ore pick up event
                    if (!api.getAFKUtil().isPlayerAFK(p)){
                        PlayerGeneratorCollectEvent event = new PlayerGeneratorCollectEvent(p, item, a, amount);
                        Bukkit.getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            return true;
                        } else {
                            iStack.setItemMeta(itemMeta);
                        }
                    } else return true; //Cancel event if player is afk
                }
            }
        }
        return false;
    }

    /**
     * @return true to cancel the event.
     */
    public static boolean manageDrop(Entity player, Item item) {
        if (!(player instanceof Player)) return false;
        if (api.getServerType() == ServerType.MULTIARENA) {
            //noinspection ConstantConditions
            if (player.getLocation().getWorld().getName().equalsIgnoreCase(api.getLobbyWorld())) {
                return true;
            }
        }
        IArena a = api.getArenaUtil().getArenaByPlayer((Player) player);
        if (a == null) return false;

        if (!a.isPlayer((Player) player)) {
            return true;
        }

        if (a.getStatus() != GameState.playing) {
            return true;
        } else {
            ItemStack i = item.getItemStack();
            if (i.getType() == Material.COMPASS) {
                return true;
            }
        }

        return a.getRespawnSessions().containsKey(player);
    }
}
