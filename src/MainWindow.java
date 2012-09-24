/**
 * MainWindow.java
 */

import java.util.TreeMap;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.NoSuchElementException;
import java.util.IdentityHashMap;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;

import javax.swing.border.Border;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Container;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame implements ActionListener, WindowListener, KeyListener
{
    private Vector<Connection> conn;

    private TreeMap<String, JTextArea> tmta;
    private TreeMap<String, JTextField> tmtf;
    //    private Hashtable<String, JTextArea> h_chan_ta;
    //    private Hashtable<String, JTextField> h_chan_tf;
    private TreeMap<String, String> tm_usage_messages;
    private TreeMap<String, JTabbedPane> tmNet2JTabbedPane;
    private IdentityHashMap<JPanel, Connection> ihmPanelToConnection;

    private Vector<JButton> ht_nick_buttons;

    private JTabbedPane tp;
    private Container pane;

    private JButton nick_button;

    private JMenuBar mb;

    private JMenu jChat;
    private JMenuItem net_list;
    private JMenuItem new_mi;
    private JMenuItem close;
    private JMenuItem quit;

    private JMenu view;
    private JCheckBoxMenuItem show_menu_bar;
    private JCheckBoxMenuItem show_topic_bar;
    
    private JMenu server;
    private JMenuItem disconnect;
    private JMenuItem reconnect;
    private JMenuItem join_a_channel;
    private JMenuItem list_of_channels;

    private JCheckBoxMenuItem marked_away;

    private JMenu settings;
    
    private JMenu window;
    private JMenuItem clear_text;
    private JMenuItem search_text;

    private JMenu help;
    private JMenuItem contents;
    private JMenuItem about;

    //    private JMenuItem 
    private JScrollPane sp;

    private JTextArea server_ta;
    private JTextField server_tf;
    private JTextField serv_topic_tf;

    private int tabNumber;

    private UserSettings userSettings;
    
    public MainWindow()
    {
	super( Constants.appTitle );
	//	conn = c;
	tmNet2JTabbedPane = new TreeMap<String, JTabbedPane>();
	/*
	tabNumber = 0;
	tmta = new TreeMap<String, JTextArea>();
	tmtf = new TreeMap<String, JTextField>();
	tm_topic_tf = new TreeMap<String, JTextField>();
	tm_members_list = new TreeMap<String, JList<String>>();
	tm_chan_members = new TreeMap<String, Vector<String>>();
	tm_actual_members = new TreeMap<String, TreeSet<String>>();
	tm_cell_renderers = new TreeMap<String, CellRenderer>();
	tm_list_labels = new TreeMap<String, JLabel>();
	tm_net_2_buttons = new TreeMap<String, Vector<JButton>>();
	tm_netname_2_conn = new TreeMap<String, Connection>();
	tm_network_2_channels = new TreeMap<String, Vector<String>>();
	tm_network_2_pms = new TreeMap<String, Vector<String>>();
	tm_nick_2_hostname_tf = new TreeMap<String, JTextField>();
	tm_usage_messages = new TreeMap<String, String>();
	tmExistedNetwork2Channels = new TreeMap<String, Vector<String>>();

	ht_nick_buttons = new Vector<JButton>();
	*/

	ihmPanelToConnection = new IdentityHashMap<JPanel, Connection>();
	tm_usage_messages = new TreeMap<String, String>();
	initialiseUsageMessages();
	init();
    }

    private void initialiseUsageMessages()
    {
	tm_usage_messages.put("PART", "Usage: PART [<channel>] [<reason>], leaves the channel, by default the current one");
	tm_usage_messages.put("JOIN", "Usage: JOIN <channel>, joins the channel");
	tm_usage_messages.put("MSG", "Usage: MSG <nick> <message>, sends a private message");
	tm_usage_messages.put("NICK", "Usage: NICK <nickname>, sets your nick");
    }

    public void setUserSettings(UserSettings us)
    {
	userSettings = us;
    }

    public void init()
    {
	this.setLocationRelativeTo( null );
	this.setExtendedState( Frame.MAXIMIZED_BOTH );

	jChat = new JMenu("JChat");
	net_list = Utility.createMenuItem( Constants.net_list_mi_text, Constants.net_list_mi_ac, jChat, this );
	close = Utility.createMenuItem( Constants.close_mi_text, Constants.close_mi_ac, jChat, this );
	quit = Utility.createMenuItem( Constants.quit_mi_text, Constants.quit_mi_ac, jChat, this );

	view = new JMenu("View");
	show_menu_bar = Utility.createCheckBoxMenuItem( Constants.show_menu_bar_text, Constants.show_menu_bar_ac, view, this );
	show_topic_bar = Utility.createCheckBoxMenuItem( Constants.show_topic_bar_text, Constants.show_topic_bar_ac, view, this );

	server = new JMenu("Server");
	disconnect = Utility.createMenuItem( Constants.disconnect_mi_text, Constants.disconnect_mi_ac, server, this );
	reconnect = Utility.createMenuItem( Constants.reconnect_mi_text, Constants.reconnect_mi_ac, server, this );
	join_a_channel = Utility.createMenuItem( Constants.join_a_channel_mi_text, Constants.join_a_channel_mi_ac, server, this );
	list_of_channels = Utility.createMenuItem( Constants.list_of_channels_mi_text, Constants.list_of_channels_mi_ac, server, this );
	marked_away = Utility.createCheckBoxMenuItem( Constants.marked_away_text, Constants.marked_away_ac, server, this );
	
	window = new JMenu("Window");
	clear_text = Utility.createMenuItem( Constants.clear_text_mi_text, Constants.clear_text_mi_ac, window, this );
	search_text = Utility.createMenuItem( Constants.search_text_mi_text, Constants.search_text_mi_ac, window, this );

	help = new JMenu("Help");
	contents = Utility.createMenuItem( Constants.contents_mi_text, Constants.contents_mi_ac, help, this );
	about = Utility.createMenuItem( Constants.about_mi_text, Constants.about_mi_ac, help, this );

	mb = new JMenuBar();
	mb.add( jChat );
	mb.add( view );
	mb.add( server );
	mb.add( window );
	mb.add( help );

	mb.setVisible( true );

	this.setJMenuBar( mb );
	tp = new JTabbedPane();
	tp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	pane = this.getContentPane();
	//	Dimension size = this.getSize();
	//	pane.setSize((int)size.getWidth(), (int)size.getHeight());
	//	SpringLayout layout = new SpringLayout();

	BoxLayout layout = new BoxLayout(pane, BoxLayout.X_AXIS);
	//	layout.preferredLayoutSize(pane);
	pane.setLayout(layout);
	//	this.setLayout(layout);
	pane.add( tp );
	//	addTabbedPane(pane);
	//	nick_button = new JButton();
	//	ht_nick_buttons.add(nick_button);
	//	tm_net_2_buttons.put(ht_nick_buttons);
    }

    /*
    public JTabbedPane getNewJTabbedPane(String networkName)
    {
	JTabbedPane p2 = new JTabbedPane();
	p2.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	pane.add( p2 );
	this.add(p2);
	tmNet2JTabbedPane.put(networkName, p2);
	System.out.println("In getNewJTabbedPane.");
	return p2;
	//	p2.add("Pane 2", new JLabel("Test Tab"));	
    }

    */
    public JTabbedPane getJTabbedPane()
    {
	//	return tmNet2JTabbedPane.get(networkName);
	return tp;
    }

    public JTabbedPane getNewJTabbedPane(String networkName, Connection conn)
    {
	JTabbedPane tabbedPane = new JTabbedPane();
	tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	tmNet2JTabbedPane.put( networkName, tabbedPane );
	int index = 0;
	if( tp.getTabCount() > 0 )
	    index = tp.getTabCount();
	tp.add( networkName, tabbedPane );
	JPanel panel = new ButtonTabOuterComponent(tp, networkName, this);
	ihmPanelToConnection.put(panel, conn);
	tp.setTabComponentAt( index, panel);       
	return tabbedPane;
    }

    public Connection getConnection(JPanel panel)
    {
 	return ihmPanelToConnection.get(panel);
    }

    public void removeConnection(JPanel panel)
    {
	ihmPanelToConnection.remove(panel);
    }

    public void visible()
    {
	//	System.out.println("In visible");
	this.setVisible( true );
	this.pack();
    }

    public void setTextArea()
    {
	//	textArea = new JTextArea();
	
    }

    /*
    public void addTabGroup(TabGroup tabGroup)
    {
	
    }
    */

    /*
    public void initialise( final String tabname, String nick )
    {
	JPanel p = new JPanel( new BorderLayout() );
	JPanel p1 = new JPanel( new BorderLayout() );
	JPanel p2 = new JPanel( new BorderLayout() );
	JPanel p3 = new JPanel( new BorderLayout() );
	JPanel p4 = new JPanel( new BorderLayout() );
	JPanel p6 = new ButtonTabComponent(tp, tabname, this);

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
	tmta.put( tabname, server_ta );
	sp = new JScrollPane( server_ta );
	
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
			    parseCommand(tabname,tabname, s);
			}
		    else
			{
			    setText(tabname, "Please type an actual IRC command on this tab.");
			}
		}
	    });
	server_tf.setActionCommand( Constants.server_tf_ac );
	tmtf.put( tabname, server_tf );

	nick_button.setText( nick );
	nick_button.addActionListener( new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    //		    System.out.println("In actionPerformed of nick_but");
		    try
			{
			    //			    System.out.println("In try block");
			    changeNick(tabname);
			}
		    catch(NullPointerException npe)
			{}
		    catch(HeadlessException he)
			{}
		}
	    });
	ht_nick_buttons.add( nick_button );
	tm_net_2_buttons.put(tabname, ht_nick_buttons);

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
	
	tp.add( tabname, p );
	tp.setTabComponentAt( tabNumber, p6);
	tabNumber++;
	//	tp.setComponentAt(tp.indexOfTab(tabname), new ButtonTabComponent(tp));

	this.pack();
	//	System.out.println("Initialised");
    }

    public void create_chan_tab( final String network_name, final String channel_name )
    {
	String networkName = network_name;
	String channelName = channel_name;

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
	
	JPanel p = new JPanel( new BorderLayout() );
	JPanel p0 = new JPanel( new BorderLayout() );
	JPanel p1 = new JPanel( new BorderLayout() );
	JPanel p2 = new JPanel( new BorderLayout() );
	JPanel p3 = new JPanel( new BorderLayout() );
	JPanel p4 = new JPanel( new BorderLayout() );
	JPanel p5 = new JPanel( new BorderLayout() );
	JPanel p6 = new JPanel( new BorderLayout() );
	
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
			    parseCommand(network_name, channel_name, s);
			}
		    else
			{
			    tm_netname_2_conn.get(network_name).sendChannelMessage(channel_name, s);
			    setText(channel_name, "< " + nick_button.getText() + " > : " + s);
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
			    changeNick(network_name);
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

	p.add( p0, BorderLayout.EAST );
	p.add( p1, BorderLayout.CENTER );
	p.add( p4, BorderLayout.SOUTH );

	/*
	if( p.isRequestFocusEnabled() )
	    System.err.println("Error in requestFocusInWindow()");
	p.requestFocus();
	*/
	//	    System.err.println("Error in requestFocusInWindow()");
    //	tp.addTab( channel_name, p );

    //	tp.setSelectedComponent( p );
	//	System.out.println("Initialised");
	
    //    }

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

    /*    public void create_privmsg_tab( final String network_name, final String nick_name, String hostname, String message )
    {
	String networkName = network_name;
	String senderNick = nick_name;

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
	
	JPanel p = new JPanel( new BorderLayout() );
	JPanel p0 = new JPanel( new BorderLayout() );
	JPanel p1 = new JPanel( new BorderLayout() );
	JPanel p2 = new JPanel( new BorderLayout() );
	JPanel p3 = new JPanel( new BorderLayout() );
	JPanel p4 = new JPanel( new BorderLayout() );
	JPanel p5 = new JPanel( new BorderLayout() );
	JPanel p6 = new JPanel( new BorderLayout() );
	
	/*
	p0.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p0.add( p5, BorderLayout.NORTH );
	p0.add( sp0, BorderLayout.CENTER  );
	*/

    /*	JTextField hostname_tf = new JTextField();
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
			    parseCommand(network_name, nick_name, s);
			}
		    else
			{
			    tm_netname_2_conn.get(network_name).sendPrivateMessage(nick_name, s);
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
			    changeNick(network_name);
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

	/*
	if( p.isRequestFocusEnabled() )
	    System.err.println("Error in requestFocusInWindow()");
	p.requestFocus();
	*/
	//	    System.err.println("Error in requestFocusInWindow()");

    /*
	tp.addTab( nick_name, p );

	tp.setSelectedComponent( p );
	//	System.out.println("Initialised");
    }

    /**
     * Method for setting text on JTextArea 
     * @param tabname 
     * name of the tab on which text is to be appended
     * @param text 
     * text to be appended on the textarea
     */
    /*
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

    public void setNickButtonText( String networkName, String nick )
    {
	Vector<JButton> buttons = tm_net_2_buttons.get(networkName);
	for( int i = 0; i < ht_nick_buttons.size(); i++ )
	    {
		JButton jb = buttons.elementAt( i );
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

    /**
     * Method for setting topic for the given channel
     */

    /*
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

    /*
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

    /*
    public void set_chan_members( String networkName, String channel_name, TreeMap<String, Vector<String>> treemap )
    {
	//	Iterator<String> iter = treeset.iterator();

	/*
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

    public boolean getChannelTabExisted(String networkName, String channelName)
    {
	if( tmExistedNetwork2Channels.containsKey(networkName))
	    {
		return tmExistedNetwork2Channels.get(networkName).contains(channelName);
	    }
	return false;
    }

    public void reInitialiseChannel(String networkName, String channelName)
    {
	JList<String> list = tm_members_list.get(channelName);
	CellRenderer cr = new CellRenderer();
	list.setCellRenderer(cr);
	tm_cell_renderers.put(channelName, cr);
	tm_chan_members.put(channelName, new Vector<String>());
	Vector<String> channels = tm_network_2_channels.get(networkName);
	channels.add(channelName);
	tm_network_2_channels.put(networkName, channels);
	tmExistedNetwork2Channels.get(networkName).remove(channelName);
	setText(channelName, "Now talking on " + channelName);
    }

    public void clearChanMembersList(String networkName, String channelName)
    {
	JList<String> list = tm_members_list.get(channelName);
	list.setListData(new Vector<String>());
	tm_cell_renderers.remove(channelName);
	JLabel listLabel = tm_list_labels.get(channelName);
	listLabel.setText("  ");
	tm_list_labels.put(channelName, listLabel);
	tm_chan_members.remove(channelName);
	tm_network_2_channels.get(networkName).remove(channelName);
	tm_topic_tf.get(channelName).setText("");
	if( tmExistedNetwork2Channels.containsKey(networkName) )
	    {
		Vector<String> existedChannels = tmExistedNetwork2Channels.get(networkName);
		existedChannels.add(channelName);
		tmExistedNetwork2Channels.put(networkName, existedChannels);
	    }
	else
	    {
		Vector<String> existedChannels = new Vector<String>();
		existedChannels.add(channelName);
		tmExistedNetwork2Channels.put(networkName, existedChannels);
	    }
    }

    private void changeNick(String networkName)
    {
	System.out.println("changeNick");
	try
	    {
		String presentNick = nick_button.getText();
		String newNick = (String)JOptionPane.showInputDialog(this, Constants.promptNick, Constants.promptNick, JOptionPane.QUESTION_MESSAGE, null, null, presentNick);
		if( newNick != "" && newNick != null)
		    tm_netname_2_conn.get(networkName).changeNick(newNick);
	    }
	catch(NullPointerException npe)
	    {}
	catch(HeadlessException he)
	    {}
    }

    public void appendToAllTa(String networkName, String text)
    {
	Set<Map.Entry<String, JTextArea>> netname_ta = tmta.entrySet();
	Iterator<Map.Entry<String, JTextArea>> iterator = netname_ta.iterator();
	
	while( iterator.hasNext() )
	    {
		Map.Entry<String, JTextArea> entry = iterator.next();
		JTextArea ta = entry.getValue();
		ta.append(text + "\n");
	    }
    }

    public void modifyChannelList(String networkName, String oldNick, String newNick)
    {
	Vector<String> channels = tm_network_2_channels.get(networkName);
	
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

    /*
    public void addNickToChannelList(String networkName, String channelName, String nick, TreeMap<String, Vector<String>> treemap)
    {
	//	Vector<String> chanMembers = tm_chan_members.get(channels.elementAt(i));
	//	if(chanMembers.contains(oldNick))
	//		    {

	Vector<String> ops = treemap.get("o");
	Vector<String> voiced = treemap.get("v");
	Vector<String> others = treemap.get("r");

	JList<String> list = tm_members_list.get(channelName);
	CellRenderer cr = tm_cell_renderers.get(channelName);
	cr.setStatusNickTM(treemap);
	
	//	cr.changeNick(oldNick, newNick);
	Vector<String> for_list = tm_chan_members.get(channelName);
	TreeSet<String> treeset2 = tm_actual_members.get(channelName);

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

    */

    public void actionPerformed( ActionEvent ae )
    {
	String action = ae.getActionCommand();

	if( action.equals( Constants.net_list_mi_ac ) )
	    {
		userSettings.visible();
	    }
	else if( action.equals( Constants.nick_button_ac ) )
	    {
		/*
		try
		    {
			String nick = JOptionPane.showInputDialog( this, "Enter new nickname:");
			if( nick != null )
			    conn.change_nick( nick );
		    }
		catch( HeadlessException he )
		    {}
		*/
	    }
	else if( action.equals( Constants.join_a_channel_mi_ac ) )
	    {
		/*
		try
		    {
			String channel_name = JOptionPane.showInputDialog( this, "Enter channel to join:");
			if( channel_name != null )
			    //			    conn.join_channel( channel_name );
		    }
		catch( HeadlessException he )
		    {}
		*/
	    }
	else
	    {
		System.out.println("In else, actionPerformed");
		/*
		Enumeration<String> channels = htf.keys();
		try
		    {
			while( channels.hasMoreElements() )
			    {
				String element = channels.nextElement();
				if( action.equals( element ) )
				    {
					JTextField tf = (JTextField)(tmtf.get( element ));
					String input = tf.getText();
					//					conn.process_input( input );
					tf.setText("");
					break;
				    }
			    }
		    }
		catch( NoSuchElementException nsee )
		    {}
		*/
	    }
    }

    /*
    public void setAway(String networkName, String nick)
    {
	System.out.println("In setAway.");
	Vector<String> channels = tm_network_2_channels.get(networkName);
	
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

    private void parseCommand(String netname, String channelName, String command)
    {
	command = command.substring(1);
	String tokens[] = command.split(" ");
	tokens[0] = tokens[0].toUpperCase();
	Connection conn = tm_netname_2_conn.get(netname);

	if( tokens[0].equals("AWAY"))
	    {
		String awayMessage = command.substring(5);
		conn.setAway(awayMessage);
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
				conn.joinChannel(channel_name, key);
			    }
			else 
			    {
				conn.joinChannel(channel_name);
			    }
		    }
		else
		    {
			setText(netname, tm_usage_messages.get("JOIN"));
			if( !channelName.equals("") )
			    setText(channelName, tm_usage_messages.get("JOIN"));
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
			conn.kick(channelName, kickNick, reason);
		    }
		else
		    {
			setText(channelName, tm_usage_messages.get("PART"));
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
		String partUsageMessage = tm_usage_messages.get("PART");
		Vector<String> pms = tm_network_2_pms.get(netname);
		/*
		for( int i = 0; i < pms.size(); i++ )
		    {
			System.out.println("pms.elementAt( " + i + " ) = " + pms.elementAt(i));
		    }
		*/
    /*
		if( tokens.length >= 2 )
		    {
			partChannel = tokens[1];
			//			System.out.println("tm_topic_tf containsKey returns true.");
			if( tokens.length > 2 )
			    {
				partMessage = command.substring("/PART ".length() + tokens[1].length());
			    }
			conn.part( partChannel, partMessage );
			/*
			System.out.println("partChannel : " + partChannel);
			System.out.println("partMessage : " + partMessage);
			*/
    /*
		    }
		else if( pms != null && !pms.equals(null) && pms.contains(channelName))
		    {
			//			System.out.println("Tab is a pm");
			setText(netname, partUsageMessage);
			setText(channelName, partUsageMessage);
		    }
		else if( netname.equals(channelName) )
		    {
			setText(netname, partUsageMessage);			
		    }
		else
		    {
			conn.part(channelName, partMessage);
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

    public void removeTab(String networkName, String channelName)
    {
	tm_topic_tf.remove(channelName);
	tm_members_list.remove(channelName);
	tm_cell_renderers.remove(channelName);

	Vector<String> channels = tm_network_2_channels.get(networkName);
	channels.remove(channelName);
	tm_network_2_channels.put(networkName, channels);

	tm_chan_members.remove(channelName);
	tm_actual_members.remove(channelName);
	tm_list_labels.remove(channelName);

	int indexOfChannel = tp.indexOfTab(channelName);
	tp.removeTabAt(indexOfChannel);

	Vector<JButton> buttons = tm_net_2_buttons.get(networkName);
	buttons.removeElementAt(indexOfChannel - 1);
	tm_net_2_buttons.put(channelName, buttons);
	
	ht_nick_buttons.removeElementAt(indexOfChannel-1);
    }
    */

    public void windowActivated( WindowEvent we )
    {}

    public void windowClosed( WindowEvent we )
    {}

    public void windowDeactivated( WindowEvent we )
    {}

    public void windowDeiconified( WindowEvent we )
    {}

    public void windowIconified( WindowEvent we )
    {}

    public void windowOpened( WindowEvent we )
    {}

    public void windowClosing( WindowEvent we )
    {}

    public void keyPressed( KeyEvent ke )
    {}

    public void keyReleased( KeyEvent ke )
    {}

    public void keyTyped( KeyEvent ke )
    {}
}
