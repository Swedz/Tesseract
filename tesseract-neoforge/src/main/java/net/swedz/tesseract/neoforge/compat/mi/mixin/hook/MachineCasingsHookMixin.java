package net.swedz.tesseract.neoforge.compat.mi.mixin.hook;

import aztech.modern_industrialization.machines.models.MachineCasings;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.MachineCasingsMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = MachineCasings.class,
		remap = false
)
public class MachineCasingsHookMixin
{
	@Inject(
			method = "<clinit>",
			at = @At("TAIL")
	)
	private static void init(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners((hook) -> hook.machineCasings(new MachineCasingsMIHookContext()));
	}
}
