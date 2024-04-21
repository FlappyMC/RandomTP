package fr.flappy.randomtp.configurations;

import fr.flappy.randomtp.SatisRandomTP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Config {
    private File file;
    private FileConfiguration fileConfiguration;
    private final String name;

    public Config(String name){
        if(!name.endsWith(".yml")) name += ".yml";
        this.name = name;

        fileConfiguration = YamlConfiguration.loadConfiguration(file());
        save();
        reload();
    }

    private File file(){return new File(JavaPlugin.getPlugin(SatisRandomTP.class).getDataFolder(), name);}
    public FileConfiguration options(){return fileConfiguration;}

    public void save(){
        if(file == null) {
            file = file();
        }
        if(!file.exists()){
            JavaPlugin.getPlugin(SatisRandomTP.class).saveResource(name, false);
        }
    }

    public void reload(){fileConfiguration = YamlConfiguration.loadConfiguration(file);}
}
