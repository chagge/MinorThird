/* Copyright 2003, Carnegie Mellon, All Rights Reserved */

package edu.cmu.minorthird.util.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A Viewer coupled with a ViewerControls object.
 * 
 * @author William Cohen
 */

public class ControlledViewer extends Viewer
{
	private Viewer viewer=null;
	private ViewerControls controls=null;

	public ControlledViewer()
	{
		super();
	}
	public ControlledViewer(Viewer viewer,ViewerControls controls)
	{
		super();
		setComponents(viewer,controls);
	}

	public void setComponents(Viewer viewer,ViewerControls controls) 
	{ 
		if (!(viewer instanceof Controllable)) throw new IllegalArgumentException("viewer must be controllable");
		this.controls = controls; 
		this.viewer = viewer;
		controls.setControlledViewer(viewer);
		viewer.setSuperView(this);
		removeAll();
		add(viewer, fillerGBC());
		GridBagConstraints gbc = fillerGBC();
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridy = 1;
		gbc.weightx = gbc.weighty = 0;
		add(controls, gbc);
	}

	public ViewerControls getControls() { return controls; }

	protected void initialize() { setLayout(new GridBagLayout()); }
	
	//
	// delegate signals & content to sub-viewer
	//

	public void clearContent() 
	{ 
		viewer.clearContent(); 
	}
	public boolean canReceive(Object obj) 
	{ 
		return viewer.canReceive(obj); 
	}
	public void receiveContent(Object obj)
	{
		viewer.receiveContent(obj);
	}
	protected boolean canHandle(int signal,Object argument,ArrayList senders) 
	{ 
		return viewer.canHandle(signal,argument,senders); 
	}
	protected void handle(int signal,Object argument,ArrayList senders) 
	{
		viewer.handle(signal,argument,senders); 
	}

	//
	// a very simple test case
	//

	/** a test case */
	public static void main(String[] argv)
	{
		Viewer v = new ControlledViewer(new MyViewer(), new MyControls());
		v.setContent("William Cohen");
		ViewerFrame f = new ViewerFrame("test", v);
	}
	// for test case
	static private class MyViewer extends TransformedVanillaViewer implements Controllable
	{
		private boolean uc;
		private String prefix;
		private Object lastObj;
		public Object transform(Object o) {
			lastObj = o;
			String s = o.toString();
			String result = uc?s.toUpperCase():s;
			if (prefix!=null) result = prefix + result;
			System.out.println("transform: "+o+" => "+result);
			return result;
		}
		public void applyControls(ViewerControls c)
		{
			System.out.println("controls: "+c);
			MyControls mc = (MyControls)c;
			uc = mc.ucBox.isSelected();
			prefix = mc.prefixField.getText();
			System.out.println("recieving: "+lastObj+" with uc="+uc+" prefix="+prefix);
			receiveContent(lastObj);
			revalidate();
		}
	}
	// for test case
	static private class MyControls extends ViewerControls 
	{
		public JCheckBox ucBox;
		public JTextField prefixField;
		public void initialize() 
		{
			setFloatable(false);
			ucBox = new JCheckBox("uc");
			ucBox.addActionListener(this);
			add(ucBox);
			prefixField = new JTextField("the man: ");
			add(prefixField);
			addApplyButton();
		}
		public String toString() { return "[uc: "+ucBox.isSelected()+" prefix: "+prefixField.getText()+"]"; }
	}
}
