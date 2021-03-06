package ink.rainbowbridge.yggdrasil.commands.sub;

import ink.rainbowbridge.yggdrasil.Yggdrasil;
import io.izzel.taboolib.module.command.base.BaseSubCommand;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.tellraw.TellrawJson;
import io.izzel.taboolib.util.item.Items;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Save extends BaseSubCommand {
    @Override
    public void onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1){
            asItemStack(commandSender, strings);
        }
        else if (strings.length == 2){
            switch (strings[1].toLowerCase()){
                case "itemstack":{
                    asItemStack(commandSender, strings);
                }
                case "itemjson":{
                    if (Yggdrasil.items.containsKey(strings[0])){
                        Yggdrasil.send(commandSender,"物品 &f"+strings[0]+" &7已经存在");
                        TellrawJson.create().append(ChatColor.translateAlternateColorCodes('&',Yggdrasil.config.getString("Partly-Prefix","&7&l[&f&lYggdrasil&7&l] &7")+"点此以手上物品覆盖！"))
                                .clickCommand("/yggdrasil cover "+strings[0]+" itemjson")
                                .hoverText("点此以手中物品覆盖！")
                                .send(commandSender);
                        return;
                    }
                    ItemStack item = ((Player)commandSender).getInventory().getItemInMainHand();
                    if (!Items.isNull(item)) {
                        Yggdrasil.items.put(strings[0],item);
                        TConfig conf = TConfig.create(new File(Yggdrasil.getDir(),strings[0]+".yml"));
                        conf.set("ItemJson",Items.toJson(item));
                        conf.saveToFile();
                        Yggdrasil.send(commandSender,"已经以内部名称: &f"+strings[0]+" &7保存 ["+ Items.getName(item)+"&7] (ItemJson)");
                        return;
                    }
                    Yggdrasil.send(commandSender,"&4错误: 手中物品不能为空");
                }
                }
            }
        }

    private void asItemStack(@NotNull CommandSender commandSender, @NotNull String[] strings) {
        if (Yggdrasil.items.containsKey(strings[0])){
            Yggdrasil.send(commandSender,"物品 &f"+strings[0]+" &7已经存在");
            TellrawJson.create().append(ChatColor.translateAlternateColorCodes('&',Yggdrasil.config.getString("Partly-Prefix","&7&l[&f&lYggdrasil&7&l] &7")+"点此以手上物品覆盖！"))
                    .clickCommand("/yggdrasil cover "+strings[0]+" itemstack")
                    .hoverText("点此以手中物品覆盖！")
                    .send(commandSender);
            return;
        }
        ItemStack item = ((Player)commandSender).getInventory().getItemInMainHand();
        if (!Items.isNull(item)) {
            Yggdrasil.items.put(strings[0],item);
            TConfig conf = TConfig.create(new File(Yggdrasil.getDir(),strings[0]+".yml"));
            conf.set("ItemStack",item);
            conf.saveToFile();
            Yggdrasil.send(commandSender,"已经以内部名称: &f"+strings[0]+" &7保存 ["+ Items.getName(item)+"&7] (ItemStack)");
            return;
        }
        Yggdrasil.send(commandSender,"&4错误: 手中物品不能为空");
    }
}
