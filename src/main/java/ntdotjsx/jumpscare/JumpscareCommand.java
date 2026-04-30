package ntdotjsx.jumpscare;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JumpscareCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public JumpscareCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("jumpscare.use")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /jumpscare <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }

        triggerJumpscare(target);
        sender.sendMessage("§aSent jumpscare to " + target.getName());
        return true;
    }

    private void triggerJumpscare(Player player) {
        // 1. เล่นเสียง ItemsAdder
        player.playSound(player.getLocation(),
                "minecafe:welcome_sound", 2.0f, 1.0f);

        // 2. จอสั่น
        player.getWorld().spawnParticle(
                org.bukkit.Particle.EXPLOSION,
                player.getLocation(), 1);

        // ใช้ shake camera ของ Paper API
        shakeCamera(player);

        // 3. Title เต็มจอ
        player.sendTitle(
                "§4§l⚠ BOO! ⚠",
                "§cYou have been jumpscared!",
                5, 40, 10
        );
    }

    private void shakeCamera(Player player) {
        // Paper API 1.21 รองรับ shake โดยตรง
        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            player.getWorld().playEffect(
                    player.getLocation(),
                    org.bukkit.Effect.BLAZE_SHOOT, 0);
        }, 0L, 1L);

        // หยุดสั่นหลัง 2 วินาที
        Bukkit.getScheduler().runTaskLater(plugin, () -> {}, 40L);
    }
}