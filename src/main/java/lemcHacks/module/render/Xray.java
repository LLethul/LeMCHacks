package lemcHacks.module.render;

import lemcHacks.module.Module;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import lemcHacks.module.settings.BooleanSetting;

public class Xray extends Module {
    private static MinecraftClient mc = MinecraftClient.getInstance();
	public static ArrayList<Block> blocks = new ArrayList<>();
    public BooleanSetting bedrock = new BooleanSetting("Bedrock", false);
    public Xray() {
        super("Xray", "look through the world... and beyond", Category.RENDER, true);
        addSetting(bedrock);
        Registry.BLOCK.forEach(block -> {
            if (isGoodBlock(block)) blocks.add(block);
        });
    }

    boolean isGoodBlock(Block block) {
        boolean c1 = block == Blocks.LAVA || block == Blocks.CHEST || block == Blocks.FURNACE || block == Blocks.END_GATEWAY || block == Blocks.COMMAND_BLOCK || block == Blocks.ANCIENT_DEBRIS || block == Blocks.NETHER_PORTAL ||block == Blocks.SPAWNER;
        boolean c2 = block instanceof OreBlock || block instanceof RedstoneOreBlock || block instanceof ShulkerBoxBlock;
        return c1 || c2;
    }
    
    @Override
    public void onEnable() {
        mc.worldRenderer.reload();
        mc.chunkCullingEnabled = false;
        super.onEnable();
    }

    @Override
    public void onTick() {
        if (this.bedrock.isEnabled() && !blocks.contains(Blocks.BEDROCK)) {
            blocks.add(Blocks.BEDROCK);
            mc.worldRenderer.reload();
        } else if (!this.bedrock.isEnabled() && blocks.contains(Blocks.BEDROCK)) {
            blocks.remove(Blocks.BEDROCK);
            mc.worldRenderer.reload();
        }
        super.onTick();
    }

    @Override
    public void onDisable() {
        mc.worldRenderer.reload();
        mc.chunkCullingEnabled = true;

        super.onDisable();
    }
}