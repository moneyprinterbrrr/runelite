package net.runelite.client.plugins.freezetimers;

import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;

@Slf4j
@Singleton
public class TimerWorldPoint
{
	@Getter
	private Long time;

	@Getter
	private WorldPoint worldPoint;

	TimerWorldPoint(Long time, WorldPoint worldPoint)
	{
		this.time = time;
		this.worldPoint = worldPoint;
	}
}
