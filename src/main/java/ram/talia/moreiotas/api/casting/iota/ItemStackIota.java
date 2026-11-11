package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import static ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.ITEM_STACK;

public class ItemStackIota extends Iota {

    public final ItemStack itemStack;

    private ItemStackIota(ItemStack stack) {
        super(() -> ITEM_STACK);
        itemStack = stack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }


    @Override
    protected boolean toleratesOther(Iota iota) {
        return iota instanceof ItemStackIota iiota && ItemStack.matches(this.getItemStack(), iiota.getItemStack());
    }

    @Override
    public boolean isTruthy(CastingEnvironment env) {
        return !this.getItemStack().isEmpty();
    }

    public static ItemStackIota createFiltered(ItemStack originalStack) {
        var stack = originalStack.copy();
        return new ItemStackIota(stack);
    }

    @Override
    public Component display() {
        if (itemStack.isEmpty())
            return Component.translatable("moreiotas.tooltip.stack.empty").withStyle(Style.EMPTY.withColor(0x1E90FF));

        return Component.translatable("moreiotas.tooltip.stack.format", itemStack.getCount(), itemStack.getDisplayName()).withStyle(Style.EMPTY.withColor(0x1E90FF));
    }

    @Override
    public int hashCode() {
        return itemStack.hashCode();
    }

    public static IotaType<ItemStackIota> TYPE = new IotaType<>() {
        public static final MapCodec<ItemStackIota> MAP_CODEC = ItemStack.CODEC.xmap(ItemStackIota::new, ItemStackIota::getItemStack).fieldOf("itemstack");
        public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackIota> STREAM_CODEC =
                ItemStack.STREAM_CODEC.map(ItemStackIota::new, ItemStackIota::getItemStack).mapStream(buf -> buf);

        @Override
        public MapCodec<ItemStackIota> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ItemStackIota> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public int color() {
            return 0xFF_1E90FF;
        }
    };

    private static final String TAG_STACK_ID = "moreiotas:stack_id";
    private static final String TAG_STACK_COUNT = "moreiotas:stack_count";
    private static final String TAG_STACK_TAG = "moreiotas:stack_tag";
}
