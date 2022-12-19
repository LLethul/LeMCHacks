package lemcHacks.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import lemcHacks.event.events.WorldRenderEvent;
import lemcHacks.eventbus.LeMCSubscribe;
import lemcHacks.interfaces.IWorld;
import lemcHacks.module.Module;
import lemcHacks.util.Color;
import lemcHacks.util.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.chunk.BlockEntityTickInvoker;

import java.util.ArrayList;
import java.util.List;

public class ChestESP extends Module {
    public ChestESP() {
        super("ChestESP", "Chest esp", Category.RENDER, true);
    }

    public void onRender(MatrixStack matrixStack, float tickDelta) {
        for (BlockEntityTickInvoker tickInvoker : ((IWorld)mc.world).getBlockEntityTickers()) {
            BlockEntity blockEntity = mc.world.getBlockEntity(tickInvoker.getPos());
            if(blockEntity instanceof ChestBlockEntity) {
                Box box = new Box(blockEntity.getPos());
                RenderUtils.draw3DBox(matrixStack, box, new Color(255,0,0), 0.2f);
            }
        }
    }
}
