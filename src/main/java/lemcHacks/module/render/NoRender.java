package lemcHacks.module.render;

import lemcHacks.module.settings.BooleanSetting;
import lemcHacks.event.events.PacketEvent;
import lemcHacks.event.events.ParticleEvent;
import lemcHacks.eventbus.LeMCSubscribe;
import lemcHacks.module.Module;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.particle.ParticleTypes;

public class NoRender extends Module {
    public BooleanSetting particles = new BooleanSetting("Particles", true);
    public BooleanSetting bossbar = new BooleanSetting("Bossbar", true);
    public BooleanSetting fog = new BooleanSetting("Fog", true);
    public BooleanSetting effecthud = new BooleanSetting("EffectHud", true);
    public BooleanSetting hurtcam = new BooleanSetting("Hurtcam", true);
    public BooleanSetting overlay = new BooleanSetting("Overlay", true);



    public NoRender() {
        super("NoRender", "removes bunch of things", Category.RENDER, true);
        addSettings(fog,particles,bossbar,effecthud,hurtcam, overlay);
    }

    @LeMCSubscribe
    public void onParticle(ParticleEvent.Normal event) {
        if (this.particles.isEnabled() && event.getParticle() instanceof ExplosionLargeParticle) event.setCancelled(true);
    }

    @LeMCSubscribe
    public void onParticleEmitter(ParticleEvent.Emitter event) {
        if (this.particles.isEnabled() && event.getEffect().getType() == ParticleTypes.TOTEM_OF_UNDYING) event.setCancelled(true);
    }
}