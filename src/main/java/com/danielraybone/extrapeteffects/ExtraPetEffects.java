package com.danielraybone.extrapeteffects;

import org.bukkit.plugin.java.JavaPlugin;
import net.splodgebox.elitepets.ElitePetsAPI;

public final class ExtraPetEffects extends JavaPlugin {

    private Effect effect;

    @Override
    public void onEnable() {
        this.getServer().getScheduler().runTaskLater(this, () -> {
            this.getLogger().info("Registering effects");
            this.effect = new Effect(this,"WEB", "Place a web under players with in a radius", "WEB:x:y:z:ticks");
            ElitePetsAPI.getPetAPI().registerEffects(this.effect);
        }, 20L);
    }

    @Override
    public void onDisable() {
        this.effect.removeAllWebs();
    }
}
