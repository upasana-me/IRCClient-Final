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
	startHTML = "<html><body><table cellspacing=\"0\" >";
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
	text = middleHTML + "<tr><td style=\"color:blue;text-align:right;\"><b>*</b></td><td style=\"color:black;text-align:left;\">" + serverInfo + "</td></tr>";
	appendHTML();
    }

    public void setErrorMessage(String errorMsg)
    {
	text = middleHTML + "<tr><td style=\"color:red;text-align:right;\"><b>*</b></td><td style=\"color:red;text-align:left;\">" + errorMsg + "</td></tr>";
	appendHTML();
    }

    public void setChanInfo(String info)
    {
	text = middleHTML + "<tr><td style=\"color:black;text-align:right;\"><b>*</b></td><td style=\"color:black;text-align:left;\">" + info + "</td></tr>";
	appendHTML();
    }

    public void setPmInfo(String info)
    {
	text = middleHTML + "<tr><td style=\"color:black;text-align:right;\"><b>*</b></td><td style=\"color:black;text-align:left;\">" + info + "</td></tr>";
	appendHTML();
    }

    public void setChanJoinText(String joinText)
    {
	text = middleHTML + "<tr><td style=\"text-align:right;color:green;\"><b>*</b></td>" +
	    "<td style=\"color:green;text-align:left;\">" +
	    joinText + 
	    "</td></tr>";
	appendHTML();
    }

    public void setJoinText(String channelName, String nick, String userName, String hostName)
    {
	text = middleHTML + 
	    "<tr><td style=\"text-align:right;color:green;\"><b>*</b></td><td style=\"color:green;text-align:left;\">" + 
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
	    "<tr><td style=\"text-align:right;color:#990000\"><b>*</b></td><td style=\"color:#990000;text-align:left;\">" + 
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
	    "<tr><td style=\"text-align:right;color:#6600FF;\"><b>*</b></td><td style=\"color:#6600FF;text-align:left;\">" + 
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
	    "<tr><td style=\"text-align:right;color:#6600FF;\"><b>*</b></td><td style=\"color:#6600FF;text-align:left;\">" + 
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
	    "<tr><td style=\"text-align:right;color:#FF0066;\"><b>" + 
	    nick + 
	    "</b></td><td style=\"text-align:left;color:#FF0066;\">" + 
	    message +
	    "</td></tr>";
	appendHTML();
    }

    public void setRegularMessage(String nick, String message)
    {
	text = middleHTML +
	    "<tr><td style=\"text-align:right;color:#000000;\"><b>" + 
	    nick + 
	    "</b></td><td style=\"text-align:left;color:#000000;\">" + 
	    message +
	    "</td></tr>";
	appendHTML();
    }

    public void setMessage(String nick, String message)
    {
	text = middleHTML +
	    "<tr><td style=\"text-align:right;color:#8B0000;\"><b>" + 
	    nick + 
	    "</b></td><td style=\"text-align:left;color:#8B0000;\">" + 
	    message +
	    "</td></tr>";
	appendHTML();	    
    }

    public void setSelfNickChangeText(String newNick)
    {
	System.out.println("In TextPaneExtended, setSelfNickChangeText.");
	text = middleHTML +
	    "<tr><td style=\"text-align:right;color:#000000;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000;\">" + 
	    Constants.selfNickChangeText + newNick +
	    "</td></tr>";
	appendHTML();
    }

    public void setNickChangeText(String oldNick, String newNick)
    {
	text = middleHTML +
	    "<tr><td style=\"text-align:right;color:#000000;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
	    oldNick + Constants.nickChangeText + newNick +
	    "</td></tr>";
	appendHTML();
    }
    
    public void setInvitationText(String channelName, String nick, String hostName)
    {
	text = middleHTML +
	    "<tr><td style=\"text-align:right;color:#000000;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
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
	    "<tr><td style=\"text-align:right;color:#000000;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#00008B\">" + 
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
	    "<tr><td style=\"text-align:right;color:#000000;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#00008B\">" + 
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

    public void setWhoText(String nick, 
			   String userName, 
			   String hostName, 
			   String realName, 
			   String hereOrGone, 
			   String channelName, 
			   String serverName, 
			   int hopCount)
    {
	text = middleHTML + 
	    "<tr><td style=\"text-align:right;color:blue;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
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
	text = middleHTML + 
	    "<tr><td style=\"text-align:right;color:blue;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
	    "[" + nick + "] (" + userName + "@" + hostName + "): " + realName +
	    "</td></tr>" +
	    "<tr><td style=\"text-align:right;color:blue;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
	    "[" + nick + "] attached to " + server + " :"  + serverInfo +
	    "</td></tr>" +
	    "<tr><td style=\"text-align:right;color:blue;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
	    "[" + nick + "] idle " + idleTime + ", signon: " + signOnTimeStr +
	    "</td></tr>" +
	    "<tr><td style=\"text-align:right;color:blue;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
	    "[" + nick + "] is member of " + whoisChannels +
	    "</td></tr>" +
	    "<tr><td style=\"text-align:right;color:blue;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 	    
	    "[" + nick + "] " + endOfList +
	    "</td></tr>";
	appendHTML();
    }
 
    public void setWhoWasText(String nick, String userName, String hostName, String realName)
    {
	text = middleHTML + 
	    "<tr><td style=\"text-align:right;color:blue;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
	    "[" + nick + "] (" + userName + "@" + hostName + "): " + realName +
	    "</td></tr>";
	appendHTML();
    }

    public void setWhoWasRemainingText(String nick, String remainingText)
    {
	text = middleHTML + 
	    "<tr><td style=\"text-align:right;color:blue;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000\">" + 
	    "[" + nick + "] " + remainingText +
	    "</td></tr>";
	appendHTML();	
    }

    public void setMsgText(String nick, String message)
    {
	text = middleHTML +
	    "<tr><td style=\"text-align:right;color:#000000;\"><b>&#62;" + 
	    nick + 
	    "&#60;</b>" +
	    "</td>" +
	    "<td style=\"text-align:left;color:#000000;\">" + 
	    message +
	    "</td></tr>";
	appendHTML();	
    }

    public void setDccChatInvitation(String nick, String ip, int port)
    {
	text = middleHTML + 
	    "<tr><td style=\"text-align:right;color:#000000;\"><b>*</b></td>" +
	    "<td style=\"text-align:left;color:#000000;\">" + 
	    "Received a DCC CHAT offer from " +
	    nick +
	    " (" + 
	    ip +
	    ":" +
	    port +
	    ") " +
	    "</td></tr>";
	appendHTML();
    }

    public void setServNotice(String notice)
    {
	text = middleHTML + "<tr><td style=\"text-align:right;color:#FF0066;\"><b>*</b></td><td style=\"color:black;text-align:left\">" + notice + "</td></tr>";
	appendHTML();
    }

    public void setNotice(String nick, String noticeText)
    {
	text = middleHTML + "<tr> <td style=\"color:#800080\";text-align=\"right\"><b>" + nick + "</b></td><td style=\"color:black;text-align:left\">" + noticeText + "</td></tr>";
	appendHTML();
    }

    public void setSockClosed(String nick, String message)
    {
	text = middleHTML + 
	    "<tr> <td style=\"color:#800080\";text-align=\"right\"><b>*</b></td><td style=\"color:black;text-align:left\">DCC CHAT to " + 
	    nick + 
	    " lost (" + 
	    message + 
	    "</td></tr>";
	appendHTML();
    }
}