package jerklib.events.impl;

import jerklib.Session;
import jerklib.events.KillEvent;

/**
 * @see KickEvent
 * @author mohadib
 *
 */
public class KillEventImpl implements KillEvent
{

	private final Type type = Type.KILL_EVENT;
	private final String byWho, who, message, rawEventData;
	private final Session session;

	public KillEventImpl
	(
		String rawEventData, 
		Session session, 
		String byWho, 
		String who, 
		String message
	)
	{
		this.rawEventData = rawEventData;
		this.session = session;
		this.byWho = byWho;
		this.who = who;
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see jerklib.events.KillEvent#byWho()
	 */
	public String byWho()
	{
		return byWho;
	}

	/* (non-Javadoc)
	 * @see jerklib.events.KillEvent#getWho()
	 */
	public String getWho()
	{
		return who;
	}

	/* (non-Javadoc)
	 * @see jerklib.events.KillEvent#getMessage()
	 */
	public String getMessage()
	{
		return message;
	}

	/* (non-Javadoc)
	 * @see jerklib.events.IRCEvent#getRawEventData()
	 */
	public String getRawEventData()
	{
		return rawEventData;
	}

	/* (non-Javadoc)
	 * @see jerklib.events.IRCEvent#getSession()
	 */
	public Session getSession()
	{
		return session;
	}

	/* (non-Javadoc)
	 * @see jerklib.events.IRCEvent#getType()
	 */
	public Type getType()
	{
		return type;
	}

}
