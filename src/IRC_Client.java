import java.nio.channels.ReadableByteChannel;
import java.nio.ByteBuffer;

import java.io.FileInputStream;
import java.io.File;


public class IRC_Client
{
    UserSettings us;

    IRC_Client()
    {
	us = new UserSettings();
	us.visible();	
    }

    public static void main(String args[])
    {
	new IRC_Client();
    }
}