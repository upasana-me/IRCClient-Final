/*
 * Constants.java
*/

public final class Constants {
    protected static final String userSettingsTitle = "User Settings";
    protected static final String appTitle = "IRC Client";

    protected static final String servlist_file = "servlist_.conf";

    protected static final String default_user_name = "user";
    protected static final String default_real_name = "real";

    protected static final String user_info_label = "User Information";
    protected static final String nick_name_label = "Nick name";
    protected static final String second_choice_label = "Second choice";
    protected static final String third_choice_label = "Third choice";
    protected static final String user_name_label = "User name";
    protected static final String real_name_label = "Real name";
    protected static final String networks_label = "Networks";

    protected static final String add_button_ac = "addButtonAc";
    protected static final String remove_button_ac = "removeButtonAc";
    protected static final String edit_button_ac = "editButtonAc";
    protected static final String sort_button_ac = "sortButtonAc";
    protected static final String close_button_ac = "closeButtonAc";
    protected static final String connect_button_ac = "connectButtonAc";
    protected static final String skip_net_list_ac = "skipNetListAc";

    protected static final String add_button_text = "Add";
    protected static final String remove_button_text = "Remove";
    protected static final String edit_button_text = "Edit";
    protected static final String sort_button_text = "Sort";
    protected static final String close_button_text = "Close";
    protected static final String connect_button_text = "Connect";
    protected static final String skip_net_list_text = "Skip this network list on start up";

    protected static final String net_list_mi_text = "Network List";
    protected static final String close_mi_text = "Close";
    protected static final String quit_mi_text = "Quit";
    
    protected static final String show_menu_bar_text = "Show menu bar";
    protected static final String show_topic_bar_text = "Show topic bar";

    protected static final String disconnect_mi_text = "Disconnect";
    protected static final String reconnect_mi_text = "Reconnect";
    protected static final String join_a_channel_mi_text = "Join a channel";
    protected static final String list_of_channels_mi_text = "List of channels";
    protected static final String marked_away_text = "Marked away";
    
    protected static final String clear_text_mi_text = "Clear text";
    protected static final String search_text_mi_text = "Search text";
    
    protected static final String contents_mi_text = "Contents";
    protected static final String about_mi_text = "About";
    
    protected static final String net_list_mi_ac = "net_list_mi_ac";
    protected static final String close_mi_ac = "close_mi_ac";
    protected static final String quit_mi_ac = "quit_mi_ac";
    
    protected static final String show_menu_bar_ac = "show_menu_bar_ac";
    protected static final String show_topic_bar_ac = "show_topic_bar_ac";

    protected static final String disconnect_mi_ac = "disconnect_mi_ac";
    protected static final String reconnect_mi_ac = "reconnect_mi_ac";
    protected static final String join_a_channel_mi_ac = "join_a_channel_mi_ac";
    protected static final String list_of_channels_mi_ac = "list_of_channels_mi_ac";
    protected static final String marked_away_ac = "marked_away_ac";
    
    protected static final String clear_text_mi_ac = "clear_text_mi_ac";
    protected static final String search_text_mi_ac = "search_text_mi_ac";
    
    protected static final String contents_mi_ac = "contents_mi_ac";
    protected static final String about_mi_ac = "about_mi_ac";

    protected static final String server_tf_ac = "server_tf_ac";
    protected static final String nick_button_ac = "nick_button_ac";

    protected static final String db_dir = "./sqlite";
    protected static final String db_name = "sqlite/ircclient";

    protected static final String create_nick_table = "CREATE TABLE NICKS( Nick_name VARCHAR(30),Second_choice VARCHAR(30),Third_choice VARCHAR(30),User_name VARCHAR(30), Real_name VARCHAR(30) );";
    protected static final String create_network_table = "CREATE TABLE NETWORKS( Names VARCHAR(30),Server VARCHAR(50),Port INT,Selected bool,PRIMARY KEY(Names,Server) );";
    protected static final String create_skip_selected = "CREATE TABLE SKIP_SELECTED( Skip bool )";
    protected static final String create_chats_table = "CREATE TABLE CHATS(Name VARCHAR(40),Text TEXT );";

    protected static final String select_nick_name = "SELECT Nick_name FROM NICKS;";
    protected static final String select_second_choice = "SELECT Second_choice FROM NICKS;";
    protected static final String select_third_choice = "SELECT Third_choice FROM NICKS;";
    protected static final String select_user_name = "SELECT User_name FROM NICKS;";
    protected static final String select_real_name = "SELECT Real_name FROM NICKS;";
    
    protected static final String select_network_server = "SELECT Server FROM NETWORKS;";
    protected static final String select_network_ports = "SELECT Port FROM NETWORKS;";
    protected static final String select_select_network = "SELECT Server, Port FROM NETWORKS WHERE SELECTED='true';";
    protected static final String select_network = "SELECT * FROM NETWORKS;";
    
    protected static final String select_chats_names = "SELECT Name FROM CHATS;";
    protected static final String select_chats_text = "SELECT Text FROM CHATS;";
    protected static final String select_chats = "SELECT * FROM CHATS;";

    protected static final String select_skip_selected = "SELECT Skip FROM SKIP_SELECTED;";

    protected static final String insert_nicks = "INSERT INTO NICKS VALUES ( ?, ?, ?, ?, ?);";
    
    protected static final String update_nick_name = "UPDATE NICKS SET Nick_name=? WHERE Second_choice=? AND Third_choice=? AND User_name=? AND Real_name=?;";
    protected static final String update_second_choice = "UPDATE NICKS SET Second_choice=? WHERE Nick_name=? AND Third_choice=? AND User_name=? AND Real_name=?;";
    protected static final String update_third_choice = "UPDATE NICKS SET Third_choice=? WHERE Nick_name=? AND Second_choice=? AND User_name=? AND Real_name=?;";
    protected static final String update_user_name = "UPDATE NICKS SET User_name=? WHERE Nick_name=? AND Second_choice=? AND Third_choice=? AND Real_name=?;";
    protected static final String update_real_name = "UPDATE NICKS SET Real_name=? WHERE Nick_name=? AND Second_choice=? AND Third_choice=? AND User_name=?;";

    protected static final String user_real_necessary = "Username & realname cannot be left blank";

    protected static final String skUserName = "UserName";
    protected static final String skRealName = "RealName";
    protected static final String skNickName = "NickName";
    protected static final String skNetworkName = "NetworkName";
    protected static final String skPort = "Port";

    protected static final int nicksCount = 3;

    protected static final String chan_join_text = "Now talking on ";


    protected static final String skipList = "skipList";
	
    protected static final String acNetList = "NetworkList";
    protected static final String acCloseIRC = "Close";	

    protected static final String acTopicBar = "TopicBar";
    protected static final String acUserList = "UserList";
    protected static final String acUserListButtons = "UserListButtons";
    protected static final String acModeButtons = "ModeButtons";

    protected static final String acConnect = "Connect";
    protected static final String acCancel = "Cancel";
    protected static final String acClose = "Close";
    protected static final String acDisconnect = "Disconnect";
    protected static final String acReconnect = "Reconnect";
    protected static final String acJoinAChannel = "JoinAChannel";
    protected static final String acListOfChannels = "ListOfChannels";
	
    protected static final String acMarkedAway = "Marked Away";

    protected static final String acBanList = "BanList"; 		
    protected static final String acCharChart = "CharChart";	
    protected static final String acFriendsList = "FriendsList";
    protected static final String acIgnoreList = "IgnoreList";
    protected static final String acClearText = "ClearText";
    protected static final String acSearchText = "SearchText";
	
    protected static final String acTextField = "textField";

    protected static final String acAbout = "About";

    protected static final String CRLF = "\r\n";
    protected static final String WSSTRING = " :";

    protected static final String acTxtCommand = "txtCommand";
    protected static final int BUFFERSIZE = 2048;
}
