import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JViewport;

import javax.swing.border.Border;

import java.awt.Dimension;
import java.awt.Container;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import java.util.Vector;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;

public class ChannelTab extends JPanel
{
    private TabGroup tabGroup;
    private Connection connection;

    private JPanel p0;
    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;
    private JPanel p5;
    private JPanel p6;
    private JPanel p7;
	
    private String channelName;
    private String nickName;
    private JList<String> nicksList;
    private JTextField textField;
    private JTextField topicTextField;
    private CellRenderer cellRenderer;
    private JLabel chanInfo;
    private JScrollPane textPaneScroller;
    private JScrollPane nicksListScroller;
    private TextPaneExtended textPane;
    private JButton nickButton;
    private JTabbedPane tabbedPane;
    private KeyListener keyListener;

    private Vector<String> nicks;
    private TreeSet<String> nicksTreeSet;
    
    public ChannelTab(String channelName, TabGroup tabGroup)
    {
	super(new BorderLayout());
	this.channelName = channelName;
	this.tabGroup = tabGroup;
	this.tabbedPane = tabGroup.getJTabbedPane();
	this.connection = tabGroup.getConnection();
	this.nickName = tabGroup.getNickName();

	nicks = new Vector<String>();
	nicksTreeSet = new TreeSet<String>(new IgnoreCaseSort<String>());
	nicksList = new JList<String>();
	chanInfo = new JLabel();
	cellRenderer = new CellRenderer();
	textPane = new TextPaneExtended();
	textPaneScroller = new JScrollPane(textPane);
	JViewport viewport = textPaneScroller.getViewport();
	textPaneScroller.setViewport(viewport);
	nicksListScroller = new JScrollPane(nicksList);
	topicTextField = new JTextField();
	textField = new JTextField();
	nickButton = new JButton( tabGroup.getNickName() );
	p0 = new JPanel(new BorderLayout());
	p1 = new JPanel(new BorderLayout());
	p2 = new JPanel(new BorderLayout());
	p3 = new JPanel(new BorderLayout());
	p4 = new JPanel(new BorderLayout());
	p5 = new JPanel(new BorderLayout());
	p6 = new JPanel(new BorderLayout());
	p7 = new ButtonTabComponent(tabGroup.getJTabbedPane(), channelName, tabGroup );
	initialise();
    }

    public void setKeyListener(KeyListener kl)
    {
	keyListener = kl;
    }

    public void addKeyListeners()
    {
	this.addKeyListener(keyListener);
	p0.addKeyListener(keyListener);
	p1.addKeyListener(keyListener);
	p2.addKeyListener(keyListener);
	p3.addKeyListener(keyListener);
	p4.addKeyListener(keyListener);
	p5.addKeyListener(keyListener);
	p6.addKeyListener(keyListener);
	p7.addKeyListener(keyListener);
	textField.addKeyListener(keyListener);
	topicTextField.addKeyListener(keyListener);
	nicksListScroller.addKeyListener(keyListener);
	textPaneScroller.addKeyListener(keyListener);
	textPane.addKeyListener(keyListener);
	nicksList.addKeyListener(keyListener);
	/*
	p0.addKeyListener(keyListener);
	p0.addKeyListener(keyListener);
	p0.addKeyListener(keyListener);
	*/
    }

    public void initialise()
    {
	nicksList.setCellRenderer(cellRenderer);

	p5.setBorder( BorderFactory.createLineBorder( Color.black, 1 ) );
	p5.add( chanInfo, BorderLayout.CENTER );

	p0.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p0.add( p5, BorderLayout.NORTH );
	p0.add( nicksListScroller, BorderLayout.CENTER  );

	topicTextField.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	topicTextField.setVisible(UserPrefs.get_showTopicBar());
	topicTextField.addActionListener(new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    String s = topicTextField.getText();
		    topicTextField.setText("");
		    s = s.trim();
		    if( !s.equals(""))
			{
			    connection.setTopic(channelName, s);
			}
		}		
	    });

	p6.add( topicTextField, BorderLayout.CENTER );

	textPane.setMargin(new Insets(0,50,10,10));
	//	textPane.setLineWrap(true);

	//	textArea.append( chan_join_text );
	
	textPane.setEditable( false );
	textPane.setContentType("text/html");

	p1.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p1.add( textPaneScroller, BorderLayout.CENTER  );

	textField.addActionListener( new ActionListener() 
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    String s = textField.getText();
		    textField.setText("");
		    s = s.trim();
		    if( s.startsWith("/") )
			{
			    tabGroup.parseCommand(channelName, s);
			}
		    else
			{
			    if( !s.equals(""))
				{
				    connection.sendChannelMessage(channelName, s);
				    textPane.setMessage(nickName, s);
				}
			}
		}
	    });

	nickButton.addActionListener( new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    try
			{
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

	tabbedPane.add(channelName, this);
	tabbedPane.setSelectedComponent(this);
	tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, p7);
	tabbedPane.setSelectedComponent( this );
	textField.requestFocusInWindow();
    }

    public void showTopicTextField(boolean visible)
    {
	topicTextField.setVisible(visible);
    }

    public void setTextFieldFocus()
    {
	textField.requestFocusInWindow();
    }

    public void setNotice(String nick, String message)
    {
	textPane.setNotice(nick, message);
    }

    public void setJoinText(String nick, String userName, String hostName)
    {
	textPane.setJoinText(channelName, nick, userName, hostName);
    }

    public void setPartText(String nick, String userName, String hostName, String partMessage)
    {
	textPane.setPartText(channelName, nick, userName, hostName, partMessage);
    }

    public void setTopicText(String topic)
    {
	textPane.setTopicText(channelName, topic);
    }

    public void setTopicSetTimeText(String topicSetter, String topicTime)
    {
	textPane.setTopicSetTimeText(channelName, topicSetter, topicTime);
    }

    public void setHighlightedMessage(String nick, String message)
    {
	textPane.setHighlightedMessage( nick, message );
    }

    public void setRegularMessage(String nick, String message)
    {
	textPane.setRegularMessage(nick, message);
    }

    public void setSelfNickChangeText(String newNick)
    {
	System.out.println("In ChannelTab, setSelfNickChangeText.");
	textPane.setSelfNickChangeText(newNick);
    }

    public void setNickChangeText(String oldNick, String newNick)
    {
	if( nicks.contains(oldNick) )
	    textPane.setNickChangeText(oldNick, newNick );
    }

    public void setInvitationText(String channelName, String nick, String hostName)
    {
	textPane.setInvitationText(channelName, nick, hostName);
    }

    public void setSelfKickText(String channelName, String byWho, String reason)
    {
	textPane.setSelfKickText(channelName, byWho, reason);
    }

    public void setKickText(String channelName, String nick, String byWho, String reason)
    {
	textPane.setKickText(channelName, nick, byWho, reason);
    }

    public void setText(String text)
    {
	textPane.setChanInfo(text);
    }

    public void setNickButtonText(String nick)
    {
	this.nickName = nick;
	nickButton.setText(nick);
    }

    public void setTopic(String topic)
    {
	topicTextField.setText(topic);
    }

    public void setChanJoinText(String text)
    {
	textPane.setChanJoinText(text);
    }

    public void setMsgText(String nick, String message)
    {
	textPane.setMsgText(nick, message);
    }

    public void setDccChatInvitation(String nick, String ip, int port)
    {
	textPane.setDccChatInvitation(nick, ip, port);
    }

    public void setChanMembers( TreeMap<String, Vector<String>> treemap )
    {
	Vector<String> ops = treemap.get("o"); // key "o" is for operators
	Vector<String> voiced = treemap.get("v"); // key "v" is for voiced members
	Vector<String> others = treemap.get("r"); // key "r" is for rest of the members

	cellRenderer.setStatusNickTM(treemap);

	nicks.clear();
	nicksTreeSet.clear();

	Iterator<String> iterator;
	
	nicksTreeSet.addAll(ops);
	iterator = nicksTreeSet.iterator();
	while( iterator.hasNext() )
	    {
		String s = iterator.next();
		nicks.add(s);
	    }
		
	nicksTreeSet.clear();
	nicksTreeSet.addAll(voiced);
		
	iterator = nicksTreeSet.iterator();
	
	while( iterator.hasNext() )
	    {
		String s = iterator.next();
		nicks.add(s);
	    }

	nicksTreeSet.clear();
	nicksTreeSet.addAll(others);
	
	iterator = nicksTreeSet.iterator();
	
	while( iterator.hasNext() )
	    {
		String s = iterator.next();
		if( !nicks.contains(s) )
		    {
			nicks.add(s);
		    }
	    }

	int ops_no = ops.size();
	int voiced_no = voiced.size();
	int others_no = others.size();

	int total = ops_no + voiced_no + others_no;

	chanInfo.setText(ops_no + " ops, " + total + " total");

	nicksList.setListData( nicks );
    }    

    public void clearChanMembersList()
    {
	nicksList.setListData(new Vector<String>());
	chanInfo.setText("  ");
	topicTextField.setText("");
	//	existedChannels.add(channelName);
	//	System.out.println("At the end of clearChannelMembersList");
    }

    public void signalDisconnected(String text)
    {
	clearChanMembersList();
	setDisconnectedText(text);
    }

    public void modifyChannelList(String oldNick, String newNick)
    {
	//	Vector<String> channels = tm_network_2_channels.get(networkName);
	if(nicks.contains(oldNick))
	    {
		cellRenderer.changeNick( oldNick, newNick );
		int index_of_nick = nicks.indexOf(oldNick);
		nicks.remove(oldNick);
		//			chanMembers.remove(oldNick);
			//			chanMembers.add(index_of_nick, newNick);
		nicks.add(index_of_nick, newNick);
		nicksList.setListData(nicks);
	    }
    }

    public void setSelfAway()
    {
	setAway(nickName);
	textPane.setSelfAwayText("You have been marked as away.");
    }
			    
    public void setSelfUnAway()
    {
	setUnAway(nickName);
	textPane.setSelfAwayText("You have been marked as unaway.");
    }
			    
    public void setAway(String nick)
    {
	if(nicks.contains(nick))
	    {
		cellRenderer.addNickToAway(nick);
		System.out.println("After adding nick to away of cr.");
		int index_of_nick = nicks.indexOf(nick);
		nicks.remove(nick);
		nicks.add(index_of_nick, nick);
		nicksList.setCellRenderer(cellRenderer);
		nicksList.setListData(nicks);
	    }
    }

    public void setUnAway(String nick)
    {
	if(nicks.contains(nick))
	    {
		cellRenderer.removeNickFromAway(nick);
		System.out.println("After adding nick to away of cr.");
		int index_of_nick = nicks.indexOf(nick);
		nicks.remove(nick);
		nicks.add(index_of_nick, nick);
		nicksList.setCellRenderer(cellRenderer);
		nicksList.setListData(nicks);
	    }
    }

    private void setDisconnectedText(String text)
    {
	textPane.setDisconnectedText(text);
    }
}
