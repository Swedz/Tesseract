package net.swedz.tesseract.neoforge.compat.mi.component.craft;

import aztech.modern_industrialization.stats.PlayerStatistics;
import aztech.modern_industrialization.stats.PlayerStatisticsData;
import aztech.modern_industrialization.util.Simulation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ModularCrafterAccessBehavior
{
	default boolean isEnabled()
	{
		return true;
	}
	
	long consumeEu(long max, Simulation simulation);
	
	default boolean canConsumeEu(long amount)
	{
		return this.consumeEu(amount, Simulation.SIMULATE) == amount;
	}
	
	default boolean isRecipeBanned(long recipeEuCost)
	{
		return recipeEuCost > this.getMaxRecipeEu();
	}
	
	long getBaseRecipeEu();
	
	long getBaseMaxRecipeEu();
	
	@ApiStatus.NonExtendable
	default long getMaxRecipeEu()
	{
		return this.getBaseMaxRecipeEu();
	}
	
	// can't use getWorld() or the remapping will fail
	Level getCrafterWorld();
	
	default int getMaxFluidOutputs()
	{
		return Integer.MAX_VALUE;
	}
	
	@Nullable
	UUID getOwnerUuid();
	
	default PlayerStatistics getStatsOrDummy()
	{
		var uuid = getOwnerUuid();
		if(uuid == null)
		{
			return PlayerStatistics.DUMMY;
		}
		else
		{
			return PlayerStatisticsData.get(getCrafterWorld().getServer()).get(uuid);
		}
	}
}
