package space.quinoaa.minechef.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.entity.CashierWorker;
import space.quinoaa.minechef.entity.ClientEntity;
import space.quinoaa.minechef.entity.CookWorker;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MinechefEntity {
    private static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Minechef.MODID);

    public static final RegistryObject<EntityType<ClientEntity>> CLIENT = REGISTRY.register("client", ()->EntityType.Builder.<ClientEntity>of(ClientEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).noSave().clientTrackingRange(32).updateInterval(2).build("client"));
    public static final RegistryObject<EntityType<CashierWorker>> CASHIER = REGISTRY.register("cashier", ()->EntityType.Builder.<CashierWorker>of(CashierWorker::new, MobCategory.MISC).sized(0.6F, 1.8F).noSave().clientTrackingRange(32).updateInterval(2).build("client"));
    public static final RegistryObject<EntityType<CookWorker>> COOK = REGISTRY.register("cook", ()->EntityType.Builder.<CookWorker>of(CookWorker::new, MobCategory.MISC).sized(0.6F, 1.8F).noSave().clientTrackingRange(32).updateInterval(2).build("client"));



    public static void init() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(CLIENT.get(), ClientEntity.createAttributes().build());
        event.put(CASHIER.get(), CashierWorker.createAttributes().build());
        event.put(COOK.get(), CookWorker.createAttributes().build());
    }
}
