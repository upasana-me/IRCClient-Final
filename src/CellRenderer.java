import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import java.awt.Component;

public class CellRenderer extends JLabel implements ListCellRenderer<Object>
{
    private String icon_path;
    private TreeMap<String, ImageIcon> name_2_icon;
    private TreeMap<String, Vector<String>> status_2_nicks;
    private Vector<String> ops;
    private Vector<String> voiced;
    private Vector<String> others;

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
	    }
	else if( voiced != null && voiced.contains(s) )
	    {
		ImageIcon icon = new ImageIcon(Constants.voice_sign_file_path);
		setIcon(icon);
		name_2_icon.put(s,icon);
	    }
	else if( others.contains(s) )
	    {
		ImageIcon icon = new ImageIcon(Constants.none_sign_file_path);
		setIcon(icon);
		name_2_icon.put(s,icon);
	    }

	if (isSelected) {
	    setBackground(list.getSelectionBackground());
	    setForeground(list.getSelectionForeground());
	} else {
	    setBackground(list.getBackground());
	    setForeground(list.getForeground());
	}
	//	setEnabled(list.isEnabled());
	//	setFont(list.getFont());
	return this;
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
