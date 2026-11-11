package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.ENTITY_TYPE;

public class EntityTypeIota extends Iota {

    public final EntityType<?> entityType;

    public EntityTypeIota(@NotNull EntityType<?> entityType) {
        super(() -> ENTITY_TYPE);
        this.entityType = entityType;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that) &&
                that instanceof EntityTypeIota dent &&
                this.getEntityType().equals(dent.getEntityType());
    }

    @Override
    public boolean isTruthy(CastingEnvironment env) {
        return true;
    }

    @Override
    public Component display() {
        return entityType.getDescription().copy().append(" ").append(Component.translatable("moreiotas.spelldata.entity_type")).withStyle(ChatFormatting.DARK_AQUA);
    }

    @Override
    public int hashCode() {
        return entityType.hashCode();
    }

    public static IotaType<EntityTypeIota> TYPE = new IotaType<>() {
        public static final MapCodec<EntityTypeIota> MAP_CODEC = ResourceLocation.CODEC.xmap(
                location -> new EntityTypeIota(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.get(location))),
                entityTypeIota -> BuiltInRegistries.ENTITY_TYPE.getKey(entityTypeIota.entityType)).fieldOf("entityType");
        public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeIota> STREAM_CODEC =
                ResourceLocation.STREAM_CODEC.map(location -> new EntityTypeIota(BuiltInRegistries.ENTITY_TYPE.get(location)),
                        entityTypeIota -> BuiltInRegistries.ENTITY_TYPE.getKey(entityTypeIota.entityType)).mapStream(buf -> buf);

        @Override
        public MapCodec<EntityTypeIota> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EntityTypeIota> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public int color() {
            return 0xff_5555ff;
        }
    };
}
