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