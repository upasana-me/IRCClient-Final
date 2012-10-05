import javax.swing.JTextPane;

public class TextPaneExtended extends JTextPane
{
    String startHTML;
    String endHTML;
    String middleHTML;
    String html;
    String text;

    public TextPaneExtended()
    {
	this.setContentType("text/html");
	startHTML = "<html><body><table cellspacing=\"0\"><colgroup><col></col><col></col></colgroup>";
	endHTML = "</table></body></html>";
	middleHTML = "";
	this.setText(startHTML + endHTML);
    }

    public void appendHTML()
    {
	html = startHTML + text + endHTML;
	middleHTML = text;
	this.setText(html);
	this.setCaretPosition(this.getDocument().getLength());
    }

    public void setServerInfo(String serverInfo)
    {
	text = middleHTML + "<tr><b><td style=\"color:blue\";align=\"right\"><label>*</label></td></b><td style=\"color:black;align:left\">" + serverInfo + "</td></tr>";
	appendHTML();
    }

    public void setChanJoinText(String joinText)
    {
	text = middleHTML + "<tr><b><td style=\"align:right\"><label></label></td></b>" +
	    "<td style=\"color:green;align:left;\">" +
	    joinText + 
	    "</td></tr>";
	appendHTML();
    }

    public void setJoinText(String channelName, String nick, String userName, String hostName)
    {
	text = middleHTML + 
	    "<tr><b><td style=\"align:right\"><label></label></td></b><td style=\"color:green;align:left;\">" + 
	    nick + 
	    " (" + 
	    userName +
	    "@" +
	    hostName +
	    ") has joined "+
	    channelName +
	    "</td></tr>";
	appendHTML();
    }

    public void setPartText(String channelName, String nick, String userName, String hostName, String partReason)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right\"><label></label></td></b><td style=\"color:#990000;align:left;\">" + 
	    nick +
	    " (" +
	    userName +
	    "@" +
	    hostName +
	    ") " +
	    " has left " +
	    channelName +
	    " (" +
	    partReason +
	    ")";
	appendHTML();
    }

    public void setTopicText(String channelName, String topic)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right\"><label></label></td></b><td style=\"color:#6600FF;align:left;\">" + 
	    Constants.topicStr +
	    channelName +
	    " is : " +
	    topic + 
	    "</td></tr>";
	appendHTML();
    }

    public void setTopicSetTimeText(String channelName, String topicSetter, String topicTime)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right\"><label></label></td></b><td style=\"color:#6600FF;align:left;\">" + 
	    Constants.topicStr +
	    channelName +
	    " set by " +
	    topicSetter + 
	    " at " +
	    topicTime +
	    "</td></tr>";
	appendHTML();
    }

    public void setHighlightedMessage(String nick, String message)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right;color:#FF0066;\"><label>" + 
	    nick + 
	    "</label></td></b><td style=\"align:left;color:#FF0066;\">" + 
	    message +
	    "</td></tr>";
	appendHTML();
    }

    public void setRegularMessage(String nick, String message)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right;color:#000000;\"><label>" + 
	    nick + 
	    "</label></td></b><td style=\"align:left;color:#000000;\">" + 
	    message +
	    "</td></tr>";
	appendHTML();
    }

    public void setMessage(String nick, String message)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right;color:#8B0000;\"><label>" + 
	    nick + 
	    "</label></td></b><td style=\"align:left;color:#8B0000;\">" + 
	    message +
	    "</td></tr>";
	appendHTML();	    
    }

    public void setSelfNickChangeText(String newNick)
    {
	System.out.println("In TextPaneExtended, setSelfNickChangeText.");
	text = middleHTML +
	    "<tr><b><td style=\"align:right;color:#000000;\"><label></label></td></b>" +
	    "<td style=\"align:left;color:#000000;\">" + 
	    Constants.selfNickChangeText + newNick +
	    "</td></tr>";
	appendHTML();
    }

    public void setNickChangeText(String oldNick, String newNick)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right;color:#000000;\"><label></label></td></b>" +
	    "<td style=\"align:left;color:#000000\">" + 
	    oldNick + Constants.nickChangeText + newNick +
	    "</td></tr>";
	appendHTML();
    }
    
    public void setInvitationText(String channelName, String nick, String hostName)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right;color:#000000;\"><label></label></td></b>" +
	    "<td style=\"align:left;color:#000000\">" + 
	    "You have been invited to " + 
	    channelName +
	    " by " +
	    nick + 
	    " (" +
	    hostName + 
	    ")" +
	    "</td></tr>";
	appendHTML();
    }

    public void setSelfKickText(String channelName, String byWho, String reason)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right;color:#000000;\"><label></label></td></b>" +
	    "<td style=\"align:left;color:#00008B\">" + 
	    "You have been kicked from " + 
	    channelName +
	    " by " +
	    byWho + 
	    " (" +
	    reason + 
	    ")" +
	    "</td></tr>";
	appendHTML();
    }

    public void setKickText(String channelName, String nick, String byWho, String reason)
    {
	text = middleHTML +
	    "<tr><b><td style=\"align:right;color:#000000;\"><label></label></td></b>" +
	    "<td style=\"align:left;color:#00008B\">" + 
	    byWho + 
	    " has kicked " +
	    nick +
	    " from " +
	    channelName +
	    " (" +
	    reason + 
	    ")" +
	    "</td></tr>";
	appendHTML();
    }

    public void setWhoText(String nick, String userName, String hostName, String realName, 
			   String hereOrGone, String channelName, String serverName, int hopCount)
    {
	text = middleHTML + 
	    "<tr><b><td style=\"align:right;color:#000000;\"><label>*</label></td></b>" +
	    "<td style=\"align:left;color:#000000\">" + 
	    "User " + 
	    nick + 
	    ", (" +
	    userName + 
	    "@" + 
	    hostName + 
	    ") \"" + 
	    realName + 
	    "\" (" + 
	    hereOrGone + 
	    "), member of " +
	    channelName + 
	    ", is connected to " +
	    serverName + 
	    ", " + 
	    hopCount + " hop(s)." +
	    "</td></tr>";
	appendHTML();
    }
  
    public void setServNotice(String notice)
    {
	text = middleHTML + "<tr><b><td style=\"align:right;color:#FF0066;\"><label>*</label></td></b><td style=\"color:black;align:left\">" + notice + "</td></tr>";
	appendHTML();
    }

    public void setNotice(String nick, String noticeText)
    {
	text = middleHTML + "<tr><b><td style=\"color:#800080\";align=\"right\"><label>" + nick + "</label></td></b><td style=\"color:black;align:left\">" + noticeText + "</td></tr>";
	appendHTML();
    }

}