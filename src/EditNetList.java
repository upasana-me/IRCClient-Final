import java.util.Set;
import java.util.Iterator;
import java.util.Map;

import java.util.HashMap;
import java.util.Vector;

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
    private HashMap<String, Vector<String>> network_2_serv;
    private HashMap<String, Vector<String>> network_2_chan;

    EditNetList()
    {
	network_2_serv = new HashMap<String, Vector<String>>();	
	network_2_chan = new HashMap<String, Vector<String>>();
    }

    protected void add_server(String network_name, String server)
    {
	Vector<String> servers = new Vector<String>();
	if( network_2_serv.containsKey(network_name))
	    {
		servers = (Vector<String>)network_2_serv.get(network_name);		
	    }
	servers.add(server);
	network_2_serv.put(network_name, servers);		
    }

    protected void add_auto_join_chan(String network_name, String channel_list)
    {
	String[] chans = channel_list.split(" ");
	Vector<String> channels = new Vector<String>();
	for( int i = 0; i < chans.length; i++ )
	    {
		channels.add(chans[i]);
	    }
	network_2_chan.put(network_name, channels);
    }

    protected void show_chans_and_serv()
    {
	Set set = network_2_chan.entrySet();
	// Get an iterator
	Iterator i = set.iterator();
	// Display elements
	while(i.hasNext()) 
	    {
		Map.Entry me = (Map.Entry)i.next();
		System.out.print(me.getKey() + ": ");
		System.out.println(me.getValue());
	    } 
	set = network_2_serv.entrySet();
	// Get an iterator
	i = set.iterator();
	// Display elements
	while(i.hasNext()) 
	    {
		Map.Entry me = (Map.Entry)i.next();
		System.out.print(me.getKey() + ": ");
		System.out.println(me.getValue());
	    } 
    }
}