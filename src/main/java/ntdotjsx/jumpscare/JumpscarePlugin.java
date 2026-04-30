package ntdotjsx.jumpscare;

import org.bukkit.plugin.java.JavaPlugin;

public class JumpscarePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("jumpscare").setExecutor(new JumpscareCommand(this));
        getLogger().info("JumpscarePlugin enabled!");
    }
}