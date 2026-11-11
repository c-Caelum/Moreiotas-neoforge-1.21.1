package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.IOTA_TYPE;

public class IotaTypeIota extends Iota {

    public final IotaType<?> iotaType;

    public IotaTypeIota(@NotNull IotaType<?> iotaType) {
        super(() -> IOTA_TYPE);
        this.iotaType = iotaType;
    }

    public IotaType<?> getIotaType() {
        return iotaType;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that) &&
                that instanceof IotaTypeIota dent &&
                this.getIotaType().equals(dent.getIotaType());
    }

    @Override
    public boolean isTruthy(CastingEnvironment env) {
        return true;
    }

    @Override
    public Component display() {
        ResourceLocation location = HexIotaTypes.REGISTRY.getKey(iotaType);
        assert location != null;
        return Component.translatable("hexcasting.iota.%s".formatted(location.toString()))
                .withStyle(ChatFormatting.DARK_PURPLE);
    }

    @Override
    public int hashCode() {
        return iotaType.hashCode();
    }

    public static IotaType<IotaTypeIota> TYPE = new IotaType<>() {

        public static final MapCodec<IotaTypeIota> MAP_CODEC = ResourceLocation.CODEC.xmap(
                location -> new IotaTypeIota(Objects.requireNonNull(HexIotaTypes.REGISTRY.get(location))),
                iotaTypeIota -> HexIotaTypes.REGISTRY.getKey(iotaTypeIota.iotaType)).fieldOf("iotatype");
        public static final StreamCodec<RegistryFriendlyByteBuf, IotaTypeIota> STREAM_CODEC =
                ResourceLocation.STREAM_CODEC.map(location -> new IotaTypeIota(HexIotaTypes.REGISTRY.get(location)),
                        iotaTypeIota -> HexIotaTypes.REGISTRY.getKey(iotaTypeIota.iotaType)).mapStream(buf -> buf);

        @Override
        public MapCodec<IotaTypeIota> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IotaTypeIota> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public int color() {
            return 0xff_553355;
        }
    };
}
