package lunatic.athenarpg.itemlistener.utils;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.api.item.build.ItemStackBuilder;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.stat.data.DoubleData;
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

    public String getMMOItemDamage(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.ATTACK_DAMAGE));
    }

    public void setMMOItemDamage(ItemStack itemStack, double damage) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.ATTACK_DAMAGE, new DoubleData(damage));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Critical Strike Chance
    public String getMMOItemCritChance(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.CRITICAL_STRIKE_CHANCE));
    }

    public void setMMOItemCritChance(ItemStack itemStack, double critChance) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.CRITICAL_STRIKE_CHANCE, new DoubleData(critChance));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Critical Strike Power
    public String getMMOItemCritPower(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.CRITICAL_STRIKE_POWER));
    }

    public void setMMOItemCritPower(ItemStack itemStack, double critPower) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.CRITICAL_STRIKE_POWER, new DoubleData(critPower));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Block Power
    public String getMMOItemBlockPower(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.BLOCK_POWER));
    }

    public void setMMOItemBlockPower(ItemStack itemStack, double blockPower) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.BLOCK_POWER, new DoubleData(blockPower));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Block Rating
    public String getMMOItemBlockRating(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.BLOCK_RATING));
    }

    public void setMMOItemBlockRating(ItemStack itemStack, double blockRating) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.BLOCK_RATING, new DoubleData(blockRating));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Dodge Rating
    public String getMMOItemDodgeRating(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.DODGE_RATING));
    }

    public void setMMOItemDodgeRating(ItemStack itemStack, double dodgeRating) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.DODGE_RATING, new DoubleData(dodgeRating));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Parry Rating
    public String getMMOItemParryRating(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.PARRY_RATING));
    }

    public void setMMOItemParryRating(ItemStack itemStack, double parryRating) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.PARRY_RATING, new DoubleData(parryRating));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // PvE Damage
    public String getMMOItemPveDamage(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.PVE_DAMAGE));
    }

    public void setMMOItemPveDamage(ItemStack itemStack, double pveDamage) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.PVE_DAMAGE, new DoubleData(pveDamage));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // PvP Damage
    public String getMMOItemPvpDamage(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.PVP_DAMAGE));
    }

    public void setMMOItemPvpDamage(ItemStack itemStack, double pvpDamage) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.PVP_DAMAGE, new DoubleData(pvpDamage));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Weapon Damage
    public String getMMOItemWeaponDamage(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.WEAPON_DAMAGE));
    }

    public void setMMOItemWeaponDamage(ItemStack itemStack, double weaponDamage) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.WEAPON_DAMAGE, new DoubleData(weaponDamage));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Projectile Damage
    public String getMMOItemProjectileDamage(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.PROJECTILE_DAMAGE));
    }

    public void setMMOItemProjectileDamage(ItemStack itemStack, double projectileDamage) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.PROJECTILE_DAMAGE, new DoubleData(projectileDamage));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Physical Damage
    public String getMMOItemPhysicalDamage(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.PHYSICAL_DAMAGE));
    }

    public void setMMOItemPhysicalDamage(ItemStack itemStack, double physicalDamage) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.PHYSICAL_DAMAGE, new DoubleData(physicalDamage));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Defense
    public String getMMOItemDefense(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.DEFENSE));
    }

    public void setMMOItemDefense(ItemStack itemStack, double defense) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.DEFENSE, new DoubleData(defense));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }

    // Damage Reduction
    public String getMMOItemDamageReduction(ItemStack itemStack) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        return String.valueOf(mmoItem.getData(ItemStats.DAMAGE_REDUCTION));
    }

    public void setMMOItemDamageReduction(ItemStack itemStack, double damageReduction) {
        LiveMMOItem mmoItem = new LiveMMOItem(itemStack);
        mmoItem.setData(ItemStats.DAMAGE_REDUCTION, new DoubleData(damageReduction));

        ItemStackBuilder builder = new ItemStackBuilder(mmoItem);
        ItemStack newItemStack = builder.build();

        itemStack.setItemMeta(newItemStack.getItemMeta());
    }
}
