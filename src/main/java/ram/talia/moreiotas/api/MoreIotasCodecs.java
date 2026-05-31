package ram.talia.moreiotas.api;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;
import static ram.talia.moreiotas.MoreIotasNeoforge.LOGGER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoreIotasCodecs {
    public static final Codec<SimpleMatrix> SIMPLEMATRIX = Codec.DOUBLE.listOf().listOf().<SimpleMatrix>xmap((List<List<Double>> list) -> {
        int rows = list.size();
        int cols = list.get(0).size();
        SimpleMatrix matrix = SimpleMatrix.filled(rows, cols, 0.0);
        if (rows * cols != 0) {
            for (int i = 0; i < rows; i++) {
                List<Double> currentRow = list.get(i);
                for (int j = 0; j < cols; j++) {
                    matrix.set(i, j, currentRow.get(j));
                }
            }
        }
        return matrix;
    }, matrix -> {
        int rows = matrix.getNumRows();
        int cols = matrix.getNumCols();
        List<List<Double>> list = new ArrayList<>();
        LOGGER.info("len of list {}, rows {}.", list.size(), rows);
        if (rows * cols != 0) {
            for (int i = 0; i < rows; i++) {
                List<Double> currentRow = new ArrayList<>();
                for (int j = 0; j < cols; j++) {
                    currentRow.add(matrix.get(i, j));
                }
                list.add(currentRow);
            }
        }
        return list;
    });
    public static final StreamCodec<ByteBuf, SimpleMatrix> SIMPLEMATRIX_STREAM = new StreamCodec<ByteBuf, SimpleMatrix>() {
        @Override
        public SimpleMatrix decode(ByteBuf buffer) {
            int rows = buffer.readInt();
            int cols = buffer.readInt();
            SimpleMatrix matrix = SimpleMatrix.filled(rows, cols, 0);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                   matrix.set(i, j, buffer.readDouble());
                }
            }

            return matrix;
        }

        @Override
        public void encode(ByteBuf buffer, SimpleMatrix matrix) {
            buffer.writeInt(matrix.getNumRows());
            buffer.writeInt(matrix.getNumCols());
            int rows = matrix.getNumRows();
            int cols = matrix.getNumCols();
            double[][] data = matrix.toArray2();
            for (int i = 0; i < rows; i++) {
                double[] currentRow = data[i];
                for (int j = 0; j < cols; j++) {
                    buffer.writeDouble(currentRow[j]);
                }
            }
        }
    };
}
