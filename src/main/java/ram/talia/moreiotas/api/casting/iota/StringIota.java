package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.MoreIotasConfig;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

public class StringIota extends Iota {
    public final String string;


    private StringIota(@NotNull String string) {
        super(() -> MoreIotasIotaTypes.STRING);
        this.string = string;
    }

    public static StringIota make(@NotNull String string) throws MishapInvalidIota {
        if (string.length() > MoreIotasConfig.maxStringSize.get())
            throw MishapInvalidIota.of(new StringIota(string), 0, "string.max_size", MoreIotasConfig.maxStringSize.get(), string.length());
        return new StringIota(string);
    }

    public static StringIota makeUnchecked(@NotNull String string) {
        return new StringIota(string);
    }

    public String getString() {
        return (String) this.string;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
                && that instanceof StringIota sthat
                && this.getString().equals(sthat.getString());
    }

    @Override
    public Component display() {
        return Component.literal(String.format("\"%s\"", string)).withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public boolean isTruthy() {
        return !this.getString().isEmpty();
    }

    public static final IotaType<StringIota> TYPE = new IotaType<StringIota>() {
        public static final MapCodec<StringIota> MAP_CODEC = Codec.STRING.xmap(StringIota::new, StringIota::getString).fieldOf("string");
        public static final StreamCodec<RegistryFriendlyByteBuf, StringIota> STREAM_CODEC =
                (ByteBufCodecs.STRING_UTF8.map(StringIota::new, StringIota::getString)).mapStream(byteBuf -> byteBuf);

        @Override
        public MapCodec<StringIota> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, StringIota> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public int color() {
            return 0xffa453da;
        }

        @Override
        public boolean validate(StringIota iota, ServerLevel level) {
            return iota.string.length() <= MoreIotasConfig.maxStringSize.get();
        }
    };
}
