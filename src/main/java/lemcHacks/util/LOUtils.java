package lemcHacks.util;
import lemcHacks.module.Module;
import lemcHacks.module.ModuleManager;
import lemcHacks.module.movement.FallDamage;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

public class LOUtils {
    public static void patchPacket(Args args) {
        double patchedX = Math.round((double) args.get(0) * 100) / 100d;
        double patchedZ = Math.round((double) args.get(2) * 100) / 100d;
        patchedZ = Math.nextAfter(patchedZ, patchedZ + Math.signum(patchedZ));
        patchedX =  Math.nextAfter(patchedX, patchedX + Math.signum(patchedX));
        args.set(0, patchedX);
        args.set(2, patchedZ);
        if (ModuleManager.Instance.getModule(FallDamage.class).isEnabled()) {
            args.set(5, true);
        }
    }
}