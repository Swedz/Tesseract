package net.swedz.tesseract.neoforge.isolatedlistener;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.swedz.tesseract.neoforge.event.FarmlandLoseMoistureEvent;
import net.swedz.tesseract.neoforge.event.TreeGrowthEvent;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class IsolatedListeners
{
	private static boolean initialized;
	
	private static IsolatedListenerMultiMap listeners = new IsolatedListenerMultiMap();
	
	public static void init()
	{
		if(initialized)
		{
			throw new IllegalStateException("IsolatedListeners has already been initialized");
		}
		initialized = true;
		
		NeoForge.EVENT_BUS.addListener(ServerStoppedEvent.class, (__) -> serverStopCleanup());
		
		withListener(
				BlockEvent.FarmlandTrampleEvent.class,
				(event) -> event.getLevel() instanceof Level,
				(event) -> (Level) event.getLevel(),
				(event) -> new ChunkPos(event.getPos())
		);
		withListener(
				FarmlandLoseMoistureEvent.class,
				(event) -> event.getLevel() instanceof Level,
				(event) -> (Level) event.getLevel(),
				(event) -> new ChunkPos(event.getPos())
		);
		withListener(
				TreeGrowthEvent.class,
				(event) -> event.getLevel() instanceof Level,
				(event) -> (Level) event.getLevel(),
				(event) -> new ChunkPos(event.getPos())
		);
		withListener(
				BlockEvent.NeighborNotifyEvent.class,
				(event) -> event.getLevel() instanceof Level,
				(event) -> (Level) event.getLevel(),
				(event) -> new ChunkPos(event.getPos())
		);
	}
	
	public static <E extends Event> IsolatedListener<E> register(Level level, ChunkPos chunk, Class<E> listenerClass, IsolatedListener<E> listener)
	{
		listeners.add(level, chunk, listenerClass, listener);
		return listener;
	}
	
	public static <E extends Event> IsolatedListener<E> register(Level level, Iterable<ChunkPos> chunks, Class<E> listenerClass, IsolatedListener<E> listener)
	{
		for(ChunkPos chunk : chunks)
		{
			register(level, chunk, listenerClass, listener);
		}
		return listener;
	}
	
	public static <E extends Event> void unregister(Level level, ChunkPos chunk, Class<E> listenerClass, IsolatedListener<E> listener)
	{
		listeners.remove(level, chunk, listenerClass, listener);
	}
	
	public static <E extends Event> void unregister(Level level, Iterable<ChunkPos> chunks, Class<E> listenerClass, IsolatedListener<E> listener)
	{
		for(ChunkPos chunk : chunks)
		{
			unregister(level, chunk, listenerClass, listener);
		}
	}
	
	private static <E extends Event> void withListener(Class<E> listenerClass, Predicate<E> condition, Function<E, Level> level, Function<E, ChunkPos> chunk)
	{
		NeoForge.EVENT_BUS.addListener(listenerClass, (event) ->
		{
			if(condition.test(event))
			{
				Set<IsolatedListener<E>> listenerInstances = listeners.get(level.apply(event), chunk.apply(event), listenerClass);
				if(listenerInstances != null)
				{
					for(IsolatedListener<E> listener : listenerInstances)
					{
						listener.on(event);
					}
				}
			}
		});
	}
	
	private static void serverStopCleanup()
	{
		if(listeners.size() != 0)
		{
			listeners = new IsolatedListenerMultiMap();
		}
	}
}
