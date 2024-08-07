package space.quinoaa.minechef.init;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.menu.CashRegisterMenu;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;

public class MinechefMenus {
    private static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Minechef.MODID);

    public static final RegistryObject<MenuType<RestaurantBoardMenu>> RESTAURANT_BOARD = REGISTRY.register("restaurant_board", ()-> IForgeMenuType.create(RestaurantBoardMenu::new));
    public static final RegistryObject<MenuType<CashRegisterMenu>> CASH_REGISTER = REGISTRY.register("cash_register", ()->IForgeMenuType.create(CashRegisterMenu::new));


    public static void init() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
