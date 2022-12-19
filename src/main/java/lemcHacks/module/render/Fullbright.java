package lemcHacks.module.render;

import lemcHacks.module.Module;

public class Fullbright extends Module {

    public Fullbright() {
        super("Fullbright", "bright up your world", Category.RENDER, true);
    }

    @Override
    public void onEnable() {
        mc.options.getGamma().setValue(100.0);
        super.onEnable();
    }
    @Override
    public void onDisable() {
        mc.options.getGamma().setValue(1.0);
        super.onDisable();
    }
}