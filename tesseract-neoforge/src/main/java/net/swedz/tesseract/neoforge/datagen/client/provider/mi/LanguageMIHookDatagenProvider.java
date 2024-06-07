package net.swedz.tesseract.neoforge.datagen.client.provider.mi;

import aztech.modern_industrialization.MI;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.swedz.tesseract.neoforge.compat.mi.hook.MIHookTracker;

import java.util.function.Consumer;

public final class LanguageMIHookDatagenProvider extends LanguageProvider
{
	public LanguageMIHookDatagenProvider(GatherDataEvent event)
	{
		super(event.getGenerator().getPackOutput(), MI.ID, "en_us");
	}
	
	@Override
	protected void addTranslations()
	{
		for(Consumer<LanguageProvider> action : MIHookTracker.getLanguageEntries(MI.ID))
		{
			action.accept(this);
		}
	}
	
	@Override
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
}
