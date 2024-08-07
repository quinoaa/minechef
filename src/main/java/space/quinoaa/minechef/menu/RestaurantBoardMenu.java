package space.quinoaa.minechef.menu;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.block.entity.RestaurantBoardEntity;
import space.quinoaa.minechef.init.MinechefMenus;
import space.quinoaa.minechef.restaurant.FundItem;
import space.quinoaa.minechef.restaurant.Menu;
import space.quinoaa.minechef.restaurant.Restaurant;

import java.util.function.IntConsumer;

public class RestaurantBoardMenu extends AbstractContainerMenu {
    public final BlockPos pos;

    public final DepositSlot depositSlot;
    public final FoodSlot foodSlot;

    public final Restaurant restaurant;

    public RestaurantBoardMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        this(pContainerId, inventory, buf.readBlockPos(), new Restaurant(null).load(buf.readNbt()));
    }

    public RestaurantBoardMenu(int pContainerId, Inventory inventory, BlockPos pos, Restaurant restaurant) {
        super(MinechefMenus.RESTAURANT_BOARD.get(), pContainerId);

        this.pos = pos;

        this.restaurant = restaurant;
        depositSlot = new DepositSlot(152, 88);
        foodSlot = new FoodSlot(8, 87);
        addSlot(depositSlot);
        addSlot(foodSlot);

        int offset = 116;
        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, offset + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, offset + 58));
        }
    }



    public @Nullable RestaurantBoardEntity getBlockEntity(Level level){
        if(level.getBlockEntity(pos) instanceof RestaurantBoardEntity entity) return entity;
        return null;
    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        Slot slot = getSlot(pIndex);
        if(slot != depositSlot && depositSlot.active){
            var item = slot.getItem();
            depositSlot.safeInsert(item);
        }
        if(slot != foodSlot && foodSlot.active){
            var item = slot.getItem();
            foodSlot.safeInsert(item);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pos.distToCenterSqr(pPlayer.position()) < 16 * 16;
    }

    public static void open(ServerPlayer player, BlockPos pos){
        var entity = player.level().getBlockEntity(pos);
        if(!(entity instanceof RestaurantBoardEntity cast)) return;
        NetworkHooks.openScreen(
                player,
                new SimpleMenuProvider(((id, inv, plr) -> new RestaurantBoardMenu(id, inv, pos, cast.restaurant)),
                Component.translatable("minechef.screen.board.title")),
                buf->{
                        buf.writeBlockPos(pos);
                        buf.writeNbt(cast.restaurant.save());
                }
        );
        cast.startTrackingMenu(player);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
    }

    public class DepositSlot extends Slot{
        public boolean active = true;

        public DepositSlot(int pX, int pY) {
            super(new SimpleContainer(1), 0, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return FundItem.canDeposit(pStack);
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public ItemStack safeInsert(ItemStack pStack, int pIncrement) {
            if(!mayPlace(pStack)) return pStack;

            restaurant.addFund(FundItem.getDepositValue(pStack, pIncrement));
            pStack.shrink(pIncrement);

            return pStack;
        }

    }

    public class FoodSlot extends Slot{
        public boolean active = true;
        public Runnable changeListener = null;

        public FoodSlot(int pX, int pY) {
            super(new SimpleContainer(1), 0, pX, pY);
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return Menu.isItemValid(pStack);
        }

        public void setChangeListener(Runnable listener){
            this.changeListener = listener;
        }


        @Override
        public ItemStack safeInsert(ItemStack pStack, int pIncrement) {
            restaurant.menu.addItem(pStack.copyWithCount(1));
            if(changeListener != null) changeListener.run();
            return pStack;
        }
    }
}
