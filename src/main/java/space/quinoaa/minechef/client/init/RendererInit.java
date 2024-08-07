package space.quinoaa.minechef.client.init;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import space.quinoaa.minechef.client.renderer.ClientEntityRenderer;
import space.quinoaa.minechef.client.renderer.WorkerEntityRenderer;
import space.quinoaa.minechef.init.MinechefEntity;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RendererInit {

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(MinechefEntity.CLIENT.get(), ClientEntityRenderer::new);
        event.registerEntityRenderer(MinechefEntity.COOK.get(), WorkerEntityRenderer::new);
        event.registerEntityRenderer(MinechefEntity.CASHIER.get(), WorkerEntityRenderer::new);
    }

}
