package space.quinoaa.minechef.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.minechef.init.MinechefCapabilities;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;

import java.util.function.Supplier;

public class BoardSelectRestaurantPacket {
    public void encode(FriendlyByteBuf friendlyByteBuf) {
    }

    public static BoardSelectRestaurantPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new BoardSelectRestaurantPacket();
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isServer()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var plr = ctx.getSender();

            if(plr == null) return;
            if(!(plr.containerMenu instanceof RestaurantBoardMenu menu)) return;

            var cap = plr.getCapability(MinechefCapabilities.RESTAURANT).resolve();
            if(cap.isEmpty()) return;

            cap.get().setSelectedRestaurant(menu.pos);
        });
    }
}
