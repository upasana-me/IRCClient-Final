import javax.swing.JTabbedPane;

import java.util.TreeMap;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.NoSuchElementException;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import javax.swing.border.Border;

import java.awt.Dimension;
import java.awt.Container;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



public class TabGroup
{
    private JFrame mw;

    private String networkName;
    private Connection connection;

    private TreeMap<String, JTextArea> tmta;
    private TreeMap<String, JTextField> tmtf;
    //    private Hashtable<String, JTextArea> h_chan_ta;
    //    private Hashtable<String, JTextField> h_chan_tf;
    private TreeMap<String, JTextField> tm_topic_tf;
    private TreeMap<String, JList<String>> tm_members_list;
    private TreeMap<String, CellRenderer> tm_cell_renderers;
    //    private TreeMap<String, Vector<String>> tm_network_2_pms;
    private TreeMap<String, Vector<String>> tm_chan_members;
    private TreeMap<String, TreeSet<String>> tm_actual_members;
    private TreeMap<String, JLabel> tm_list_labels;
    //    private TreeMap<String, Vector<JButton>> tm_net_2_buttons;
    //    private TreeMap<String, Connection> tm_netname_2_conn;
    private TreeMap<String, JTextField> tm_nick_2_hostname_tf;
    //    private TreeMap<String, String> tm_usage_messages;
    //    private TreeMap<String, Vector<String>> tmExistedNetwork2Channels;
    private TreeMap<String, String> tmTopicSet;

    private Vector<String> channels;
    private Vector<String> pms; 
    private Vector<JButton> ht_nick_buttons;
    private Vector<String> existedChannels;

    private JTextArea server_ta;
    private JTextField server_tf;
    private JTextField serv_topic_tf;

    private JButton nick_button;

    private JTabbedPane tabbedPane;
    private int tabNumber;

    public TabGroup(String networkName, Connection connection)
    {
	this.networkName = networkName;
	this.connection = connection;
	tabNumber = 0;

	tmta = new TreeMap<String, JTextArea>();
	tmtf = new TreeMap<String, JTextField>();
	tm_topic_tf = new TreeMap<String, JTextField>();
	tm_members_list = new TreeMap<String, JList<String>>();
	tm_chan_members = new TreeMap<String, Vector<String>>();
	tm_actual_members = new TreeMap<String, TreeSet<String>>();
	tm_cell_renderers = new TreeMap<String, CellRenderer>();
	tm_list_labels = new TreeMap<String, JLabel>();
	tm_nick_2_hostname_tf = new TreeMap<String, JTextField>();
	//	tm_usage_messages = new TreeMap<String, String>();
	//	tmExistedNetwork2Channels = new TreeMap<String, Vector<String>>();
	tmTopicSet = new TreeMap<String, String>();
	ht_nick_buttons = new Vector<JButton>();

	channels = new Vector<String>();
	pms = new Vector<String>();
	existedChannels = new Vector<String>();
    }

    public void setMainWindow(JFrame f)
    {
	this.mw = f;
    }

    public void setTextOnSelectedTab(String text)
    {
	String selectedTab = getSelectedTab();
	setText(selectedTab, text);
    }

    public String getSelectedTab()
    {
	int selectedIndex = tabbedPane.getSelectedIndex();
	String title = tabbedPane.getTitleAt(selectedIndex);
	return title;
    }

    public void setJTabbedPane(JTabbedPane tabbedPane)
    {
	this.tabbedPane = tabbedPane;
    }

    public void initialise( String nick )
    {
	JPanel p = new JPanel( new BorderLayout() );
	JPanel p1 = new JPanel( new BorderLayout() );
	JPanel p2 = new JPanel( new BorderLayout() );
	JPanel p3 = new JPanel( new BorderLayout() );
	JPanel p4 = new JPanel( new BorderLayout() );
	//need modification
	JPanel p6 = new ButtonTabComponent(tabbedPane, networkName, this);

	BorderLayout bl = new BorderLayout();
	bl.setHgap( 0 );
	bl.setVgap( 0 );
	JPanel p5 = new JPanel( bl );

	serv_topic_tf = new JTextField();
	serv_topic_tf.setEnabled( false );
	serv_topic_tf.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	//	p5.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p5.add( serv_topic_tf, BorderLayout.CENTER );

	server_ta = new JTextArea();
	server_ta.setMargin(new Insets(0,50,0,10));
	server_ta.setLineWrap(true);
	tmta.put( networkName, server_ta );
	JScrollPane sp = new JScrollPane( server_ta );
	
	server_ta.setEditable( false );

	server_tf = new JTextField();
	server_tf.addActionListener( new ActionListener() 
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    String s = server_tf.getText();
		    server_tf.setText("");
		    if( s.startsWith("/") )
			{
			    parseCommand(networkName, s);
			}
		    else
			{
			    setText(networkName, "Please type an actual IRC command on this tab.");
			}
		}
	    });
	server_tf.setActionCommand( Constants.server_tf_ac );
	tmtf.put( networkName, server_tf );

	nick_button = new JButton(nick);
	//	nick_button.setText( nick );
	nick_button.addActionListener( new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    //		    System.out.println("In actionPerformed of nick_but");
		    try
			{
			    //			    System.out.println("In try block");
			    changeNick();
			}
		    catch(NullPointerException npe)
			{}
		    catch(HeadlessException he)
			{}
		}
	    });
	ht_nick_buttons.add( nick_button );
	//	tm_net_2_buttons.put(tabname, ht_nick_buttons);

	p1.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p1.add( sp, BorderLayout.CENTER  );
	//	p.setBorder( BorderFactory.createLineBorder( Color.black, 2 ) );

	p2.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
 	p2.add( nick_button, BorderLayout.WEST );

	p3.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p3.add( server_tf );

	p.setBorder( BorderFactory.createLineBorder( Color.black, 2 ) );

	p4.add( p2, BorderLayout.LINE_START );
	p4.add( p3 );

	//	p.add(p6);
	p.add( p5, BorderLayout.NORTH );
	p.add( p1, BorderLayout.CENTER );
	p.add( p4, BorderLayout.SOUTH );

	
	tabbedPane.add( networkName, p );
	tabbedPane.setTabComponentAt( tabbedPane.getTabCount() - 1, p6);
	System.out.println("Added panel on JTabbedPane.");
	//need modification
	//	tabNumber++;
	//	tp.setComponentAt(tp.indexOfTab(tabname), new ButtonTabComponent(tp));

	//	this.pack();
      	//	System.out.println("Initialised");
    }

    public void create_chan_tab( final String channel_name )
    {
	String channelName = channel_name;
	channels.add(channelName);

	/*
	if( tm_network_2_channels.size() == 0 )
	    {
		Vector<String> channels = new Vector<String>();
		channels.add(channel_name);
		tm_network_2_channels.put(networkName, channels);
	    }
	else 
	    {
		Vector<String> channels = tm_network_2_channels.get(networkName);
		channels.add(channelName);
		tm_network_2_channels.put(networkName, channels);
	    }
	*/
	
	JPanel p = new JPanel( new BorderLayout() );
	JPanel p0 = new JPanel( new BorderLayout() );
	JPanel p1 = new JPanel( new BorderLayout() );
	JPanel p2 = new JPanel( new BorderLayout() );
	JPanel p3 = new JPanel( new BorderLayout() );
	JPanel p4 = new JPanel( new BorderLayout() );
	JPanel p5 = new JPanel( new BorderLayout() );
	JPanel p6 = new JPanel( new BorderLayout() );
	
	//need modification
	JPanel p7 = new ButtonTabComponent(tabbedPane, channel_name, this );

	Vector<String> vector = new Vector<String>();
	TreeSet<String> treeset = new TreeSet<String>();
	JLabel chan_info = new JLabel();
	CellRenderer cr = new CellRenderer();

	JList<String> chan_list = new JList<String>(vector);
	chan_list.setCellRenderer(cr);
	tm_members_list.put( channel_name, chan_list );
	tm_chan_members.put( channel_name, vector );
	tm_actual_members.put( channel_name, treeset );
	tm_cell_renderers.put(channel_name, cr);
	tm_list_labels.put(channel_name, chan_info);
	
	JScrollPane sp0 = new JScrollPane( chan_list );
	
	p5.setBorder( BorderFactory.createLineBorder( Color.black, 1 ) );
	p5.add( chan_info, BorderLayout.CENTER );

	p0.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p0.add( p5, BorderLayout.NORTH );
	p0.add( sp0, BorderLayout.CENTER  );

	JTextField chan_topic_tf = new JTextField();
	tm_topic_tf.put( channel_name, chan_topic_tf );
	chan_topic_tf.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	
	p6.add( chan_topic_tf, BorderLayout.CENTER );

	final JTextArea chan_ta = new JTextArea();
	chan_ta.setMargin(new Insets(0,50,0,10));
	chan_ta.setLineWrap(true);
	tmta.put( channel_name, chan_ta );
	JScrollPane sp = new JScrollPane( chan_ta );
	String chan_join_text = Constants.chan_join_text + channel_name + "\n";
	setText( channel_name, chan_join_text );
	
	chan_ta.setEditable( false );

	p1.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p1.add( sp, BorderLayout.CENTER  );

	final JTextField chan_tf = new JTextField();
	tmtf.put( channel_name, chan_tf );
	chan_tf.addActionListener( new ActionListener() 
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    String s = chan_tf.getText();
		    chan_tf.setText("");
		    s = s.trim();
		    if( s.startsWith("/") )
			{
			    parseCommand(channel_name, s);
			}
		    else
			{
			    if( !s.equals(""))
				{
				    connection.sendChannelMessage(channel_name, s);
				    //			    tm_netname_2_conn.get(network_name).sendChannelMessage(channel_name, s);
				    setText(channel_name, "<" + nick_button.getText() + "> : " + s);
				}
			}
		}
	    });
	chan_tf.requestFocus();

	JButton nick_but = new JButton( nick_button.getText() );
	nick_but.addActionListener( new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    //		    System.out.println("In actionPerformed of nick_but");
		    try
			{
			    //	    System.out.println("In try block");
			    changeNick();
			}
		    catch(NullPointerException npe)
			{}
		    catch(HeadlessException he)
			{}
		}
	    });
	ht_nick_buttons.add( nick_but );

	p2.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
 	p2.add( nick_but, BorderLayout.WEST );

	p3.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p3.add( chan_tf );

	p.setBorder( BorderFactory.createLineBorder( Color.black, 2 ) );

	p4.add( p2, BorderLayout.LINE_START );
	p4.add( p3 );

	p.add( p6, BorderLayout.NORTH );
	p.add( p0, BorderLayout.EAST );
	p.add( p1, BorderLayout.CENTER );
	p.add( p4, BorderLayout.SOUTH );

	tabbedPane.add( channel_name, p );
	//need modification
	tabbedPane.setTabComponentAt( tabbedPane.getTabCount() -1, p7);
	//	tabNumber++;
	/*
	if( p.isRequestFocusEnabled() )
	    System.err.println("Error in requestFocusInWindow()");
	p.requestFocus();
	*/
	//	    System.err.println("Error in requestFocusInWindow()");

	tabbedPane.setSelectedComponent( p );
	//	System.out.println("Initialised");
	
    }

    /**
     * Method for creating a Private Message tab
     * @param network_name 
     * name of the network to which this tab belongs
     * @param nick_name
     * nick name of the other person
     * @param hostPlusUser
     * hostPlusUser of the other person
     * @param message
     * message sent by the other person
     */
    public void create_privmsg_tab( final String nick_name, String hostname, String message )
    {
	String senderNick = nick_name;
	pms.add(nick_name);
	
	/*
	if( tm_network_2_pms.size() == 0 )
	    {
		Vector<String> pms = new Vector<String>();
		pms.add(senderNick);
		tm_network_2_pms.put(networkName, pms);
	    }
	else 
	    {
		Vector<String> pms = tm_network_2_pms.get(networkName);
		pms.add(senderNick);
		tm_network_2_pms.put(networkName, pms);
	    }
	*/
	
	JPanel p = new JPanel( new BorderLayout() );
	JPanel p0 = new JPanel( new BorderLayout() );
	JPanel p1 = new JPanel( new BorderLayout() );
	JPanel p2 = new JPanel( new BorderLayout() );
	JPanel p3 = new JPanel( new BorderLayout() );
	JPanel p4 = new JPanel( new BorderLayout() );
	JPanel p5 = new JPanel( new BorderLayout() );
	JPanel p6 = new JPanel( new BorderLayout() );
	
	//need modification
	JPanel p7 = new ButtonTabComponent(tabbedPane, nick_name, this);

	/*
	p0.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p0.add( p5, BorderLayout.NORTH );
	p0.add( sp0, BorderLayout.CENTER  );
	*/

	JTextField hostname_tf = new JTextField();
	hostname_tf.setText(hostname);
	hostname_tf.setEditable(false);
	tm_nick_2_hostname_tf.put( nick_name, hostname_tf );
	hostname_tf.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );

	p6.add( hostname_tf, BorderLayout.CENTER );

	final JTextArea pm_ta = new JTextArea();
	//	chan_ta.setMargin(new Insets(0,50,0,10));
	pm_ta.setLineWrap(true);
	tmta.put( nick_name, pm_ta );
	System.out.println("In create_privmsg_tab, after putting tmta.put(nick_name, pm_ta)");
	JScrollPane sp = new JScrollPane( pm_ta );
	//	String chan_join_text = Constants.chan_join_text + channel_name + "\n";
	setText( nick_name, message );
	pm_ta.setEditable( false );

	p1.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p1.add( sp, BorderLayout.CENTER  );

	final JTextField pm_tf = new JTextField();
	tmtf.put( nick_name, pm_tf );
	pm_tf.addActionListener( new ActionListener() 
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    String s = pm_tf.getText();
		    pm_tf.setText("");
		    if( s.startsWith("/") )
			{
			    parseCommand( nick_name, s);
			}
		    else
			{
			    connection.sendPrivateMessage(nick_name, s);
			    //			    tm_netname_2_conn.get(network_name).sendPrivateMessage(nick_name, s);
			    setText(nick_name, "< " + nick_button.getText() + " > : " + s);
			}
		}
	    });

	JButton nick_but = new JButton( nick_button.getText() );
	nick_but.addActionListener( new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    //		    System.out.println("In actionPerformed of nick_but");
		    try
			{
			    //	    System.out.println("In try block");
			    changeNick();
			}
		    catch(NullPointerException npe)
			{}
		    catch(HeadlessException he)
			{}
		}
	    });
	ht_nick_buttons.add( nick_but );

	p2.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
 	p2.add( nick_but, BorderLayout.WEST );

	p3.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p3.add( pm_tf );

	p.setBorder( BorderFactory.createLineBorder( Color.black, 2 ) );

	p4.add( p2, BorderLayout.LINE_START );
	p4.add( p3 );

	p.add( p6, BorderLayout.NORTH );
	p.add( p0, BorderLayout.EAST );
	p.add( p1, BorderLayout.CENTER );
	p.add( p4, BorderLayout.SOUTH );

	tabbedPane.add( nick_name, p );
	//need modification
	tabbedPane.setTabComponentAt( tabbedPane.getTabCount() - 1, p7);
	tabNumber++;
	/*
	if( p.isRequestFocusEnabled() )
	    System.err.println("Error in requestFocusInWindow()");
	p.requestFocus();
	*/
	//	    System.err.println("Error in requestFocusInWindow()");
	//	tp.addTab( nick_name, p );

	tabbedPane.setSelectedComponent( p );
	//	System.out.println("Initialised");
    }

    /**
     * Method for setting text on JTextArea 
     * @param tabname 
     * name of the tab on which text is to be appended
     * @param text 
     * text to be appended on the textarea
     */
    public void setText( String tabname, String text )
    {
	//	System.out.println("tabname : " + tabname );
	//	System.out.println("text : " + text );
	JTextArea ta = tmta.get( tabname );
	//	System.out.println( ta.toString() );
	//	System.out.println("text = " + text );
	if( text.equals("\n") || text.equals("") || text.equals(" ") || text.equals(null) || text == null )
	    return;
	ta.append( text + "\n" );
    }

    public void setNickButtonText( String nick )
    {
	for( int i = 0; i < ht_nick_buttons.size(); i++ )
	    {
		JButton jb = ht_nick_buttons.elementAt( i );
		jb.setText( nick );
	    }
    }

    public String get_input()
    {
	return server_tf.getText();
    }

    /**
     * Method for adding a Connection instance
     * @param netname
     * name of the network to which Connection instance belongs
     * @param conn
     * Connection instance
     */
    /*
    public void addConnection(String netname, Connection conn)
    {
	tm_netname_2_conn.put(netname, conn);
    }

    /**
     * Method for removing a Connection instance
     * @param netname 
     * name of the network
     */
    /*
    public void removeConnection(String netname)
    {
	tm_netname_2_conn.remove(netname);
    }

    /**
     * @param netname 
     * name of the network
     * @return Connection instance associated with the given network
     */
  
    /*
  public Connection getConnection(String netname)
    {
	return tm_netname_2_conn.get(netname);
    }
*/


    public void removeTab(String channelName)
    {
	if( channels.contains(channelName))
	    {
		System.out.println("channles contains " + channelName);
		tmta.remove(channelName);
		tmtf.remove(channelName);
		channels.remove(channelName);
		tm_topic_tf.remove(channelName);
		tm_members_list.remove(channelName);
		tm_cell_renderers.remove(channelName);

		//	Vector<String> channels = tm_network_2_channels.get(networkName);
		//	tm_network_2_channels.put(networkName, channels);

		tm_chan_members.remove(channelName);
		tm_actual_members.remove(channelName);
		tm_list_labels.remove(channelName);

		int indexOfChannel = tabbedPane.indexOfTab(channelName);
		tabbedPane.removeTabAt(indexOfChannel);

		//	Vector<JButton> buttons = tm_net_2_buttons.get(networkName);
		//	buttons.removeElementAt(indexOfChannel - 1);
		//	tm_net_2_buttons.put(channelName, buttons);
	
		ht_nick_buttons.removeElementAt(indexOfChannel-1);
		connection.part(channelName, null);
		System.out.println("At the end of if block");
	    }
	else
	    {
		System.out.println("channles doesn't contain contains " + channelName);
		pms.remove(channelName);
		tm_nick_2_hostname_tf.remove(channelName);
		tmta.remove(channelName);
		tmtf.remove(channelName);

		int indexOfChannel = tabbedPane.indexOfTab(channelName);
		tabbedPane.removeTabAt(indexOfChannel);
		ht_nick_buttons.removeElementAt(indexOfChannel-1);
	    }
	
    }

    public boolean tabAlreadyRemoved(String channelName)
    {
	int indexOfTab = tabbedPane.indexOfTab(channelName);
	if( indexOfTab < 0 )
	    return true;
	return false;
    }

    public boolean getPmExists(String channelName)
    {
	return pms.contains(channelName);
    }

    public JFrame getMainWindow()
    {
	return mw;
    }

    public boolean channelExisted(String channelName)
    {
	return existedChannels.contains(channelName);
    }

    public void quit()
    {
	System.out.println("In quit.");
	connection.quit("");
	for( int i = 0; i < channels.size(); i++ )
	    {
		int indexOfChannel = tabbedPane.indexOfTab(channels.elementAt(i));
		tabbedPane.removeTabAt(indexOfChannel);
	    }
	setText(networkName, "Disconnected.");
    }

    public void setTopic( String channel_name, String topic )
    {
	try
	    {

		/*
		Enumeration e = tm_topic_tf.keys();		
		System.out.println( "In set_topic");
		System.out.println("ht_topic_tf : " + ht_topic_tf.toString() );
		System.out.println("channel_name : " + channel_name );
		System.out.println( "Object = " + ht_topic_tf.containsKey( channel_name ) );
		*/
		JTextField topic_tf = tm_topic_tf.get( channel_name );
		//		System.out.println();
		//		System.out.println( topic_tf.toString() );
		topic_tf.setText( topic );
		
	    }
	catch( NullPointerException npe )
	    {
		System.out.println( npe.getMessage() );
	    }
    }

    /**
     * Method for setting list of channel members of the given channel of the given network
     * @param treemap
     * contains members' list for the channel
     */
    public void set_chan_members( String channel_name, TreeMap<String, Vector<String>> treemap )
    {
	//	Iterator<String> iter = treeset.iterator();

	Vector<String> ops = treemap.get("o"); // key "o" is for operators
	Vector<String> voiced = treemap.get("v"); // key "v" is for voiced members
	Vector<String> others = treemap.get("r"); // key "r" is for rest of the members

	JList<String> list = tm_members_list.get( channel_name );
	CellRenderer cr = tm_cell_renderers.get(channel_name);
	cr.setStatusNickTM(treemap);

	Vector<String> for_list = tm_chan_members.get(channel_name);
	for_list.clear();
	TreeSet<String> treeset2 = tm_actual_members.get(channel_name);

	TreeSet<String> ts = new TreeSet<String>(new IgnoreCaseSort<String>());
	Iterator<String> iterator;
	
	ts.addAll(ops);
	iterator = ts.iterator();
	while( iterator.hasNext() )
	    {
		String s = iterator.next();
		for_list.add(s);
	    }
		
	ts.clear();
	ts.addAll(voiced);
		
	iterator = ts.iterator();
	
	while( iterator.hasNext() )
	    {
		String s = iterator.next();
		for_list.add(s);
	    }

	ts.clear();
	ts.addAll(others);
	
	iterator = ts.iterator();
	
	while( iterator.hasNext() )
	    {
		String s = iterator.next();
		if( !for_list.contains(s) )
		    {
			for_list.add(s);
		    }
	    }

	int ops_no = ops.size();
	int voiced_no = voiced.size();
	int others_no = others.size();

	int total = ops_no + voiced_no + others_no;

	JLabel label = tm_list_labels.get(channel_name);
	label.setText(ops_no + " ops, " + total + " total");

	tm_chan_members.put(channel_name, for_list);
	tm_cell_renderers.put(channel_name, cr);
	list.setListData( for_list );
    }

    /*
    public boolean getChannelTabExisted(String channelName)
    {
	if( tmExistedNetwork2Channels.containsKey(networkName))
	    {
		return tmExistedNetwork2Channels.get(networkName).contains(channelName);
	    }
	return false;
    }
    */

    public void reInitialiseChannel(String channelName)
    {
	JList<String> list = tm_members_list.get(channelName);
	CellRenderer cr = new CellRenderer();
	list.setCellRenderer(cr);
	tm_cell_renderers.put(channelName, cr);
	tm_chan_members.put(channelName, new Vector<String>());
	//	Vector<String> channels = tm_network_2_channels.get(networkName);
	channels.add(channelName);
	//	tm_network_2_channels.put(networkName, channels);
	//	tmExistedNetwork2Channels.get(networkName).remove(channelName);
	setText(channelName, "Now talking on " + channelName);
    }

    public void clearChanMembersList(String channelName)
    {
	System.out.println("In clearChannelMembersList");
	JList<String> list = tm_members_list.get(channelName);
	list.setListData(new Vector<String>());
	tm_cell_renderers.remove(channelName);
	JLabel listLabel = tm_list_labels.get(channelName);
	listLabel.setText("  ");
	tm_list_labels.put(channelName, listLabel);
	tm_chan_members.remove(channelName);
	channels.remove(channelName);
	tm_topic_tf.get(channelName).setText("");
	existedChannels.add(channelName);
	System.out.println("At the end of clearChannelMembersList");
    }

    public int getChannelCount()
    {
	return channels.size();
    }

    public int getPmsCount()
    {
	return pms.size();
    }

    public String getNetworkName()
    {
	return networkName;
    }

    private void changeNick()
    {
	System.out.println("changeNick");
	try
	    {
		String presentNick = nick_button.getText();
		String newNick = (String)JOptionPane.showInputDialog(mw, Constants.promptNick, Constants.promptNick, JOptionPane.QUESTION_MESSAGE, null, null, presentNick);
		if( newNick != "" && newNick != null)
		    {
			connection.changeNick(newNick);
			//			tm_netname_2_conn.get(networkName).changeNick(newNick);
		    }
	    }
	catch(NullPointerException npe)
	    {}
	catch(HeadlessException he)
	    {}
    }

    public void appendToAllTa(String text)
    {
	/*
	Set<Map.Entry<String, JTextArea>> netname_ta = tmta.entrySet();
	Iterator<Map.Entry<String, JTextArea>> iterator = netname_ta.iterator();
	
	while( iterator.hasNext() )
	    {
		Map.Entry<String, JTextArea> entry = iterator.next();
		JTextArea ta = entry.getValue();
		ta.append(text + "\n");
	    }
	*/
    }

    public void modifyChannelList(String oldNick, String newNick)
    {
	//	Vector<String> channels = tm_network_2_channels.get(networkName);
	
	for(int i = 0; i < channels.size(); i++ )
	    {
		Vector<String> chanMembers = tm_chan_members.get(channels.elementAt(i));
		if(chanMembers.contains(oldNick))
		    {
			JList<String> list = tm_members_list.get(channels.elementAt(i));
			CellRenderer cr = tm_cell_renderers.get(channels.elementAt(i));
			cr.changeNick(oldNick, newNick);
			tm_cell_renderers.put(channels.elementAt(i), cr);
			Vector<String> for_list = tm_chan_members.get(channels.elementAt(i));
			int index_of_nick = for_list.indexOf(oldNick);
			for_list.remove(oldNick);
			//			chanMembers.remove(oldNick);
			//			chanMembers.add(index_of_nick, newNick);
			for_list.add(index_of_nick, newNick);
			tm_chan_members.put(channels.elementAt(i), for_list);
			list.setListData(for_list);
			tm_members_list.put(channels.elementAt(i), list);
		    }
	    }	
    }


    public void setAway(String nick)
    {
	System.out.println("In setAway.");
	//	Vector<String> channels = tm_network_2_channels.get(networkName);
	
	//	System.out.println("In setAway.");
	for(int i = 0; i < channels.size(); i++ )
	    {
		Vector<String> chanMembers = tm_chan_members.get(channels.elementAt(i));
		if(chanMembers.contains(nick))
		    {
			JList<String> list = tm_members_list.get(channels.elementAt(i));
			CellRenderer cr = tm_cell_renderers.get(channels.elementAt(i));			
			cr.addNickToAway(nick);
			System.out.println("After adding nick to away of cr.");
			tm_cell_renderers.put(channels.elementAt(i), cr);
			Vector<String> for_list = tm_chan_members.get(channels.elementAt(i));
			int index_of_nick = for_list.indexOf(nick);
			for_list.remove(nick);
			for_list.add(index_of_nick, nick);
			tm_chan_members.put(channels.elementAt(i), for_list);
			list.setCellRenderer(cr);
			list.setListData(for_list);
			tm_members_list.put(channels.elementAt(i), list);
		    }
	    }	
    }

    private void parseCommand(String channelName, String command)
    {
	command = command.substring(1);
	String tokens[] = command.split(" ");
	tokens[0] = tokens[0].toUpperCase();
	//	Connection conn = tm_netname_2_conn.get(netname);

	if( tokens[0].equals("AWAY"))
	    {
		String awayMessage = command.substring(5);
		connection.setAway(awayMessage);
	    }
	else if( tokens[0].equals("BAN"))
	    {}
	else if( tokens[0].equals("CTCP"))
	    {}
	else if( tokens[0].equals("DCC"))
	    {}
	else if( tokens[0].equals("DEOP"))
	    {
	    }
	else if( tokens[0].equals("HELP"))
	    {}
	else if( tokens[0].equals("INVITE"))
	    {}
	else if( tokens[0].equals("JOIN") )
	    {
		if( tokens.length >= 2 )
		    {
			String channel_name = tokens[1];
			String key = "";
			if( tokens.length > 2 )
			    {
				key = tokens[2];
				connection.joinChannel(channel_name, key);
			    }
			else 
			    {
				connection.joinChannel(channel_name);
			    }
		    }
		else
		    {
			//			setText(networkName, tm_usage_messages.get("JOIN"));
			if( !channelName.equals("") )
			    {}
			    //			    setText(channelName, tm_usage_messages.get("JOIN"));
		    }
	    }
	else if( tokens[0].equals("KICK"))
	    {
		if( tokens.length >= 2 )
		    {
			String kickNick = tokens[1];
			String reason = "";
			if( tokens.length > 2 )
			    reason = command.substring("/KICK ".length() + tokens[1].length() );
			connection.kick(channelName, kickNick, reason);
		    }
		else
		    {
			//			setText(channelName, tm_usage_messages.get("PART"));
		    }
	    }
	else if( tokens[0].equals("MODE"))
	    {
	    }
	else if(tokens[0].equals("MSG") )
	    {
	    }
	else if( tokens[0].equals("NICK"))
	    {}
	else if( tokens[0].equals("NOTICE"))
	    {}
	else if( tokens[0].equals("NOTIFY"))
	    {}
	else if( tokens[0].equals("OP"))
	    {}
	else if( tokens[0].equals("PART"))
	    {
		String partChannel = channelName;
		String partMessage = "";
		//		String partUsageMessage = tm_usage_messages.get("PART");
		//		Vector<String> pms = tm_network_2_pms.get(netname);
		/*
		for( int i = 0; i < pms.size(); i++ )
		    {
			System.out.println("pms.elementAt( " + i + " ) = " + pms.elementAt(i));
		    }
		*/
		if( tokens.length >= 2 )
		    {
			partChannel = tokens[1];
			//			System.out.println("tm_topic_tf containsKey returns true.");
			if( tokens.length > 2 )
			    {
				partMessage = command.substring("/PART ".length() + tokens[1].length());
			    }
			connection.part( partChannel, partMessage );
			/*
			System.out.println("partChannel : " + partChannel);
			System.out.println("partMessage : " + partMessage);
			*/
		    }
		else if( pms != null && !pms.equals(null) && pms.contains(channelName))
		    {
			//			System.out.println("Tab is a pm");
			//			setText(netname, partUsageMessage);
			//			setText(channelName, partUsageMessage);
		    }
		else if( networkName.equals(channelName) )
		    {
			//			setText(netname, partUsageMessage);			
		    }
		else
		    {
			connection.part(channelName, partMessage);
		    }
		    
	    }
	else if( tokens[0].equals("QUIT"))
	    {
		
	    }
	else if( tokens[0].equals("QUOTE"))
	    {
		
	    }
	else if( tokens[0].equals("SERVER"))
	    {}
	else if( tokens[0].equals("TOPIC"))
	    {}
	else if( tokens[0].equals("UNBAN"))
	    {}
	else if( tokens[0].equals("VOICE"))
	    {}
    }
}