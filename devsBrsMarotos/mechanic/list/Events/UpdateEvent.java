/*

 */
package devsBrsMarotos.mechanic.list.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Carlos André Feldmann Júnior
 */
public class UpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private UpdateType _type;

    public UpdateEvent(UpdateType example)
    {
        this._type = example;
    }

    public UpdateType getType()
    {
        return this._type;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
