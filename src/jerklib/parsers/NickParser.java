package jerklib.parsers;

import jerklib.EventToken;
import jerklib.Session;
import jerklib.events.IRCEvent;
import jerklib.events.impl.NickChangeEventImpl;

public class NickParser implements CommandParser
{
	public IRCEvent createEvent(EventToken token, IRCEvent event)
	{
		Session session = event.getSession();
		String oldNick = token.nick();
		String newNick = token.arg(0);
		if(oldNick.equals(session.getNick()))
		   {
		       session.setNick(newNick);
		   }
		return new NickChangeEventImpl
		(
				token.data(), 
				session, 
				oldNick, // old
				newNick, // new nick
				token.hostName(), // hostname
				token.userName() // username
		); 
	}
}
