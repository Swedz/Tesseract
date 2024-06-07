package net.swedz.tesseract.neoforge.registry.holder;

import aztech.modern_industrialization.definition.FluidLike;
import aztech.modern_industrialization.fluid.MIBucketItem;
import aztech.modern_industrialization.fluid.MIFluid;
import aztech.modern_industrialization.fluid.MIFluidBlock;
import aztech.modern_industrialization.fluid.MIFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.swedz.tesseract.neoforge.registry.MIFluidProperties;

import java.util.function.Consumer;

public class MIFluidHolder extends FluidHolder<MIFluid, MIFluidHolder.FakedMIFluidType, MIFluidBlock, MIBucketItem> implements FluidLike
{
	private final MIFluidProperties properties;
	
	public MIFluidHolder(ResourceLocation location, String englishName,
						 DeferredRegister<Fluid> registerFluids,
						 DeferredRegister<FluidType> registerFluidTypes,
						 DeferredRegister.Blocks registerBlocks,
						 DeferredRegister.Items registerItems,
						 MIFluidProperties properties)
	{
		super(
				location, englishName,
				registerFluids, (holder) -> new MIFluid(
						() -> holder.block().get(),
						() -> holder.bucketItem().get(),
						() -> holder.registerableFluidType().getOrThrow(),
						properties.color()
				),
				registerFluidTypes, (holder) ->
				{
					FluidType.Properties fluidTypeProperties = FluidType.Properties.create()
							.descriptionId(holder.block().get().getDescriptionId());
					if(properties.isGas())
					{
						fluidTypeProperties.density(-1000);
					}
					return new FakedMIFluidType(location.getNamespace(), holder.block().registerableBlock().get(), fluidTypeProperties);
				},
				registerBlocks, (__, ___) -> new MIFluidBlock(properties.color()),
				registerItems, (holder, p) -> new MIBucketItem(holder.registerableFluid().getOrThrow(), properties.color(), p)
		);
		this.properties = properties;
	}
	
	public MIFluidProperties properties()
	{
		return properties;
	}
	
	public static final class FakedMIFluidType extends MIFluidType
	{
		private final String namespace;
		
		private final DeferredBlock<MIFluidBlock> fluidBlock;
		
		public FakedMIFluidType(String namespace, DeferredBlock<MIFluidBlock> fluidBlock, Properties properties)
		{
			super(null, properties);
			this.namespace = namespace;
			this.fluidBlock = fluidBlock;
		}
		
		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
		{
			consumer.accept(new IClientFluidTypeExtensions()
			{
				private ResourceLocation textureLocation;
				
				@Override
				public ResourceLocation getStillTexture()
				{
					if(textureLocation == null)
					{
						textureLocation = new ResourceLocation(namespace, "fluid/%s_still".formatted(fluidBlock.getId().getPath()));
					}
					return textureLocation;
				}
				
				@Override
				public ResourceLocation getFlowingTexture()
				{
					return IClientFluidTypeExtensions.of(Fluids.WATER).getFlowingTexture();
				}
			});
		}
	}
}
