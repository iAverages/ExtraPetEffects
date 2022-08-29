package com.danielraybone.extrapeteffects;

import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.splodgebox.elitepets.ElitePetsAPI;

public final class ExtraPetEffects extends JavaPlugin {

    private Effect effect;
    private Essentials essentials = null;

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            this.getLogger().info("Found Essentials, will bypass teleport invulnerability.");
            this.essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        }
        this.getServer().getScheduler().runTaskLater(this, () -> {
            this.getLogger().info("Registering effects");
            this.effect = new Effect(this,"WEB", "Place a web under players with in a radius", "WEB:x:y:z:ticks");
            ElitePetsAPI.getPetAPI().registerEffects(this.effect);
        }, 20L);
    }

    public boolean isEssentialsEnabled() {
        return essentials != null;
    }

    public Essentials getEssentials() {
        return this.essentials;
    }
    @Override
    public void onDisable() {
        this.effect.removeAllWebs();
    }
}
