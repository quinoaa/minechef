package space.quinoaa.minechef.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;
import space.quinoaa.minechef.restaurant.worker.WorkerData;

import java.util.function.Supplier;

public class BoardHirePacket {
    WorkerData.Type type;

    public BoardHirePacket(WorkerData.Type type) {
        this.type = type;
    }

    public void encode(FriendlyByteBuf b) {
        b.writeEnum(type);
    }

    public static BoardHirePacket decode(FriendlyByteBuf b) {
        return new BoardHirePacket(b.readEnum(WorkerData.Type.class));
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isServer()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var plr = ctx.getSender();
            if(plr == null) return;
            if(!(plr.containerMenu instanceof RestaurantBoardMenu menu)) return;

            if(!menu.restaurant.hasFund(type.price)) return;
            menu.restaurant.addFund(-type.price);
            menu.restaurant.workers.addWorker(type);
        });
    }
}
