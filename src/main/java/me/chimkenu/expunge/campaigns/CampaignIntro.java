package me.chimkenu.expunge.campaigns;

public interface CampaignIntro {
    int play();

    //        VectorAndRotation[] points = gameMap.getMapLocations().scenicPoints();
    //
    //        if (points.length < 1) {
    //            return 1;
    //        }
    //
    //        new BukkitRunnable() {
    //            @Override
    //            public void run() {
    //                for (Player player : players.keySet()) {
    //                    player.setGameMode(GameMode.SPECTATOR);
    //                    player.teleport(new Location(map.getWorld(), points[0].vector().getX(), points[0].vector().getY(), points[0].vector().getZ(), points[0].yaw(), points[0].pitch()));
    //                }
    //            }
    //        }.runTaskLater(plugin, 1);
    //
    //        int i;
    //        for (i = 0; i < points.length - 1; i++) {
    //            int j = i + 1;
    //            new BukkitRunnable() {
    //                @Override
    //                public void run() {
    //                    for (Player player : players.keySet()) {
    //                        if (j == 1)
    //                            player.sendTitle(gameMap.getName(), GameMaps.buildersToString(gameMap.getBuilders()), 20, 60, 20);
    //                        player.teleport(new Location(map.getWorld(), points[j].vector().getX(), points[j].vector().getY(), points[j].vector().getZ(), points[j].yaw(), points[j].pitch()));
    //                    }
    //                }
    //            }.runTaskLater(plugin, 101 + (i * 60L));
    //        }
    //
    //        return 161 + (i * 60);
}
