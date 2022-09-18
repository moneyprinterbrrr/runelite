package net.runelite.api.events;

import javax.annotation.Nullable;
import lombok.Data;
import net.runelite.api.widgets.WidgetInfo;

/**
 * A MenuManager widget menu was clicked. This event is fired only for MenuManager managed custom menus.
 */
@Data
@Deprecated
public class WidgetMenuOptionClicked
{
    /**
     * The clicked menu option.
     */
    private String menuOption;
    /**
     * The clicked menu target.
     */
    private String menuTarget;
    /**
     * The WidgetInfo of the widget that was clicked, if available.
     */
    @Nullable
    private WidgetInfo widget;
    /**
     * The widget id of the widget that was clicked.
     */
    private int widgetId;
}