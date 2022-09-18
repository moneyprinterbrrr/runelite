package net.runelite.client.plugins.specbar;

import net.runelite.api.Client;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import javax.inject.Inject;

// Notes:
// Depends on SpecbarRedraw.rs2asm
// Info on how rs2asm works: https://github.com/runelite/runelite/wiki/Working-with-client-scripts
// Updates can be found at RuneStar: https://github.com/Joshua-F/cs2-scripts/blob/master/scripts/%5Bproc,settings_special_attack_bar_tooltip%5D.cs2
// Updates in flat cache: https://github.com/Abextm/osrs-cache/releases
// Source: https://github.com/open-osrs/runelite/blob/master/runelite-client/src/main/scripts/SpecbarRedraw.rs2asm
// Source: https://github.com/xKylee/plugins-source/blob/master/specbar/src/main/java/net/runelite/client/plugins/specbar/SpecBarPlugin.java

@PluginDescriptor(
        name = "Specbar",
        description = "Shows specbar",
        tags = {"pvp"}
)
public class SpecbarPlugin extends Plugin
{
    @Inject
    private Client client;

    @Subscribe
    private void onScriptCallbackEvent(ScriptCallbackEvent event)
    {
        if (!"drawSpecbarAnyway".equals(event.getEventName()))
        {
            return;
        }

        int[] iStack = client.getIntStack();
        int iStackSize = client.getIntStackSize();
        iStack[iStackSize - 1] = 1;
    }

}
