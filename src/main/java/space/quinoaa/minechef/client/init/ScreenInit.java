package space.quinoaa.minechef.client.init;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import space.quinoaa.minechef.client.screen.BoardScreen;
import space.quinoaa.minechef.client.screen.CashRegisterScreen;
import space.quinoaa.minechef.init.MinechefMenus;
import space.quinoaa.minechef.menu.CashRegisterMenu;

public class ScreenInit {



    @Subscribe
    public static void initClient(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            MenuScreens.register(MinechefMenus.RESTAURANT_BOARD.get(), BoardScreen::new);
            MenuScreens.register(MinechefMenus.CASH_REGISTER.get(), CashRegisterScreen::new);
        });
    }
}
