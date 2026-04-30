package ntdotjsx.jumpscare;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        sender.sendMessage("§aSent jumpscare to §e" + target.getName());
        return true;
    }

    private void triggerJumpscare(Player player) {

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            showJumpscareHUD(player);
            shakeCamera(player);
            player.addPotionEffect(new PotionEffect(
                    PotionEffectType.BLINDNESS, 60, 1, false, false, false
            ));
            player.playSound(player.getLocation(), "minecraft:welcome", 2.0f, 1.0f);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                hideJumpscareHUD(player);
            }, 30L);

        }, 10L);
    }

    private void showJumpscareHUD(Player player) {
        Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                "iaplaytotemanimation jumpscare:jumpscare_face " + player.getName()
        );
    }

    private void hideJumpscareHUD(Player player) {
        if (!player.isOnline()) return;
        // ลบ blindness ออก
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

    private void shakeCamera(Player player) {
        final int[] ticks = {0};
        final int duration = 15;

        Bukkit.getScheduler().runTaskTimer(plugin, task -> {
            if (ticks[0] >= duration || !player.isOnline()) {
                task.cancel();
                return;
            }
            player.damage(0.0);
            ticks[0]++;
        }, 0L, 2L);
    }
}