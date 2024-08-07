package space.quinoaa.minechef.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.menu.CashRegisterMenu;

import java.util.function.Supplier;

public class CashRegisterSetRequestPacket {
    public ItemStack item = null;

    public CashRegisterSetRequestPacket(@Nullable ItemStack item) {
        this.item = item;
    }



    public void encode(FriendlyByteBuf b) {
        b.writeBoolean(item != null);
        if(item != null) b.writeItem(item);
    }

    public static CashRegisterSetRequestPacket decode(FriendlyByteBuf b) {
        if(b.readBoolean()) return new CashRegisterSetRequestPacket(b.readItem());
        return new CashRegisterSetRequestPacket(null);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var ctx = supplier.get();
        if(!ctx.getDirection().getReceptionSide().isClient()) return;
        ctx.setPacketHandled(true);

        ctx.enqueueWork(()->{
            var plr = Minecraft.getInstance().player;
            if(plr == null || !(plr.containerMenu instanceof CashRegisterMenu menu)) return;

            menu.required = item != null ? item.copy() : null;
            menu.changed = true;
        });
    }
}
