import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
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

import javax.swing.border.Border;

import java.awt.Dimension;
import java.awt.Container;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
    private JScrollPane textAreaScroller;
    private JScrollPane nicksListScroller;
    private JTextPane textPane;
    private JButton nickButton;
    private JTabbedPane tabbedPane;

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
	textPane = new JTextArea();
	textPaneScroller = new JScrollPane(textPane);
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

    public void initialise()
    {
	nicksList.setCellRenderer(cellRenderer);

	p5.setBorder( BorderFactory.createLineBorder( Color.black, 1 ) );
	p5.add( chanInfo, BorderLayout.CENTER );

	p0.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	p0.add( p5, BorderLayout.NORTH );
	p0.add( nicksListScroller, BorderLayout.CENTER  );

	topicTextField.setBorder( BorderFactory.createLineBorder( Color.black, 7 ) );
	
	p6.add( topicTextField, BorderLayout.CENTER );

	textPane.setMargin(new Insets(0,50,0,10));
	textPane.setLineWrap(true);

	String chan_join_text = Constants.chan_join_text + channelName + "\n";
	textArea.append( chan_join_text );
	
	textPane.setEditable( false );

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
				    setText("<" + nickName + "> : " + s);
				}
			}
		}
	    });
	textField.requestFocus();

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
	tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, p7);
	tabbedPane.setSelectedComponent( this );
    }

    public void setText(String text)
    {
	textArea.append(text + "\n");
	textPane.setCaretPosition(textArea.getDocument().getLength());
    }

    public void setNickButtonText(String nick)
    {
	nickButton.setText(nick);
    }

    public void setTopic(String topic)
    {
	topicTextField.setText(topic);
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

}
