package ram.talia.moreiotas.xplat;

import at.petrak.hexcasting.common.msgs.IMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class NeoforgeXplatAbstractions implements IXplatAbstractions {
    @Override
    public boolean isPhysicalClient() {
        return false;
    }

    @Override
    public void sendPacketToPlayer(ServerPlayer target, IMessage packet) {

    }

    @Override
    public void sendPacketNear(Vec3 pos, double radius, ServerLevel dimension, IMessage packet) {

    }

    @Override
    public Packet<?> toVanillaClientboundPacket(IMessage message) {
        return null;
    }

    @Override
    public boolean isBreakingAllowed(Level level, BlockPos pos, BlockState state, Player player) {
        return false;
    }

    @Override
    public @Nullable String lastMessage(@Nullable Player player) {
        return "";
    }

    @Override
    public void setChatPrefix(Player player, @Nullable String prefix) {

    }

    @Override
    public @Nullable String getChatPrefix(Player player) {
        return "";
    }
}
