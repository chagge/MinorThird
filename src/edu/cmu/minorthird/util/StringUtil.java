package edu.cmu.minorthird.util;

/**
 * String utilities.
 *
 */
public class StringUtil
{
	/** Indent a string */
	static public String indent(int tabs,String s)
	{
		StringBuffer tabbuf = new StringBuffer();
		for (int i=0; i<tabs; i++) tabbuf.append("|  ");
		String tab = tabbuf.toString();
		return tab+s.replaceAll("\\n","\n"+tab);
	}

	/** Convert an array to a string. */
	static public String toString(Object[] x)
	{
		StringBuffer buf = new StringBuffer("");
		buf.append("[");
		for (int i=0; i<x.length; i++) {
			if (i>0) buf.append(",");
			if (x[i]==null) buf.append("null");
			else buf.append(x[i].toString());
		}
		buf.append("]");
		return buf.toString();
	}

	/** Convert an array of doubles to a string. */
	static public String toString(double[] x)
	{
		StringBuffer buf = new StringBuffer("");
		buf.append("[");
		for (int i=0; i<x.length; i++) {
			if (i>0) buf.append(",");
			buf.append(Double.toString(x[i]));
		}
		buf.append("]");
		return buf.toString();
	}

	/** Convert an array of ints to a string. */
	static public String toString(int[] x)
	{
		StringBuffer buf = new StringBuffer("");
		buf.append("[");
		for (int i=0; i<x.length; i++) {
			if (i>0) buf.append(",");
			buf.append(Integer.toString(x[i]));
		}
		buf.append("]");
		return buf.toString();
	}

	/** Convert a string to an integer. Throws an IllegalArgumentException 
	 * if the string is not a legal integer. */
	static public int atoi(String intName)
	{
		try {
			return Integer.parseInt(intName);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("illegal integer '"+intName+"'");
		}
	}

	/** Convert a string to a double. Throws an IllegalArgumentException 
	 * if the string is not a legal double. */
	static public double atof(String doubleName)
	{
		try {
			return Double.parseDouble(doubleName);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("illegal double '"+doubleName+"'");
		}
	}

	/** Truncate a string to fixed length. */
	static public String truncate(int len,String s)
	{
		if (len>=s.length()) return s;
		else return s.substring(0,len)+"...";
	}
}
