package space.quinoaa.minechef.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;

import java.util.function.Supplier;

public class BoardUpdateFundPacket {
    public final int amount;

    public BoardUpdateFundPacket(int amount) {
        this.amount = amount;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(amount);
    }

    public static BoardUpdateFundPacket decode(FriendlyByteBuf buf) {
        return new BoardUpdateFundPacket(buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        if(!context.get().getDirection().getReceptionSide().isClient()) return;

        context.get().setPacketHandled(true);
        context.get().enqueueWork(()->{
            var player = Minecraft.getInstance().player;
            if(player == null) return;

            if(player.containerMenu instanceof RestaurantBoardMenu menu){
                menu.restaurant.setFund(amount);
            }
        });
    }
}
