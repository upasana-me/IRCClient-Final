import java.util.Comparator;

public class IgnoreCaseSort<T> implements Comparator<T>
{
    public int compare(T o1,T o2)
    {	
	String s1 = o1.toString();
	String s2 = o2.toString();
	int i = s1.compareToIgnoreCase(s2);
	if( i == 0 )
	    {
		return -1;
	    }
	else
	    return i;
    }

    public boolean equals(Object obj)
    {	
	return true;
    }
}