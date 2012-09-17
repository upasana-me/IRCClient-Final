import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;
import jerklib.ServerInformation;
import jerklib.Channel;

import jerklib.events.*;

import jerklib.events.IRCEvent.Type;

import jerklib.events.modes.*;

import jerklib.tasks.TaskImpl;
import jerklib.listeners.IRCEventListener;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.text.DateFormat;

import java.util.Date;
import java.util.Vector;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;

import java.net.Socket;

public class Connection implements Runnable, IRCEventListener
{
    private MainWindow mw;
    private EditNetList enl;
    private String[] nicks;
    private String nick_name;
    private String user_name;
    private String real_name;
    private int cur_nick;
    private String network_name;
    private Vector<String> servers;
    private TreeMap<String, String> channels;
    private TreeMap<String, TreeMap<String, Vector<String>>> chanName_2_chanMembers;
    private Vector<String> nicks_pms;

    private Socket[] sockets;

    private int no_of_sockets;
    private int free_socket;

    private ConnectionManager manager;
    private Session session;
    private Profile profile;

    Connection(Vector<String> serv)
    {
	cur_nick = 0;
	channels = new TreeMap<String, String>();
	chanName_2_chanMembers = new TreeMap<String, TreeMap<String, Vector<String>>>();
	nicks_pms = new Vector<String>();
	servers = serv;
	//	free_socket = 0;
	//	no_of_sockets = 100;
	//	sockets = new Socket[no_of_sockets];
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
	boolean initial_text = (e.getType() == Type.SERVER_VERSION_EVENT || e.getType() == Type.SERVER_INFORMATION || e.getType() == Type.MOTD);

	//	System.out.println(e.getRawEventData());
	if( e.getType() == Type.NICK_IN_USE )
	    {
		NickInUseEvent ne = (NickInUseEvent)e;
		String inUseNick = ne.getInUseNick();
		cur_nick++;
		nick_name = nicks[cur_nick];
		mw.setText(network_name, inUseNick + " is already in use.");
		mw.setText(network_name, "Retrying with " + nick_name);
		mw.setNickButtonText(network_name, nick_name);
	    }
	else if( e.getType() == Type.AWAY_EVENT )
	    {
		System.out.println("In Awayevent");
		AwayEvent ae = (AwayEvent)e;
		String nick = ae.getNick();
		String awayMessage = ae.getAwayMessage();
		mw.setAway(network_name, nick);
	    }
	else if( e.getType() == Type.NOTICE )
	    {
		NoticeEvent ne = (NoticeEvent)e;
		try
		    {
			String channelName = ne.getChannel().getName();		    
			// to be done later on
		    }
		catch(NullPointerException npe)
		    {}

		if( ne.toWho().equals("*"))
		    mw.setText(network_name, ne.getNoticeMessage());		
	    }
	else if( e.getType() == Type.MOTD )
	    {
		System.out.println("Motd Event : ");
		MotdEvent me = (MotdEvent)e;
		//		String host_name = me.getHostName();
		//		String motd_line = me.getMotdLine();
		//		System.out.println("host name : " + host_name);
		//		System.out.println("motd line : " + motd_line);
		String motd = me.getRawEventData();
		System.out.println("motd  : " + motd);
		String s = extract_motd(motd);
 
		//		String s = extract_serv_info(motd);
		System.out.println("s  : " + s);
		mw.setText(network_name, s);

		//		mw.setText(network_name, "host name : " + host_name );
		//		mw.setText(network_name, "motd line : " + motd_line );
	    }
	else if( e.getType() == Type.CONNECT_COMPLETE )
	    {
		channels = enl.get_auto_join_channels(network_name);
		Set<Map.Entry<String, String>> chan_pswd = channels.entrySet();
		Iterator<Map.Entry<String,String>> i = chan_pswd.iterator();
		while( i.hasNext() )
		    {
			Map.Entry<String, String> me = i.next();
			e.getSession().join(me.getKey(), me.getValue());
		    }
	    }
	else if( e.getType() == Type.JOIN )
	    {
		JoinEvent je = (JoinEvent)e;
		String channelName = je.getChannelName();
		String nick = je.getNick();
		String hostName = je.getHostName();
		String userName = je.getUserName();
		mw.setText( channelName, nick + " (" + userName + "@" + hostName + ") has joined " + channelName );

		TreeMap<String, Vector<String>> status_2_members = chanName_2_chanMembers.get(channelName);
		Vector<String> rest = status_2_members.get("r"); 
		//		Vector<String> rest = channelMembers.get("r");
		rest.add(nick);
		status_2_members.put("r",rest);
		chanName_2_chanMembers.put(channelName, status_2_members);
		mw.set_chan_members(network_name, channelName, status_2_members);
		
	    }
	else if( e.getType() == Type.PART )
	    {
		PartEvent pe = (PartEvent)e;
		String channelName = pe.getChannelName();
		String hostName = pe.getHostName();
		String partMessage = pe.getPartMessage();
		String userName = pe.getUserName();
		String partedNick = pe.getWho();
		
		mw.setText(channelName, partedNick + " (" + userName + "@" + hostName + ") has left " + channelName + " (" + partMessage + ")");

		TreeMap<String, Vector<String>> status_2_members = chanName_2_chanMembers.get(channelName);
		status_2_members.get("r").remove(partedNick); 
		status_2_members.get("o").remove(partedNick); 
		status_2_members.get("v").remove(partedNick); 

		chanName_2_chanMembers.put(channelName, status_2_members);
		mw.set_chan_members(network_name, channelName, status_2_members);
	    }
	else if( e.getType() == Type.TOPIC )
	    {
		/*
		TopicEvent te = (TypeEvent)e;
		Channel channel = te.getChannel();
		String topicSetter = te.getSetBy();
		Date date = te.getSetWhen();
		*/
	    }
	else if( e.getType() == Type.JOIN_COMPLETE )
	    {
		JoinCompleteEvent jce = (JoinCompleteEvent)e;
		Channel channel = jce.getChannel();

		String channel_name = channel.getName();
		mw.create_chan_tab(network_name, channel_name);
		
		try
		    {
			Thread.sleep(500);
		    }
		catch(InterruptedException ie)
		    {}

		//		TreeSet<String> treeset = new TreeSet<String>();

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
		
		chanName_2_chanMembers.put(channel_name, status_2_members);
		mw.set_chan_members(network_name, channel_name, status_2_members);

		String topic = channel.getTopic();
		String topicSetter = channel.getTopicSetter();
		Date topicDate = channel.getTopicSetTime();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM);
		mw.setTopic(channel_name, topic);
		if( !topic.equals("") )
		    {
			mw.setText( channel_name, Constants.topicStr + channel_name + " is : " + topic );
			mw.setText( channel_name, Constants.topicStr + channel_name + " set by " + topicSetter + " at " + dateFormat.format(topicDate) );
		    }
	    }
	else if( e.getType() == Type.CHANNEL_MESSAGE )
	    {
		MessageEvent me = (MessageEvent)e;
		String message = me.getMessage();
		String nick = me.getNick();
		Channel channel = me.getChannel();
		String channelName = channel.getName();
		mw.setText(channelName, "< " + nick + " > : " + message);
	    }
	else if( e.getType() == Type.NICK_CHANGE )
	    {
		NickChangeEvent nce = (NickChangeEvent)e;
		String newNick = nce.getNewNick();
		String userName = nce.getUserName();
		String oldNick = nce.getOldNick();

		/*		
		System.out.println("userName : " + userName);
		System.out.println("real_name : " + real_name);
		System.out.println("user_name : " + user_name);
		*/

		if( oldNick.equals(nick_name) )
		    {
			nick_name = newNick;
			mw.appendToAllTa( network_name, Constants.selfNickChangeText + newNick );
			mw.setNickButtonText(network_name, newNick );
			mw.modifyChannelList(network_name, oldNick, newNick);
		    }
		else
		    {
			mw.appendToAllTa( network_name, oldNick + Constants.nickChangeText + newNick ); 
			mw.modifyChannelList( network_name, oldNick, newNick);
		    }
	    }
	
	else if( e.getType() == Type.PRIVATE_MESSAGE )
	    {
		MessageEvent me = (MessageEvent)e;
		String message = me.getMessage();
		String nick = me.getNick();
		String hostname = me.getHostName();
		if( !nicks_pms.contains(nick) )
		    {
			nicks_pms.add(nick);
			mw.create_privmsg_tab(network_name, nick, hostname, "< " + nick + " > : " + message );
		    }
		else
		    {
			mw.setText( nick, "< " + nick + " > : " + message );
		    }
	    }
	else if( initial_text )
	    {
		String s = extract_serv_info(e.getRawEventData());
		mw.setText( network_name, s);
	    }
	else if( e.getType() == Type.ERROR )
	    {
		ErrorEvent ee = (ErrorEvent) e;
	    }
	else if( e.getType() == Type.EXCEPTION )
	    {
		System.out.println("Exception");		
	    }
	else if( e.getType() == Type.MODE_EVENT )
	    {}
	else 
	    {
		String rawData = e.getRawEventData();
		String s = extract_serv_info(rawData);

		if( s.equals("") )
		    {
			s = extract_start_info(rawData);
			mw.setText(network_name, s);
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
						mw.setText(network_name, s);
					    }
				    }
				else
				    {
					mw.setText(network_name, s);
				    }
			    }
		    }
	    }
	
    }

    public void setMainWindow(MainWindow m)
    {
	mw = m;
    }

    public void connect()
    {
	try
	    {
		profile = new Profile(user_name, nicks[0], nicks[1], nicks[2]);
		manager = new ConnectionManager(profile);
		session = manager.requestConnection(servers[curServ]);
		session.addIRCEventListener(this);
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
       This method is for parsing the lines sent by the server in the beginning of connection establishment
     */
    private String extract_start_info(String data)
    {
	Pattern pattern = Pattern.compile("(^:.*)( " + nick_name + " :)(.*)");
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
       this method is for parsing SERVER_INFORMATION, SERVER_VERSION & Channel List
    */
    private String extract_serv_info(String data)
    {
	//	mw.setText(network_name, si.getCaseMapping());
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
		//		mw.setText(network_name, group3);
	    }

	return parsed_str;
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
}