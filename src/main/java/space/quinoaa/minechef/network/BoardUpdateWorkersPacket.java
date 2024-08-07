package space.quinoaa.minechef.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;
import space.quinoaa.minechef.restaurant.worker.WorkerManager;

import java.util.function.Supplier;

public class BoardUpdateWorkersPacket {
    private CompoundTag tag;

    public BoardUpdateWorkersPacket(WorkerManager workers) {
        tag = workers.save();
    }

    public BoardUpdateWorkersPacket(CompoundTag compoundTag) {
        tag = compoundTag;
    }

    public void encode(FriendlyByteBuf b) {
        b.writeNbt(tag);
    }

    public static BoardUpdateWorkersPacket decode(FriendlyByteBuf b) {
        return new BoardUpdateWorkersPacket(b.readNbt());
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isClient()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var plr = Minecraft.getInstance().player;
            if(plr == null) return;
            if(!(plr.containerMenu instanceof RestaurantBoardMenu menu)) return;

            menu.restaurant.workers.load(tag);
        });
    }
}
