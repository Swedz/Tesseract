package net.swedz.tesseract.neoforge.compat.mi.mixin.hook;

import aztech.modern_industrialization.machines.init.SingleBlockSpecialMachines;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.SingleBlockSpecialMachinesMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = SingleBlockSpecialMachines.class,
		remap = false
)
public class SingleBlockSpecialMachinesHookMixin
{
	@Inject(
			method = "init",
			at = @At("TAIL")
	)
	private static void init(CallbackInfo callback)
	{
		MIHooks.triggerHooks((hook) -> hook.singleBlockSpecialMachines(new SingleBlockSpecialMachinesMIHookContext()));
	}
}
