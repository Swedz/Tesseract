package net.swedz.tesseract.neoforge.compat.mi.hook.context;

import aztech.modern_industrialization.machines.blockentities.multiblocks.ElectricBlastFurnaceBlockEntity;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookContext;

import java.util.List;

public final class BlastFurnaceTiersMIHookContext implements MIHookContext
{
	private final List<ElectricBlastFurnaceBlockEntity.Tier> tiers = Lists.newArrayList();
	
	public void register(ResourceLocation coilBlockId, long maxBaseEu, String englishName)
	{
		tiers.add(new ElectricBlastFurnaceBlockEntity.Tier(coilBlockId, maxBaseEu, englishName));
	}
	
	public List<ElectricBlastFurnaceBlockEntity.Tier> getRegisteredTiers()
	{
		return tiers;
	}
}
