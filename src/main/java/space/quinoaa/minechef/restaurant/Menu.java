package space.quinoaa.minechef.restaurant;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final Restaurant restaurant;
    public List<ItemStack> items = new ArrayList<>();

    public Menu(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public boolean isItemPresent(ItemStack item){
        for (ItemStack itemStack : items) {
            if(ItemStack.isSameItemSameTags(itemStack, item)) return true;
        }
        return false;
    }

    public static boolean isItemValid(ItemStack item){
        return item.isEdible();
    }

    public static int calculatePrice(ItemStack item){
        var prop = item.getFoodProperties(null);

        int effectPrice = 0;
        for (Pair<MobEffectInstance, Float> effect : prop.getEffects()) {
            var ins = effect.getFirst();
            int currentPrice = ins.getAmplifier() * Math.max(1, ins.getDuration() / 20 / 20);
            if(!ins.getEffect().isBeneficial()) currentPrice *= -1;

            effectPrice += currentPrice;
        }
        return (int) (prop.getNutrition() * Math.max(prop.getSaturationModifier() * 10, 1) + effectPrice * 40);
    }

    public void addItem(ItemStack item){
        if(isItemPresent(item)) return;
        if(!isItemValid(item)) return;

        items.add(item.copyWithCount(1));
        restaurant.queueTrackingUpdate(Restaurant.QueuedUpdate.MENU);
    }

    public void removeItem(int index){
        if(index >= items.size()) return;
        items.remove(index);
        restaurant.queueTrackingUpdate(Restaurant.QueuedUpdate.MENU);
    }

    public ItemStack getRandomMenuItem(){
        if(items.isEmpty()) return null;
        return items.get(((int) (items.size() * Math.random())) % items.size()).copy();
    }

    public CompoundTag save(){
        CompoundTag list = new CompoundTag();
        for (int i = 0; i < items.size(); i++) {
            CompoundTag tag = new CompoundTag();
            items.get(i).save(tag);
            list.put(Integer.toString(i), tag);
        }
        return list;
    }

    public void load(CompoundTag tag){
        this.items.clear();

        for (String key : tag.getAllKeys()) {
            items.add(ItemStack.of(tag.getCompound(key)));
        }
        restaurant.queuedTrackingUpdate.add(Restaurant.QueuedUpdate.MENU);
    }
}
