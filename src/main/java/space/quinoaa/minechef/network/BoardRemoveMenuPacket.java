package space.quinoaa.minechef.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;

import java.util.function.Supplier;

public class BoardRemoveMenuPacket {
    public final int index;

    public BoardRemoveMenuPacket(int index) {
        this.index = index;
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(index);
    }

    public static BoardRemoveMenuPacket decode(FriendlyByteBuf buf) {
        return new BoardRemoveMenuPacket(buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isServer()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var plr = ctx.getSender();
            if(plr == null) return;
            if(!(plr.containerMenu instanceof RestaurantBoardMenu menu)) return;

            menu.restaurant.menu.removeItem(index);
        });
    }
}
