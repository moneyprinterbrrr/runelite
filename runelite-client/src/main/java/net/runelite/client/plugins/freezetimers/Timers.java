package net.runelite.client.plugins.freezetimers;

import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

@Slf4j
@Singleton
public class Timers
{

	@Inject
	private Client client;

//	private HashMap<Actor, HashMap<TimerType, Long>> timerMap = new HashMap<>();
	private HashMap<Actor, HashMap<TimerType, TimerWorldPoint>> timerMap = new HashMap<>();

	public void gameTick()
	{
		// Movement for freeze timers

		// Remove TB timer if not in wilderness
	}

	public void setTimerEnd(Actor actor, TimerType type, long n)
	{
		if (!timerMap.containsKey(actor))
		{
			timerMap.put(actor, new HashMap<>());
		}
		TimerWorldPoint twp = new TimerWorldPoint(n, actor.getWorldLocation());
		timerMap.get(actor).put(type, twp);
	}

	public TimerWorldPoint getTimerEnd(Actor actor, TimerType type)
	{
		if (!timerMap.containsKey(actor))
		{
			timerMap.put(actor, new HashMap<>());
		}
		TimerWorldPoint default_twp = new TimerWorldPoint((long) 0, actor.getWorldLocation());
		TimerWorldPoint twp = timerMap.get(actor).getOrDefault(type, default_twp);
		return twp;
	}

	public boolean areAllTimersZero(Actor actor)
	{
		for (TimerType type : TimerType.values())
		{
			if (getTimerEnd(actor, type).getTime() != 0)
			{
				return false;
			}
		}
		return true;
	}

}
