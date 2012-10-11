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
import javax.swing.JFrame;
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

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class TabGroup
{
    private JFrame mw;
    private MainWindow mainWindow;

    private String networkName;
    private String nickName;
    private Connection connection;

    private TreeMap<String, ChannelTab> tmChannelTab;
    private TreeMap<String, PrivateMessageTab> tmPrivateMessageTab;

    private Vector<String> channels;
    private Vector<String> pms; 
    private Vector<String> existedChannels;

    private TextPaneExtended server_ta;
    private JTextField server_tf;
    private JTextField serv_topic_tf;

    private JButton nick_button;

    private JTabbedPane tabbedPane;
    private JTabbedPane parentPane;
    private int tabNumber;
    
    private KeyListener keyListener;

    private JPanel p;
    private JPanel p1;
    private JPanel p2;
    private JPanel p3;
    private JPanel p4;
    private JPanel p5;
    private JPanel p6;

    public TabGroup(String networkName, Connection connection )
    {
	this.networkName = networkName;
	this.connection = connection;
	tabNumber = 0;

	tmChannelTab = new TreeMap<String, ChannelTab>();
	tmPrivateMessageTab = new TreeMap<String, PrivateMessageTab>();

	channels = new Vector<String>();
	pms = new Vector<String>();
	existedChannels = new Vector<String>();

    }

    public String getNickName()
    {
	return nickName;
    }

    public void setMainWindow(MainWindow mw)
    {
	this.mainWindow = mw;
    }

    public void setJFrame(JFrame f)
    {
	this.mw = f;
    }

    public void setKeyListener(KeyListener kl)
    {
	this.keyListener = kl;
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

    public void setParentPane(JTabbedPane pp)
    {
	this.parentPane = pp;
    }

    public JTabbedPane getJTabbedPane()
    {
	return tabbedPane;
    }

    public JTabbedPane getParentPane()
    {
	return parentPane;
    }

    public String getNickButtonText()
    {
	return nickName;
    }

    public void initialise( String nick )
    {
	this.nickName = nick;

	p = new JPanel( new BorderLayout() );
	p1 = new JPanel( new BorderLayout() );
	p2 = new JPanel( new BorderLayout() );
	p3 = new JPanel( new BorderLayout() );
	p4 = new JPanel( new BorderLayout() );
	//need modification
	p6 = new ButtonTabComponent(tabbedPane, networkName, this);

	BorderLayout bl = new BorderLayout();
	bl.setHgap( 0 );
	bl.setVgap( 0 );
	p5 = new JPanel( bl );
	p5.addKeyListener(keyListener);

	serv_topic_tf = new JTextField();
	serv_topic_tf.setEnabled( false );
	serv_topic_tf.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	serv_topic_tf.setVisible(UserPrefs.get_showTopicBar());
	serv_topic_tf.addKeyListener(keyListener);
	//	p5.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p5.add( serv_topic_tf, BorderLayout.CENTER );

	server_ta = new TextPaneExtended();
	server_ta.setMargin(new Insets(0,50,10,10));
	server_ta.addKeyListener(keyListener);
	//	server_ta.setLineWrap(true);
	//	tmta.put( networkName, server_ta );
	JScrollPane sp = new JScrollPane( server_ta );
	
	server_ta.setEditable( false );

	server_tf = new JTextField();
	server_tf.addKeyListener(keyListener);
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
	server_tf.requestFocusInWindow();
	//	tmtf.put( networkName, server_tf );

	nick_button = new JButton(nick);
	//	nick_button.setText( nick );
	nick_button.addActionListener( new ActionListener()
	    {
		public void actionPerformed(ActionEvent ae)
		{
		    try
			{
			    changeNick();
			}
		    catch(NullPointerException npe)
			{}
		    catch(HeadlessException he)
			{}
		}
	    });

	p1.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p1.add( sp, BorderLayout.CENTER  );
	p1.addKeyListener(keyListener);

	p2.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
 	p2.add( nick_button, BorderLayout.WEST );
	p2.addKeyListener(keyListener);

	p3.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p3.add( server_tf );
	p3.addKeyListener(keyListener);

	p.setBorder( BorderFactory.createLineBorder( Color.black, 2 ) );
	p.addKeyListener(keyListener);

	p4.add( p2, BorderLayout.LINE_START );
	p4.add( p3 );
	p4.addKeyListener(keyListener);

	p.add( p5, BorderLayout.NORTH );
	p.add( p1, BorderLayout.CENTER );
	p.add( p4, BorderLayout.SOUTH );

	p.addKeyListener(keyListener);
	tabbedPane.requestFocusInWindow();
	p.requestFocusInWindow();
	tabbedPane.add( networkName, p );
	tabbedPane.setSelectedComponent(p);
	tabbedPane.setTabComponentAt( tabbedPane.getTabCount() - 1, p6);
	tabbedPane.addChangeListener(new ChangeListener()
	    {
		public void stateChanged(ChangeEvent ce)
		{
		    JTabbedPane jtp = (JTabbedPane)ce.getSource();
		    int index = jtp.getSelectedIndex();
		    String selectedTab = jtp.getTitleAt(index);
		    setTextFieldFocus(selectedTab);
		    if( connection.isConnected())
			{
			    boolean isAway = connection.isAway();
			    mainWindow.setAway(isAway);
			}
		}
	    });
	System.out.println("Added panel on JTabbedPane.");
    }

    public void create_chan_tab(String channelName)
    {
	channels.add(channelName);
	ChannelTab channelTab = new ChannelTab(channelName, this);
	channelTab.setKeyListener(keyListener);
	channelTab.addKeyListeners();
	tmChannelTab.put(channelName, channelTab);
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
    public void create_privmsg_tab(String senderNickName, String hostname, String message )
    {
	pms.add(senderNickName);
	PrivateMessageTab privateMessageTab = new PrivateMessageTab(senderNickName, this);
	tmPrivateMessageTab.put(senderNickName, privateMessageTab );
	privateMessageTab.addKeyListener(keyListener);
	privateMessageTab.setMessage(senderNickName, message);
	privateMessageTab.setHostName(hostname);
    }

    public Connection getConnection()
    {
	return connection;
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
	if( text.equals("\n") || text.equals("") || text.equals(" ") || text.equals(null) || text == null )
	    return;

	if( tabname.equals(networkName))
	    setServerInfo(text);
	else if( channels.contains(tabname) )
	    {
		ChannelTab channelTab = tmChannelTab.get( tabname );
		channelTab.setText(text);
	    }
	else 
	    {
		PrivateMessageTab privateMessageTab = tmPrivateMessageTab.get(tabname);
		privateMessageTab.setText(text);
	    }
    }

    public void setServerInfo(String text)
    {
	server_ta.setServerInfo(text);
	//	server_ta.append(text + "\n");
	//	server_ta.setCaretPosition(server_ta.getDocument().getLength());
    }

    public void setErrorMessage(String errorMessage)
    {
	server_ta.setErrorMessage(errorMessage);
    }

    public void setChanJoinText(String channelName, String text)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setChanJoinText(text);
    }

    public void setServNotice(String notice)
    {
	server_ta.setServNotice(notice);	
    }

    public void setChannelNotice(String channelName, String byWho, String message)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setNotice(byWho, message);		
    }
    public void setNotice(String nick, String notice)
    {
	String selectedTab = getSelectedTab();
	if(tmChannelTab.containsKey(selectedTab))
	    {
	       ChannelTab channelTab = tmChannelTab.get(selectedTab);
	       channelTab.setNotice(nick, notice);	
	    }
	else if( tmPrivateMessageTab.containsKey(selectedTab))
	    {
		PrivateMessageTab privateMessageTab = tmPrivateMessageTab.get(selectedTab);
	    }
	else
	    server_ta.setNotice(nick, notice);
    }

    public void setJoinText(String channelName, String nick, String userName, String hostName)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setJoinText(nick, userName, hostName);
    }

    public void setPartText(String channelName, String nick, String userName, String hostName, String partMessage)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setPartText(nick, userName, hostName, partMessage);
    }
    
    public void setTopicText(String channelName, String topic)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setTopicText(topic);
    }

    public void setTopicSetTimeText(String channelName, String topicSetter, String topicTime )
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setTopicSetTimeText(topicSetter, topicTime);
    }

    public void setHighlightedMessage(String channelName, String nick, String message)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setHighlightedMessage(nick, message);	
    }

    public void setRegularMessage(String tabName, String nick, String message)
    {
	if( tmChannelTab.containsKey(tabName) )
	    {
		ChannelTab channelTab = tmChannelTab.get(tabName);
		channelTab.setRegularMessage(nick, message);	
	    }
	else if( tmPrivateMessageTab.containsKey(tabName) )
	    {
		PrivateMessageTab privateMessageTab = tmPrivateMessageTab.get(tabName);
		privateMessageTab.setMessage(nick, message);		
	    }
	    
    }

    public void setSelfNickChangeText(String newNick)
    {
	server_ta.setServerInfo(Constants.selfNickChangeText + newNick);
	System.out.println("In setSelfNickChangeText.");
	Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
	for( int i = 0; i < channelTabs.size(); i++ )
	    {
		channelTabs.elementAt(i).setSelfNickChangeText(newNick);
	    }
    }

    public void setNickChangeText(String channelName, String oldNick, String newNick)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setNickChangeText(oldNick, newNick );	
    }

    public void setInvitationText(String channelName, String nick, String hostName)
    {
	String selectedTab = getSelectedTab();
	if(tmChannelTab.containsKey(selectedTab))
	    {
		ChannelTab channelTab = tmChannelTab.get(selectedTab);
		channelTab.setInvitationText(channelName, nick, hostName);
	    }
	else if( tmPrivateMessageTab.containsKey(selectedTab))
	    {
		PrivateMessageTab privateMessageTab = tmPrivateMessageTab.get(selectedTab);
		privateMessageTab.setInvitationText(channelName, nick, hostName);
	    }
	else
	    {
		server_ta.setInvitationText(channelName, nick, hostName);
	    }
    }
    
    public void setSelfKickText(String channelName, String byWho, String reason)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setSelfKickText(channelName, byWho, reason);
    }

    public void setKickText(String channelName, String nick, String byWho, String reason)
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setKickText(channelName, nick, byWho, reason);	
    }

    public void setWhoText(String nick,
			   String userName, 
			   String hostName, 
			   String realName, 
			   String hereOrGone,
			   String channelName, 
			   String serverName, 
			   int hopCount)
    {
	server_ta.setWhoText(nick, userName, hostName, realName, hereOrGone, 
			     channelName, serverName, hopCount);
    }

    public void setWhoisText(String nick, 
			     String userName,
			     String hostName, 
			     String realName, 
			     String server, 
			     String serverInfo,
			     String idleTime,
			     String signOnTimeStr,
			     String whoisChannels, 
			     String endOfList)
    {
	server_ta.setWhoisText(nick, userName, hostName, realName, server, serverInfo, idleTime, signOnTimeStr, whoisChannels, endOfList);
    }
  
    public void setWhoWasText(String nick, String userName, String hostName, String realName)
    {
	server_ta.setWhoWasText(nick, userName, hostName, realName);
    }

    public void setWhoWasRemainingText(String nick, String remainingText)
    {
	server_ta.setWhoWasRemainingText(nick, remainingText);
    }

    public void setMsgText(String tabName, String nick, String message)
    {
	if(tmChannelTab.containsKey(tabName))
	    {
		ChannelTab channelTab = tmChannelTab.get(tabName);
		channelTab.setMsgText(nick, message);
	    }
	else if(tmPrivateMessageTab.containsKey(tabName))
	    {
		PrivateMessageTab privateMessageTab = tmPrivateMessageTab.get(tabName);
		privateMessageTab.setMsgText(nick, message);
	    }
	else
	    server_ta.setMsgText(nick, message);
    }

    public void setDccChatInvitation(String nick, String ip, int port)
    {
	String tabName = getSelectedTab();
	if(tmChannelTab.containsKey(tabName))
	    {
		ChannelTab channelTab = tmChannelTab.get(tabName);
		channelTab.setDccChatInvitation(nick, ip, port);
	    }
	else if(tmPrivateMessageTab.containsKey(tabName))
	    {
		PrivateMessageTab privateMessageTab = tmPrivateMessageTab.get(tabName);
		privateMessageTab.setDccChatInvitation(nick, ip, port);
	    }
	else
	    server_ta.setDccChatInvitation(nick, ip, port);	
    }

    public void setNickButtonText( String nick )
    {
	this.nickName = nick;
	try
	    {
		nick_button.setText(nick);
		Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
		for( int i = 0; i < channelTabs.size(); i++ )
		    channelTabs.elementAt(i).setNickButtonText(nick);
		Vector<PrivateMessageTab> privateMessageTabs = new Vector<PrivateMessageTab>(tmPrivateMessageTab.values());
		for( int i = 0; i < privateMessageTabs.size(); i++ )
		    privateMessageTabs.elementAt(i).setNickButtonText(nick);	
	    }
	catch(NullPointerException npe)
	    {
		npe.printStackTrace();
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

    public void removeTab(String channelName)
    {
	if( channels.contains(channelName))
	    {
		System.out.println("channles contains " + channelName);
		channels.remove(channelName);
		tmChannelTab.remove(channelName);
		int indexOfChannel = tabbedPane.indexOfTab(channelName);
		tabbedPane.removeTabAt(indexOfChannel);
		connection.part(channelName, null);
		System.out.println("At the end of if block");
	    }
	else
	    {
		System.out.println("channles doesn't contain contains " + channelName);
		pms.remove(channelName);
		tmPrivateMessageTab.remove(channelName);
		int indexOfChannel = tabbedPane.indexOfTab(channelName);
		tabbedPane.removeTabAt(indexOfChannel);
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
	connection.quit("");
	int indexOfChannel = parentPane.indexOfComponent(tabbedPane);
	tabbedPane.removeAll();
	parentPane.remove(tabbedPane);
    }

    public void showTopicTextField(boolean visible)
    {
	try
	    {
		serv_topic_tf.setVisible(visible);
		Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
		for( int i = 0; i < channelTabs.size(); i++ )
		    channelTabs.elementAt(i).showTopicTextField(visible);
		Vector<PrivateMessageTab> privateMessageTabs = new Vector<PrivateMessageTab>(tmPrivateMessageTab.values());
		for( int i = 0; i < privateMessageTabs.size(); i++ )
		    privateMessageTabs.elementAt(i).showHostNameTextField(visible);	
	    }
	catch(NullPointerException npe)
	    {
		npe.printStackTrace();
	    }	
    }

    public void setTextFieldFocus(String channelName)
    {
	if( tmChannelTab.containsKey(channelName))
	    {
		ChannelTab channelTab = tmChannelTab.get(channelName);
		channelTab.setTextFieldFocus();
	    }
	else if(tmPrivateMessageTab.containsKey(channelName))
	    {
		PrivateMessageTab privateMessageTab = tmPrivateMessageTab.get(channelName);
		privateMessageTab.setTextFieldFocus();
	    }
	else
	    server_tf.requestFocusInWindow();
    }

    public void setTopic( String channelName, String topic )
    {
	try
	    {
		ChannelTab channelTab = tmChannelTab.get(channelName);
		channelTab.setTopic(topic);		
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
    public void set_chan_members( String channelName, TreeMap<String, Vector<String>> treemap )
    {
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.setChanMembers(treemap);
	
    }

    public void reInitialiseChannel(String channelName)
    {
	channels.add(channelName);
	tmChannelTab.get(channelName).setText("Now talking on " + channelName);
    }

    public void clearChanMembersList(String channelName)
    {
	System.out.println("In clearChannelMembersList");
	channels.remove(channelName);
	ChannelTab channelTab = tmChannelTab.get(channelName);
	channelTab.clearChanMembersList();
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

    public void changeNick()
    {
	System.out.println("changeNick");
	try
	    {
		String newNick = (String)JOptionPane.showInputDialog(mw, Constants.promptNick, Constants.promptNick, JOptionPane.QUESTION_MESSAGE, null, null, nickName);
		if( newNick != "" && newNick != null)
		    {
			connection.changeNick(newNick);
		    }
	    }
	catch(NullPointerException npe)
	    {}
	catch(HeadlessException he)
	    {}
    }

    public void appendToAllTa(String text)
    {
	setServerInfo( text );

	Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
	for( int i = 0; i < channelTabs.size(); i++ )
	    {
		channelTabs.elementAt(i).setText(text);
	    }

	Vector<PrivateMessageTab> privateMessageTabs = new Vector<PrivateMessageTab>(tmPrivateMessageTab.values());
	for( int i = 0; i < privateMessageTabs.size(); i++ )
	    {
		//		privateMessageTabs.elementAt(i).setText(text);
	    }	
    }

    public void setDisconnectedText(String text)
    {
	System.out.println("In setDisconnectedText");
	server_ta.setDisconnectedText(text);
	existedChannels = channels;

	Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
	for( int i = 0; i < channelTabs.size(); i++ )
	    {
		System.out.println("In setDisconnectedText, i = " + i);
		channelTabs.elementAt(i).signalDisconnected(text);
	    }

	Vector<PrivateMessageTab> privateMessageTabs = new Vector<PrivateMessageTab>(tmPrivateMessageTab.values());
	for( int i = 0; i < privateMessageTabs.size(); i++ )
	    {
		System.out.println("In setDisconnectedText, i = " + i);
		privateMessageTabs.elementAt(i).setDisconnectedText(text);
	    }	
    }

    public boolean channelTabExists(String tabName)
    {
	return tmChannelTab.containsKey(tabName);
    }

    public void modifyChannelList(String oldNick, String newNick)
    {
	Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
	
	for(int i = 0; i < channelTabs.size(); i++ )
	    {
		channelTabs.elementAt(i).modifyChannelList(oldNick, newNick);
	    }	
    }

    public void setSelfAway()
    {
	server_ta.setSelfUnAwayText("You have been marked as away.");
	Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
	for(int i = 0; i < channelTabs.size(); i++ )
	    {
		channelTabs.elementAt(i).setSelfAway();
	    }
	Vector<PrivateMessageTab> privateMessageTabs = new Vector<PrivateMessageTab>(tmPrivateMessageTab.values());
	for(int i = 0; i < privateMessageTabs.size(); i++ )
	    {
		privateMessageTabs.elementAt(i).setSelfAwayText();
	    }
    }

    public void setSelfUnAway()
    {
	server_ta.setSelfUnAwayText("You have marked as unaway.");
	Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
	for(int i = 0; i < channelTabs.size(); i++ )
	    {
		channelTabs.elementAt(i).setSelfUnAway();
	    }
	Vector<PrivateMessageTab> privateMessageTabs = new Vector<PrivateMessageTab>(tmPrivateMessageTab.values());
	for(int i = 0; i < privateMessageTabs.size(); i++ )
	    {
		privateMessageTabs.elementAt(i).setSelfUnAwayText();
	    }
    }

    public void setAway(String nick)
    {
	System.out.println("In setAway.");
	
	Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
	for(int i = 0; i < channelTabs.size(); i++ )
	    {
		channelTabs.elementAt(i).setAway(nick);
	    }
    }

    public void setUnAway(String nick)
    {
	System.out.println("In setAway.");
	
	Vector<ChannelTab> channelTabs = new Vector<ChannelTab>(tmChannelTab.values());
	for(int i = 0; i < channelTabs.size(); i++ )
	    {
		channelTabs.elementAt(i).setUnAway(nick);
	    }
    }

    public void parseCommand(String channelName, String command)
    {
	System.out.println("In parseCommand");
	command = command.substring(1);
	String tokens[] = command.split(" ");
	tokens[0] = tokens[0].toUpperCase();

	if( tokens[0].equals("ACTION") )
	    {
		String action = "";
		if( tokens.length > 1 )
		    action = command.substring(tokens[0].length() + 1);
		connection.sendActionMessage(channelName, action);
		setText(channelName, getNickName() + " " + action);
	    }
	else if( tokens[0].equals("AWAY"))
	    {
		String awayMessage = "AWAY";
		if( tokens.length > 1 )
		    awayMessage = command.substring(5);
		connection.setAway(awayMessage);
	    }
	else if( tokens[0].equals("BAN"))
	    {
		if( tokens.length >= 2 )
		    {
			String ban = tokens[1];
			connection.setMode(channelName,"+b " + ban);
		    }
		else
		    {
			//usage string
		    }
	    }
	else if( tokens[0].equals("CTCP"))
	    {
		if( tokens.length >= 3 )
		    {
			String target = tokens[1];
			String message = command.substring( tokens[0].length() + tokens[1].length() + 2 );
			connection.ctcp(target, message);
		    }
	    }
	else if( tokens[0].equals("DCC"))
	    {
		System.out.println("DCC");
		if( tokens.length >= 2 )
		    {
			if( tokens[1].equals("ACCEPT"))
			    {}
			else if( tokens[1].equalsIgnoreCase("CHAT"))
			    {
				System.out.println("DCC CHAT");
				if( tokens.length >= 3 )
				    {
					String nick = tokens[2];
					DccConnection dccConnection = new DccConnection(nickName, nick, mw, this);
					new Thread(dccConnection).start();
					try
					    {
						Thread.sleep(60);
					    }
					catch(InterruptedException ie)
					    {}
					int ipAddress = dccConnection.getIpBytes();
					int port = dccConnection.getPort();
					String message = "\001DCC CHAT chat " + ipAddress + " " + port + "\001";
					System.out.println("message : " + message);
					connection.sendPrivateMessage(nick, message);
				    }
				else
				    {}
			    }
			else if( tokens[1].equals("CLOSE"))
			    {}
			else if( tokens[1].equals("DECLINE"))
			    {}
			else if( tokens[1].equals("LIST"))
			    {}
			else if( tokens[1].equals("SEND"))
			    {}
		    }
	    }
	else if( tokens[0].equals("DEOP"))
	    {
		if( tokens.length < 2 )
		    {}
		else
		    {
			for( int i = 1; i < tokens.length; i++ )
			    {
				connection.deop(channelName, tokens[i]);
			    }
		    }		
	    }
	else if( tokens[0].equals("HELP"))
	    {}
	else if( tokens[0].equals("INVITE"))
	    {
		if( tokens.length < 2 )
		    {
			//usage message display
		    }
		else
		    {
			//			System.out.println(tokens[1]);
			connection.invite(channelName, tokens[1]);
			setText(channelName, "You've invited " + tokens[1] + " to " + channelName);
		    }		
	    }
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
	else if( tokens[0].equals("ME"))
	    {
		String action = "";
		if( command.length() > 3 )
		    action = command.substring(tokens[0].length() + 1);
		connection.sendActionMessage(channelName, action);		
		setText(channelName, getNickName() + " " + action);
	    }
	else if( tokens[0].equals("MODE"))
	    {
		if( tokens.length > 1 )
		    {
			String channel = tokens[1];
			String modeString = command.substring(tokens[0].length() + tokens[1].length() + 2);
			connection.setMode(channelName, modeString);
		    }
		else
		    {
			//usage string
		    }
	    }
	else if(tokens[0].equals("MSG") )
	    {
		String nick = tokens[1];
		String message = command.substring(tokens[0].length() + tokens[1].length() + 2 );
		connection.sendPrivateMessage(nick, message);
		setMsgText(channelName, nick, message);
	    }
	else if( tokens[0].equals("NICK"))
	    {
		//		System.out.println(tokens.length);
		if( tokens.length < 2 )
		    {
			//usage message display
		    }
		else
		    {
			//			System.out.println(tokens[1]);
			connection.changeNick(tokens[1]);
		    }
	    }
	else if( tokens[0].equals("NOTICE"))
	    {
		if( tokens.length < 2 )
		    {}
		else
		    {
			String target = tokens[1];
			String message = command.substring(tokens[0].length() + tokens[1].length() + 2);
			connection.notice(target, message);
		    }
	    }
	else if( tokens[0].equals("NOTIFY"))
	    {}
	else if( tokens[0].equals("OP"))
	    {
		if( tokens.length < 2 )
		    {}
		else
		    {
			for( int i = 1; i < tokens.length; i++ )
			    {
				connection.op(channelName, tokens[i]);
			    }
		    }
	    }
	else if( tokens[0].equals("PART"))
	    {
		String partChannel = channelName;
		String partMessage = "";
		if( tokens.length >= 2 )
		    {
			partChannel = tokens[1];
			if( tokens.length > 2 )
			    {
				partMessage = command.substring("/PART ".length() + tokens[1].length());
			    }
			connection.part( partChannel, partMessage );
		    }
		else if( pms != null && !pms.equals(null) && pms.contains(channelName))
		    {
			//usage message
		    }
		else if( networkName.equals(channelName) )
		    {
			//usage message
		    }
		else
		    {
			connection.part(channelName, partMessage);
		    }
		    
	    }
	else if( tokens[0].equals("QUIT"))
	    {
		String message = "";
		if( tokens.length >= 2 )
		    message = command.substring(tokens[0].length() + 1 );
		connection.quit(message);
	    }
	else if( tokens[0].equals("QUOTE"))
	    {
		String message = command.substring(tokens[0].length() + 1);
		connection.quote(message);
	    }
	else if( tokens[0].equals("SERVER"))
	    {
		
	    }
	else if( tokens[0].equals("TOPIC"))
	    {
		String topic = command.substring(tokens[0].length() + 1);
		connection.setTopic(channelName, topic);
	    }
	else if( tokens[0].equals("UNBAN"))
	    {
		if( tokens.length > 1 )
		    {
			String unban = command.substring(tokens[0].length() + 1);
			String prefix = "-";
			for(int i = 1; i < tokens.length; i++ )
			    prefix += "b";
			prefix += " ";
			connection.setMode( channelName, prefix + unban );
		    }
		else
		    {
			//usage string
		    }
	    }
	else if( tokens[0].equals("VOICE"))
	    {
		String nick = tokens[1];
		connection.voice(channelName, nick);
	    }
	else if( tokens[0].equals("WHO"))
	    {
		if( tokens.length < 2 )
		    {
			setText(networkName, "who :Syntax error");
		    }
		else
		    {
			for( int i = 1; i < tokens.length; i++ )
			    {
				connection.who(tokens[i]);
			    }
		    }
	    }
	else if( tokens[0].equals("WHOIS"))
	    {
		if( tokens.length < 2 )
		    {
			setText(networkName, "whois :Syntax error");
		    }
		else
		    {
			for( int i = 1; i < tokens.length; i++ )
			    {
				connection.whois(tokens[i]);
			    }
		    }
	    }
	else if( tokens[0].equals("WHOWAS"))
	    {
		if( tokens.length < 2 )
		    {
			setText(networkName, "whowas :Syntax error");
		    }
		else
		    {
			for( int i = 1; i < tokens.length; i++ )
			    {
				connection.whoWas(tokens[i]);
			    }
		    }
	    }
    }
}