import java.io.File;

/*
 * Constants.java
 */

public final class Constants {
    public static final String userSettingsTitle = "User Settings";
    public static final String appTitle = "IRC Client";

    public static final String topicStr = "Topic for ";

    public static final String servlist_file = "servlist_.conf";
    //    public static final String op_sign_file_path = "Images" + File.separator + "op_sign.jpg";
    public static final String op_sign_file_path = "Images" + File.separator + "op.gif";
    //    public static final String voice_sign_file_path = "Images" + File.separator + "voice_sign.png";
    public static final String voice_sign_file_path = "Images" + File.separator + "voiced.gif";
    public static final String none_sign_file_path = "Images" + File.separator + "none_sign.png";

    public static final String default_user_name = "user";
    public static final String default_real_name = "real";

    public static final String modePlusO = " gives channel operator status to ";
    public static final String modeMinusO = " removes channel operator status from ";
    public static final String modePlusV = " gives voice to ";
    public static final String modeMinusV = " removes voice from ";
    public static final String modePlusL = " sets channel limit to ";
    public static final String modeMinusL = " removes channel limit";
    public static final String modePlusK = " sets channel keyword to ";
    public static final String modeMinusK = " removes channel keyword";
    public static final String modePlusB = " sets ban on ";
    public static final String modeMinusB = " removes ban on ";
    public static final String modePlusE = " sets exempt on ";
    public static final String modeMinusE = " removes exempt on ";

    public static final String user_info_label = "User Information";
    public static final String nick_name_label = "Nick name";
    public static final String second_choice_label = "Second choice";
    public static final String third_choice_label = "Third choice";
    public static final String user_name_label = "User name";
    public static final String real_name_label = "Real name";
    public static final String networks_label = "Networks";

    public static final String add_button_ac = "addButtonAc";
    public static final String remove_button_ac = "removeButtonAc";
    public static final String edit_button_ac = "editButtonAc";
    public static final String sort_button_ac = "sortButtonAc";
    public static final String close_button_ac = "closeButtonAc";
    public static final String connect_button_ac = "connectButtonAc";
    public static final String skip_net_list_ac = "skipNetListAc";
    public static final String nickname_tf_ac = "nickname_tfAc";
    public static final String second_choice_tf_ac = "second_choice_tfAc";
    public static final String third_choice_tf_ac = "third_choice_tfAc";
    public static final String username_tf_ac = "username_tfAc";
    public static final String realname_tf_ac = "realname_tfAc";

    public static final String add_button_text = "Add";
    public static final String remove_button_text = "Remove";
    public static final String edit_button_text = "Edit";
    public static final String sort_button_text = "Sort";
    public static final String close_button_text = "Close";
    public static final String connect_button_text = "Connect";
    public static final String skip_net_list_text = "Skip this network list on start up";

    public static final String net_list_mi_text = "Network List";
    //    public static final String close_mi_text = "Close";
    public static final String quit_mi_text = "Quit";
    
    public static final String show_menu_bar_text = "Show menu bar";
    public static final String show_topic_bar_text = "Show topic bar";

    public static final String disconnect_mi_text = "Disconnect";
    public static final String reconnect_mi_text = "Reconnect";
    public static final String join_a_channel_mi_text = "Join a channel";
    public static final String list_of_channels_mi_text = "List of channels";
    public static final String marked_away_text = "Marked away";
    
    public static final String clear_text_mi_text = "Clear text";
    public static final String search_text_mi_text = "Search text";
    
    public static final String contents_mi_text = "Contents";
    public static final String about_mi_text = "About";
    
    public static final String net_list_mi_ac = "net_list_mi_ac";
    //    public static final String close_mi_ac = "close_mi_ac";
    public static final String quit_mi_ac = "quit_mi_ac";
    
    public static final String show_menu_bar_ac = "show_menu_bar_ac";
    public static final String show_topic_bar_ac = "show_topic_bar_ac";

    public static final String disconnect_mi_ac = "disconnect_mi_ac";
    public static final String reconnect_mi_ac = "reconnect_mi_ac";
    public static final String join_a_channel_mi_ac = "join_a_channel_mi_ac";
    public static final String list_of_channels_mi_ac = "list_of_channels_mi_ac";
    public static final String marked_away_ac = "marked_away_ac";
    
    public static final String clear_text_mi_ac = "clear_text_mi_ac";
    public static final String search_text_mi_ac = "search_text_mi_ac";
    
    public static final String contents_mi_ac = "contents_mi_ac";
    public static final String about_mi_ac = "about_mi_ac";

    public static final String server_tf_ac = "server_tf_ac";
    public static final String nick_button_ac = "nick_button_ac";

    public static final String db_dir = "./sqlite";
    public static final String db_name = "sqlite/ircclient";

    public static final String create_nick_table = "CREATE TABLE NICKS( Nick_name VARCHAR(30),Second_choice VARCHAR(30),Third_choice VARCHAR(30),User_name VARCHAR(30), Real_name VARCHAR(30) );";
    public static final String create_network_table = "CREATE TABLE NETWORKS( Names VARCHAR(30),Server VARCHAR(50),Port INT,Selected bool,PRIMARY KEY(Names,Server) );";
    public static final String create_skip_selected = "CREATE TABLE SKIP_SELECTED( Skip bool )";
    public static final String create_chats_table = "CREATE TABLE CHATS(Name VARCHAR(40),Text TEXT );";

    public static final String select_nick_name = "SELECT Nick_name FROM NICKS;";
    public static final String select_second_choice = "SELECT Second_choice FROM NICKS;";
    public static final String select_third_choice = "SELECT Third_choice FROM NICKS;";
    public static final String select_user_name = "SELECT User_name FROM NICKS;";
    public static final String select_real_name = "SELECT Real_name FROM NICKS;";
    
    public static final String select_network_server = "SELECT Server FROM NETWORKS;";
    public static final String select_network_ports = "SELECT Port FROM NETWORKS;";
    public static final String select_select_network = "SELECT Server, Port FROM NETWORKS WHERE SELECTED='true';";
    public static final String select_network = "SELECT * FROM NETWORKS;";
    
    public static final String select_chats_names = "SELECT Name FROM CHATS;";
    public static final String select_chats_text = "SELECT Text FROM CHATS;";
    public static final String select_chats = "SELECT * FROM CHATS;";

    public static final String select_skip_selected = "SELECT Skip FROM SKIP_SELECTED;";

    public static final String insert_nicks = "INSERT INTO NICKS VALUES ( ?, ?, ?, ?, ?);";
    
    public static final String update_nick_name = "UPDATE NICKS SET Nick_name=? WHERE Second_choice=? AND Third_choice=? AND User_name=? AND Real_name=?;";
    public static final String update_second_choice = "UPDATE NICKS SET Second_choice=? WHERE Nick_name=? AND Third_choice=? AND User_name=? AND Real_name=?;";
    public static final String update_third_choice = "UPDATE NICKS SET Third_choice=? WHERE Nick_name=? AND Second_choice=? AND User_name=? AND Real_name=?;";
    public static final String update_user_name = "UPDATE NICKS SET User_name=? WHERE Nick_name=? AND Second_choice=? AND Third_choice=? AND Real_name=?;";
    public static final String update_real_name = "UPDATE NICKS SET Real_name=? WHERE Nick_name=? AND Second_choice=? AND Third_choice=? AND User_name=?;";

    public static final String user_real_necessary = "Username & realname cannot be left blank";
    public static final String promptNick = "Enter new nickname : ";
    public static final String joinChannelPrompt = "Enter channel to join:";

    public static final String nickChangeText = " is now known as ";
    public static final String selfNickChangeText = "You are now known as ";

    public static final String skUserName = "UserName";
    public static final String skRealName = "RealName";
    public static final String skNickName = "NickName";
    public static final String skNetListSkip = "net_list_skip";
    public static final String skSelListIndex = "sel_list_index";
    public static final String skHideMenuBar = "hideMenuBar";
    public static final String skHideTopicBar = "hideTopicBar";

    public static final int nicksCount = 3;

    public static final String chan_join_text = "Now talking on ";


    public static final String skipList = "skipList";
	
    public static final String acNetList = "NetworkList";
    public static final String acCloseIRC = "Close";	

    public static final String acTopicBar = "TopicBar";
    public static final String acUserList = "UserList";
    public static final String acUserListButtons = "UserListButtons";
    public static final String acModeButtons = "ModeButtons";

    public static final String acConnect = "Connect";
    public static final String acCancel = "Cancel";
    public static final String acClose = "Close";
    public static final String acDisconnect = "Disconnect";
    public static final String acReconnect = "Reconnect";
    public static final String acJoinAChannel = "JoinAChannel";
    public static final String acListOfChannels = "ListOfChannels";
	
    public static final String acMarkedAway = "Marked Away";

    public static final String acBanList = "BanList"; 		
    public static final String acCharChart = "CharChart";	
    public static final String acFriendsList = "FriendsList";
    public static final String acIgnoreList = "IgnoreList";
    public static final String acClearText = "ClearText";
    public static final String acSearchText = "SearchText";
	
    public static final String acTextField = "textField";

    public static final String acAbout = "About";

    public static final String CRLF = "\r\n";
    public static final String WSSTRING = " :";

    public static final String acTxtCommand = "txtCommand";
    public static final int BUFFERSIZE = 2048;
}
