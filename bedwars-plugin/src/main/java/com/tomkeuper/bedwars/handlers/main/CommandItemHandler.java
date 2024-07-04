/*
 * BedWars2023 - A bed wars mini-game.
 * Copyright (C) 2024 Tomas Keuper
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact e-mail: contact@fyreblox.com
 */

package com.tomkeuper.bedwars.handlers.main;

import com.tomkeuper.bedwars.api.BedWars;
import com.tomkeuper.bedwars.api.arena.IArena;
import com.tomkeuper.bedwars.api.configuration.ConfigPath;
import com.tomkeuper.bedwars.api.items.handlers.HandlerType;
import com.tomkeuper.bedwars.api.items.handlers.IPermanentItem;
import com.tomkeuper.bedwars.api.items.handlers.PermanentItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.tomkeuper.bedwars.BedWars.config;

public class CommandItemHandler extends PermanentItemHandler {
    public CommandItemHandler(String id, Plugin plugin, BedWars api) {
        super(id, plugin, api);
    }

    @Override
    public void handleUse(Player player, IArena arena, IPermanentItem lobbyItem) {
        String command;
        if (arena == null) {
            command = config.getYml().getString(ConfigPath.GENERAL_CONFIGURATION_LOBBY_ITEMS_COMMAND.replace("%path%", lobbyItem.getIdentifier()));
        } else {
            if (arena.isSpectator(player)) {
                command = config.getYml().getString(ConfigPath.GENERAL_CONFIGURATION_SPECTATOR_ITEMS_COMMAND.replace("%path%", lobbyItem.getIdentifier()));
            } else {
                command = config.getYml().getString(ConfigPath.GENERAL_CONFIGURATION_PRE_GAME_ITEMS_COMMAND.replace("%path%", lobbyItem.getIdentifier()));
            }
        }
        if (command == null) {
            Bukkit.getLogger().warning("Command for item `" + lobbyItem.getIdentifier() + "` is not set.");
            return;
        }
        player.performCommand(command);
    }

    @Override
    public HandlerType getType() {
        return HandlerType.COMMAND;
    }
}