package jerklib.parsers;

import jerklib.EventToken;
import jerklib.Session;
import jerklib.events.IRCEvent;
import jerklib.events.impl.KillEventImpl;

/**
 * @author mohadib
 *
 */
public class KillParser implements CommandParser
{
	public IRCEvent createEvent(EventToken token, IRCEvent event)
	{
		Session session = event.getSession();
		
		String msg = "";
		if (token.args().size() > 3)
		{
		    msg = token.joinTokens(3);
		}
		
		return new KillEventImpl
		(
			token.data(), 
			session, 
			token.getTokenAt(0), //			token.nick(), // byWho
			token.arg(2), // victim
			msg // message
		);
	}
}
