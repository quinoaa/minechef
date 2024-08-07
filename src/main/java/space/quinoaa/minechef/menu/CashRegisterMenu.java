package space.quinoaa.minechef.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.block.entity.CashRegisterEntity;
import space.quinoaa.minechef.block.entity.RestaurantBoardEntity;
import space.quinoaa.minechef.init.MinechefMenus;

public class CashRegisterMenu extends AbstractContainerMenu {
    public ItemStack required = null;
    public boolean changed = false;
    public final BlockPos pos;
    public final AcceptSlot acceptSlot;
    private final Inventory inv;

    public CashRegisterMenu(int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
        this(pContainerId, inventory, buf.readBoolean() ? buf.readItem() : null, buf.readBlockPos());
    }

    public CashRegisterMenu(int pContainerId, Inventory inventory, ItemStack required, BlockPos pos) {
        super(MinechefMenus.CASH_REGISTER.get(), pContainerId);
        this.inv = inventory;
        this.required = required;
        this.pos = pos;

        acceptSlot = new AcceptSlot(134, 45);
        addSlot(acceptSlot);

        int offset = 135 - 58;
        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(inventory, i1 + k * 9 + 9, 8 + i1 * 18, offset + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, offset + 58));
        }

    }

    public static void open(ServerPlayer player, BlockPos pos){
        var entity = player.level().getBlockEntity(pos);
        if(!(entity instanceof CashRegisterEntity cast)) return;
        NetworkHooks.openScreen(
                player,
                new SimpleMenuProvider(((id, inv, plr) -> new CashRegisterMenu(id, inv, cast.currentRequest != null ? cast.currentRequest.copy() : null, pos)),
                        Component.translatable("minechef.screen.board.title")),
                buf->{
                    buf.writeBoolean(cast.currentRequest != null);
                    if(cast.currentRequest != null) buf.writeItem(cast.currentRequest);
                    buf.writeBlockPos(pos);
                }
        );
        cast.startTrackingMenu(player);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        var slot = slots.get(pIndex);
        if(slot != acceptSlot){
            acceptSlot.safeInsert(slot.getItem());
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    private @Nullable CashRegisterEntity getRegister(){
        var level = inv.player.level();
        if(level.isClientSide) return null;

        if(!(level.getBlockEntity(pos)instanceof CashRegisterEntity entity)) return null;
        return entity;
    }

    public class AcceptSlot extends Slot{

        public AcceptSlot(int pX, int pY) {
            super(new SimpleContainer(1), 0, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            var register = getRegister();
            if(register == null || register.currentRequest == null) return false;
            return ItemStack.isSameItemSameTags(register.currentRequest, pStack);
        }

        @Override
        public ItemStack safeInsert(ItemStack pStack, int pIncrement) {
            if(!mayPlace(pStack)) return ItemStack.EMPTY;
            if(inv.player.level().isClientSide) return ItemStack.EMPTY;

            var register = getRegister();
            if(register == null) return ItemStack.EMPTY;

            if(mayPlace(pStack)) {
                if(register.fillRequest()) pStack.shrink(1);
            }

            return pStack;
        }
    }
}
