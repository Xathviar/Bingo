package github.xathviar.plugins.bingo;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BIngoData {

    public static HashMap<HumanEntity, ItemStack> hashmap;

    public BIngoData() {
        hashmap = new HashMap<>();
    }

    public ItemStack getItem(HumanEntity entity) {
        return hashmap.get(entity);
    }

    public void setItem(HumanEntity entity, ItemStack itemStack) {
        hashmap.put(entity, itemStack);
    }

}
