import java.util.prefs.Preferences;

import java.util.prefs.BackingStoreException;
/**
   UserPrefs.java
   This class load and save preferences of a user like nick names, user names etc
**/

public class UserPrefs
{
    private static String nicks[];
    private static String user_name;
    private static String real_name;
    private static boolean net_list_skip;
    private static boolean showMenuBar;
    private static boolean showTopicBar;
    private static int sel_list_index;

    private static Preferences preferences;

    static 
    {
	preferences = Preferences.userNodeForPackage( UserPrefs.class );
    }

    public static void load_prefs()
    {
	String s = System.getProperty("user.name");
	nicks = new String[Constants.nicksCount];

	for(int i = 0; i < nicks.length; i++) {
	    nicks[i] = preferences.get(Constants.skNickName + i, s);
	    s = s + '_';
	}

	user_name = preferences.get(Constants.skUserName, s );
	real_name = preferences.get(Constants.skRealName, "Real name" );		
	net_list_skip = preferences.getBoolean(Constants.skNetListSkip, false);
	sel_list_index = preferences.getInt(Constants.skSelListIndex, 0);
	showMenuBar = preferences.getBoolean(Constants.skHideMenuBar, true);
	showTopicBar = preferences.getBoolean(Constants.skHideTopicBar, true );
	//	networkName = preferences.get(Constants.skNetworkName,"127.0.0.1");
	//	port = preferences.getInt(Constants.skPort, 6667 );
	
    }
    
    public static void save_prefs()
    {
	for(int i = 0; i < nicks.length; i++) 
	    {
		preferences.put(Constants.skNickName + i, nicks[i]);
	    }

	preferences.put(Constants.skUserName, user_name );
	preferences.put(Constants.skRealName, real_name );
	preferences.putBoolean(Constants.skNetListSkip, net_list_skip);
	preferences.putInt(Constants.skSelListIndex, sel_list_index);
	preferences.putBoolean(Constants.skHideMenuBar, showMenuBar );
	preferences.putBoolean(Constants.skHideTopicBar, showTopicBar );
    }
    
    public static void save_net_list_skip(boolean b)
    {
	net_list_skip = b;
	preferences.putBoolean(Constants.skNetListSkip, b);
    }

    public static void save_sel_list_index(int i)
    {
	sel_list_index = i;
	preferences.putInt(Constants.skSelListIndex, i);
    }

    public static void save_nick1(String nick)
    {
	nicks[0] = nick;
	preferences.put(Constants.skNickName + 0, nick);
    }

    public static void save_nick2(String nick)
    {
	nicks[1] = nick;
	preferences.put(Constants.skNickName + 1, nick);
    }

    public static void save_nick3(String nick)
    {
	nicks[2] = nick;
	preferences.put(Constants.skNickName + 2, nick);
    }

    public static void save_username(String username)
    {
	user_name = username;
	preferences.put(Constants.skUserName, username);
    }

    public static void save_realname(String realname)
    {
	real_name = realname;
	preferences.put(Constants.skRealName, realname);
    }

    public static void save_showMenuBar(boolean hide)
    {
	showMenuBar = hide;
	preferences.putBoolean(Constants.skHideMenuBar, hide);
    }

    public static void save_showTopicBar(boolean hide)
    {
	showTopicBar = hide;
	preferences.putBoolean(Constants.skHideTopicBar, hide);
    }

    public static boolean get_net_list_skip()
    {
	return net_list_skip;
    }

    public static int get_sel_list_index()
    {
	return sel_list_index;
    }

    public static String get_nick1()
    {
	return nicks[0];
    }

    public static String get_nick2()
    {
	return nicks[1];
    }

    public static String get_nick3()
    {
	return nicks[2];
    }

    public static String get_username()
    {
	return user_name;
    }

    public static String get_realname()
    {
	return real_name;
    }

    public static boolean get_showMenuBar()
    {
	return showMenuBar;
    }

    public static boolean get_showTopicBar()
    {
	return showTopicBar;
    }
}