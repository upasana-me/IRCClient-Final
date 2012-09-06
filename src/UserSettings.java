import java.util.Vector;

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

class UserSettings extends JDialog implements ActionListener, WindowListener, MouseListener, KeyListener
{
    //    private Staring start;
    //    private Vector<Connection> conn;
    //    private DB_connection db_conn;
    //    private MainWindow mw;

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
    private JList<String> network_list;
    private JButton add_button;
    private JButton remove_button;
    private JButton edit_button;
    private JButton sort_button;
    
    private JCheckBox skip_net_list;

    private JButton close_button;
    private JButton connect_button;

    UserSettings()
    {
	this.setTitle(Constants.userSettingsTitle);
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
	nick_name_tf = Utility.createTextField( UserPrefs.get_nick1(), true );
	pane.add( nick_name_tf, gbc );

	gbc = Utility.modifyGbc( 1, 2, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	second_choice_tf = Utility.createTextField( UserPrefs.get_nick2(), true );
	pane.add( second_choice_tf, gbc );

	gbc = Utility.modifyGbc( 1, 3, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	third_choice_tf = Utility.createTextField( UserPrefs.get_nick3(), true );
	pane.add( third_choice_tf, gbc );

	gbc = Utility.modifyGbc( 1, 4, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	user_name_tf = Utility.createTextField( UserPrefs.get_username(), true );
	pane.add( user_name_tf, gbc );

	gbc = Utility.modifyGbc( 1, 5, GridBagConstraints.REMAINDER, 1, 2, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets( 0, 5, 0, 0 ), 0, 0 );
	real_name_tf = Utility.createTextField( UserPrefs.get_realname(), true );
	pane.add( real_name_tf, gbc );

	gbc = Utility.modifyGbc( 0, 6, GridBagConstraints.RELATIVE, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, new Insets( 0, 5, 0, 0 ), 0, 0 );
	pane.add( Utility.createLabel("",""), gbc );

	gbc = Utility.modifyGbc( 0, 7, GridBagConstraints.RELATIVE, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, new Insets( 10, 0, 0, 0 ), 0, 0 );
	networks = Utility.createLabel( Constants.networks_label, "" );
	pane.add( networks, gbc );

	gbc = Utility.modifyGbc( 0, 8, 2, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets( 0, 0, 0, 0 ), 0, 0 );
	skip_net_list = Utility.createCheckBox( Constants.skip_net_list_text, "", Constants.skip_net_list_ac, this, false );
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
	pane.add( new JScrollPane( network_list ), gbc );
	
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
	connect_button = Utility.createButton( Constants.connect_button_text, "", Constants.connect_button_ac, this );
	pane.add( connect_button, gbc );

	/*
	*/

        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	this.setVisible( true );
    }

    protected Vector<String> init_servlist()
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
		edit_netlist.show_chans_and_serv();
	    }
	
	return servers;
    }

    protected void add_net_to_file(String net_name)
    {
	String text = "N=" + net_name + "\n";
	text += "J=\n";
	text += "E=IRC (Latin/Unicode Hybrid)\n";
	text += "F=19\n";
	text += "D=0\n";

	String tokens[] = net_name.toLowerCase().split(" ");
	String serv_name = "";
	for( int i = 0; i < tokens.length; i++ )
	    serv_name += tokens[i];

	text += "S=" + serv_name + "/6667\n\n";
	
	System.out.println("text = " + text);

	String file_text = Utility.read_whole_file( "conf" + File.separator + Constants.servlist_file );
	text += file_text;

	System.out.println("\n\ntext = " + text);

	Utility.write_whole_file("conf" + File.separator + Constants.servlist_file, text );
    }

    protected void visible()
    {
	this.setVisible( true );
    }

    protected void invisible()
    {
	this.setVisible( false );
    }

    protected String get_nick1()
    {
	return nick_name_tf.getText();	
    }

    protected String get_nick2()
    {
	return second_choice_tf.getText();
    }

    protected String get_nick3()
    {
	return third_choice_tf.getText();
    }

    protected String get_username()
    {
	return user_name_tf.getText();
    }

    protected String get_realname()
    {
	return real_name_tf.getText();
    }

    public void actionPerformed( ActionEvent ae )
    {
	String action = ae.getActionCommand();

	if( action.equals( Constants.connect_button_ac ) )
	    {
		UserPrefs.save_nick1( get_nick1() );
		UserPrefs.save_nick2( get_nick2() );
		UserPrefs.save_nick3( get_nick3() );
		UserPrefs.save_username( get_username() );
		UserPrefs.save_realname( get_realname() );
		UserPrefs.save_prefs();
	    }
	else if( action.equals( Constants.add_button_ac ) )
	    {		
		System.out.println("Add button ac");
		String new_net_name = JOptionPane.showInputDialog(this, "Enter name of the new network", "New Network");
		servers.add(0, new_net_name);
		add_net_to_file(new_net_name);
		network_list.setListData( servers );
	    }

	/*
	else if( action.equals( Constants.remove_button_ac ) )
	    {}
	else if( action.equals( Constants.edit_button_ac ) )
	    {}
	else if( action.equals( Constants.sort_button_ac ) )
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
}