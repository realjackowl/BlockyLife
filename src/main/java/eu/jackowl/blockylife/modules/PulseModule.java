package eu.jackowl.blockylife.modules;

import eu.jackowl.blockylife.calculators.BreathCalculator;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PulseModule {

    public void sendBreath(@NotNull Player player) {
        final Location loc1 = player.getEyeLocation();
        final Location loc2 = BreathCalculator.run(player, loc1, -.5, 3.25);
        final float add = -player.getLocation().getPitch() / 90;
        final Location loc3 = loc2.add(0, add, 0);
        final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1);
        player.spawnParticle(Particle.REDSTONE, loc3, 1, 0, 0, 0, 1, dustOptions);
    }
}
