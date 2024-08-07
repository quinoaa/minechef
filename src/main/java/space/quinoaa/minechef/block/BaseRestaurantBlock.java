package space.quinoaa.minechef.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.block.entity.BaseRestaurantBlockEntity;
import space.quinoaa.minechef.block.entity.RestaurantBoardEntity;
import space.quinoaa.minechef.init.MinechefCapabilities;

public abstract class BaseRestaurantBlock extends BaseEntityBlock {
    public static int MAX_DISTANCE = 64;

    public BaseRestaurantBlock(Properties pProperties) {
        super(pProperties);
    }





    public BlockItem createBlockItem(){
        return new BlockItem(this, new Item.Properties()){

            @Override
            protected boolean placeBlock(BlockPlaceContext ctx, BlockState state) {
                var plr = ctx.getPlayer();
                if(plr == null) return false;

                if(!plr.level().isClientSide){
                    ServerPlayer srvPlr = (ServerPlayer) plr;
                    ServerLevel lvl = srvPlr.serverLevel();
                    var opt = plr.getCapability(MinechefCapabilities.RESTAURANT).resolve();
                    if(opt.isEmpty()) return false;

                    var restaurantPos = opt.get().getSelectedRestaurant();

                    if(restaurantPos == null || !(lvl.getBlockEntity(restaurantPos) instanceof RestaurantBoardEntity cast)){
                        srvPlr.sendSystemMessage(Component.translatable("minechef.chat.no_selected_restaurant").withStyle(ChatFormatting.RED));
                        srvPlr.inventoryMenu.sendAllDataToRemote();
                        return false;
                    }

                    if(restaurantPos.distSqr(ctx.getClickedPos()) > MAX_DISTANCE * MAX_DISTANCE){
                        srvPlr.sendSystemMessage(Component.translatable("minechef.chat.to_far_away").withStyle(ChatFormatting.RED));
                        srvPlr.inventoryMenu.sendAllDataToRemote();
                        return false;
                    }
                    var result = super.placeBlock(ctx, state);

                    if(result){
                        var blockEntity = lvl.getBlockEntity(ctx.getClickedPos());
                        if(blockEntity instanceof BaseRestaurantBlockEntity entity){
                            entity.setRestaurantPos(restaurantPos);
                            cast.restaurant.blocks.addBlock(ctx.getClickedPos());
                        }
                    }

                    return result;
                }else{
                    return super.placeBlock(ctx, state);
                }
            }
        };
    }
}
