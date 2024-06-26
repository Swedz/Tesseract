package net.swedz.tesseract.neoforge.localizedlistener;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class LocalizedListenerMultiMap
{
	private final Map<LevelAccessor, Map<ChunkPos, Map<Class<?>, Set<LocalizedListener<?>>>>> storage = Maps.newHashMap();
	
	public <E extends Event> void add(LevelAccessor level, ChunkPos chunk, Class<E> listenerClass, LocalizedListener<E> listener)
	{
		storage.computeIfAbsent(level, (__) -> Maps.newHashMap())
				.computeIfAbsent(chunk, (__) -> Maps.newHashMap())
				.computeIfAbsent(listenerClass, (__) -> Sets.newHashSet())
				.add(listener);
	}
	
	public <E extends Event> void remove(LevelAccessor level, ChunkPos chunk, Class<E> listenerClass, LocalizedListener<E> listener)
	{
		Map<ChunkPos, Map<Class<?>, Set<LocalizedListener<?>>>> chunkMap = storage.get(level);
		if(chunkMap == null)
		{
			return;
		}
		Map<Class<?>, Set<LocalizedListener<?>>> listenerMap = chunkMap.get(chunk);
		if(listenerMap == null)
		{
			return;
		}
		Set<LocalizedListener<?>> listeners = listenerMap.get(listenerClass);
		if(listeners == null)
		{
			return;
		}
		
		if(!listeners.remove(listener))
		{
			throw new RuntimeException("Could not remove element at position " + chunk + " as it does not exist.");
		}
		
		if(listeners.size() == 0)
		{
			listenerMap.remove(listenerClass);
			if(listenerMap.size() == 0)
			{
				chunkMap.remove(chunk);
				if(chunkMap.size() == 0)
				{
					storage.remove(level);
				}
			}
		}
	}
	
	@Nullable
	public <E extends Event> Set<LocalizedListener<E>> get(LevelAccessor level, ChunkPos chunk, Class<E> listenerClass)
	{
		Map<ChunkPos, Map<Class<?>, Set<LocalizedListener<?>>>> chunkMap = storage.get(level);
		if(chunkMap == null)
		{
			return null;
		}
		Map<Class<?>, Set<LocalizedListener<?>>> listenerMap = chunkMap.get(chunk);
		if(listenerMap == null)
		{
			return null;
		}
		Set<LocalizedListener<?>> listeners = listenerMap.get(listenerClass);
		if(listeners == null)
		{
			return null;
		}
		return listeners.stream()
				.map((l) -> (LocalizedListener<E>) l)
				.collect(Collectors.toSet());
	}
	
	public int size()
	{
		return storage.size();
	}
}
