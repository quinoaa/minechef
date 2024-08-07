package space.quinoaa.minechef.restaurant;

import net.minecraft.world.item.ItemStack;
import space.quinoaa.minechef.entity.CookWorker;

public class FoodRequest {
    public final ItemStack item;
    public boolean finished = false;
    public CookWorker handler = null;

    public FoodRequest(ItemStack item) {
        this.item = item;
    }

    public boolean isStillValid(){
        if(finished) return false;
        if(handler == null) return true;
        return !handler.isRemoved();
    }


}
