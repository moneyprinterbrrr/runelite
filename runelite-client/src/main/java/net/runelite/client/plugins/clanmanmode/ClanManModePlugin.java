package net.runelite.client.plugins.clanmanmode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.runelite.api.events.ClanChanged;
import net.runelite.api.events.ClanMemberJoined;
import net.runelite.api.events.ClanMemberLeft;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.client.eventbus.Subscribe;
import com.google.inject.Provides;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;

@PluginDescriptor(
	name = "Clan Man Mode",
	description = "Assists in clan PVP scenarios",
	tags = {"highlight", "minimap", "overlay", "players"}
)
public class ClanManModePlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ClanManModeConfig config;

	@Inject
	private ClanManModeOverlay ClanManModeOverlay;

	@Inject
	private ClanManModeTileOverlay ClanManModeTileOverlay;

	@Inject
	private ClanManModeMinimapOverlay ClanManModeMinimapOverlay;

	@Inject
	private Client client;

	@Provides
	ClanManModeConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ClanManModeConfig.class);
	}

	int wildernessLevel;
	int clanmin;
	int clanmax;
	int inwildy;
	int ticks;
	Map<String, Integer> clan = new HashMap<>();
	private List<String> clanMembers = new ArrayList<>();

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(ClanManModeOverlay);
		overlayManager.add(ClanManModeTileOverlay);
		overlayManager.add(ClanManModeMinimapOverlay);
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(ClanManModeOverlay);
		overlayManager.remove(ClanManModeTileOverlay);
		overlayManager.remove(ClanManModeMinimapOverlay);
		clan.clear();
		clanMembers.clear();
		ticks = 0;
		wildernessLevel = 0;
		clanmin = 0;
		clanmax = 0;
		inwildy = 0;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged state) {
		GameState gameState = state.getGameState();

		if (gameState == GameState.LOGIN_SCREEN || gameState == GameState.CONNECTION_LOST || gameState == GameState.HOPPING)
		{
			ticks = 0;
			clanMembers.clear();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		ticks++;
		final Player localPlayer = client.getLocalPlayer();
		if (!clan.containsKey(localPlayer.getName())) {
			clan.put(localPlayer.getName(), localPlayer.getCombatLevel());
		}
		WorldPoint a = localPlayer.getWorldLocation();
		int underLevel = ((a.getY() - 9920) / 8) + 1;
		int upperLevel = ((a.getY() - 3520) / 8) + 1;
		wildernessLevel = a.getY() > 6400 ? underLevel : upperLevel;
		inwildy = client.getVar(Varbits.IN_WILDERNESS);
		if (clan.size() > 0) {
			clanmin = Collections.min(clan.values());
			clanmax = Collections.max(clan.values());
		}
	}

	@Subscribe
	public void onClanMemberJoined(ClanMemberJoined event)
	{
		final ClanMember member = event.getMember();

		if (member.getWorld() == client.getWorld())
		{
			final Player local = client.getLocalPlayer();
			final String memberName = Text.toJagexName(member.getUsername());

			for (final Player player : client.getPlayers())
			{
				if (player != null && player != local && memberName.equals(Text.toJagexName(player.getName())))
				{
					clanMembers.add(Text.toJagexName(player.getName()));
					break;
				}
			}
		}
	}

	@Subscribe
	public void onClanMemberLeft(ClanMemberLeft event)
	{
		final ClanMember member = event.getMember();

		if (member.getWorld() == client.getWorld())
		{
			final String memberName = Text.toJagexName(member.getUsername());
			final Iterator<String> each = clanMembers.iterator();

			while (each.hasNext())
			{
				if (memberName.equals(Text.toJagexName(each.next())))
				{
					each.remove();
					break;
				}
			}
		}
	}

	@Subscribe
	public void onPlayerSpawned(PlayerSpawned event)
	{
		final Player local = client.getLocalPlayer();
		final Player player = event.getPlayer();

		if (player != local && player.isClanMember())
		{
			clanMembers.add(Text.toJagexName(player.getName()));
		}
	}

	@Subscribe
	public void onClanChanged(ClanChanged event)
	{
		clanMembers.clear();
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event) {
		if (!config.hideAtkOpt()) {
			return;
		}
		if (client.getGameState() != GameState.LOGGED_IN) {
			return;
		}

		final String option = Text.removeTags(event.getOption()).toLowerCase();

		if (option.equals("attack")) {
			final Pattern ppattern = Pattern.compile(".*<col=ffffff>(.+?)<col=.*");
			final Matcher pmatch = ppattern.matcher(event.getTarget());
			pmatch.find();
//			System.out.println(event.getTarget());
//			System.out.println(pmatch.matches());
//			System.out.println(pmatch.group(1));
			if (pmatch.matches()) {
				String match_username = pmatch.group(1);
				if (match_username != null) {
//					if (clan.containsKey(pmatch.group(1).replace(" ", " "))) {
//					if (clan.containsKey(pmatch.group(1))) {
//					if (isClanMember(match_username)) {
					if (clanMembers.contains(Text.toJagexName(match_username))) {
						MenuEntry[] entries = client.getMenuEntries();
//						System.out.println(entries);
						entries = ArrayUtils.removeElement(entries, entries[entries.length - 1]);
						client.setMenuEntries(entries);
					}
				}
			}
		}
	}

//	private boolean isClanMember(String username)
//	{
//		for (String member: clanMembers) {
//			if (Text.toJagexName(member).equals(Text.toJagexName(username))) {
//				return true;
//			}
//		}
//		return false;
//	}


}
