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
    private TreeMap<String, String> channels;

    private Socket[] sockets;

    private int no_of_sockets;
    private int free_socket;

    private ConnectionManager manager;
    private Session session;

    Connection()
    {
	cur_nick = 0;
	channels = new TreeMap<String, String>();
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
	else if( e.getType() == Type.CONNECT_COMPLETE )
	    {
		channels = enl.get_auto_join_channels(network_name);
		Set<Map.Entry<String, String>> chan_pswd = channels.entrySet();
		Iterator<Map.Entry<String,String>> i = chan_pswd.iterator();
		while( i.hasNext() )
		    {
			Map.Entry<String, String> me = (Map.Entry<String, String>)i.next();
			e.getSession().join(me.getKey(), me.getValue());
		    }
	    }
	else if( e.getType() == Type.JOIN_COMPLETE )
	    {
		JoinCompleteEvent jce = (JoinCompleteEvent)e;
		Channel channel = jce.getChannel();

		String channel_name = channel.getName();
		mw.create_chan_tab(channel_name);
		
		try
		    {
			Thread.sleep(500);
		    }
		catch(InterruptedException ie)
		    {}

		Vector<String> vector = new Vector<String>(channel.getNicksForMode(ModeAdjustment.Action.valueOf("PLUS"),'o'));
		Vector<String> vector2 = new Vector<String>(channel.getNicks());
		/*
		System.out.println("channel_name : " + channel_name);
		System.out.println("vector.size() : " + vector.size());
		System.out.println("channel_name : " + channel_name);
		System.out.println("vector2.size() : " + vector2.size());
		*/

		vector = new Vector<String>(channel.getNicks());
		mw.set_chan_ops(channel_name, vector );

		vector = new Vector<String>(channel.getNicksForMode(ModeAdjustment.Action.valueOf("PLUS"),'v'));
		mw.set_chan_voiced(channel_name, vector );
		
		vector = new Vector<String>(channel.getNicks());
		vector.add(nick_name);
		TreeSet<String> treeset = new TreeSet<String>(vector);
		mw.set_chan_members(channel_name, treeset );


		/*
		String[] nicks_for_mode = channel.getNicksForMode(ModeAjustment.Action.valueOf("PLUS"),'v').toArray();
		mw.set_chan_ops(channel_name, nicks_for_mode );

		String[] channel_nicks = channel.getNicks().toArray();
		*/		

		
	    }
	else if( initial_text )
	    {
		//		ServerInformationEvent sie = (ServerInformationEvent)e;
		//		ServerInformation si = sie.getServerInformation();		
		String s = extract_serv_info(e.getRawEventData());
		mw.setText( network_name, s);
	    }
	/*
	else if(  )
	    {
		ServerInformationEvent sie = (ServerInformationEvent)e;
		ServerInformation si = sie.getServerInformation();
		//		String rawData = sie.getRawEventData();
		//		mw.setText(network_name, si.getCaseMapping());
		//		System.out.println("Case-mappings : " + si.getCaseMapping());		
		extract_serv_info(sie.getRawEventData());


		//si.parseServerInfo(rawData);
	    }
	else if( )
	    {
		//		MotdEvent 
		//		mw.setText(network_name, 
	    }
	*/
	else if( e.getType() == Type.ERROR )
	    {
		ErrorEvent ee = (ErrorEvent) e;
		//		System.out.println(ee.getErrorType());
	    }
	else if( e.getType() == Type.EXCEPTION )
	    {
		System.out.println("Exception");		
	    }
	else 
	    {
		/*
		//		extract_serv_info(e.getRawEventData());
		System.out.println("In last else");
		String s = extract_serv_info(e.getRawEventData());
		String[] tokens = s.split(" ");
		
		for( int i = 0; i < tokens.length; i++ )
		    {
			System.out.println("tokens[ " + i + " ] = " + tokens[i]);
		    }

		for( int i = 0; i < channels.size(); i++ )
		    {
			System.out.println("channels[ " + i + " ] = " + channels.elementAt(i));			
		    }

		if(tokens.length >= 1 && !channels.contains(tokens[0]))
		    mw.setText(network_name, s);
		else if(tokens.length >= 2 && !channels.contains(tokens[1]))
		    mw.setText(network_name, s);

		*/
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
		Profile profile = new Profile(user_name, nicks[0], nicks[1], nicks[2]);
		manager = new ConnectionManager(profile);
		session = manager.requestConnection(network_name);
		session.addIRCEventListener(this);
	    }
	catch(InstantiationError ie)
	    {
		ie.printStackTrace();
	    }
    }

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
		System.out.println("3 : " + matcher.group(3));
		*/

		String group3 = matcher.group(3);
		parsed_str += group3;
		//		mw.setText(network_name, group3);
	    }

	return parsed_str;
    }
}