package net.swedz.tesseract.neoforge.compat.mi.mixin.hook;

import aztech.modern_industrialization.machines.init.SingleBlockCraftingMachines;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHooks;
import net.swedz.tesseract.neoforge.compat.mi.hook.context.SingleBlockCraftingMachinesMIHookContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		value = SingleBlockCraftingMachines.class,
		remap = false
)
public class SingleBlockCraftingMachinesHookMixin
{
	@Inject(
			method = "init",
			at = @At("TAIL")
	)
	private static void init(CallbackInfo callback)
	{
		MIHooks.triggerHookListeners((hook) -> hook.singleBlockCraftingMachines(new SingleBlockCraftingMachinesMIHookContext()));
	}
}
