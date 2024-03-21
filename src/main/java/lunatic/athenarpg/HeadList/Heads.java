package lunatic.athenarpg.HeadList;

import lunatic.athenarpg.Main;
import org.bukkit.inventory.ItemStack;

public enum Heads {
    dragonLaser("MjBkZTVlODk3NDk0MDM3NTkzNGQzMmY3MWM5MWFkMmQ1NzI4ZDM4ZTUxNjQ3ZGNjOGYzOTIwNmMwOTlhNTRjMiJ9fX0=", "dragonLaser"),
    SPIRIT("ZTY3OTkxOGU1MmYzZjhmMmNhYmJiZWFjNmE5NzY4MWYyZjhhYTEwYzBiMmU4MTg1OTI4ODVhNGEwZTlkMjI3In19fQ==", "spirit"),
    dragonQuiver("Nzg1NmNmOTcxNjIzOWE3NzA2MjY4NjUyZWZmM2IyOWNlNWRhY2RmYWIxZmIyZmIzMGE1NGIwNzk2NzQwMDYyIn19fQ==", "dragonQuiver"),
    StarvingOrb("NmIxY2MwYTNjMWE0NzYzZDhjN2FlYzU0ODM0NGRhYTI5M2VmODYzYjNiNGZkYzVlNTkyYTkzZTA3NTBlZmY2NSJ9fX0=", "starvingorb");

    private final ItemStack item;
    private final String idTag;
    private final String url;
    public String prefix = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";

    private Heads(String texture, String id) {
        this.item = Main.createSkull(this.prefix + texture, id);
        this.idTag = id;
        this.url = this.prefix + texture;
    }

    public String getUrl() {
        return this.url;
    }

    public ItemStack getItemStack() {
        return this.item;
    }

    public String getName() {
        return this.idTag;
    }
}
