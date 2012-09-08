import java.util.prefs.Preferences;

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
    private static int sel_list_index;

    private static Preferences preferences;

    static 
    {
	preferences = Preferences.userNodeForPackage( UserPrefs.class );
    }

    protected static void load_prefs()
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

	//	networkName = preferences.get(Constants.skNetworkName,"127.0.0.1");
	//	port = preferences.getInt(Constants.skPort, 6667 );
	
    }
    
    protected static void save_prefs()
    {
	for(int i = 0; i < nicks.length; i++) 
	    {
		preferences.put(Constants.skNickName + i, nicks[i]);
	    }

	preferences.put(Constants.skUserName, user_name );
	preferences.put(Constants.skRealName, real_name );
	preferences.putBoolean(Constants.skNetListSkip, net_list_skip);
	preferences.putInt(Constants.skSelListIndex, sel_list_index);
    }
    
    protected static void save_net_list_skip(boolean b)
    {
	net_list_skip = b;
    }

    protected static void save_sel_list_index(int i)
    {
	sel_list_index = i;
    }

    protected static void save_nick1(String nick)
    {
	nicks[0] = nick;
    }

    protected static void save_nick2(String nick)
    {
	nicks[1] = nick;
    }

    protected static void save_nick3(String nick)
    {
	nicks[2] = nick;
    }

    protected static void save_username(String username)
    {
	user_name = username;
    }

    protected static void save_realname(String realname)
    {
	real_name = realname;
    }

    protected static boolean get_net_list_skip()
    {
	return net_list_skip;
    }

    protected static int get_sel_list_index()
    {
	return sel_list_index;
    }

    protected static String get_nick1()
    {
	return nicks[0];
    }

    protected static String get_nick2()
    {
	return nicks[1];
    }

    protected static String get_nick3()
    {
	return nicks[2];
    }

    protected static String get_username()
    {
	return user_name;
    }

    protected static String get_realname()
    {
	return real_name;
    }
}