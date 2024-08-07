package space.quinoaa.minechef.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;
import space.quinoaa.minechef.restaurant.worker.WorkerData;

import java.util.function.Supplier;

public class BoardFirePacket {
    WorkerData.Type type;

    public BoardFirePacket(WorkerData.Type type) {
        this.type = type;
    }

    public void encode(FriendlyByteBuf b) {
        b.writeEnum(type);
    }

    public static BoardFirePacket decode(FriendlyByteBuf b) {
        return new BoardFirePacket(b.readEnum(WorkerData.Type.class));
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isServer()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var plr = ctx.getSender();
            if(plr == null) return;
            if(!(plr.containerMenu instanceof RestaurantBoardMenu menu)) return;

            menu.restaurant.workers.removeWorkerOf(type);
        });
    }
}
