package jerklib.events;

/**
 * Event fired when someone is kicked from a channel
 * @author mohadib
 * @see Channel#kick(String, String)
 */
public interface KillEvent extends IRCEvent
{
    /**
     * Gets the nick of the user who
     * did the killing
     *
     * @return nick
     */
    public String byWho();

    /**
     * Gets the kill message
     *
     * @return message
     */
    public String getMessage();

    /**
     * Gets the nick of who was kicked
     *
     * @return who was killed
     */
    public String getWho();
}
