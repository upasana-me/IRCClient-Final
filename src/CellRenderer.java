import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import java.awt.Component;
import java.awt.Color;

public class CellRenderer extends JLabel implements ListCellRenderer<Object>
{
    private String icon_path;
    private TreeMap<String, ImageIcon> name_2_icon;
    private TreeMap<String, Vector<String>> status_2_nicks;
    private Vector<String> ops;
    private Vector<String> voiced;
    private Vector<String> others;
    private Vector<String> away;

    CellRenderer()
    {
	status_2_nicks = new TreeMap<String, Vector<String>>();
	name_2_icon = new TreeMap<String, ImageIcon>();
	setOpaque( true );
    }

    public void setStatusNickTM(TreeMap<String, Vector<String>> treemap)
    {
	status_2_nicks = treemap;
	ops = treemap.get("o");
	voiced = treemap.get("v");
	others = treemap.get("r");
	away = treemap.get("a");
    }

    public void addNickToAway(String nick)
    {
	away = status_2_nicks.get("a");
	away.add(nick);
	status_2_nicks.put("a", away);
    }

    public void removeNickFromAway(String nick)
    {
	away.remove(nick);
    }

    public void changeNick(String oldNick, String newNick)
    {
	if( ops.contains(oldNick) )
	    {
		int index_of_oldNick = ops.indexOf( oldNick );
		ops.remove(oldNick);
		ops.add(index_of_oldNick, newNick);
	    }
	else if( voiced.contains(oldNick) )
	    {
		int index_of_oldNick = voiced.indexOf( oldNick );
		voiced.remove(oldNick);
		voiced.add(index_of_oldNick, newNick);
	    }
	else if( others.contains(oldNick))
	    {
		int index_of_oldNick = others.indexOf( oldNick );
		others.remove(oldNick);
		others.add(index_of_oldNick, newNick);
	    }
	else if( away.contains(oldNick))
	    {
		int index_of_oldNick = away.indexOf(oldNick);
		away.remove(oldNick);
		away.add(index_of_oldNick, newNick);
	    }
    }

    public Component getListCellRendererComponent(
						  JList<?> list,              // the list
						  Object value,            // value to display
						  int index,               // cell index
						  boolean isSelected,      // is the cell selected
						  boolean cellHasFocus)    // does the cell have focus
    {
	String s = value.toString();
	setText(s);

	if( ops != null && ops.contains(s) )
	    {
		ImageIcon icon = new ImageIcon(Constants.op_sign_file_path);
		setIcon(icon);
		name_2_icon.put(s,icon);
		if( away.contains(s) )
		    setForeground(Color.red);
	    }
	else if( voiced != null && voiced.contains(s) )
	    {
		ImageIcon icon = new ImageIcon(Constants.voice_sign_file_path);
		setIcon(icon);
		name_2_icon.put(s,icon);
		if( away.contains(s) )
		    setForeground(Color.red);
	    }
	else if( others.contains(s) )
	    {
		ImageIcon icon = new ImageIcon(Constants.none_sign_file_path);
		setIcon(icon);
		name_2_icon.put(s,icon);
		if( away.contains(s) )
		    setForeground(Color.red);
	    }

	if (isSelected) {
	    setBackground(list.getSelectionBackground());
	    if( others.contains(s) )
		{
		    setIcon(null);
		    setText("  " + s);
		}
	    if( !away.contains(s) )
		setForeground(list.getSelectionForeground());	    
	} else {
	    setBackground(list.getBackground());
	    if( !away.contains(s) )
		setForeground(list.getForeground());
	}
	return this;
    }

    public String toString()
    {
	return "CellRenderer";
    }

    /*
    public void setIconPath(String path)
    {
	icon_path = path;
    }

    public void setOps(boolean o)
    {
	ops = o;
    }

    public void setVoiced(boolean v)
    {
	voiced = v;
    }

    public void setNone(boolean n)
    {
	none = n;
    }

    /*
    public void setData(Vector<String> v)
    {
	vector = v;
	System.out.println(vector.size());
    }
    */
}
