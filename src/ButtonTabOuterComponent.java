import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.HeadlessException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class ButtonTabOuterComponent extends JPanel 
{
    private final JTabbedPane pane;
    //    private TabGroup tabGroup;
    private String tabName;
    private MainWindow mainWindow;
    private JPanel panel;
    private JLabel label;
    private BlinkingPanel blinkingPanel;
    private Thread thread;

    public ButtonTabOuterComponent(final JTabbedPane pane, final String labelText, MainWindow mw) 
    {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
	this.mainWindow = mw;
	this.panel = this;
	//	this.tabGroup = tabGroup;

        setOpaque(false);
        
        //make JLabel read titles from JTabbedPane	
	label = new JLabel(labelText);
	this.tabName = labelText;
	//	label.setOpaque(true);
        add(label);
        //add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        //tab button
        JButton button = new TabButton();
        add(button);
	//	button.setOpaque(true);
	blinkingPanel = new BlinkingPanel(pane, tabName);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton extends JButton implements ActionListener 
    {
        public TabButton() 
	{
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener( new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {			
			String message = "Do you really want to close this network?";
			
			try
			    {
				int yesOrNo = JOptionPane.showConfirmDialog(mainWindow, 
									    message,
									    "",
									    JOptionPane.YES_NO_OPTION,
									    JOptionPane.QUESTION_MESSAGE);
				if( yesOrNo == JOptionPane.YES_OPTION )
				    {
					Connection connection = mainWindow.getConnection((ButtonTabOuterComponent)panel);
					connection.quit(tabName);
					int indexOfTab = pane.indexOfTabComponent(panel);
					System.out.println("indexOfTab : " + indexOfTab);
					pane.removeTabAt(indexOfTab);
					System.out.println("indexOfTab : " + indexOfTab);
					mainWindow.removeConnection(panel);
					if(pane.getTabCount() == 0 )
					    {
						mainWindow.setTextPane();
					    }
				    }
			    }
			catch(HeadlessException he)
			    {}
		    }
		});
        }

        public void actionPerformed(ActionEvent e) 
	{
            int i = pane.indexOfTabComponent(ButtonTabOuterComponent.this);
            if (i != -1) 
		{
		    pane.remove(i);
		}
        }

        //we don't want to update UI for this button
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) 
	{
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    public void setFocusTabColor()
    {
	if( thread != null && thread.isAlive())
	    blinkingPanel.setHighlighted(false);	
	//	pane.setBackgroundAt(pane.indexOfTab(tabName), null);
	label.setForeground(new Color(0, 0, 0));
    }

    public void setLabelColor(Color color)
    {
	Color labelColor = label.getForeground();
	if( !blinkingPanel.isBlinking() )// color.equals(new Color(255, 0, 102 ) ) && !thread.isAlive() )
	    {
		label.setForeground(Color.BLACK);
		thread = new Thread(blinkingPanel);
		blinkingPanel.setHighlighted(true);
		thread.start();
	    }
	else if( thread == null || !thread.isAlive() )
	    {
		label.setForeground(color);
	    }
    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() 
	{
	    public void mouseEntered(MouseEvent e) 
	    {
		Component component = e.getComponent();
		if (component instanceof AbstractButton) {
		    AbstractButton button = (AbstractButton) component;
		    button.setBorderPainted(true);
		}
	    }

	    public void mouseExited(MouseEvent e) 
	    {
		Component component = e.getComponent();
		if (component instanceof AbstractButton) {
		    AbstractButton button = (AbstractButton) component;
		    button.setBorderPainted(false);
		}
	    }
	};
}
