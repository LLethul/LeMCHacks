package lemcHacks.module;

import java.io.File;
import java.util.ArrayList;

import java.util.List;

import lemcHacks.module.Module.Category;
import lemcHacks.module.liveoverflow.*;
import lemcHacks.module.movement.*;
import lemcHacks.module.render.*;
import lemcHacks.module.misc.*;
import lemcHacks.module.combat.*;
import lemcHacks.module.exploit.*;

public class ModuleManager {
	
	public static final ModuleManager Instance = new ModuleManager();
	public List<Module> modules = new ArrayList<Module>();

	public ModuleManager() {
		addModules();
	}
	
	public List<Module> getModules() {
		return modules;
	}
	
	public List<Module> getEnabledModules() {
		List<Module> enabledModules = new ArrayList<Module>();
		
		for (Module mod : modules) {
			if (!mod.isEnabled()) continue;
			enabledModules.add(mod);
		}
		
		return enabledModules;
	}
	
	public List<Module> getModulesInCategory(Category cat) {
		List<Module> list = new ArrayList<Module>();
		
		for (Module mod : modules) {
			if (mod.getCategory() == cat) {
				list.add(mod);
			}
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
		return (T) modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
	}
	
	public void addModules() {
		modules.add(new Fly());
		modules.add(new Sprint());
		modules.add(new FallDamage());
		modules.add(new NoRender());
		modules.add(new ViewClip());
		modules.add(new Xray());
		modules.add(new PortalGUI());
		modules.add(new ChatTimestamps());
		modules.add(new Criticals());
		modules.add(new Aura());
		modules.add(new Reach());
		modules.add(new LOServerHumanBypass());
		modules.add(new Velocity());
		modules.add(new ArrowJuke());
		modules.add(new FastMine());
		modules.add(new BowBot());
		modules.add(new Triggerbot());
		modules.add(new LOHarvest());
		modules.add(new BoatFly());
		modules.add(new EntityControl());
		modules.add(new NoSlow());
		modules.add(new Tracers());
		modules.add(new Fullbright());
		modules.add(new Surround());
		modules.add(new AntiHunger());
		modules.add(new ElytraBoost());
		modules.add(new LOServerBorderBypass());
		modules.add(new ChestESP());
		modules.add(new Waypoint());
		modules.add(new BedrockBreaker());
		modules.add(new Speed());
	}
	
}
