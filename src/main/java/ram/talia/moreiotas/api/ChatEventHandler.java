package ram.talia.moreiotas.api;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static ram.talia.moreiotas.MoreIotasNeoforge.LOGGER;

public class ChatEventHandler {
    private static final String TAG_CHAT_PREFIX = "moreiotas:prefix";

    private static final Map<UUID, @Nullable String> lastMessages = new HashMap<>();

    private static @Nullable String lastMessage = null;

    public static void setPrefix(Player player, @Nullable String prefix) {
        if (prefix == null)
            player.getPersistentData().remove(TAG_CHAT_PREFIX);
        else
            player.getPersistentData().putString(TAG_CHAT_PREFIX, prefix);
    }

    public static @Nullable String getPrefix(Player player) {
        if (!player.getPersistentData().contains(TAG_CHAT_PREFIX))
            return null;
        return player.getPersistentData().getString(TAG_CHAT_PREFIX);
    }

    public static @Nullable String getLastMessage(@Nullable Player player) {
        if (player == null)
            return lastMessage;
        LOGGER.info("Last message is {}.", lastMessages.get(player.getUUID()));
        return lastMessages.get(player.getUUID());
    }

    public static void chatMessageSent(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        UUID uuid = player.getUUID();
        String text = event.getRawText();

        if (event.isCanceled())
            return;

        String prefix = getPrefix(player);

        if (prefix == null) {
            lastMessages.put(uuid, text);
            lastMessage = text;
            return;
        }

        if (text.startsWith(prefix)) {
            event.setCanceled(true);
            lastMessages.put(uuid, text.substring(prefix.length()));
            return;
        }

        lastMessage = text;
    }
}
