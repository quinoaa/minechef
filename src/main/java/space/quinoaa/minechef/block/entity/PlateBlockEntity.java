package space.quinoaa.minechef.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import space.quinoaa.minechef.init.MinechefBlockEntities;

public class PlateBlockEntity extends BaseRestaurantBlockEntity implements Container {
    public static final int CONTAINER_SIZE = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    public PlateBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MinechefBlockEntities.PLATE.get(), pPos, pBlockState);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        CompoundTag content = new CompoundTag();
        ContainerHelper.saveAllItems(content, items);
        pTag.put("items", content);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        items.clear();
        if(pTag.contains("items", Tag.TAG_COMPOUND)) ContainerHelper.loadAllItems(pTag.getCompound("items"), items);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return items.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ContainerHelper.removeItem(items, pSlot, pAmount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return items.remove(pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        items.set(pSlot, pStack);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.distanceToSqr(this.getBlockPos().getCenter()) < 16*16;
    }

    @Override
    public void clearContent() {
        items.clear();
    }
}
