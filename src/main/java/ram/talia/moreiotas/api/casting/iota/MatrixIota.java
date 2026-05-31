package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.MoreIotasConfig;
import ram.talia.moreiotas.api.MoreIotasCodecs;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

import static ram.talia.moreiotas.MoreIotas.matrixIsEmpty;

public class MatrixIota extends Iota {

    public final SimpleMatrix matrix;

    public MatrixIota(@NotNull SimpleMatrix matrix) throws MishapInvalidIota {
        super(() -> MoreIotasIotaTypes.MATRIX);
        this.matrix = matrix;
        if (matrix.getNumRows() > MoreIotasConfig.maxMatrixSize.get() || matrix.getNumCols() > MoreIotasConfig.maxMatrixSize.get())
            throw MishapInvalidIota.of(this,
                    0,
                    "matrix.max_size",
                    MoreIotasConfig.maxMatrixSize.get(),
                    matrix.getNumRows(),
                    matrix.getNumCols());
    }

    public SimpleMatrix getMatrix() {
        return matrix;
    }

    @Override
    protected boolean toleratesOther(Iota that) {
        return false;
    }

    @Override
    public Component display() {
        var out = Component.empty();

        out.append(String.format("(%d, %d)", matrix.getNumRows(), matrix.getNumCols()));
        if (!matrixIsEmpty(matrix))
            out.append(" | ");

        for (int r = 0; r < matrix.getNumRows(); r++) {
            for (int c = 0; c < matrix.getNumCols(); c++) {
                out.append(Component.literal(String.format("%.2f", matrix.get(r,c))).withStyle(ChatFormatting.GREEN));
                if (c < matrix.getNumCols() - 1) {
                    out.append(", ");
                }
            }
            if (r < matrix.getNumRows() - 1) {
                out.append("; ");
            }
        }

        return Component.translatable("hexcasting.tooltip.list_contents", out).withStyle(ChatFormatting.AQUA);
    }

    @Override
    public int hashCode() {
        return matrix.hashCode();
    }

    @Override
    public boolean isTruthy() {
        // is true if it has entries, and at least one has abs(entry)>0
        return !matrixIsEmpty(matrix) && this.getMatrix().elementMaxAbs() > DoubleIota.TOLERANCE;
    }

    public static IotaType<MatrixIota> TYPE = new IotaType<>() {
        public static final MapCodec<MatrixIota> MAP_CODEC = MoreIotasCodecs.SIMPLEMATRIX.xmap(MatrixIota::new, MatrixIota::getMatrix).fieldOf("matrix");
        public static final StreamCodec<RegistryFriendlyByteBuf, MatrixIota> STREAM_CODEC =
                MoreIotasCodecs.SIMPLEMATRIX_STREAM.map(MatrixIota::new, MatrixIota::getMatrix).mapStream(buf -> buf);

        @Override
        public MapCodec<MatrixIota> codec() {
            return MAP_CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MatrixIota> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public int color() {
            return 0xff_55ffff;
        }
    };
}
