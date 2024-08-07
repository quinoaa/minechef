package space.quinoaa.minechef.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.block.*;

public class MinechefBlocks {
    private static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, Minechef.MODID);

    public static final RegistryObject<RestaurantBoard> RESTAURANT_BOARD = REGISTRY.register("restaurant_board", ()-> new RestaurantBoard(BlockBehaviour.Properties.of().strength(2.0F, 3600000.0F).noOcclusion()));
    public static final RegistryObject<CashRegister> CASH_REGISTER = REGISTRY.register("cash_register", ()-> new CashRegister(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<RestaurantPortal> RESTAURANT_PORTAL = REGISTRY.register("restaurant_portal", ()->new RestaurantPortal(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<RestaurantDirectionalBlock> FOOD_WORKBENCH = REGISTRY.register("food_workbench", ()->new RestaurantDirectionalBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<RestaurantDirectionalBlock> COOKER = REGISTRY.register("cooker", ()->new RestaurantDirectionalBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<PlateBlock> PLATE = REGISTRY.register("plate", ()->new PlateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
    public static final RegistryObject<FridgeBlock> FRIDGE = REGISTRY.register("fridge", ()->new FridgeBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static void init() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
