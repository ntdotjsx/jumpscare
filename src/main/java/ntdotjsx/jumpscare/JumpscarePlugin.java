package ntdotjsx.jumpscare;

import org.bukkit.plugin.java.JavaPlugin;

public class JumpscarePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("jumpscare").setExecutor(new JumpscareCommand(this));
        getLogger().info("JumpscarePlugin enabled!");
    }
}