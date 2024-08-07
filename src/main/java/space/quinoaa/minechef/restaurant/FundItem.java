package space.quinoaa.minechef.restaurant;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.Minechef;

import java.util.HashMap;
import java.util.Map;

public enum FundItem {
    IRON(1, Items.IRON_INGOT),
    GOLD(5, Items.GOLD_INGOT),
    DIAMOND(20, Items.DIAMOND),

    ;
    private static final Map<Item, FundItem> items;
    public final int value;
    public final Item item;

    FundItem(int value, Item item) {
        this.value = value;
        this.item = item;
    }

    static {
        items = new HashMap<>();
        for (FundItem value : values()) {
            items.put(value.item, value);
        }
    }

    public static @Nullable FundItem getFromStack(ItemStack stack){
        return items.get(stack.getItem());
    }

    public static boolean canDeposit(ItemStack stack){
        return getFromStack(stack) != null;
    }

    public static int getDepositValue(ItemStack stack, int amount){
        var item = getFromStack(stack);

        if(item == null) return 0;
        return item.value * amount;
    }
}
