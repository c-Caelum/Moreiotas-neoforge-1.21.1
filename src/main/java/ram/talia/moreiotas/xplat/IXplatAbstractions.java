package ram.talia.moreiotas.xplat;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.common.msgs.IMessage;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ServiceLoader;
import java.util.UUID;
import java.util.stream.Collectors;

public interface IXplatAbstractions {
    GameProfile MOREIOTAS = new GameProfile(UUID.fromString("8BE7E9DA-1667-11EE-BE56-0242AC120002"), "[MoreIotas]");

    boolean isPhysicalClient();

    void sendPacketToPlayer(ServerPlayer target, CustomPacketPayload packet);

    void sendPacketNear(Vec3 pos, double radius, ServerLevel dimension, CustomPacketPayload packet);

    // https://github.com/VazkiiMods/Botania/blob/13b7bcd9cbb6b1a418b0afe455662d29b46f1a7f/Xplat/src/main/java/vazkii/botania/xplat/IXplatAbstractions.java#L157
    Packet<?> toVanillaClientboundPacket(CustomPacketPayload message);

    boolean isBreakingAllowed(Level level, BlockPos pos, BlockState state, Player player);

    @Nullable String lastMessage(@Nullable Player player);

    void setChatPrefix(Player player, @Nullable String prefix);

    @Nullable String getChatPrefix(Player player);

    IXplatAbstractions INSTANCE = new ForgeXplatImpl();

    private static IXplatAbstractions find() {
        var providers = ServiceLoader.load(IXplatAbstractions.class).stream().toList();
        if (providers.size() != 1) {
            var names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
            throw new IllegalStateException(
                "There should be exactly one IXplatAbstractions implementation on the classpath. Found: " + names);
        } else {
            var provider = providers.get(0);
            HexAPI.LOGGER.debug("Instantiating xplat impl: " + provider.type().getName());
            return provider.get();
        }
    }
}
