/*
 * Utility.java
 */


//package in.upasna.irc.urc;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public final class Utility 
{
    protected static String join_tokens( String[] tokens, int start, int end )
    {
	String s = "";
	System.out.println( s );
	
	for( int i = start; i <= end; i++ )
	    {
		//		System.out.println( s );
		s += ( tokens[ i ] + " " );
	    }
	return s;
    }

    public static JMenuItem createMenuItem(String name, String actionCommand, JMenu menu, ActionListener listener) 
    {
	JMenuItem mi = new JMenuItem(name);
	mi.setActionCommand(actionCommand);
	mi.addActionListener(listener);
	menu.add(mi);
	return mi;
    }

    public static JCheckBoxMenuItem createCheckBoxMenuItem( String name, String actionCommand, JMenu menu, ActionListener listener)
    {
	JCheckBoxMenuItem cbmi = new JCheckBoxMenuItem( name );
	cbmi.setActionCommand(actionCommand);
	cbmi.addActionListener( listener );
	menu.add( cbmi );
	return cbmi;
    }

    public static JLabel createLabel(String label, String toolTip) 
    {
	JLabel l = new JLabel(label);

	l.setToolTipText(toolTip);

	return l;
    }

    public static JButton createButton(String button, String toolTip, String actionCommand, ActionListener listener)
    {
	JButton b = new JButton( button );
	b.setToolTipText( toolTip );
	b.addActionListener( listener );
	b.setActionCommand( actionCommand );
	return b;
    }

    public static JTextField createTextField( String text, boolean editable )
    {
	JTextField tf = new JTextField( text );
	tf.setEditable( editable );
	return tf;
    }

    public static JCheckBox createCheckBox( String text, String toolTip, String actionCommand, ActionListener listener, boolean selected )
    {
	JCheckBox cb = new JCheckBox( text, selected );
	cb.setToolTipText( toolTip );
	cb.addActionListener( listener );
	cb.setActionCommand( actionCommand );
	return cb;
    }

    protected static GridBagConstraints modifyGbc( int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady )
    {
	GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady );
	return gbc;
    }

    /*
    public static JList createList( Vector<String> items )
    {
	JList list = new JList( items );
    }

    public static void showError(String msg, MainWindow w ) {
	JOptionPane.showMessageDialog(w, msg, "Error", 
				      JOptionPane.ERROR_MESSAGE);
    }
    */

}
