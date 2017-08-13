package me.ryanhamshire.GriefPrevention;

class WorldGuardWrapper
{
//    private WorldGuardPlugin worldGuard = null;
//
//    public WorldGuardWrapper() throws ClassNotFoundException
//    {
//        this.worldGuard = (WorldGuardPlugin)GriefPrevention.instance.getServer().getPluginManager().getPlugin("WorldGuard");
//    }
//
//    public boolean canBuild(Location lesserCorner, Location greaterCorner, Player creatingPlayer)
//    {
//        World world = lesserCorner.getWorld();
//
//        if (worldGuard == null)
//        {
//            GriefPrevention.AddLogEntry("WorldGuard is out of date. Please update or remove WorldGuard.");
//            return true;
//        }
//
//        if(new RegionPermissionModel(this.worldGuard, creatingPlayer).mayIgnoreRegionProtection(world)) return true;
//
//        RegionManager manager = this.worldGuard.getRegionManager(world);
//
//        if(manager != null)
//        {
//            ProtectedCuboidRegion tempRegion = new ProtectedCuboidRegion(
//                "GP_TEMP",
//                new BlockVector(lesserCorner.getX(), 0, lesserCorner.getZ()),
//                new BlockVector(greaterCorner.getX(), world.getMaxHeight(), greaterCorner.getZ()));
//            ApplicableRegionSet overlaps = manager.getApplicableRegions(tempRegion);
//            LocalPlayer localPlayer = worldGuard.wrapPlayer(creatingPlayer);
//            for (ProtectedRegion r : overlaps.getRegions()) {
//                if (!manager.getApplicableRegions(r).testState(localPlayer, DefaultFlag.BUILD)) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        return true;
//    }
}
