package space.quinoaa.minechef.init;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.block.entity.*;

import java.util.Set;

public class MinechefBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Minechef.MODID);

    public static final RegistryObject<BlockEntityType<BlockEntity>> RESTAURANT_BOARD = REGISTRY.register("restaurant_board", ()->new BlockEntityType<>(RestaurantBoardEntity::new, Set.of(MinechefBlocks.RESTAURANT_BOARD.get()), null));
    public static final RegistryObject<BlockEntityType<BlockEntity>> BASE_RESTAURANT_BLOCK = REGISTRY.register("base_restaurant_block", ()->new BlockEntityType<>((pos, state)->new BaseRestaurantBlockEntity(MinechefBlockEntities.RESTAURANT_BOARD.get(), pos, state), Set.of(
            MinechefBlocks.RESTAURANT_PORTAL.get(),
            MinechefBlocks.FOOD_WORKBENCH.get(),
            MinechefBlocks.COOKER.get()
    ), null));
    public static final RegistryObject<BlockEntityType<CashRegisterEntity>> CASH_REGISTER = REGISTRY.register("cash_register", ()->new BlockEntityType<>(CashRegisterEntity::new, Set.of(MinechefBlocks.CASH_REGISTER.get()), null));
    public static final RegistryObject<BlockEntityType<PlateBlockEntity>> PLATE = REGISTRY.register("plate", ()->new BlockEntityType<>(PlateBlockEntity::new, Set.of(MinechefBlocks.PLATE.get()), null));
    public static final RegistryObject<BlockEntityType<FridgeBlockEntity>> FRIDGE = REGISTRY.register("fridge", ()->new BlockEntityType<>(FridgeBlockEntity::new, Set.of(MinechefBlocks.FRIDGE.get()), null));

    public static void init() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
