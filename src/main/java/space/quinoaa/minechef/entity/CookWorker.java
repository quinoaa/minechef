package space.quinoaa.minechef.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.init.MinechefBlocks;
import space.quinoaa.minechef.init.MinechefEntity;
import space.quinoaa.minechef.restaurant.Restaurant;
import space.quinoaa.minechef.restaurant.worker.WorkerData;

import java.util.*;

public class CookWorker extends BaseWorkerEntity {
    public CookWorker(EntityType<? extends CookWorker> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, null, null);
    }

    public CookWorker(Level pLevel, @Nullable BlockPos restaurantPos, @Nullable WorkerData data) {
        super(MinechefEntity.COOK.get(), pLevel, restaurantPos, data);
    }

    private List<Action> actions = new ArrayList<>();

    @Override
    public void serverTick(Restaurant restaurant) {
        if(!actions.isEmpty()){
            if(!actions.get(0).handle()){
                actions.remove(0);
            }
            return;
        }

        if(restaurant.foodQueue.isEmpty()) {
            var loc = restaurant.blocks.getFirstBlock(MinechefBlocks.PLATE.get(), null);

            if(loc != null) handleMove(loc);
            return;
        }
        var food = restaurant.foodQueue.remove(0);
        var recipe = getRecipe(food);
        if(recipe == null) return;

        List<ItemStack> ingredients = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredients.addAll(Arrays.asList(ingredient.getItems()));
        }

        for (ItemStack ingredient : ingredients) {
            actions.add(()-> {
                var rest = getRestaurant();
                if(rest == null) return false;
                return !queryFridge(rest, ingredient);
            });
        }

        var result = food.copyWithCount(1);
        actions.add(()->{
            var rest = getRestaurant();
            if(rest == null) return true;

            BlockPos buildTarget;
            if(recipe.getType() == RecipeType.SMELTING){
                buildTarget = rest.blocks.getFirstBlock(MinechefBlocks.COOKER.get(), null);
            }else{
                buildTarget = rest.blocks.getFirstBlock(MinechefBlocks.FOOD_WORKBENCH.get(), null);
            }
            if(buildTarget == null) return true;

            if(handleMove(buildTarget)){
                setItemInHand(InteractionHand.MAIN_HAND, result.copyWithCount(1));
                return false;
            }
            return true;
        });

        actions.add(()->{
            var loc = restaurant.blocks.getFirstBlock(MinechefBlocks.PLATE.get(), (level, pos)->{
                if(level.getBlockEntity(pos) instanceof Container container){
                    return container.hasAnyMatching(ItemStack::isEmpty);
                }
                return false;
            });
            if(loc == null) return true;
            if(!handleMove(loc)) return true;

            if(level().getBlockEntity(loc) instanceof Container container){
                for (int i = 0; i < container.getContainerSize(); i++) {
                    if(container.getItem(i).isEmpty()){
                        container.setItem(i, result);
                        setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        return false;
                    }
                }
            }
            return true;
        });
    }

    private boolean queryFridge(Restaurant restaurant, ItemStack required){
        var loc = restaurant.blocks.getFirstBlock(MinechefBlocks.FRIDGE.get(), (level, pos)->{
            if(level.getBlockEntity(pos) instanceof Container container){
                return container.hasAnyMatching(is->ItemStack.isSameItemSameTags(is, required));
            }
            return false;
        });

        if(loc == null) return false;
        if(handleMove(loc)){
            if(level().getBlockEntity(loc) instanceof Container container){
                for (int i = 0; i < container.getContainerSize(); i++) {
                    var item = container.getItem(i);
                    if(ItemStack.isSameItemSameTags(item, required)){
                        setItemInHand(InteractionHand.MAIN_HAND, item.copyWithCount(1));
                        item.shrink(1);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Map<CompoundTag, Recipe<?>> recipes = new HashMap<>();
    private @Nullable Recipe<?> getRecipe(ItemStack target){
        CompoundTag tag = target.serializeNBT();
        if(recipes.containsKey(tag)) return recipes.get(tag);

        Recipe<?> correctRecipe = null;
        for (Recipe<?> recipe : level().getRecipeManager().getRecipes()) {
            if(recipe.getType() != RecipeType.CRAFTING && recipe.getType() != RecipeType.SMELTING) continue;
            if(!ItemStack.isSameItemSameTags(recipe.getResultItem(level().registryAccess()), target)) continue;

            correctRecipe = recipe;
        }
        recipes.put(tag, correctRecipe);
        return correctRecipe;
    }

    interface Action{
        boolean handle();
    }
}
