import java.util.Set;
import java.util.Iterator;
import java.util.Map;

import java.util.TreeMap;
import java.util.Vector;
import java.util.HashMap;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.sql.SQLException;

import java.io.File;

public class EditNetList
{
    private TreeMap<String, Vector<String>> network_2_serv;
    //    private TreeMap<String, Vector<String>> network_2_chan;
    private TreeMap<String, TreeMap<String, String>> network_2_pswd_chan;

    EditNetList()
    {
	network_2_serv = new TreeMap<String, Vector<String>>();	
	//	network_2_chan = new TreeMap<String, Vector<String>>();
	network_2_pswd_chan = new TreeMap<String, TreeMap<String, String>>();
    }

    public void add_server(String network_name, String server)
    {
	System.out.println("In add_server, network_name : " + network_name + ", server : " + server);
	Vector<String> servers = new Vector<String>();
	if( network_2_serv.containsKey(network_name))
	    {
		servers = network_2_serv.get(network_name);		
		if(network_name.equals("Rizon"))
		    {
			System.out.println("server " + server);
		    }
	    }
	else 
	    servers.add(server);

	if(network_name.equals("Rizon"))
	    {
		System.out.println("server " + server);
	    }
	network_2_serv.put(network_name, servers);		
    }

    public HashMap<String, String> getServersPort(String networkName)
    {
	System.out.println("In getServersPort, networkName : " + networkName);
	Vector<String> serversPorts = network_2_serv.get(networkName);
	HashMap<String, String> serverPort = new HashMap<String, String>();

	for( int i = 0; i < serversPorts.size(); i++ )
	    {
		String tokens[] = serversPorts.elementAt(i).split("/");
		String server = "";
		String port = "6667";
		if( tokens.length == 1 )
		    {
			server = tokens[0];
		    }
		else
		    {
			server = tokens[0];
			port = tokens[1];
		    }
		serverPort.put(server, port);
		/*
		Pattern pattern = Pattern.compile("(.*)(/*)(.*)*");
		Matcher matcher = pattern.matcher(serversPorts.elementAt(i));
		System.out.println("before matcher.find()");
		while(matcher.find() )
		    {
			String server = matcher.group(1);
			System.out.println("server : " + server);
			String port = "6667";
			System.out.println("port : " + port);
			String portString = matcher.group(3);
			System.out.println("portString : " + portString);
			if( !portString.equals("") && !portString.equals(null))
			    port = portString;
			serverPort.put(server, port);
			System.out.println("Server : " + server + ", port : " + port);
		    }
		*/
	    }

	return serverPort;
    }

    public void add_auto_join_chan(String network_name, String channel_list)
    {
	//	System.out.println("In add_auto_join_chan, channel_list :\n" + channel_list);

	String[] all_chans_keywords = channel_list.split(" ");

	if( all_chans_keywords.length < 1 )
	    {
		network_2_pswd_chan.put(network_name, new TreeMap<String, String>());
		return;
	    }

	String[] channels = all_chans_keywords[0].split(",");
	String[] keys = {};
	boolean keys_present = false;
	
	if( all_chans_keywords.length >= 2 )
	    {
		keys_present = true;
		keys = all_chans_keywords[1].split(",");
	    }

	TreeMap<String, String> chan_2_keys = new TreeMap<String, String>();

	for( int i = 0; i < channels.length; i++ )
	    {
		if( i < keys.length )
		    chan_2_keys.put(channels[i],keys[i]);
		else
		    chan_2_keys.put(channels[i],"");
	    }

	network_2_pswd_chan.put(network_name, chan_2_keys);
    }

    public TreeMap<String,String> get_auto_join_channels(String network_name)
    {
	/*
	System.out.println("In get_auto_join_channels");
	System.out.println("network_name : " + network_name + " hello");

	Vector<Vector<String>> values = (Vector<Vector<String>>)network_2_chan.values();
	for( int i = 0; i < values.size(); i++ )
	    {
		for( int j = 0; j < values.elementAt(i).size(); j++ )
		    System.out.println(values.elementAt(i).elementAt(j));
	    }
	*/

	TreeMap<String, String> treemap = new TreeMap<String, String>();
	if( network_2_pswd_chan.containsKey(network_name) )
	    {
		//		System.out.println("contains");
		treemap = network_2_pswd_chan.get(network_name);
	    }
	/*
	for( int i = 0; i < vector.size(); i++ )
	    {
		System.out.println(vector.elementAt(i));
	    }
	*/

	return treemap;
    }

    public void delete_network(String net_name)
    {
	network_2_serv.remove(net_name);
	network_2_pswd_chan.remove(net_name);
    }

    /*
    public void show_chans_and_serv()
    {
	Set set = network_2_chan.entrySet();
	// Get an iterator
	Iterator i = set.iterator();
	// Display elements
	while(i.hasNext()) 
	    {
		Map.Entry me = (Map.Entry)i.next();
		//		System.out.print(me.getKey() + ": ");
		//		System.out.println(me.getValue());
	    } 
	set = network_2_serv.entrySet();
	// Get an iterator
	i = set.iterator();
	// Display elements
	while(i.hasNext()) 
	    {
		Map.Entry me = (Map.Entry)i.next();
		//		System.out.print(me.getKey() + ": ");
		//		System.out.println(me.getValue());
	    } 
    }
    */
    
}
