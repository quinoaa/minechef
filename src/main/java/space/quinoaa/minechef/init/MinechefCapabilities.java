package space.quinoaa.minechef.init;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import space.quinoaa.minechef.capability.IRestaurantHandler;

public class MinechefCapabilities {
    public static final Capability<IRestaurantHandler> RESTAURANT = CapabilityManager.get(new CapabilityToken<IRestaurantHandler>() {});


}
