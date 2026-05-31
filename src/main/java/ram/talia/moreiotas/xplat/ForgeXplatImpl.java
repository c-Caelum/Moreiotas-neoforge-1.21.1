package ram.talia.moreiotas.xplat;

import at.petrak.hexcasting.forge.network.ForgePacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import ram.talia.moreiotas.api.ChatEventHandler;

public class ForgeXplatImpl implements IXplatAbstractions {

	@Override
	public boolean isPhysicalClient() {
		return FMLLoader.getDist() == Dist.CLIENT;
	}

	@Override
	public void sendPacketToPlayer(ServerPlayer target, CustomPacketPayload packet) {
		PacketDistributor.sendToPlayer(target, packet);
	}
	
	@Override
	public void sendPacketNear(Vec3 pos, double radius, ServerLevel dimension, CustomPacketPayload packet) {
		PacketDistributor.sendToPlayersNear(dimension, null, pos.x, pos.y, pos.z, radius, packet);
	}
	
	@Override
	public Packet<?> toVanillaClientboundPacket(CustomPacketPayload message) {
		//noinspection unchecked
		return (Packet<ClientGamePacketListener>) (Object) message.toVanillaClientbound();
	}

	@Override
	public boolean isBreakingAllowed (Level level, BlockPos pos, BlockState state, Player player) {
		return NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, state, player)).isCanceled();
	}

	@Override
	public @Nullable String lastMessage(@Nullable Player player) {
		return ChatEventHandler.getLastMessage(player);
	}

	@Override
	public void setChatPrefix(Player player, @Nullable String prefix) {
		ChatEventHandler.setPrefix(player, prefix);
	}

	@Override
	public @Nullable String getChatPrefix(Player player) {
		return ChatEventHandler.getPrefix(player);
	}
}
