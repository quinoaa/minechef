package space.quinoaa.minechef.restaurant.worker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import space.quinoaa.minechef.entity.BaseWorkerEntity;
import space.quinoaa.minechef.entity.CashierWorker;
import space.quinoaa.minechef.entity.CookWorker;
import space.quinoaa.minechef.init.MinechefBlocks;
import space.quinoaa.minechef.restaurant.Restaurant;

public class WorkerData {
    public Type type;

    @Nullable
    public BaseWorkerEntity entity = null;
    @Nullable
    private Vec3 lastPos = null;
    public boolean discarded = false;

    public int skinId = (int) (Math.random() * Integer.MAX_VALUE);

    public WorkerData(Type type) {
        this.type = type;
    }

    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();

        tag.putString("type", type.name());
        if(lastPos != null) {
            CompoundTag pos = new CompoundTag();
            pos.putDouble("x", lastPos.x());
            pos.putDouble("y", lastPos.y());
            pos.putDouble("z", lastPos.z());
            tag.put("position", pos);
        }

        return tag;
    }

    public void discard(){
        discarded = true;
        if(entity != null) entity.discard();
    }

    public void load(CompoundTag tag){
        type = Type.valueOf(tag.getString("type"));

        lastPos = null;
        if(tag.contains("position", Tag.TAG_COMPOUND)){
            var com = tag.getCompound("position");
            lastPos = new Vec3(com.getDouble("x"), com.getDouble("y"), com.getDouble("z"));
        }
    }

    public void tick(Restaurant restaurant) {
        if(restaurant.level == null) return;

        if(entity == null || entity.isRemoved()){
            var pos = restaurant.blocks.getFirstBlock(MinechefBlocks.RESTAURANT_PORTAL.get(), null);
            if(pos == null) return;
            entity = switch (type){
                case COOK -> new CookWorker(restaurant.level, restaurant.position, this);
                case CASHIER -> new CashierWorker(restaurant.level, restaurant.position, this);
            };
            entity.setPos(pos.above().getCenter());
            restaurant.level.addFreshEntity(entity);
        }
    }

    public enum Type{
        CASHIER(Component.translatable("minechef.worker.type.cashier"), 1000),
        COOK(Component.translatable("minechef.worker.type.cook"), 1600),

        ;
        public final Component name;
        public final int price;

        Type(Component name, int price) {
            this.name = name;
            this.price = price;
        }
    }
}
