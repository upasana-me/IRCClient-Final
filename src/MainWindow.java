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
import java.awt.event.WindowAdapter;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainWindow extends JFrame implements ActionListener, WindowListener, KeyListener
{
    private Vector<Connection> conn;

    private TreeMap<String, JTextArea> tmta;
    private TreeMap<String, JTextField> tmtf;
    //    private Hashtable<String, JTextArea> h_chan_ta;
    //    private Hashtable<String, JTextField> h_chan_tf;
    private TreeMap<String, String> tm_usage_messages;
    private TreeMap<String, JTabbedPane> tmNet2JTabbedPane;
    private IdentityHashMap<ButtonTabOuterComponent, Connection> ihmPanelToConnection;
    private IdentityHashMap<String, Connection> ihmNetname2Connection;
    private IdentityHashMap<String, TabGroup> ihmNet2TabGroup;
    private IdentityHashMap<String, ButtonTabOuterComponent> ihmNetname2Panel;

    private Vector<JButton> ht_nick_buttons;

    private JTabbedPane tp;
    private Container pane;

    private JButton nick_button;

    private JMenuBar mb;

    private JMenu jChat;
    private JMenuItem net_list;
    private JMenuItem new_mi;
    //    private JMenuItem close;
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

    private TextPaneExtended server_ta;
    private JTextField server_tf;
    private JTextField serv_topic_tf;

    private int previousKeyCode;
    private boolean noNetwork;

    private UserSettings userSettings;
    
    private String selectedNetwork;
    private String selectedChannel;
    private String selectedTabNick;

    public MainWindow()
    {
	super( Constants.appTitle );
	tmNet2JTabbedPane = new TreeMap<String, JTabbedPane>();
	server_ta = new TextPaneExtended();
	server_tf = new JTextField();
	serv_topic_tf = new JTextField();
	noNetwork = false;

	ihmNetname2Panel = new IdentityHashMap<String, ButtonTabOuterComponent>();
	ihmNetname2Connection = new IdentityHashMap<String, Connection>();
	ihmPanelToConnection = new IdentityHashMap<ButtonTabOuterComponent, Connection>();
	ihmNet2TabGroup = new IdentityHashMap<String, TabGroup>();
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

    public void setTextPane()
    {
	noNetwork = true;
	JPanel p = new JPanel( new BorderLayout() );
	JPanel p1 = new JPanel( new BorderLayout() );
	JPanel p2 = new JPanel( new BorderLayout() );
	JPanel p3 = new JPanel( new BorderLayout() );
	JPanel p4 = new JPanel( new BorderLayout() );
	//need modification
	//	JPanel p6 = new ButtonTabComponent(tabbedPane, "Client", this);

	BorderLayout bl = new BorderLayout();
	bl.setHgap( 0 );
	bl.setVgap( 0 );
	JPanel p5 = new JPanel( bl );

	serv_topic_tf = new JTextField();
	serv_topic_tf.setEnabled( false );
	serv_topic_tf.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	//	p5.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p5.add( serv_topic_tf, BorderLayout.CENTER );

	server_ta = new TextPaneExtended();
	server_ta.setMargin(new Insets(0,50,0,10));
	//	server_ta.setLineWrap(true);

	//	tmta.put( networkName, server_ta );
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
			    System.out.println("parse command.");
			    //parseCommand(networkName, s);
			}
		    else
			{
			    server_ta.setServerInfo("Please type an actual IRC command on this tab.");
			}
		}
	    });
	server_tf.setActionCommand( Constants.server_tf_ac );
	//	tmtf.put( networkName, server_tf );

	nick_button = new JButton(userSettings.get_nick1());
	//	nick_button.setText( nick );
	nick_button.addActionListener( new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    try
			{
			    //			    changeNick();
			}
		    catch(NullPointerException npe)
			{}
		    catch(HeadlessException he)
			{}
		}
	    });

	p1.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p1.add( sp, BorderLayout.CENTER  );

	p2.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
 	p2.add( nick_button, BorderLayout.WEST );

	p3.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p3.add( server_tf );

	p.setBorder( BorderFactory.createLineBorder( Color.black, 2 ) );

	p4.add( p2, BorderLayout.LINE_START );
	p4.add( p3 );

	p.add( p5, BorderLayout.NORTH );
	p.add( p1, BorderLayout.CENTER );
	p.add( p4, BorderLayout.SOUTH );

	/*
	tabbedPane.add( networkName, p );
	tabbedPane.setSelectedComponent(p);
	tabbedPane.setTabComponentAt( tabbedPane.getTabCount() - 1, p6);
	*/
	tp.add("Client", p);
    }

    public void init()
    {
	this.setLocationRelativeTo( null );
	this.setExtendedState( Frame.MAXIMIZED_BOTH );

	jChat = new JMenu("JChat");
	net_list = Utility.createMenuItem( Constants.net_list_mi_text, Constants.net_list_mi_ac, jChat, this );
	//	close = Utility.createMenuItem( Constants.close_mi_text, Constants.close_mi_ac, jChat, this );
	quit = Utility.createMenuItem( Constants.quit_mi_text, Constants.quit_mi_ac, jChat, this );

	view = new JMenu("View");
	show_menu_bar = Utility.createCheckBoxMenuItem( Constants.show_menu_bar_text, Constants.show_menu_bar_ac, view, this, UserPrefs.get_showMenuBar() );
	show_topic_bar = Utility.createCheckBoxMenuItem( Constants.show_topic_bar_text, Constants.show_topic_bar_ac, view, this, UserPrefs.get_showTopicBar() );

	server = new JMenu("Server");
	disconnect = Utility.createMenuItem( Constants.disconnect_mi_text, Constants.disconnect_mi_ac, server, this );
	reconnect = Utility.createMenuItem( Constants.reconnect_mi_text, Constants.reconnect_mi_ac, server, this );
	join_a_channel = Utility.createMenuItem( Constants.join_a_channel_mi_text, Constants.join_a_channel_mi_ac, server, this );
	list_of_channels = Utility.createMenuItem( Constants.list_of_channels_mi_text, Constants.list_of_channels_mi_ac, server, this );
	marked_away = Utility.createCheckBoxMenuItem( Constants.marked_away_text, Constants.marked_away_ac, server, this, false );
	
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

	//	if( !UserPrefs.get_showMenuBar())
       	mb.setVisible( UserPrefs.get_showMenuBar() );
	//	mb.setVisible(true);

	this.setJMenuBar( mb );
	tp = new JTabbedPane();
	tp.addChangeListener(new ChangeListener()
	    {
		public void stateChanged(ChangeEvent ce)
		{
		    System.out.println("In mainWindow stateChanged");
		    if( !noNetwork )
			{
			    System.out.println("In if !noNetwork");
			    JTabbedPane jtp = (JTabbedPane)ce.getSource();
			    int index = jtp.getSelectedIndex();
			    if( index >= 0 )
				{
				    String selectedTab = jtp.getTitleAt(index);
				    setFocusTabColor(selectedTab);
				    if(networkExist(selectedTab))
					{
					    //					    JTabbedPane tabbedPane = ihmNet2JTabbedPane.get(selectedTab);
					    TabGroup tabGroup = ihmNet2TabGroup.get(selectedTab);
					    String selectedSubTab = tabGroup.getSelectedTab();
					    if( selectedSubTab != null )
						{
					    //					    int tabCount = tabbedPane.getTabCount();
						    selectedNetwork = selectedTab;
						    if( !selectedSubTab.equals(selectedTab))
							{
							    selectedChannel = selectedTab;
							    selectedTabNick = tabGroup.getNickName();
							    setTitle("IRC Client: " + selectedTabNick + " @ " + selectedTab + " / " + selectedChannel);
							}
						    else
							{
							    selectedChannel = "";
							    selectedTabNick = tabGroup.getNickName();
							    setTitle("IRC Client: " + selectedTabNick + " @ " + selectedTab);   
							}
						}
					    if( ihmNetname2Connection.containsKey(selectedTab))
						{
						    Connection connection = ihmNetname2Connection.get(selectedTab);
						    setAway(connection.isAway());
						}
					}
				}
			}
		}
	    });
	
	tp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	tp.requestFocusInWindow();
	pane = this.getContentPane();

	BoxLayout layout = new BoxLayout(pane, BoxLayout.X_AXIS);
	pane.setLayout(layout);
	pane.add( tp );
    }

    public JTabbedPane getJTabbedPane()
    {
	return tp;
    }

    public void setAway(boolean isAway)
    {
	marked_away.setState(isAway);
    }

    private boolean networkExist(String networkName)
    {
	return ihmNet2TabGroup.containsKey(networkName);
    }

    public JTabbedPane getNewJTabbedPane(String networkName, Connection conn, TabGroup tg)
    {
	JTabbedPane tabbedPane = new JTabbedPane();
	tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	ihmNet2TabGroup.put(networkName, tg);
	tmNet2JTabbedPane.put( networkName, tabbedPane );
	int index = 0;
	if( noNetwork )
	    {
		tp.removeAll();
		noNetwork = false;
	    }
	if( tp.getTabCount() > 0 )
	    index = tp.getTabCount();
	tp.add( networkName, tabbedPane );
	ButtonTabOuterComponent panel = new ButtonTabOuterComponent(tp, networkName, this);
	ihmPanelToConnection.put(panel, conn);
	ihmNetname2Connection.put(networkName, conn);
	ihmNetname2Panel.put(networkName, panel);
	tp.setTabComponentAt( index, panel);       
	tp.setSelectedComponent(tabbedPane);
	//	tp.setBackgroundAt(tp.indexOfTab(networkName), Color.WHITE);
	return tabbedPane;
    }

    private String getSelectedTabName()
    {
	int index = tp.getSelectedIndex();
	if( index >= 0 )
	    return tp.getTitleAt(index);	
	else 
	    return null;
    }

    public void setMessageTabColor(String networkName)
    {
	if( !getSelectedTabName().equals(networkName) )
	    {
		ButtonTabOuterComponent panel = ihmNetname2Panel.get(networkName);
		panel.setLabelColor(Color.MAGENTA);
	    }
    }

    public void setInfoTabColor(String networkName)
    {
	if( !getSelectedTabName().equals(networkName) )
	    {
		ButtonTabOuterComponent panel = ihmNetname2Panel.get(networkName);
		panel.setLabelColor(new Color(102, 0, 102));
	    }
    }

    public void setFocusTabColor(String networkName)
    {
	if( ihmNetname2Panel.containsKey(networkName))
	    {
		ButtonTabOuterComponent panel = ihmNetname2Panel.get(networkName);
		panel.setFocusTabColor();
	    }
    }

    public void setHighlightedTabColor(String networkName)
    {
	if( !getSelectedTabName().equals(networkName) )
	    {
		ButtonTabOuterComponent panel = ihmNetname2Panel.get(networkName);
		panel.setLabelColor(new Color(255, 0, 102));
	    }
    }

    public Connection getConnection(ButtonTabOuterComponent panel)
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

    public void actionPerformed( ActionEvent ae )
    {
	String action = ae.getActionCommand();
	int selectedNet = tp.getSelectedIndex();
	String selectedNetTitle = "";
	Connection connection = null;
	TabGroup tabGroup = null;
	if( selectedNet >= 0 )
	    {
		selectedNetTitle = tp.getTitleAt(selectedNet);
		connection = ihmNetname2Connection.get(selectedNetTitle);
		tabGroup = ihmNet2TabGroup.get(selectedNetTitle);
	    }

	
	if( action.equals( Constants.net_list_mi_ac ) )
	    {
		userSettings.visible();
	    }
	else if( action.equals(Constants.show_menu_bar_ac))
	    {
		boolean showMenuBarChecked = show_menu_bar.getState();
		UserPrefs.save_showMenuBar(showMenuBarChecked);
			mb.setVisible(showMenuBarChecked);
			if( !showMenuBarChecked )
			    JOptionPane.showMessageDialog(this, "The Menubar is now hidden. You can show it again by pressing F9.");
		    }
		else if( action.equals( Constants.show_topic_bar_ac))
		    {
			boolean showTopic = show_topic_bar.getState();		
			Vector<TabGroup> tabGroups = new Vector<TabGroup>(ihmNet2TabGroup.values());
			for( int i = 0; i < tabGroups.size(); i++ )
			    {
				tabGroups.elementAt(i).showTopicTextField(showTopic);
			    }
			UserPrefs.save_showTopicBar(showTopic);
		    }
		else if( action.equals(Constants.disconnect_mi_ac))
		    {
			tabGroup.setDisconnectedText("Disconnected()");
			connection.quit("");
			connection.allRemovePreviousTopicTime();
		    }
		else if( action.equals( Constants.reconnect_mi_ac))
		    {
			if( connection.isConnected() )
			    {
				tabGroup.setDisconnectedText("Disconnected()");
				connection.quit("");
				connection.allRemovePreviousTopicTime();
			    }
			//		new Thread(connection).start();
			connection.connect();
			//		TabGroup tabGroup = ihmNet2TabGroup.get(selectedNet);
		    }
		else if( action.equals(Constants.quit_mi_ac))
		    {
			try
			    {
				int option = JOptionPane.showConfirmDialog(this, "Do you really want to quit?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.YES_OPTION)
				    System.exit(0);
			    }
			catch(HeadlessException he)
			    {}
		    }
		else if( action.equals(Constants.join_a_channel_mi_ac))
		    {
			try
			    {
				String channel = (String)JOptionPane.showInputDialog(this, Constants.joinChannelPrompt , "Join channel", JOptionPane.QUESTION_MESSAGE);
				if( channel != "" && channel != null)
				    {
					String tokens[] = channel.split(" ");
					if( tokens.length < 2 )
					    connection.joinChannel(tokens[0]);
					else
					    connection.joinChannel(tokens[0], tokens[1]);
				    }
			    }
			catch(HeadlessException he)
			    {}		
		    }
		else if( action.equals( Constants.marked_away_ac ) )
		    {
			boolean setAway = marked_away.getState();
			if( setAway && !connection.isAway() )
			    {
				connection.setAway("Away");
			    }
			else if( !setAway && connection.isAway())
			    connection.unSetAway();
		    }
		else if( action.equals( Constants.clear_text_mi_ac ) )
		    {
			tabGroup.clearTextOfSelectedTab();
		    }
		else
		    {
			System.out.println("In else, actionPerformed");
		    }
    }

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
    {
	int selectedNet = tp.getSelectedIndex();
	if( selectedNet < 0 )
	    return;
	String selectedNetTitle = tp.getTitleAt(selectedNet);
	Connection connection = ihmNetname2Connection.get(selectedNetTitle);
	TabGroup tabGroup = ihmNet2TabGroup.get(selectedNetTitle);

	//	System.out.println("In keyPressed, previousKeyCode : " + previousKeyCode + " == " + );
	int keyCode = ke.getKeyCode();
	//	if(previousKeyCode != 0 )
	//	    previousKeyCode = keyCode;
	if( keyCode == KeyEvent.VK_F9)
	    {
		boolean currentState = UserPrefs.get_showMenuBar();
		mb.setVisible(!currentState);
		UserPrefs.save_showMenuBar(!currentState);
		show_menu_bar.setState(!currentState);
	    }
	else if( keyCode == KeyEvent.VK_CONTROL )
	    {
		previousKeyCode = keyCode;
	    }
	else if( keyCode == KeyEvent.VK_W )
	    {
		if( previousKeyCode == KeyEvent.VK_CONTROL )
		    {
			try
			    {
				int option = JOptionPane.showConfirmDialog(this, "Do you really want to close this network?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.YES_OPTION)
				    {
					tabGroup.quit();
				    }
			    }
			catch(HeadlessException he)
			    {}
		    }
	    }
	else if( keyCode == KeyEvent.VK_Q )
	    {
		if( previousKeyCode == KeyEvent.VK_CONTROL )
		    {
			try
			    {
				int option = JOptionPane.showConfirmDialog(this, "Do you really want to close the application?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.YES_OPTION)
				    {
					System.exit(0);
				    }
			    }
			catch(HeadlessException he)
			    {}
		    }
	    }
    }

    public void keyReleased( KeyEvent ke )
    {
	System.out.println("In keyReleased");
	previousKeyCode = 0; 
    }

    public void keyTyped( KeyEvent ke )
    {
	System.out.println("In keyTyped");
    }
}
