package space.quinoaa.minechef.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.block.entity.RestaurantBoardEntity;
import space.quinoaa.minechef.menu.RestaurantBoardMenu;
import space.quinoaa.minechef.restaurant.FundItem;

import java.util.function.Supplier;

public class WithdrawPacket {
    public final FundItem withdraw;
    public final int amount;

    public WithdrawPacket(FundItem withdraw, int amount) {
        this.withdraw = withdraw;
        this.amount = amount;
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum(withdraw);
        friendlyByteBuf.writeInt(amount);
    }

    public static WithdrawPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new WithdrawPacket(
                friendlyByteBuf.readEnum(FundItem.class),
                friendlyByteBuf.readInt()
        );
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        if(!ctx.get().getDirection().getReceptionSide().isServer()) return;
        if(amount < 0) return;

        ctx.get().setPacketHandled(true);
        ctx.get().enqueueWork(()->{
            var plr = ctx.get().getSender();
            if(plr == null) return;
            if(plr.containerMenu instanceof RestaurantBoardMenu menu){
                if(plr.level().getBlockEntity(menu.pos) instanceof RestaurantBoardEntity entity){
                    int value = amount * withdraw.value;

                    if(entity.restaurant.hasFund(value)) {
                        ItemStack item = new ItemStack(withdraw.item, amount);
                        if(amount > withdraw.item.getMaxStackSize(item)) return;

                        plr.addItem(item);
                        entity.restaurant.addFund(-withdraw.value * (amount - item.getCount()));
                    }
                }
            }
        });
    }



}
