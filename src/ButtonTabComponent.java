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

public class ButtonTabComponent extends JPanel 
{
    private final JTabbedPane pane;
    private TabGroup tabGroup;
    private String tabName;
    private JLabel label;
    private BlinkingPanel blinkingPanel;
    private Thread thread;


    public ButtonTabComponent(final JTabbedPane pane, final String labelText, TabGroup tabGroup) 
    {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
	this.setOpaque(true);
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
	this.tabGroup = tabGroup;

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
	blinkingPanel = new BlinkingPanel( pane, tabName);
	//	blinkingPanel.setHighlighted(false);
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
			if(tabGroup.getNetworkName().equals(tabName))
			    {
				String message = "This server has still " + (tabGroup.getChannelCount() + tabGroup.getPmsCount() + 1 ) + " channels or dialogs associated with it. Close them all?";
				
				try
				    {
					int yesOrNo = JOptionPane.showConfirmDialog(tabGroup.getMainWindow(), 
										    message,
										    "",
										    JOptionPane.YES_NO_OPTION,
										    JOptionPane.QUESTION_MESSAGE);
					if( yesOrNo == JOptionPane.YES_OPTION )
					    {
						tabGroup.quit();
					    }
				    }
				catch(HeadlessException he)
				    {}
			    }
			else
			    {
				tabGroup.removeTab(tabName);
			    }
		    }
		});
        }

        public void actionPerformed(ActionEvent e) 
	{
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
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
	label.setForeground(new Color(0, 0, 0));
	//	pane.setBackgroundAt(pane.indexOfTab(tabName), null);
	//	blinkingPanel.setHighlighted(false);
    }

    public void setLabelColor(Color color)
    {
	Color labelColor = label.getForeground();
	if( !blinkingPanel.isBlinking() )
	    {
		label.setForeground(Color.BLACK);
		blinkingPanel.setHighlighted(true);
		thread = new Thread(blinkingPanel);
		thread.start();
	    }
	else if( thread == null || !thread.isAlive() )
	    label.setForeground(color);
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


