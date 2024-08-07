package space.quinoaa.minechef.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import space.quinoaa.minechef.Minechef;

public class MinechefItems {
    private static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Minechef.MODID);

    public static final RegistryObject<BlockItem> RESTAURANT_BOARD = REGISTRY.register("restaurant_board", () -> new BlockItem(MinechefBlocks.RESTAURANT_BOARD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CASH_REGISTER = REGISTRY.register("cash_register", ()->MinechefBlocks.CASH_REGISTER.get().createBlockItem());
    public static final RegistryObject<BlockItem> RESTAURANT_PORTAL = REGISTRY.register("restaurant_portal", ()->MinechefBlocks.RESTAURANT_PORTAL.get().createBlockItem());
    public static final RegistryObject<BlockItem> COOKER = REGISTRY.register("cooker", ()->MinechefBlocks.COOKER.get().createBlockItem());
    public static final RegistryObject<BlockItem> FOOD_WORKBENCH = REGISTRY.register("food_workbench", ()->MinechefBlocks.FOOD_WORKBENCH.get().createBlockItem());
    public static final RegistryObject<BlockItem> PLATE = REGISTRY.register("plate", ()->MinechefBlocks.PLATE.get().createBlockItem());
    public static final RegistryObject<BlockItem> FRIDGE = REGISTRY.register("fridge", ()->MinechefBlocks.FRIDGE.get().createBlockItem());


    public static void init() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

        var TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Minechef.MODID);
        TAB.register("minechef_items", ()-> CreativeModeTab.builder()
                .title(Component.translatable("minechef.tab.title"))
                .icon(()->new ItemStack(RESTAURANT_BOARD.get()))
                .displayItems((options, output)->{
                    for (RegistryObject<Item> entry : REGISTRY.getEntries()) {
                        output.accept(new ItemStack(entry.get()));
                    }
                })
                .build());
        TAB.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
