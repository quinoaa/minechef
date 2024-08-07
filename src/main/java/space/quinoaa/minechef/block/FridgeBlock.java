package space.quinoaa.minechef.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.block.entity.FridgeBlockEntity;

public class FridgeBlock extends RestaurantDirectionalBlock {
    public FridgeBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FridgeBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide) return InteractionResult.SUCCESS;

        if(pLevel.getBlockEntity(pPos) instanceof FridgeBlockEntity entity){
            pPlayer.openMenu(new SimpleMenuProvider(((pContainerId, pPlayerInventory, pPlayer1) -> {
                return ChestMenu.threeRows(pContainerId, pPlayerInventory, entity);
            }), Component.literal("Fridge")));
        }

        return InteractionResult.CONSUME;
    }
}
