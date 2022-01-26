package eu.jackowl.blockylife.calculators;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class BreathCalculator {
    public static @NotNull Location calculateBreath(@NotNull Player p, @NotNull Location center, double radius, double angleInRadian) {
        final double rotation = Math.toRadians((p.getLocation().getYaw() + 90) % 360);
        final double x = center.getX() + radius * Math.cos(angleInRadian + rotation);
        final double z = center.getZ() + radius * Math.sin(angleInRadian + rotation);
        final double y = center.getY();
        final Location loc = new Location(center.getWorld(), x, y, z);
        final Vector difference = center.toVector().clone().subtract(loc.toVector());
        loc.setDirection(difference);
        return loc;
    }
}