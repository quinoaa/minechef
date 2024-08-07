package space.quinoaa.minechef.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.block.entity.FridgeBlockEntity;
import space.quinoaa.minechef.block.entity.PlateBlockEntity;

public class PlateBlock extends BaseRestaurantBlock {

    public PlateBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PlateBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pLevel.isClientSide) return InteractionResult.SUCCESS;

        if(pLevel.getBlockEntity(pPos) instanceof PlateBlockEntity entity){
            pPlayer.openMenu(new SimpleMenuProvider(((pContainerId, pPlayerInventory, pPlayer1) -> {
                return new ChestMenu(MenuType.GENERIC_9x1, pContainerId, pPlayerInventory, entity, 1);
            }), Component.literal("Plate")));
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
