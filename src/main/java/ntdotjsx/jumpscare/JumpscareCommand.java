package ntdotjsx.jumpscare;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class JumpscareCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public JumpscareCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // --- /jumpscare reload ---
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.isOp()) {
                sender.sendMessage("§cคุณต้องเป็น OP เท่านั้น!");
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage("§aโหลด config ใหม่เรียบร้อย!");
            return true;
        }

        // --- OP check ---
        if (!sender.isOp()) {
            sender.sendMessage("§cคุณต้องเป็น OP เท่านั้น!");
            return true;
        }

        // --- Usage ---
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /jumpscare <player>");
            sender.sendMessage("§cUsage: /jumpscare reload");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cไม่พบผู้เล่น: §e" + args[0]);
            return true;
        }

        // --- Exempt list check ---
        List<String> exemptList = plugin.getConfig().getStringList("exempt-players");
        if (exemptList.stream().anyMatch(name -> name.equalsIgnoreCase(target.getName()))) {
            sender.sendMessage("§c" + target.getName() + " §7อยู่ในรายชื่อที่ห้าม jumpscare!");
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
            player.playSound(player.getLocation(), "minecraft:jc_01", 2.0f, 1.0f);
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