package space.quinoaa.minechef.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import space.quinoaa.minechef.Minechef;
import space.quinoaa.minechef.network.*;

public class MinechefNetwork {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Minechef.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    protected static void init(){
        CHANNEL.registerMessage(0, WithdrawPacket.class, WithdrawPacket::encode, WithdrawPacket::decode, WithdrawPacket::handle);
        CHANNEL.registerMessage(1, BoardUpdateFundPacket.class, BoardUpdateFundPacket::encode, BoardUpdateFundPacket::decode, BoardUpdateFundPacket::handle);
        CHANNEL.registerMessage(2, BoardUpdateMenuPacket.class, BoardUpdateMenuPacket::encode, BoardUpdateMenuPacket::decode, BoardUpdateMenuPacket::handle);
        CHANNEL.registerMessage(3, BoardRemoveMenuPacket.class, BoardRemoveMenuPacket::encode, BoardRemoveMenuPacket::decode, BoardRemoveMenuPacket::handle);
        CHANNEL.registerMessage(4, BoardSelectRestaurantPacket.class, BoardSelectRestaurantPacket::encode, BoardSelectRestaurantPacket::decode, BoardSelectRestaurantPacket::handle);
        CHANNEL.registerMessage(5, CashRegisterSetRequestPacket.class, CashRegisterSetRequestPacket::encode, CashRegisterSetRequestPacket::decode, CashRegisterSetRequestPacket::handle);
        CHANNEL.registerMessage(6, BoardUpdateWorkersPacket.class, BoardUpdateWorkersPacket::encode, BoardUpdateWorkersPacket::decode, BoardUpdateWorkersPacket::handle);
        CHANNEL.registerMessage(7, BoardHirePacket.class, BoardHirePacket::encode, BoardHirePacket::decode, BoardHirePacket::handle);
        CHANNEL.registerMessage(8, BoardFirePacket.class, BoardFirePacket::encode, BoardFirePacket::decode, BoardFirePacket::handle);
    }

}
