/*
 * Utility.java
 */


//package in.upasna.irc.urc;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import java.nio.ByteBuffer;

import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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

    protected static String read_whole_file(String file_name)
    {
	try 
	    {
		ReadableByteChannel channel = new FileInputStream(file_name).getChannel();
		long file_size = new File(file_name).length();
		int numRead = 0;
		ByteBuffer buf = ByteBuffer.allocate((int)file_size);
		buf.rewind();
		if( channel.read(buf) > 0)
		    {
			byte[] b = buf.array();
			return (new String(b , "UTF-8"));
		    }
		else
		    return null;
	    }
	catch (Exception e) 
	    {
		System.out.println("Exception caught.");
		System.out.println(e.getMessage());
		return null;
	    }
    }

    protected static void write_whole_file(String file_name, String text)
    {
	System.out.println("In write_whole_file :\ntext = " + text);
	try
	    {
		WritableByteChannel channel = new FileOutputStream(file_name).getChannel();
		ByteBuffer buf = ByteBuffer.allocate(text.length());
		buf.put(text.getBytes("UTF-8"));
		buf.rewind();
		System.out.println(channel.write(buf));
	    }
	catch(FileNotFoundException fnfe)
	    {
		System.err.println(fnfe.getMessage());
	    }
	catch(IOException ioe)
	    {
		System.err.println(ioe.getMessage());
	    }
	/*
	  buf.rewind();
	  if( channel.read(buf) > 0)
	  {
	  byte[] b = buf.array();
	  return (new String(b , "UTF-8"));
	  }
	  else
	  return null;
	*/
    }

}
