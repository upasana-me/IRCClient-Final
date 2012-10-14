import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;

import java.awt.Color;

public class BlinkingPanel implements Runnable
{
    private JTabbedPane pane;

    private String tabName;
    private boolean highlighted;
    private boolean isBlinking;

    BlinkingPanel(JTabbedPane pane, String tabName)
    {
	this.pane = pane;
	this.tabName = tabName;
	this.highlighted = true;
	this.isBlinking = false;
    }

    public void run()
    {
	isBlinking = true;
	while( highlighted )
	    {
		pane.setBackgroundAt(pane.indexOfTab(tabName), Color.RED);
		try
		    {
			Thread.sleep(500);
		    }
		catch(InterruptedException ie)
		    {}
		pane.setBackgroundAt(pane.indexOfTab(tabName), Color.WHITE);
		try
		    {
			Thread.sleep(500);
		    }
		catch(InterruptedException ie)
		    {}
	    }
	int index = pane.indexOfTab(tabName);
	if( index >= 0 )
	    {
		pane.setBackgroundAt(pane.indexOfTab(tabName), null);
	    }
	isBlinking = false;
    }

    public boolean isBlinking()
    {
	return isBlinking;
    }

    public void setHighlighted(boolean highlighted)
    {
	this.highlighted = highlighted;
    }

    public boolean getHighlighted()
    {
	return highlighted;
    }
}