package jerklib.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jerklib.EventToken;
import jerklib.events.IRCEvent;
import jerklib.events.impl.InviteEventImpl;

public class InviteParser implements CommandParser
{
	public IRCEvent createEvent(EventToken token, IRCEvent event)
	{
		String data = token.data();
		Pattern p = Pattern.compile("^:(\\S+)!(\\S+)@(\\S+)\\sINVITE\\s(\\S+)\\s(\\S+)");
		Matcher m = p.matcher(data);
		m.matches();

		/*
		String channel = m.group(4);
		String nick = m.group(1);
		String userName = m.group(2);
		String hostName = m.group(3);
		String group5 = m.group(5);
		System.out.println("channel : " + channel );
		System.out.println("nick : " + nick );
		System.out.println("userName : " + userName );
		System.out.println("hostName : " + hostName );
		System.out.println("group5 : " + group5 );
		*/

		return new InviteEventImpl
		(
			m.group(5).toLowerCase(), 
			m.group(1), 
			m.group(2), 
			m.group(3), 
			data, 
			event.getSession()
		); 
	}
}
