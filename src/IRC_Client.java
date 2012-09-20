import java.nio.channels.ReadableByteChannel;
import java.nio.ByteBuffer;

import java.io.FileInputStream;
import java.io.File;


public class IRC_Client
{
    private UserSettings us;
    private MainWindow mw;
    private Connection conn;

    IRC_Client()
    {
	//	conn = new Connection();
	mw = new MainWindow();
	us = new UserSettings(mw);
	//	conn.setMainWindow(mw);
	us.visible();
    }

    public static void main(String args[])
    {
	UserPrefs.load_prefs();	
	try
	    {
		new IRC_Client();
	    }
	catch(NullPointerException npe)
	    {
		System.out.println(npe.getMessage());
	    }
    }
}