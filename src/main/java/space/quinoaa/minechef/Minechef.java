package space.quinoaa.minechef;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import space.quinoaa.minechef.client.init.ClientInit;
import space.quinoaa.minechef.client.init.ScreenInit;
import space.quinoaa.minechef.init.MinechefInit;
import space.quinoaa.minechef.init.MinechefRecipesInit;


@Mod(Minechef.MODID)
public class Minechef {
    public static final String MODID = "minechef";
    public static final Logger LOG = LogManager.getLogger(MODID);


    public Minechef() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        modEventBus.addListener(ClientInit::init);
        modEventBus.addListener(MinechefRecipesInit::initRecipe);

        MinechefInit.init();
    }


}
