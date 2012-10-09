import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.ServerInformation;
import jerklib.Channel;

import jerklib.events.*;

import jerklib.events.IRCEvent.Type;
import jerklib.events.ErrorEvent.ErrorType;

import jerklib.events.dcc.DccEvent;
import jerklib.events.dcc.DccChatEvent;

import jerklib.events.modes.*;

import jerklib.events.modes.ModeEvent.ModeType;
import jerklib.events.modes.ModeAdjustment.Action;

import jerklib.tasks.TaskImpl;
import jerklib.listeners.IRCEventListener;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Vector;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

import java.net.InetAddress;

public class Connection implements Runnable, IRCEventListener
{
    private MainWindow mw;
    private EditNetList enl;
    private String[] nicks;
    private String nick_name;
    private String user_name;
    private String real_name;
    private int cur_nick;
    private int curServer;
    private String network_name;
    private HashMap<String, String> serversPorts;
    private Set<Map.Entry<String, String>> servPortES;
    private Iterator<Map.Entry<String, String>> serverPortIter;
    private Map.Entry<String, String> serverPort;
    private TreeMap<String, String> channels;
    private TreeMap<String, TreeMap<String, Vector<String>>> chanName_2_chanMembers;
    private TreeMap<String, String> previousTopicTime;
    //    private IdentityHashMap<TabGroup, Session> tabGroup2Session;

    private Vector<String> nicks_pms;

    //    private Socket[] sockets;

    private int no_of_sockets;
    private int free_socket;

    private static ConnectionManager manager;
    private static Profile defaultProfile;

    private Session session;
    private Profile profile;

    private TabGroup tabGroup;

    private boolean waitingForNextWhoWas;
    private boolean connected;

    private enum EventType 
    {
	CHANNEL_NICKS,
	END_OF_NAMES_LIST,
        NO_SUCH_NICK_OR_CHANNEL,
	STARTING_INFO,
        WHOWAS,	    
        END_WHOWAS,
        END_WHOIS,
        END_WHO
    }

    static 
    {
	defaultProfile = new Profile("");
	manager = new ConnectionManager(defaultProfile);
    }

    public Connection(HashMap<String, String> servPorts)
    {
	cur_nick = 0;
	channels = new TreeMap<String, String>();
	chanName_2_chanMembers = new TreeMap<String, TreeMap<String, Vector<String>>>();
	nicks_pms = new Vector<String>();
	serversPorts = servPorts;
	curServer = 0;
	previousTopicTime = new TreeMap<String, String>();
	servPortES = serversPorts.entrySet();
	serverPortIter = servPortES.iterator();
	waitingForNextWhoWas = false;
	//	tabGroup2Session = new IdentityHashMap<TabGroup, Session>();
	//	sessions = new Vector<Session>();

	//	free_socket = 0;
	//	no_of_sockets = 100;
	//	sockets = new Socket[no_of_sockets];
    }

    private Map.Entry<String, String> getNextServPort()
    {
	if(serverPortIter.hasNext())
	    return serverPortIter.next();
	else 
	    return null;
    }

    public void initialise(String n[], String username, String realname, String net_name)
    {
	nicks = n;
	nick_name = nicks[cur_nick];
	user_name = username;
	real_name = realname;
	network_name = net_name;
    }

    public void run()
    {
	//	System.out.println("In run");
	connect();
    }

    public void setEditNetList(EditNetList e)
    {
	enl = e;
    }

    public void receiveEvent(IRCEvent e)
    {
	boolean initial_text = (e.getType() == Type.SERVER_VERSION_EVENT || e.getType() == Type.SERVER_INFORMATION );

	//	System.out.println(e.getRawEventData());
	if( e.getType() == Type.NICK_IN_USE )
	    {
		NickInUseEvent ne = (NickInUseEvent)e;
		String inUseNick = ne.getInUseNick();
		if( !connected )
		    {
			cur_nick++;
			nick_name = nicks[cur_nick];
			tabGroup.setServerInfo(inUseNick + " is already in use.");
			tabGroup.setServerInfo("Retrying with " + nick_name);
			tabGroup.setNickButtonText(nick_name);
		    }
		else
		    {
			tabGroup.setServerInfo(inUseNick + " is already in use.");			
		    }
	    }
	else if( e.getType() == Type.AWAY_EVENT )
	    {
		AwayEvent ae = (AwayEvent)e;
		String nick = ae.getNick();
		String awayMessage = ae.getAwayMessage();
		tabGroup.setAway(nick);
	    }
	else if( e.getType() == Type.NOTICE )
	    {
		NoticeEvent ne = (NoticeEvent)e;
		String noticeMessage = ne.getNoticeMessage();
		String byWho = ne.byWho();
		String toWho = ne.toWho();
		Channel channel = ne.getChannel();

		if( toWho.equals("*"))
		    tabGroup.setServNotice( noticeMessage );		
		else if( channel != null )
		    {
			String channelName = channel.getName();
			tabGroup.setChannelNotice(channelName, byWho, noticeMessage);
		    }
		else 
		    {
			tabGroup.setNotice(byWho, noticeMessage);
		    }
	    }
	else if (e.getType() == Type.DCC_EVENT)
	    {
		DccEvent de = (DccEvent)e;
		System.out.println("DccEvent : " + de.getCtcpString());
		if( de.getDccType() == DccEvent.DccType.CHAT )
		    {
			System.out.println("Dcc Chat Event");
			DccChatEvent dce = (DccChatEvent)de;
			InetAddress ip = dce.getIp();
			String ipStr = ip.getHostAddress();
			int port = dce.getPort();
			String nick = dce.getNick();
			
			System.out.println("ipStr : " + ipStr);
			System.out.println("port : " + port);
			System.out.println("nick : " + nick);
			tabGroup.setDccChatInvitation(nick, ipStr, port);
		    }
		else if( de.getDccType() == DccEvent.DccType.ACCEPT )
		    {
			System.out.println("Dcc ACCEPT Event");
		    }
		else if( de.getDccType() == DccEvent.DccType.SEND )
		    {
			System.out.println("Dcc SEND Event");
		    }
		else if( de.getDccType() == DccEvent.DccType.RESUME )
		    {
			System.out.println("Dcc RESUME Event");			
		    }
		else if( de.getDccType() == DccEvent.DccType.UNKNOWN )
		    {
			System.out.println("Dcc UNKNOWN Event");			
		    }
		else
		    {
			System.out.println("Dcc Else Event");			
		    }
	    }
	else if( e.getType() == Type.QUIT )
	    {
		System.out.println("In quit");
		QuitEvent qe = (QuitEvent)e;
		String nick = qe.getNick();
		String message = qe.getQuitMessage();
		Vector<Channel> channelsQuit = new Vector<Channel>(qe.getChannelList());
		Vector<Channel> userChannels = new Vector<Channel>(session.getChannels());
		for( int i = 0; i < channelsQuit.size(); i++ )
		    {
			if( channelsQuit.elementAt(i).equals(userChannels.elementAt(i)) )
			    {
				String channelName = channelsQuit.elementAt(i).getName();
				tabGroup.setText(channelName, nick + " has quit (" + message + ")");
				TreeMap<String, Vector<String>> status_2_members = setStatus2Members(channelsQuit.elementAt(i), channelName);
				tabGroup.set_chan_members(channelName, status_2_members);
			    }
		    }
	    }
	else if( e.getType() == Type.MOTD )
	    {
		//		System.out.println("Motd Event : ");
		MotdEvent me = (MotdEvent)e;
		String motd = me.getRawEventData();
		//		System.out.println("motd  : " + motd);
		String s = extract_motd(motd);
 
		//		System.out.println("s  : " + s);
		tabGroup.setText(network_name, s);
	    }
	else if( e.getType() == Type.CONNECT_COMPLETE )
	    {
		connected = true;
		channels = enl.get_auto_join_channels(network_name);
		Set<Map.Entry<String, String>> chan_pswd = channels.entrySet();
		Iterator<Map.Entry<String,String>> i = chan_pswd.iterator();
		while( i.hasNext() )
		    {
			Map.Entry<String, String> me = i.next();
			e.getSession().join(me.getKey(), me.getValue());
		    }

		try
		    {
			Thread.sleep(2000);
		    }
		catch(InterruptedException ie)
		    {}
	    }
	else if( e.getType() == Type.JOIN )
	    {
		JoinEvent je = (JoinEvent)e;
		String channelName = je.getChannelName();
		String nick = je.getNick();
		String hostName = je.getHostName();
		String userName = je.getUserName();
		tabGroup.setJoinText(channelName, nick, userName, hostName );
		//		tabGroup.setText( channelName, nick + " (" + userName + "@" + hostName + ") has joined " + channelName );

		TreeMap<String, Vector<String>> status_2_members = setStatus2Members(session.getChannel(channelName), channelName);
		tabGroup.set_chan_members(channelName, status_2_members);		
	    }
	else if( e.getType() == Type.PART )
	    {
		//		System.out.println("In PART event");
		PartEvent pe = (PartEvent)e;
		String channelName = pe.getChannelName();
		String hostName = pe.getHostName();
		String partMessage = pe.getPartMessage();
		String userName = pe.getUserName();
		String partedNick = pe.getWho();

		if(partedNick.equals(nick_name) )
		    {
			if(!tabGroup.tabAlreadyRemoved(channelName))
			    tabGroup.removeTab(channelName);
			previousTopicTime.remove(channelName);
		    }
		else
		    {
			tabGroup.setPartText(channelName, partedNick, userName, hostName, partMessage);

			//			tabGroup.setText(channelName, partedNick + " (" + userName + "@" + hostName + ") has left " + channelName + " (" + partMessage + ")");

			TreeMap<String, Vector<String>> status_2_members = setStatus2Members(session.getChannel(channelName), channelName);
			tabGroup.set_chan_members(channelName, status_2_members);
		    }
	    }
	else if( e.getType() == Type.NICK_LIST_EVENT )
	    {
		NickListEvent nle = (NickListEvent)e;
		Channel channel = nle.getChannel();
		String channel_name = channel.getName();
		//		System.out.println("NICK_LIST EVENT for " + channel_name);

		TreeMap<String, Vector<String>> status_2_members = setStatus2Members(channel, channel_name);
		tabGroup.set_chan_members(channel_name, status_2_members);
		//		System.out.println("After setting nicks for " + channel_name);
	    }
	else if( e.getType() == Type.JOIN_COMPLETE )
	    {
		JoinCompleteEvent jce = (JoinCompleteEvent)e;
		Channel channel = jce.getChannel();
		String topic = channel.getTopic();		
		if(topic.equals(""))
		    {
			previousTopicTime.put(channel.getName(),"NOTOPIC");
		    }
		
		String channel_name = channel.getName();
		
		if( !tabGroup.channelExisted(channel_name))
		   {
		       if( !channels.containsKey(channel_name))
			   channels.put(channel_name," ");
		       tabGroup.create_chan_tab(channel_name);
		       tabGroup.setChanJoinText(channel_name, Constants.chan_join_text + channel_name);

 		   }
		else
		    {
		       tabGroup.reInitialiseChannel(channel_name);
		    }
	    }
	else if( e.getType() == Type.TOPIC )
	    {
		TopicEvent te = (TopicEvent)e;
		Channel channel = te.getChannel();
		String channelName = channel.getName();
		String topic = channel.getTopic();
		String topicSetter = channel.getTopicSetter();
		tabGroup.setTopic(channelName, topic);
		
		if( previousTopicTime.containsKey(channelName) )
		    {
			tabGroup.setText( channelName, topicSetter + " has changed the topic to : " + topic );
		    }
		else 
		    {
			Date topicDate = channel.getTopicSetTime();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM);
			previousTopicTime.put(channelName, dateFormat.format(topicDate));
			tabGroup.setTopicText( channelName, topic ); //Constants.topicStr + channelName + " is : " + topic );
			tabGroup.setTopicSetTimeText( channelName, topicSetter, dateFormat.format(topicDate) );
		    }
	    }
	else if( e.getType() == Type.CHANNEL_LIST_EVENT )
	    {
		ChannelListEvent cle = (ChannelListEvent)e;
		String channelName = cle.getChannelName();
		String topic = cle.getTopic();		
		if(topic.equals(""))
		    {
			previousTopicTime.put(channelName,"NOTOPIC");
		    }
	    }
	else if( e.getType() == Type.CHANNEL_MESSAGE )
	    {
		MessageEvent me = (MessageEvent)e;
		String message = me.getMessage();
		String nick = me.getNick();
		Channel channel = me.getChannel();
		String channelName = channel.getName();
		boolean containsNick = ifMessageContainsNick(message);
		if( containsNick )
		    tabGroup.setHighlightedMessage(channelName, nick, message);
		else
		    tabGroup.setRegularMessage(channelName, nick, message );
		//		tabGroup.setText(channelName, nick, message);
	    }
	else if( e.getType() == Type.NICK_CHANGE )
	    {
		NickChangeEvent nce = (NickChangeEvent)e;
		String newNick = nce.getNewNick();
		String userName = nce.getUserName();
		String oldNick = nce.getOldNick();

		//		System.out.println("After setting nicks for " + channel_name);
		if( oldNick.equals(nick_name) )
		    {
			nick_name = newNick;
			tabGroup.setSelfNickChangeText(newNick);
			//			tabGroup.appendToAllTa(Constants.selfNickChangeText + newNick );
			tabGroup.setNickButtonText(newNick );
			selfNickChange();
		    }
		else
		    {			
			nickChange(oldNick, newNick);
		    }
	    }
	
	else if( e.getType() == Type.PRIVATE_MESSAGE )
	    {
		MessageEvent me = (MessageEvent)e;
		String message = me.getMessage();
		String nick = me.getNick();
		String hostname = me.getHostName();
		if( !tabGroup.getPmExists(nick) )
		    {
			nicks_pms.add(nick);
			tabGroup.create_privmsg_tab(nick, hostname, message );
		    }
		else
		    {
			tabGroup.setRegularMessage( nick, nick, message );
		    }
	    }
	else if( e.getType() == Type.INVITE_EVENT )
	    {
		InviteEvent ie = (InviteEvent)e;
		String channelName = ie.getChannelName();
		String hostName = ie.getHostName();
		String nick = ie.getNick();
		String userName = ie.getUserName();
		//		String toBeDisplayed = "You have been invited to " + channelName + " by " + nick + " (" + hostName + ")";
		//		tabGroup.setTextOnSelectedTab(toBeDisplayed);
		tabGroup.setInvitationText(channelName, nick, hostName);
	    }
	else if( e.getType() == Type.ERROR )
	    {
		ErrorEvent ee = (ErrorEvent) e;
		if( ee.getErrorType() == ErrorType.UNRESOLVED_HOSTNAME )
		    {
			UnresolvedHostnameErrorEvent uhee = (UnresolvedHostnameErrorEvent)ee;
			String hostName = uhee.getHostName();
			String errMessage = "Unknown host, " + hostName + "\nCycling to next server in " + network_name;
			tabGroup.setText(network_name, errMessage);
			Map.Entry<String, String> servPortPair = getNextServPort();
			String server = servPortPair.getKey();
			String port = servPortPair.getValue();
			try
			    {
				session = manager.requestConnection(server,Integer.parseInt(port));
				session.addIRCEventListener(this);
			    }
			catch(NumberFormatException nfe)
			    {}
		    }
		else if( ee.getErrorType() == ErrorType.NUMERIC_ERROR )
		    {
			NumericErrorEvent nee = (NumericErrorEvent)ee;
			String errorMsg = nee.getErrorMsg();
			System.out.println("errorMsg : " + errorMsg);
			tabGroup.setErrorMessage(errorMsg);
		    }
	    }
	else if( e.getType() == Type.EXCEPTION )
	    {
		//		System.out.println("Exception");		
	    }
	else if( e.getType() == Type.KICK_EVENT )
	    {
		KickEvent ke = (KickEvent)e;
		String kickedNick = ke.getWho();
		String channelName = ke.getChannel().getName();
		String byWho = ke.byWho();
		String reason = ke.getMessage();
		if( kickedNick.equals(nick_name) )
		    {
			//			tabGroup.setText( channelName, "You have been kicked from " + channelName + " by " + byWho + " (" + reason + ")");
			tabGroup.setSelfKickText( channelName, byWho, reason );
			tabGroup.clearChanMembersList(channelName);
			//			System.out.println("In Conenction after clearChannelMembersList");
		    }
		else
		    {
			tabGroup.setKickText( channelName, kickedNick, byWho, reason );
			
			TreeMap<String, Vector<String>> status_2_members = setStatus2Members(session.getChannel(channelName), channelName);
			tabGroup.set_chan_members(channelName, status_2_members);
		    }		       		
	    }
	else if( e.getType() == Type.WHO_EVENT )
	    {
		WhoEvent we = (WhoEvent)e;
		String nick = we.getNick();
		String hostName = we.getHostName();
		String realName = we.getRealName();
		String userName = we.getUserName();
		String serverName = we.getServerName();
		String channelName = we.getChannel();
		String hereOrGone = (we.isAway() ? "Gone" : "Here");
		int hopCount = we.getHopCount();
		tabGroup.setWhoText(nick, userName, hostName, realName, hereOrGone, channelName, serverName, hopCount);
	    }
	else if( e.getType() == Type.WHOIS_EVENT )
	    {
		WhoisEvent wie = (WhoisEvent)e;
		String nick = wie.getNick();
		String userName = wie.getUser();
		String hostName = wie.getHost();
		String realName = wie.getRealName();
		String server = wie.whoisServer();
		String serverInfo = wie.whoisServerInfo();

		long secondsIdle = wie.secondsIdle();
		long hours = secondsIdle / 3600;
		long temp1 = secondsIdle % 3600;
		long minutes = temp1/60;
		long seconds = temp1 % 60;
		Date signOnTime = wie.signOnTime();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM);
		String signOnTimeStr = df.format(signOnTime);
		String idleTime =  hours + ":" + minutes + ":" + seconds;

		String whoisChannels = "";
		Vector<String> channels = new Vector<String>(wie.getChannelNames());
		for( int i = 0; i < channels.size(); i++ )
		    {
			whoisChannels += channels.elementAt(i);
			whoisChannels += " ";
		    }

		String rawData = wie.getRawEventData();
		String newLinesToks[] = rawData.split("\n");
		String tokens[] = newLinesToks[newLinesToks.length - 1].split(" ");
		String endOfList = "";
		if( tokens[1].equals("318") )
		    for( int i = 4; i < tokens.length; i++ )
			{
			    endOfList += tokens[i];
			    endOfList += " ";
			}
		tabGroup.setWhoisText(nick, userName, hostName, realName, server, serverInfo, idleTime, signOnTimeStr, whoisChannels, endOfList);
	    }    
	else if( e.getType() == Type.WHOWAS_EVENT )
	    {
		WhowasEvent wwe = (WhowasEvent)e;
		String nick = wwe.getNick();
		String hostName = wwe.getHostName();
		String realName = wwe.getRealName();
		String userName = wwe.getUserName();
		tabGroup.setWhoWasText(nick, userName, hostName, realName);
		waitingForNextWhoWas = true;
	    }
	else if( e.getType() == Type.MODE_EVENT )
	    {
		ModeEvent me = (ModeEvent)e;
		String rawEventData = e.getRawEventData();
		String setter = me.setBy();
		
		if( me.getModeType() == ModeType.USER )
		    {
			Vector<ModeAdjustment> modeAdjustments = new Vector<ModeAdjustment>(me.getModeAdjustments());
			for( int i = 0; i < modeAdjustments.size(); i++ )
			    {
				ModeAdjustment ma = modeAdjustments.elementAt(i);
				String argument = ma.getArgument();
				String action = "-";
				if( ma.getAction() == Action.PLUS )
				    action = "+";
				action += ma.getMode();
				String user = "";
				String tokens[] = rawEventData.split(" ");
				if( tokens.length > 2 )
				    {
					user = tokens[2];
				    }
				
				String toBeDisplayed = "";
				if( argument.equals("") || argument == null )
				    toBeDisplayed = "User mode for " + user + " is now " + action;
				else
				    toBeDisplayed = "User mode for " + user + " is now " + action + "(" + argument + ")";
				tabGroup.setText(network_name, toBeDisplayed);
			    }
		    }
		else if( me.getModeType() == ModeType.CHANNEL )
		    {
			Channel channel = me.getChannel();
			String channelName = channel.getName();
			Vector<ModeAdjustment> modeAdjustments = new Vector<ModeAdjustment>(me.getModeAdjustments());
			for(int i = 0; i < modeAdjustments.size(); i++ )
			    {
				ModeAdjustment ma = modeAdjustments.elementAt(i);
				String action = "-";
				if( ma.getAction() == Action.PLUS )
					action = "+";
				char modeChar = ma.getMode();
				String argument = ma.getArgument();
				String toBeDisplayed = "";
				if( modeChar == 'o' )
				    {
					if( action.equals("+") )
					    toBeDisplayed = setter + Constants.modePlusO + argument;
					else 
					    toBeDisplayed = setter + Constants.modeMinusO + argument;
					TreeMap<String, Vector<String>> status_2_members = setStatus2Members(session.getChannel(channelName), channelName);
					tabGroup.set_chan_members(channelName, status_2_members);
				    }
				else if( modeChar == 'v' )
				    {
					if( action.equals("+") )
					    toBeDisplayed = setter + Constants.modePlusV + argument;
					else
					    toBeDisplayed = setter + Constants.modeMinusV  + argument;					    
					TreeMap<String, Vector<String>> status_2_members = setStatus2Members(session.getChannel(channelName), channelName);
					tabGroup.set_chan_members(channelName, status_2_members);
				    }
				else if( modeChar == 'l' )
				    {
					if( action.equals("+") )
					    toBeDisplayed = setter + Constants.modePlusL + argument;
					else
					    toBeDisplayed = setter + Constants.modeMinusL + argument;					    					
				    }
				else if( modeChar == 'k' )
				    {
					if( action.equals("+") )
					    toBeDisplayed = setter + Constants.modePlusK + argument;
					else
					    toBeDisplayed = setter + Constants.modeMinusK + argument;
				    }
				else if( modeChar == 'b' )
				    {
					if( action.equals("+") )
					    toBeDisplayed = setter + Constants.modePlusB + argument;
					else
					    toBeDisplayed = setter + Constants.modeMinusB + argument;
				    }
				else if( modeChar == 'e' )
				    {
					if( action.equals("+") )
					    toBeDisplayed = setter + Constants.modePlusE + argument;
					else
					    toBeDisplayed = setter + Constants.modeMinusE + argument;
				    }
				else
				    toBeDisplayed = setter + " sets mode " + action + modeChar + " " +  channelName;
				if(!setter.equals(""))
				    tabGroup.setText(channelName, toBeDisplayed);
			    }
		    }
	    }
	else if( e.getType() == Type.CTCP_EVENT )
	    {
		CtcpEvent ce = (CtcpEvent)e;
		Channel channel = ce.getChannel();
		String ctcpString = ce.getCtcpString();
		String nick = ce.getNick();
		String ctcpMessage = ce.getMessage();

		if( ctcpString.startsWith("ACTION"))
		    {
			//			ctcpMessage = ctcpMessage.substring("ACTION".length() + 2 );
			//			ctcpMessage = ctcpMessage.substring(0, ctcpMessage.length() - 1 );

			ctcpString = ctcpString.substring("ACTION".length() + 1);
			String text = nick + " " + ctcpString;
			String channelName = channel.getName();
			tabGroup.setText(channelName, text);
		    }
		else if( ctcpString.startsWith("VERSION"))
		    {
			String text = "Received a CTCP " + ctcpString + " from " + nick;
			tabGroup.setText(tabGroup.getSelectedTab(), text);
			sendCTCPVersion(nick);
		    }
		else if( ctcpString.startsWith("TIME"))
		    {
			String text = "Received a CTCP " + ctcpString + " from " + nick;
			tabGroup.setText(tabGroup.getSelectedTab(), text);
			sendCTCPTime(nick);			
		    }
		else if( ctcpString.startsWith("PING"))
		    {
			String text = "Received a CTCP " + ctcpString + " from " + nick;
			tabGroup.setText(tabGroup.getSelectedTab(), text);
			//			sendCTCPTime(nick);			
		    }
	    }
	else 
	    {
		System.out.println("In last else.");
		System.out.println(e.getRawEventData());

		String rawData = e.getRawEventData();
		//		String suffix = extractSuffix(rawData);
		String tokens[] = rawData.split(" ");
		String prefix = extractNumericOrString(tokens);
		EventType eventType = getEventType(prefix);
		
		boolean endWhoIsWas = ( ( eventType == EventType.END_WHO ) || 
					( eventType == EventType.END_WHOIS ) ||
					( eventType == EventType.END_WHOWAS ) );

		if( eventType == EventType.CHANNEL_NICKS || eventType == EventType.END_OF_NAMES_LIST )
		    {}
		else if( eventType == EventType.NO_SUCH_NICK_OR_CHANNEL )
		    {
			//extract group 3
		    }
		else if( eventType == EventType.STARTING_INFO )
		    {
			String startingInfo = extractStartInfo(rawData);
			System.out.println("startingInfo = " + startingInfo );
			tabGroup.setText(network_name, startingInfo );
		    }
		else if( eventType == EventType.WHOWAS )
		    {
			waitingForNextWhoWas = false;
			String nick = tokens[3];
			String remainingText = "";
			for(int i = 4; i < tokens.length; i++ )
			    {
				remainingText += tokens[i];
				remainingText += " ";
			    }
			tabGroup.setWhoWasRemainingText(nick, remainingText);
		    }
		else if( endWhoIsWas )
		    {
			String nick = tokens[3];
			String remainingText = "";
			for(int i = 4; i < tokens.length; i++ )
			    {
				remainingText += tokens[i];
				remainingText += " ";
			    }
			System.out.println("nick : " + nick );
			System.out.println("remainingText : " + remainingText);
			tabGroup.setWhoWasRemainingText(nick, remainingText);			
		    }
		/*
		if( s.equals("") )
		    {
			s = extract_start_info(rawData);
			tabGroup.setText(network_name, s);
		    }
		else
		    {
			String[] tokens_newlines = s.split("\n");

			for( int i = 0; i < tokens_newlines.length; i++ )
			    {
				String token = tokens_newlines[i];
				String[] tokens_ws = token.split(" ");
				if( tokens_ws.length >= 2 )
				    {
					if( !(channels.containsKey(tokens_ws[1]) || channels.containsKey(tokens_ws[0])) )
					    {
						tabGroup.setText(network_name, s);
					    }
				    }
				else
				    {
					tabGroup.setText(network_name, s);
				    }
			    }
		    }
		*/
	    }
	
    }

    public void setMainWindow(MainWindow m)
    {
	mw = m;
    }

    public void setTabGroup(TabGroup tabGroup)
    {
	this.tabGroup = tabGroup;
    }

    private EventType getEventType(String str)
    {
	System.out.println("In getEventType, str = " + str);
	if(str.equals("353"))
	    return EventType.CHANNEL_NICKS;
	else if(str.equals("366"))
	    return EventType.END_OF_NAMES_LIST ;
	else if(str.equals("401"))
	    return EventType.NO_SUCH_NICK_OR_CHANNEL;
	//	else if(str.equals("KICK"))
	    //	    return EventType.KICK_EVEN;
	else if(str.equals("001") || str.equals("002") || str.equals("003") || str.equals("004") || str.equals("005") )
	    return EventType.STARTING_INFO;
	else if(str.equals("312") && waitingForNextWhoWas )
	    return EventType.WHOWAS;
	else if(str.equals("315"))
	    return EventType.END_WHO;
	else if(str.equals("318"))
	    return EventType.END_WHOIS;
	else if(str.equals("369"))
	    return EventType.END_WHOWAS;
	return null;
    } 

    private void nickChange(String oldNick, String newNick)
    {
	Vector<Channel> channels = new Vector<Channel>(session.getChannels());
	for(int i = 0; i < channels.size(); i++ )
	    {
		Vector<String> nicks = new Vector<String>(channels.elementAt(i).getNicks());
		if( nicks.contains(newNick))
		    {
			String channelName = channels.elementAt(i).getName();
			tabGroup.setNickChangeText(channels.elementAt(i).getName(), oldNick, newNick );
			TreeMap<String, Vector<String>> status_2_members = setStatus2Members(channels.elementAt(i), channelName);
			tabGroup.set_chan_members(channelName, status_2_members);
		    }
	    }
    }

    private void selfNickChange()
    {
	Vector<Channel> channels = new Vector<Channel>(session.getChannels());
	for(int i = 0; i < channels.size(); i++ )
	    {
		String channelName = channels.elementAt(i).getName();
		TreeMap<String, Vector<String>> status_2_members = setStatus2Members(channels.elementAt(i), channelName);
		tabGroup.set_chan_members(channelName, status_2_members);
	    }
    }

    private TreeMap<String, Vector<String>> setStatus2Members(Channel channel, String channelName)
    {
	Vector<String> ops = new Vector<String>(channel.getNicksForMode(ModeAdjustment.Action.valueOf("PLUS"),'o'));
	//		treeset.addAll(ops);
	TreeMap<String, Vector<String>> status_2_members = new TreeMap<String, Vector<String>>();
	status_2_members.put("o", ops);
		
	Vector<String> voiced = new Vector<String>(channel.getNicksForMode(ModeAdjustment.Action.valueOf("PLUS"),'v'));
	//		treeset.addAll(voiced);
	status_2_members.put("v", voiced);

	Vector<String> rest = new Vector<String>(channel.getNicks());
	Vector<String> rest_copy = new Vector<String>(rest.subList(0, rest.size()));
	int rest_size = rest.size();
	
	for(int i = 0; i < rest_copy.size(); i++ )
	    {
		String s = rest_copy.elementAt(i);
		try
		    {
			if( status_2_members.get("o").contains(s) || status_2_members.get("v").contains(s) )
			    {
				rest.remove(s);
			    }
			else
			    {
				//			treeset.add(s);
			    }
		    }
		catch(NullPointerException npe)
		    {}
	    }
	
	status_2_members.put("r", rest);
	status_2_members.put("a", new Vector<String>());
					
	chanName_2_chanMembers.put(channelName, status_2_members);
	return status_2_members;
    }

    private boolean ifMessageContainsNick(String message)
    {
	Pattern pattern = Pattern.compile(".*\\s" + nick_name + "\\W.*", Pattern.CASE_INSENSITIVE); //nick is anywhere in the message
	Pattern pattern2 = Pattern.compile("^" + nick_name + "\\W.*", Pattern.CASE_INSENSITIVE); // nick is only at the beginning of the message
	Pattern pattern3 = Pattern.compile(".*\\s" + nick_name + "$", Pattern.CASE_INSENSITIVE); // nick is at the end of the message
	Pattern pattern4 = Pattern.compile(nick_name, Pattern.CASE_INSENSITIVE); // message contains only nick
	Matcher matcher = pattern.matcher(message);
	Matcher matcher2 = pattern2.matcher(message);
	Matcher matcher3 = pattern3.matcher(message);
	Matcher matcher4 = pattern4.matcher(message);
	boolean b = matcher.matches();
	boolean b2 = matcher2.matches();
	boolean b3 = matcher3.matches();
	boolean b4 = matcher4.matches();
	System.out.println("message : " + message );
	//	Pattern pattern = Pattern.compile(".*\\s" + nick_name + "\\W.*", Pattern.CASE_INSENSITIVE);
	//	Matcher matcher = pattern.matcher(message);
	//	boolean b = matcher.matches();
	System.out.println("Contains nick " + ( b || b2 || b3 || b4) );
	return ( b || b2 || b3 || b4);
	//	String s = "";
	
	//	while(matcher.find())
	//	    {
		//		s = matcher.group(3);
		/*
	System.out.println(matcher.group(0));
		System.out.println(matcher.group(1));
		System.out.println(matcher.group(2));
		System.out.println(matcher.group(3));
		*/					  //	    }
	//	    }
    }


    private String getDay(String[] tokens)
    {
	if( tokens.length >= 1 )
	    return tokens[0].substring(0, tokens[0].length() - 2);
	return null;
    }

    private String getMonth(String[] tokens)
    {
	if( tokens.length >= 2 )
	    return tokens[1];
	return null;
    }

    private String getDate(String[] tokens)
    {
	if( tokens.length >= 3 )
	    return tokens[2].substring(0, tokens[2].length() - 2);;
	return null;
    }

    private String getYear(String[] tokens)
    {
	if( tokens.length >= 4 )
	    return tokens[3];
	return null;
    }

    private String getHour(String[] tokens)
    {
	if(tokens.length >= 1)
	    return tokens[0];
	return null;
    }

    private String getMinute(String[] tokens)
    {
	if(tokens.length >= 2)
	    return tokens[1];
	return null;
    }

    private String getSeconds(String[] tokens)
    {
	if(tokens.length >= 3)
	    return tokens[2];
	return null;
    }
    
    private String getAmPm(String[] tokens)
    {
	if(tokens.length >= 6)
	    return tokens[5];
	return null;
    }

    public void connect()
    {
	try
	    {
		profile = new Profile(user_name, nicks[0], nicks[1], nicks[2]);
		//		manager = new ConnectionManager(profile);
		Map.Entry<String, String> servPortPair = getNextServPort();
		String server = servPortPair.getKey();
		String port = servPortPair.getValue();
		System.out.println("server : " + server + ", port : " + port);
		try
		    {
			session = manager.requestConnection(server, Integer.parseInt(port), profile);
			System.out.println("In connect, after requestConnection().");
			session.addIRCEventListener(this);
			session.setRejoinOnKick(false);
		    }
		catch(NumberFormatException nfe)
		    {}
		
	    }
	catch(InstantiationError ie)
	    {
		ie.printStackTrace();
	    }
    }

    private String extract_motd(String motd)
    {
	Pattern pattern = Pattern.compile("(^:.*)( " + nick_name + " [:]*)(.*)");
	Matcher matcher = pattern.matcher(motd);
	String s = "";
	
	while(matcher.find())
	    {
		s = matcher.group(3);
		/*
		System.out.println(matcher.group(0));
		System.out.println(matcher.group(1));
		System.out.println(matcher.group(2));
		System.out.println(matcher.group(3));
		*/
	    }

	return s;
    }

    /**
     * This method is for parsing the lines sent by the server in the beginning of connection establishment
     */
    private String extractStartInfo(String data)
    {
	Pattern pattern = Pattern.compile("(^:.*)( " + nick_name + " [:]*)(.*)");
	Matcher matcher = pattern.matcher(data);
	String s = "";

	while(matcher.find())
	    {
		s = matcher.group(3);
		System.out.println("s : " + s);
	    }
	return s;
    }

    /**
    * this method is for parsing SERVER_INFORMATION, SERVER_VERSION & Channel List
    */
    private String extract_serv_info(String data)
    {
	//	tabGroup.setText(network_name, si.getCaseMapping());
	//	System.out.println("In extract_serv_info");

	Pattern pattern = Pattern.compile("(^:.*)( " + nick_name + " )([^:].*)");
	Matcher matcher = pattern.matcher(data);
	String parsed_str = "";

	while(matcher.find())
	    {
		/*		
		System.out.println("0 : " + matcher.group(0));
		System.out.println("1 : " + matcher.group(1));
		System.out.println("2 : " + matcher.group(2));
		*/
		System.out.println("3 : " + matcher.group(3));

		String group3 = matcher.group(3);
		parsed_str += group3;
		//		tabGroup.setText(network_name, group3);
	    }

	return parsed_str;
    }

    private String getKickedNick(String[] tokens)
    {
	if( tokens.length >= 4 )
	    return tokens[3];
	return null;
    }

    private String getKickChannel(String[] tokens)
    {
	if( tokens.length >= 3 )
	    return tokens[2];
	return null;
    }

    private String getKickedByWho(String[] tokens)
    {
	
	if( tokens.length >= 1 )
	    {
		return tokens[0].substring(tokens[0].indexOf(":") + 1, tokens[0].indexOf("!") );
	    }
	return null;
    }

    private String getKickReason(String[] tokens)
    {
	if( tokens.length >= 5 )
	    {
		String s = tokens[4].substring(1);
		for( int i = 5; i < tokens.length; i++ )
		    s += (" " + tokens[i]);
		return s;
	    }
	return null;
    }

    public void invite(String channelName, String nick)
    {
	session.invite(nick, session.getChannel(channelName));
    }

    private String extractNumericOrString(String[] tokens)
    {
	if( tokens.length >= 2 )
	    return tokens[1];
	return null;
    }

    public void executeCommand(String command)
    {
	System.out.println("In executeCommand");
	System.out.println("command : " + command );
    }

    public void sendChannelMessage(String channelName, String message)
    {
	Channel channel = session.getChannel(channelName);
	channel.say(message);
    }

    public void sendPrivateMessage(String nick, String message)
    {
	session.sayPrivate(nick,message);
    }

    public void setAway(String awayMessage)
    {
	session.setAway(awayMessage);
    }

    public void changeNick(String newNick)
    {
	session.changeNick(newNick);
    }

    public void joinChannel(String channelName)
    {
	session.join(channelName);
    }

    public void joinChannel(String channelName, String key)
    {
	session.join(channelName, key);
    }

    public void part(String channelName, String partMessage)
    {
	try
	    {
		Channel channel = session.getChannel(channelName);
		channel.part(partMessage);
	    }
	catch(NullPointerException npe)
	    {}
    }

    public void kick(String channelName, String nick, String reason)
    {
	try
	    {
		Channel channel = session.getChannel(channelName);
		channel.kick(nick, reason);
	    }
	catch(NullPointerException npe)
	    {}
    }

    public void quit(String message)
    {
	session.close(message);
    }

    public void op(String channelName, String nick)
    {
	Channel channel = session.getChannel(channelName);
	channel.op(nick);
    }

    public void deop(String channelName, String nick)
    {
	Channel channel = session.getChannel(channelName);
	channel.deop(nick);
    }

    public void quote(String message)
    {
	session.sayRaw(message);
    }

    public void voice(String channelName, String nick)
    {
	Channel channel = session.getChannel(channelName);
	channel.voice(nick);
    }

    public void setTopic(String channelName, String topic)
    {
	Channel channel = session.getChannel(channelName);
	channel.setTopic(topic);
    }

    public void mode( String channelName, String modeString, String nick )
    {
	
    }

    public void notice(String target, String message)
    {
	session.notice(target, message);
    }

    public void who(String nick)
    {
	session.who(nick);
    }

    public void whois(String nick)
    {
	session.whois(nick);
    }

    public void whoWas(String nick)
    {
	session.whoWas(nick);
    }

    public void sendActionMessage(String channelName, String action)
    {
	Channel channel = session.getChannel(channelName);
	if( channel != null )
	    session.sayChannel(channel, "\001ACTION " + action + "\001");
	else
	    session.sayPrivate(channelName, "\001ACTION " + action + "\001");
    }

    public void setMode(String channelName, String modeString)
    {
	Channel channel = session.getChannel(channelName);
	channel.mode(modeString);
    }

    public void ctcp(String target, String message)
    {
	session.ctcp(target, message);
    }

    private void sendCTCPVersion(String nick)
    {
	String message = "Java IRC Client " + System.getProperty("os.name") + " " + System.getProperty("os.version");
	session.ctcp(nick, "VERSION", message);
    }

    private void sendCTCPTime(String nick)
    {
	DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d yyyy HH:mm:ss");
	String message = dateFormat.format(new Date());
	session.ctcp(nick, "TIME", message);
    }

    //TODO later
    private void sendCTCPPing(String nick)
    {
	
    }

   /*    public Session getSession(String networkName)
    {
	
    }
    */
}