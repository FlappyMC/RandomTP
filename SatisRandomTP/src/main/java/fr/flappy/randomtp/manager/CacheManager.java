package fr.flappy.randomtp.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CacheManager {
    private final Map<UUID, PlayerManager> cache;

    public CacheManager(PlayerManager playerManager) {
        cache = new HashMap<>();
    }

    public void addPlayer(PlayerManager playerManager) {
        cache.put(playerManager.getUuid(), playerManager);
    }

    public void removePlayer(UUID uuid) {
        cache.remove(uuid);
    }

    public PlayerManager getPlayer(UUID uuid) {
        return cache.get(uuid);
    }

    public Map<UUID, PlayerManager> getCache() {
        return cache;
    }
}
