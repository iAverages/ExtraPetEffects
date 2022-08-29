package com.danielraybone.extrapeteffects;

import net.splodgebox.elitepets.data.CustomEffect;
import net.splodgebox.elitepets.data.Pet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public class Effect extends CustomEffect {

    private final ExtraPetEffects core;
    public Map<Location, Material> webs = new HashMap<>();
    public Map<Location, BukkitTask> schedules = new HashMap<>();
    public Effect(ExtraPetEffects core, String name, String description, String usage) {
        super(name, description, usage);
        this.core = core;
    }

    @Override
    public void run(Pet pet, ItemStack itemStack, Player player, LivingEntity entity, int level, String[] arguments) {
        int x = Integer.parseInt(arguments[1]);
        int y = Integer.parseInt(arguments[2]);
        int z = Integer.parseInt(arguments[3]);
        int ticks = Integer.parseInt(arguments[4]);

        Collection<Entity> players = player.getWorld().getNearbyEntities(player.getLocation(), x,y,z)
                .stream()
                .filter(entity1 -> entity1 instanceof Player)
                .collect(Collectors.toList());

        for (Entity ent : players) {
            if (ent.getUniqueId() == player.getUniqueId()) continue;
            Player p = (Player) ent;
            Location loc = p.getLocation().getBlock().getLocation();
            Material block = loc.getBlock().getType();
            p.teleport(loc.setDirection(p.getLocation().getDirection()).add(0.5,0,0.5));
            loc.getBlock().setType(Material.WEB);
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ticks, 0));
            if (webs.containsKey(loc)) {
                BukkitTask task = schedules.get(loc);
                task.cancel();
                block = webs.get(loc);
                webs.remove(loc);
            }
            Material finalBlock = block;
            BukkitTask task = Bukkit.getScheduler().runTaskLater(this.core, () -> {
                removeWeb(loc, finalBlock);
                webs.remove(loc);
            }, ticks);
            webs.put(loc, block);
            schedules.put(loc, task);
        }
    }

    public void removeAllWebs() {
        for (Map.Entry<Location, Material> entry : webs.entrySet()) {
            removeWeb(entry.getKey(), entry.getValue());
        }
    }

    public void removeWeb(Location loc, Material material) {
        loc.getBlock().setType(material);
    }
}
