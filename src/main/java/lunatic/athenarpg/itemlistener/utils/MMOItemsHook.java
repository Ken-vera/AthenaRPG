package lunatic.athenarpg.itemlistener.utils;

import io.lumine.mythic.lib.api.item.NBTItem;
import lunatic.athenarpg.Main;
import org.bukkit.inventory.ItemStack;

public class MMOItemsHook {
    public MMOItemsHook() {
    }

    public boolean isMMOItem(ItemStack itemStack) {
        return NBTItem.get(itemStack).hasType();
    }

    public String getMMOItemId(ItemStack itemStack) {
        return NBTItem.get(itemStack).getString("MMOITEMS_ITEM_ID");
    }

    public String getMMOItemName(ItemStack itemStack) {
        return NBTItem.get(itemStack).getString("MMOITEMS_NAME").replaceAll("§.", "").replaceAll("&.", "");
    }

    public String getMMOItemNbtString(ItemStack itemStack, String tag) {
        return NBTItem.get(itemStack).getString(tag);
    }

    public Integer getMMOItemNbtInteger(ItemStack itemStack, String tag) {
        return NBTItem.get(itemStack).getInteger(tag);
    }

    public boolean getMMOItemNbtBoolean(ItemStack itemStack, String tag) {
        return NBTItem.get(itemStack).getBoolean(tag);
    }

    public Double getMMOItemNbtDouble(ItemStack itemStack, String tag) {
        return NBTItem.get(itemStack).getDouble(tag);
    }
}
