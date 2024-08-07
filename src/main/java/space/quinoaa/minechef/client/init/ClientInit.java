package space.quinoaa.minechef.client.init;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientInit {
    public static void init(FMLClientSetupEvent event) {
        ScreenInit.initClient(event);
        //RendererInit.initClient(event);
    }

}
