package space.quinoaa.minechef.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;
import space.quinoaa.minechef.restaurant.Menu;
import space.quinoaa.minechef.restaurant.Restaurant;

import java.util.function.Supplier;

public class BoardUpdateMenuPacket {
    private final CompoundTag tag;

    public BoardUpdateMenuPacket(Menu menu) {
        tag = menu.save();
    }

    public BoardUpdateMenuPacket(CompoundTag tag) {
        this.tag = tag;
    }

    public void loadTo(Restaurant restaurant){
        restaurant.menu.load(tag);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(tag);
    }

    public static BoardUpdateMenuPacket decode(FriendlyByteBuf buf) {
        return new BoardUpdateMenuPacket(buf.readNbt());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isClient()) return;

        ctx.setPacketHandled(true);
        ctx.enqueueWork(()->{
            var plr = Minecraft.getInstance().player;
            if(plr == null) return;

            if(!(plr.containerMenu instanceof RestaurantBoardMenu menu)) return;
            loadTo(menu.restaurant);
        });
    }
}
