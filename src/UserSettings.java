import jerklib.ConnectionManager;
import jerklib.Profile;
import jerklib.Session;

import jerklib.events.IRCEvent;
import jerklib.events.JoinCompleteEvent;
import jerklib.events.MotdEvent;
import jerklib.events.IRCEvent.Type;

import jerklib.tasks.TaskImpl;
import jerklib.listeners.IRCEventListener;

import java.util.Vector;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
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
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

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
import java.io.UnsupportedEncodingException;

class UserSettings extends JDialog implements ActionListener, WindowListener, MouseListener, KeyListener, ListSelectionListener
{
    //    private Staring start;
    //    private Vector<Connection> conn;
    //    private DB_connection db_conn;
    private MainWindow mw;
    private Connection[] connections;

    private EditNetList edit_netlist;

    private JLabel user_info;
    private JLabel nick_name;
    private JLabel second_choice;
    private JLabel third_choice;
    private JLabel user_name;
    private JLabel real_name;
    private JLabel networks;
 
    private JTextField nick_name_tf;
    private JTextField second_choice_tf;
    private JTextField third_choice_tf;
    private JTextField user_name_tf;
    private JTextField real_name_tf;
    
    private Vector<String> servers;
    
    private JScrollPane scrollpane;
    private JList<String> network_list;
    private JButton add_button;
    private JButton remove_button;
    private JButton edit_button;
    private JButton sort_button;
    
    private JCheckBox skip_net_list;

    private JButton close_button;
    private JButton connect_button;

    private ConnectionManager manager;
    private Session session;

    private int available;

    UserSettings( MainWindow m )
    {
	this.setTitle(Constants.userSettingsTitle);
	mw = m;
	mw.setUserSettings(this);
	try
	    {
		connections = new Connection[100];
	    }
	catch(NullPointerException npe)
	    {
		//		System.out.println("In UserSettings : " + npe.getMessage());
	    }
	available = 0;
	//	conn = c;
	
	initialise();
    }

    private void initialise()
    {
	Container pane = this.getContentPane();
	pane.setLayout( new GridBagLayout() );
	this.setLocationRelativeTo( null );

	this.setResizable( false );
	this.setMinimumSize( new Dimension( 550, 500 ) );
	this.setPreferredSize( new Dimension( 550, 500 ) );
	this.setMaximumSize( new Dimension( 550, 500 ) );

	GridBagConstraints gbc;

	gbc = Utility.modifyGbc( 0, 0, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, 0 ), 0, 0 );
	user_info = Utility.createLabel( Constants.user_info_label, "" );
	pane.add( user_info, gbc );

	gbc = Utility.modifyGbc( 0, 1, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL, new Insets( 0, 0, 0, 0 ), 0, 0 );
	nick_name = Utility.createLabel( Constants.nick_name_label, "" );
	pane.add( nick_name, gbc );

	gbc = Utility.modifyGbc( 0, 2, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL, new Insets( 0, 0, 0, 0 ), 0, 0 );
	second_choice = Utility.createLabel( Constants.second_choice_label, "");
	pane.add( second_choice, gbc );

	gbc = Utility.modifyGbc( 0, 3, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL, new Insets( 0, 0, 0, 0 ), 0, 0 );
	third_choice = Utility.createLabel( Constants.third_choice_label, "");
	pane.add( third_choice, gbc );

	gbc = Utility.modifyGbc( 0, 4, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL, new Insets( 0, 0, 0, 0 ), 0, 0 );
	user_name = Utility.createLabel( Constants.user_name_label, "" );
	pane.add( user_name, gbc );

	gbc = Utility.modifyGbc( 0, 5, 1, 1, 0, 0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.VERTICAL, new Insets( 0, 0, 0, 0 ), 0, 0 );
	real_name = Utility.createLabel( Constants.real_name_label, "" );
	pane.add( real_name, gbc );
	
	gbc = Utility.modifyGbc( 1, 1, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	nick_name_tf = Utility.createTextField( UserPrefs.get_nick1(), true, this, Constants.nickname_tf_ac );
	pane.add( nick_name_tf, gbc );

	gbc = Utility.modifyGbc( 1, 2, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	second_choice_tf = Utility.createTextField( UserPrefs.get_nick2(), true, this, Constants.second_choice_tf_ac );
	pane.add( second_choice_tf, gbc );

	gbc = Utility.modifyGbc( 1, 3, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	third_choice_tf = Utility.createTextField( UserPrefs.get_nick3(), true, this, Constants.third_choice_tf_ac );
	pane.add( third_choice_tf, gbc );

	gbc = Utility.modifyGbc( 1, 4, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	user_name_tf = Utility.createTextField( UserPrefs.get_username(), true, this, Constants.username_tf_ac );
	pane.add( user_name_tf, gbc );

	gbc = Utility.modifyGbc( 1, 5, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	real_name_tf = Utility.createTextField( UserPrefs.get_realname(), true, this, Constants.realname_tf_ac );
	pane.add( real_name_tf, gbc );

	gbc = Utility.modifyGbc( 0, 6, GridBagConstraints.RELATIVE, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, new Insets( 0, 5, 0, 0 ), 0, 0 );
	pane.add( Utility.createLabel("",""), gbc );

	gbc = Utility.modifyGbc( 0, 7, GridBagConstraints.RELATIVE, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets( 10, 0, 0, 0 ), 0, 0 );
	networks = Utility.createLabel( Constants.networks_label, "" );
	pane.add( networks, gbc );

	gbc = Utility.modifyGbc( 0, 8, 2, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, 0 ), 0, 0 );
	skip_net_list = Utility.createCheckBox( Constants.skip_net_list_text, "", Constants.skip_net_list_ac, this, UserPrefs.get_net_list_skip() );
	pane.add( skip_net_list, gbc );

	gbc = Utility.modifyGbc( 3, 9, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets( 5, 0, 0, 0 ), 28, 0 );
	add_button = Utility.createButton( Constants.add_button_text, "", Constants.add_button_ac, this );
	pane.add( add_button, gbc );

	gbc = Utility.modifyGbc( 3, 10, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets( 5, 0, 0, 0 ), 0, 0 );
	remove_button = Utility.createButton( Constants.remove_button_text, "", Constants.remove_button_ac, this );
	pane.add( remove_button, gbc );

	gbc = Utility.modifyGbc( 3, 11, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets( 5, 0, 0, 0 ), 28, 0 );
	edit_button = Utility.createButton( Constants.edit_button_text, "", Constants.edit_button_ac, this );
	pane.add( edit_button, gbc );

	gbc = Utility.modifyGbc( 3, 12, GridBagConstraints.REMAINDER, 1, 0, 0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets( 5, 0, 0, 0 ), 26, 0 );
	sort_button = Utility.createButton( Constants.sort_button_text, "", Constants.sort_button_ac, this );
	pane.add( sort_button, gbc );

	servers = init_servlist();


	//	Vector<String> servers = db_conn.get_network_server();
	gbc = Utility.modifyGbc( 0, 9, 2, 8, 1, 2, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 );
	network_list = new JList<String>( servers );
	//	System.out.println("selectedIndex: " + UserPrefs.get_sel_list_index());
	network_list.setSelectedIndex(UserPrefs.get_sel_list_index() );
	//	System.out.println("Before ensureIndexIsVisible");
	network_list.addListSelectionListener( new  ListSelectionListener() 
	    {
		public void valueChanged(ListSelectionEvent lse)
		{
		    //		    System.out.println("In valueChanged" + network_list.getSelectedIndex());
		    UserPrefs.save_sel_list_index(network_list.getSelectedIndex());
		    UserPrefs.save_prefs();
		    //		    System.out.println(UserPrefs.get_sel_list_index());
		}
	    }
	    );
	scrollpane = new JScrollPane(network_list);
	scrollpane.setViewportView(network_list);
	network_list.ensureIndexIsVisible(network_list.getSelectedIndex());
	pane.add( scrollpane, gbc );

	/*
	gbc = Utility.modifyGbc( 0, 12, 2, 8, 1, 2, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets( 5, 0, 10, 5 ), 0, 0 );
	pane.add( Utility.createLabel("",""), gbc );

	gbc = Utility.modifyGbc( 1, 12, 2, 8, 1, 2, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets( 5, 0, 10, 5 ), 0, 0 );
	pane.add( Utility.createLabel("",""), gbc );

	gbc = Utility.modifyGbc( 1, 13, 2, 8, 1, 2, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets( 5, 0, 10, 5 ), 0, 0 );
	pane.add( Utility.createLabel("",""), gbc );

	gbc = Utility.modifyGbc( 1, 14, 2, 8, 1, 2, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets( 5, 0, 10, 5 ), 0, 0 );
	pane.add( Utility.createLabel("",""), gbc );

	gbc = Utility.modifyGbc( 1, 15, 2, 8, 1, 2, GridBagConstraints.LINE_END, GridBagConstraints.NONE, new Insets( 5, 0, 10, 5 ), 0, 0 );
	pane.add( Utility.createLabel("",""), gbc );
	*/


	gbc = Utility.modifyGbc( 0, 18, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 1, 2, GridBagConstraints.LAST_LINE_START, GridBagConstraints.NONE, new Insets( 0, 0, 0, 50 ), 0, 0 );
	close_button = Utility.createButton( Constants.close_button_text, "", Constants.close_button_ac, this );
	pane.add( close_button, gbc );

	gbc = Utility.modifyGbc( 1, 18, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1, 2, GridBagConstraints.LAST_LINE_END, GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 );
	connect_button = Utility.createButton( Constants.connect_button_text, "Connect", Constants.connect_button_ac, this );
	pane.add( connect_button, gbc );

	/*
	*/

        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	if(!UserPrefs.get_net_list_skip() )
	    this.setVisible( true );
    }

    private Vector<String> init_servlist()
    {
	Vector<String> servers = new Vector<String>();

	String file_text = Utility.read_whole_file( "conf" + File.separator + Constants.servlist_file );
	if( file_text != null )
	    {
		int serv_iter = 0;
		String last_network = null;
		String[] new_lines_toks = file_text.split("\n");

		edit_netlist = new EditNetList();
		for( int i = 0; i < new_lines_toks.length; i++ )
		    {
			String cur_tok = new_lines_toks[i];
			System.out.println("cur_tok : " + cur_tok);
			String[] tokens = cur_tok.split("=");
			if( cur_tok.startsWith("N") )
			    {
				last_network = tokens[1];
				servers.add( serv_iter++, tokens[1]);
			    }
			else if( cur_tok.startsWith("S"))
			    {				
				edit_netlist.add_server(last_network,tokens[1]);
			    }
			else if( cur_tok.startsWith("J"))
			    {
				if( tokens.length >= 2)
				    edit_netlist.add_auto_join_chan(last_network, tokens[1] );
			    }
		    }
		//		edit_netlist.show_chans_and_serv();
	    }
	
	return servers;
    }

    private void add_net_to_file(String net_name)
    {
	String text = "N=" + net_name + "\n";
	text += "J= \n";
	text += "E=IRC (Latin/Unicode Hybrid)\n";
	text += "F=19\n";
	text += "D=0\n";

	String tokens[] = net_name.toLowerCase().split(" ");
	String serv_name = "";
	for( int i = 0; i < tokens.length; i++ )
	    serv_name += tokens[i];

	text += "S=" + serv_name + "/6667\n\n";
	
	String file_text = Utility.read_whole_file( "conf" + File.separator + Constants.servlist_file );
	text += file_text;
	Utility.write_whole_file("conf" + File.separator + Constants.servlist_file, text );
    }

    private void del_net_from_file(int index)
    {
	String file_text = Utility.read_whole_file("conf" + File.separator + Constants.servlist_file);

	Pattern pattern = Pattern.compile("(N=.*\n(.*\n)??(.*\n)??(.*\n)??E.*\n(C.*\n)??F.*\nD.*\n(S.*\n)*\n){1}" );
	Matcher matcher = pattern.matcher(file_text);

	int i = 0;
	StringBuffer replaced = new StringBuffer();
	while (matcher.find()) 
	    {
		if( i == index )
		    {
			matcher.appendReplacement(replaced,"");
			break;
		    }
		i++;
	    }

	matcher.appendTail(replaced);
	Utility.write_whole_file("conf" + File.separator + Constants.servlist_file, replaced.toString());
    }

    private void sort_net_list()
    {
	String file_text = Utility.read_whole_file("conf" + File.separator + Constants.servlist_file);

	Pattern pattern = Pattern.compile("(N=.*\n(.*\n)??(.*\n)??(.*\n)??E.*\n(C.*\n)??F.*\nD.*\n(S.*\n)*\n){1}" );
	Matcher matcher = pattern.matcher(file_text);

	TreeMap<String, String> tm = new TreeMap<String, String>(new IgnoreCaseSort<String>());

	while (matcher.find()) 
	    {
		String matched_str = matcher.group();
		Pattern pattern2 = Pattern.compile("N=.*");
		Matcher matcher2 = pattern2.matcher(matched_str);
		if( matcher2.find() )
		    {
			String match2 = matcher2.group();
			tm.put(match2, matched_str);
		    }
	    }

	Set<Map.Entry<String, String>> set = tm.entrySet();
	Iterator<Map.Entry<String,String>> iter = set.iterator();
	String text = "";
	servers.removeAllElements();

	int i = 0;
	while( iter.hasNext() )
	    {
		Map.Entry<String, String> me = iter.next();
		String net_name = me.getKey();		
		String net_desc = me.getValue();
		text += net_desc;
		servers.add(i++, net_name.substring(2));
	    }

	network_list.setListData(servers);
	Utility.write_whole_file("conf" + File.separator + Constants.servlist_file, text);
    }


    public void visible()
    {
	this.setVisible( true );
    }

    public void invisible()
    {
	UserPrefs.save_nick1( nick_name_tf.getText() );
	UserPrefs.save_nick2( second_choice_tf.getText() );
	UserPrefs.save_nick3( third_choice_tf.getText() );
	UserPrefs.save_username( user_name_tf.getText() );
	UserPrefs.save_realname( real_name_tf.getText() );
	this.setVisible( false );
    }

    public String get_nick1()
    {
	return nick_name_tf.getText();	
    }

    public String get_nick2()
    {
	return second_choice_tf.getText();
    }

    public String get_nick3()
    {
	return third_choice_tf.getText();
    }

    public String get_username()
    {
	return user_name_tf.getText();
    }

    public String get_realname()
    {
	return real_name_tf.getText();
    }

    public void actionPerformed( ActionEvent ae )
    {
	String action = ae.getActionCommand();

	if( action.equals( Constants.connect_button_ac ) )
	    {
		//		System.out.println("Connect ac");
		UserPrefs.save_nick1(nick_name_tf.getText() );
		UserPrefs.save_nick2(second_choice_tf.getText() );
		UserPrefs.save_nick3(third_choice_tf.getText() );		
		UserPrefs.save_username(user_name_tf.getText() );
		UserPrefs.save_realname(real_name_tf.getText() );
		String nick = nick_name_tf.getText();
		String nick2 = second_choice_tf.getText();
		String nick3 = third_choice_tf.getText();
		String username = user_name_tf.getText();
		String realname = real_name_tf.getText();
		String[] nicks = { nick, nick2, nick3 };
		String networkName = network_list.getSelectedValue();
		UserPrefs.save_prefs();
		HashMap<String, String> serversPort = edit_netlist.getServersPort(networkName);
		/*
		for( int i = 0; i < servers.size(); i++ )
		    {
			System.out.println("servers( " + i + " ) = " + servers.elementAt(i));
		    }
		*/
		connections[available] = new Connection(serversPort);
		connections[available].initialise( nicks, username, realname, network_list.getSelectedValue() );
		connections[available].setEditNetList(edit_netlist);
		TabGroup tabGroup = new TabGroup(networkName,connections[available]);
		tabGroup.setJTabbedPane(mw.getNewJTabbedPane(networkName, connections[available], tabGroup));
		tabGroup.setParentPane(mw.getJTabbedPane());
		tabGroup.setMainWindow(mw);
		tabGroup.setJFrame(mw);
		tabGroup.setKeyListener(mw);
		connections[available].setTabGroup(tabGroup);
		mw.visible();
		tabGroup.initialise( nicks[0] );
		invisible();
		//		connections[available].setMainWindow(mw);
		//		mw.addConnection(networkName,connections[available]);
		//		System.out.println("before thread.start");
		new Thread( connections[available++]).start();
	    }
	else if( action.equals( Constants.add_button_ac ) )
	    {		
		//		System.out.println("Add button ac");
		try
		    {
			String new_net_name = JOptionPane.showInputDialog(this, "Enter name of the new network", "New Network");
			servers.add(0, new_net_name);
			add_net_to_file(new_net_name);
			network_list.setListData( servers );
			init_servlist();
		    }
		catch(NullPointerException npe)
		    {}
	    }
	else if( action.equals( Constants.remove_button_ac ) )
	    {
		int selected_index = network_list.getSelectedIndex();
		//		System.out.println("selected_index = " + selected_index);
		String selected_net = network_list.getSelectedValue();
		del_net_from_file( selected_index );	
		try
		    {
			servers.removeElementAt(selected_index);
			network_list.setListData(servers);
			edit_netlist.delete_network(selected_net);
		    }
		catch(ArrayIndexOutOfBoundsException exception)
		    {}
	    }
	else if( action.equals( Constants.sort_button_ac ) )
	    {
		sort_net_list();
	    }
	else if( action.equals( Constants.skip_net_list_ac))
	    {
		System.out.println("In action listener, skip_net_list_ac.isSelected():" + skip_net_list.isSelected());
		UserPrefs.save_net_list_skip(skip_net_list.isSelected());
		UserPrefs.save_prefs();
	    }
	else if( action.equals( Constants.nickname_tf_ac ) )
	    {
		System.out.println("In nickname_tf_ac");
		UserPrefs.save_nick1(nick_name_tf.getText() );
	    }
	else if( action.equals( Constants.second_choice_tf_ac ) )
	    {
		UserPrefs.save_nick2(second_choice_tf.getText() );
	    }
	else if( action.equals( Constants.third_choice_tf_ac ) )
	    {
		UserPrefs.save_nick3(third_choice_tf.getText() );		
	    }
	else if( action.equals( Constants.username_tf_ac ) )
	    {
		UserPrefs.save_username(user_name_tf.getText() );
	    }
	else if( action.equals( Constants.realname_tf_ac ) )
	    {
		UserPrefs.save_realname(real_name_tf.getText() );
	    }
	
	/*
	else if( action.equals( Constants.edit_button_ac ) )
	    {}
	else if( action.equals( Constants.close_button_ac ) )
	    {
		System.exit( 0 );
	    }
	else if( action.equals( Constants.connect_button_ac ) )
	    {
		String nick_name = nick_name_tf.getText().trim();
		String second_choice = second_choice_tf.getText().trim();
		String third_choice = third_choice_tf.getText().trim();
		String user_name = user_name_tf.getText().trim();
		String real_name = real_name_tf.getText().trim();

		if( user_name == "" || real_name == "" )
		    {
			//		Utility.showError( Constants.user_real_necessary );

			System.out.println( Constants.user_real_necessary );
		    }
		else
		    {
			invisible();
			/*
			try
			    {
				db_conn.set_nick_name( nick_name );
				db_conn.set_second_choice( second_choice );
				db_conn.set_third_choice( third_choice );
				db_conn.set_user_name( user_name );
				db_conn.set_real_name( real_name );
			    }
			catch( SQLException se )
			    {
				Utility.showError( se.getMessage(), null );
			    }
			String selectedServer = (String)network_list.getSelectedValue();
			
			conn.add( mw.get_connection() );
			
			Connection c = (Connection)conn.lastElement();

 			if( c.connect_to_server( selectedServer, 6667, db_conn, mw ) )
			    {
				
				mw.visible();
				try
				    {
					mw.initialise( selectedServer, db_conn.get_nick_name() );
				    }
				catch( SQLException se )
				    {
					Utility.showError( se.getMessage(), null );
				    }
				mw.setText( selectedServer, "Connected");
				/*
				try
				    {
					Thread.sleep( 60 );
				    }
				catch( InterruptedException ie )
				    {}
				try
				    {
					c.register();
				    }
				catch( SQLException se )
				    {
					Utility.showError( se.getMessage(), null );
				    }
				new Thread( c ).start();
			    }
		    }
	    }
	else
	    {
		System.out.println( action );
	    }
*/
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

    public void mouseClicked( MouseEvent e )
    {}

    public void mouseEntered( MouseEvent e )
    {}

    public void mouseExited( MouseEvent e )
    {}

    public void mousePressed( MouseEvent e )
    {}

    public void mouseReleased( MouseEvent e )
    {}

    public void keyPressed( KeyEvent ke )
    {}

    public void keyReleased( KeyEvent ke )
    {}

    public void keyTyped( KeyEvent ke )
    {}

    public void valueChanged(ListSelectionEvent e)
    {
	//	System.out.println("In valueChanged" + network_list.getSelectedIndex());
	UserPrefs.save_sel_list_index(network_list.getSelectedIndex());
    }
}