package space.quinoaa.minechef.capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.init.MinechefCapabilities;

@Mod.EventBusSubscriber(modid = Minechef.MODID)
public class CapabilityAttacher {

    @SubscribeEvent
    static void attachCapabilities(AttachCapabilitiesEvent<Entity> event){
        if(!(event.getObject() instanceof Player plr)) return;

        RestaurantCapabilityProvider provider = new RestaurantCapabilityProvider();

        event.addCapability(RestaurantCapabilityProvider.KEY, provider);
    }

    @SubscribeEvent
    static void clone(PlayerEvent.Clone event){
        if(!event.isWasDeath()) return;

        var originalCap = event.getOriginal().getCapability(MinechefCapabilities.RESTAURANT).resolve().orElse(null);
        if(originalCap == null) return;

        var newCap = event.getEntity().getCapability(MinechefCapabilities.RESTAURANT).resolve().orElse(null);
        if(newCap == null) return;

        newCap.copyFrom(originalCap);
    }
}
