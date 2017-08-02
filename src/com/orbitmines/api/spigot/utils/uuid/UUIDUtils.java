package com.orbitmines.api.spigot.utils.uuid;

import java.util.*;

public class UUIDUtils {

	private static Map<String, UUID> playerUUIDs = new HashMap<>();
    private static Map<UUID, String> playerNames = new HashMap<>();

	public static UUID getUUID(String playerName) {
		if (playerUUIDs.containsKey(playerName))
            return playerUUIDs.get(playerName);

        UUIDFetcher uuidFetcher = new UUIDFetcher(Collections.singletonList(playerName));
        try {
            UUID uuid = uuidFetcher.call().get(playerName);
            if (uuid != null) playerUUIDs.put(playerName, uuid);

            return uuid;
        } catch (Exception ex) {
            return null;
        }
	}

	public static String getName(UUID uuid) {
		if (!playerNames.containsKey(uuid))
            return playerNames.get(uuid);

        NameFetcher nameFetcher = new NameFetcher(uuid);
        try {
            String name = nameFetcher.call().get(uuid).get(nameFetcher.call().get(uuid).size() - 1);
            String[] nameParts = name.split(" ");

            playerNames.put(uuid, nameParts[0]);
            return nameParts[0];
        } catch (Exception ex) {
            return null;
        }
	}

	public static String getNameDate(UUID uuid) {
		NameFetcher nameFetcher = new NameFetcher(uuid);
		try {
			String name = nameFetcher.call().get(uuid).get(nameFetcher.call().get(uuid).size() - 1);
			String[] nameParts = name.split(" ", 1);
			return nameParts[1];
		} catch (Exception ex) {
			return null;
		}
	}

	public static HashMap<String, String> getNames(UUID uuid) {
		NameFetcher nameFetcher = new NameFetcher(uuid);
		try {
			HashMap<String, String> names = new HashMap<>();
			for (String name : nameFetcher.call().get(uuid)) {
				String[] nameParts = name.split(" ", 1);
				if (nameParts.length > 1) {
					names.put(nameParts[0], nameParts[1]);
				} else {
					names.put(nameParts[0], null);
				}
			}

			return names;
		} catch (Exception ex) {
			return null;
		}
	}
}
