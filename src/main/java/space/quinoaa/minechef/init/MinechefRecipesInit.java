package space.quinoaa.minechef.init;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import space.quinoaa.minechef.Minechef;

import java.util.function.Consumer;

public class MinechefRecipesInit {

    public static void initRecipe(GatherDataEvent event){
        Minechef.LOG.info("REGISTERING RECIPES");
        event.getGenerator().addProvider(event.includeServer(), (DataProvider.Factory<DataProvider>) Provider::new);
    }


    public static class Provider extends RecipeProvider {

        public Provider(PackOutput pOutput) {
            super(pOutput);
        }

        @Override
        protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
            getBase(MinechefItems.CASH_REGISTER.get())
                    .define('i', Items.IRON_INGOT)
                    .define('d', Items.DIAMOND)
                    .define('s', Items.STONE)
                    .define('r', Items.REDSTONE)
                    .pattern("idi")
                    .pattern("iri")
                    .pattern("isi")
                    .save(pWriter);
            getBase(MinechefItems.COOKER.get())
                    .define('i', Items.IRON_INGOT)
                    .define('c', Items.COAL)
                    .define('r', Items.REDSTONE)
                    .pattern("iii")
                    .pattern("iri")
                    .pattern("ici")
                    .save(pWriter);
            getBase(MinechefItems.FOOD_WORKBENCH.get())
                    .define('i', Items.IRON_INGOT)
                    .define('w', Items.CRAFTING_TABLE)
                    .define('s', Items.STONE)
                    .pattern("iii")
                    .pattern("isi")
                    .pattern("iwi")
                    .save(pWriter);
            getBase(MinechefItems.FRIDGE.get())
                    .define('i', Items.IRON_INGOT)
                    .define('b', Items.CHEST)
                    .define('r', Items.REDSTONE)
                    .pattern("iii")
                    .pattern("iri")
                    .pattern("ibi")
                    .save(pWriter);
            getBase(MinechefItems.PLATE.get())
                    .define('i', Items.IRON_INGOT)
                    .pattern("iii")
                    .save(pWriter);
            getBase(MinechefItems.RESTAURANT_BOARD.get())
                    .define('i', Items.IRON_INGOT)
                    .define('d', Items.DIAMOND)
                    .define('g', Items.GLASS)
                    .define('s', Items.STONE)
                    .pattern("igi")
                    .pattern("idi")
                    .pattern("isi")
                    .save(pWriter);
            getBase(MinechefItems.RESTAURANT_PORTAL.get())
                    .define('i', Items.IRON_INGOT)
                    .define('d', Items.DIAMOND)
                    .define('e', Items.ENDER_PEARL)
                    .define('r', Items.REDSTONE)
                    .pattern("iei")
                    .pattern("iri")
                    .pattern("idi")
                    .save(pWriter);
        }

        private ShapedRecipeBuilder getBase(Item item){
            return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, item)
                    .unlockedBy("unlock", RecipeUnlockedTrigger.unlocked(ForgeRegistries.ITEMS.getKey(item)));
        }
    }
}
