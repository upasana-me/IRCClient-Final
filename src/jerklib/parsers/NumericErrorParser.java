package jerklib.parsers;

import jerklib.EventToken;
import jerklib.events.IRCEvent;
import jerklib.events.impl.NumericEventImpl;

public class NumericErrorParser implements CommandParser
{
	public IRCEvent createEvent(EventToken token, IRCEvent event)
	{
		return new NumericEventImpl
		(
		 token.joinTokens(3), 
				token.data(), 
				token.numeric(), 
				event.getSession()
		); 
	}
}
