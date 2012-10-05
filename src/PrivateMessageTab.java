import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PrivateMessageTab extends JPanel
{
    private TextPaneExtended textPane;
    private JScrollPane scrollPane;
    private JTextField textField;
    private JTextField hostNameTextField;
    private JButton nickButton;

    private TabGroup tabGroup;
    private Connection connection;
    private String senderNickName;
    private String nickName;

    private JTabbedPane tabbedPane;

    private JPanel p0;
    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;
    private JPanel p5;
    private JPanel p6;
    private JPanel p7;

    public PrivateMessageTab(String senderNickName, TabGroup tabGroup)
    {
	super(new BorderLayout());
	this.tabGroup = tabGroup;
	this.senderNickName = senderNickName;
	this.tabbedPane = tabGroup.getJTabbedPane();
	this.nickName = tabGroup.getNickName();
	this.connection = tabGroup.getConnection();

	textPane = new TextPaneExtended();
	scrollPane = new JScrollPane(textPane);
	textField = new JTextField();
	hostNameTextField = new JTextField();
	nickButton = new JButton(nickName);

	p0 = new JPanel(new BorderLayout());
	p1 = new JPanel(new BorderLayout());
	p2 = new JPanel(new BorderLayout());
	p3 = new JPanel(new BorderLayout());
	p4 = new JPanel(new BorderLayout());
	p5 = new JPanel(new BorderLayout());
	p6 = new JPanel(new BorderLayout());
	p7 = new ButtonTabComponent(this.tabbedPane, senderNickName, tabGroup);

	initialise();
    }

    private void initialise()
    {
	//	JPanel p = new JPanel( new BorderLayout() );
	/*
	JPanel p0 = new JPanel( new BorderLayout() );
	JPanel p1 = new JPanel( new BorderLayout() );
	JPanel p2 = new JPanel( new BorderLayout() );
	JPanel p3 = new JPanel( new BorderLayout() );
	JPanel p4 = new JPanel( new BorderLayout() );
	JPanel p5 = new JPanel( new BorderLayout() );
	JPanel p6 = new JPanel( new BorderLayout() );
	
	//need modification

	/*
	p0.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p0.add( p5, BorderLayout.NORTH );
	p0.add( sp0, BorderLayout.CENTER  );
	*/

	//	hostname_tf.setText(hostname);
	hostNameTextField.setEditable(false);
	hostNameTextField.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );

	p6.add( hostNameTextField, BorderLayout.CENTER );

	//	chan_ta.setMargin(new Insets(0,50,0,10));
	//	textArea.setLineWrap(true);
	//	String chan_join_text = Constants.chan_join_text + channel_name + "\n";
	textPane.setEditable( false );

	p1.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p1.add( scrollPane, BorderLayout.CENTER  );

	textField.addActionListener( new ActionListener() 
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    String s = textField.getText();
		    textField.setText("");
		    if( s.startsWith("/") )
			{
			   tabGroup.parseCommand( senderNickName, s);
			}
		    else
			{
			    connection.sendPrivateMessage(senderNickName, s);
			    //			    tm_netname_2_conn.get(network_name).sendPrivateMessage(nick_name, s);
			    textPane.setMessage(nickName, s);
			}
		}
	    });

	nickButton.addActionListener( new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    //		    System.out.println("In actionPerformed of nick_but");
		    try
			{
			    //	    System.out.println("In try block");
			    tabGroup.changeNick();
			}
		    catch(NullPointerException npe)
			{}
		    catch(HeadlessException he)
			{}
		}
	    });

	p2.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
 	p2.add( nickButton, BorderLayout.WEST );

	p3.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p3.add( textField );

	this.setBorder( BorderFactory.createLineBorder( Color.black, 2 ) );

	p4.add( p2, BorderLayout.LINE_START );
	p4.add( p3 );

	this.add( p6, BorderLayout.NORTH );
	this.add( p0, BorderLayout.EAST );
	this.add( p1, BorderLayout.CENTER );
	this.add( p4, BorderLayout.SOUTH );

	tabbedPane.add( senderNickName, this );
	//need modification
	tabbedPane.setTabComponentAt( tabbedPane.getTabCount() - 1, p7);
	//	tabNumber++;
	/*
	if( p.isRequestFocusEnabled() )
	    System.err.println("Error in requestFocusInWindow()");
	p.requestFocus();
	*/
	//	    System.err.println("Error in requestFocusInWindow()");
	//	tp.addTab( nick_name, p );

	tabbedPane.setSelectedComponent( this );
    }

    public void setHostName(String hostName)
    {
	hostNameTextField.setText(hostName);
    }

    public void setMessage(String nick, String text)
    {
	textPane.setRegularMessage(nick, text);
    }
    
    public void setNickButtonText(String nick)
    {
	nickButton.setText(nick);
    }

    public void setInvitationText(String channelName, String nick, String hostName)
    {
	textPane.setInvitationText(channelName, nick, hostName);
    }
}