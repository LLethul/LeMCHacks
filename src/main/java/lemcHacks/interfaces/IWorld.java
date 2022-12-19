package lemcHacks.interfaces;

import java.util.List;
import java.util.stream.Stream;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.jetbrains.annotations.Nullable;

public interface IWorld {
    public List<BlockEntityTickInvoker> getBlockEntityTickers();

    public Stream<VoxelShape> getBlockCollisionsStream(@Nullable Entity entity,
                                                       Box box);
}