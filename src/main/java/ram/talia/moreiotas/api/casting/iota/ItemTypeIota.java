package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ram.talia.moreiotas.MoreIotas;

import static ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.ITEM_TYPE;

public class ItemTypeIota extends Iota {

    public final Either<Item, Block> type;

    public ItemTypeIota(@NotNull Item item) {
        super(() -> ITEM_TYPE);
        type = Either.left(item);
    }
    public ItemTypeIota(@NotNull Block block) {
        super(() -> ITEM_TYPE);
        type = Either.right(block);
    }

    public Either<Item, Block> getEither() {
        return type;
    }

    @Nullable
    public Block getBlock() {
        return this.getEither().map(item -> {
            if (item instanceof BlockItem blockItem)
                return blockItem.getBlock();
            else return null;
        }, block -> block);
    }

    /**
     * If the block has no item form this returns Items.AIR
     */
    public Item getItem() {
        return this.getEither().map(item -> item, Block::asItem);
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return typesMatch(this, that) &&
                that instanceof ItemTypeIota dent &&
                this.getEither().map(
                        itemThis -> dent.getEither().map(itemThis::equals,
                                                         blockThat -> {
                                                             var itemThat = blockThat.asItem();
                                                             if (itemThat.equals(Items.AIR) && !blockThat.equals(Blocks.AIR))
                                                                 return false;
                                                             return itemThis.equals(itemThat);
                                                         }),
                        blockThis -> dent.getEither().map(itemThat -> {
                                                              var itemThis = blockThis.asItem();
                                                              if (itemThis.equals(Items.AIR) && !blockThis.equals(Blocks.AIR))
                                                                  return false;
                                                              return itemThis.equals(itemThat);
                                                          }, blockThis::equals));
    }

    @Override
    public boolean isTruthy() {
        return this.getEither().map(
                item -> !item.equals(Items.AIR),
                block -> !block.defaultBlockState().isAir()
        );
    }

    @Override
    public Component display() {
         return type.map(item -> item.getDescription().copy(),
                 Block::getName).withStyle(ChatFormatting.GOLD);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public static ItemTypeIota fromString(String str) {
        Either<Item, Block> type;
        if (str.startsWith("item:")) {
            ResourceLocation location = ResourceLocation.read(str.substring(5)).getOrThrow();
            return new ItemTypeIota(BuiltInRegistries.ITEM.get(location));
        } else {
            ResourceLocation location = ResourceLocation.read(str.substring(6)).getOrThrow();
            Block block = BuiltInRegistries.BLOCK.get(location);
            return new ItemTypeIota(block);
        }
    }

    public static String turnIntoString(ItemTypeIota iota) {
        return iota.type.map(item -> {
            String constructed = "item:";
            constructed = constructed.concat(BuiltInRegistries.ITEM.getKey(item).toString());
            return constructed;
        }, block -> {
            String constructed = "block:";
            constructed = constructed.concat(BuiltInRegistries.BLOCK.getKey(block).toString());
            return constructed;
        });
    }

    public static IotaType<ItemTypeIota> TYPE = new IotaType<ItemTypeIota>() {
        public static final MapCodec<ItemTypeIota> CODEC = Codec.STRING.<ItemTypeIota>xmap(ItemTypeIota::fromString, ItemTypeIota::turnIntoString).fieldOf("item_block_type");
        public static final StreamCodec<RegistryFriendlyByteBuf, ItemTypeIota> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(
                ItemTypeIota::fromString, ItemTypeIota::turnIntoString
        ).mapStream(buf ->  buf);

        @Override
        public MapCodec<ItemTypeIota> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemTypeIota> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public int color() {
            return 0xff_feaa01;
        }
    };
}
