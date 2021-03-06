package tinyeiffel.eiffelviewer;

import org.eclipse.swt.*;
//import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
//import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
//import java.util.*;
//import java.io.*;
//import java.text.*;

public class ListeFichier implements MouseListener
{
	public ListeFichier(Shell shell)
	{
		//TabFolder
	win_file=new Shell(shell,/*SWT.SHELL_TRIM*/
			SWT.TITLE|SWT.BORDER|SWT.RESIZE|SWT.ON_TOP);
	win_file.setText("fichiers");
	GridLayout layout = new GridLayout();
	layout.numColumns = 1;
	win_file.setLayout(layout);	
	win_file.setSize(100, 100);
	/*tab=new TabFolder(win_file,0);
	TabItem[] t= new TabItem[2];
	t[0]=new TabItem(tab,0);
	t[0].setText("test 1");
	t[1]=new TabItem(tab,0);
	t[1].setText("test 2");*/
	
	tab=new TabFolder(win_file,SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	GridData spec = new GridData();
	spec.horizontalAlignment = GridData.FILL;
	spec.grabExcessHorizontalSpace = true;
	spec.verticalAlignment = GridData.FILL;
	spec.grabExcessVerticalSpace = true;
	tab.setLayoutData(spec);

	TabItem t=new TabItem(tab,0);
	t.setText("sans titre");
	liste=new org.eclipse.swt.widgets.List(tab,SWT.SINGLE);
	t.setControl(liste);
	//liste.add("a");
	//liste.add("b");
	//tab.setTabList(t);
	spec = new GridData();
	spec.horizontalAlignment = GridData.FILL;
	spec.grabExcessHorizontalSpace = true;
	spec.verticalAlignment = GridData.FILL;
	spec.grabExcessVerticalSpace = true;
	liste.setLayoutData(spec);
	liste.addMouseListener(this);
	win_file.open ();
		/*tab=new TabItem(tab2,0);
		final StyledText text1=create_text(tab2/*shell/*t[0]*//*);
		tab.setControl(text1);
		text=text1;
		if(nom==null)
			this.nom="Sans titre";
		else
			this.nom=nom;
		tab.setText(this.nom);
		final String txt=textString;
		
		if(txt!=null)
		{
			// Guard against superfluous mouse move events -- defer action until later
			Display display = text1.getDisplay();
			display.asyncExec(new Runnable() {
				public void run() {
					text1.setText(txt);
				}
			});
			
			// parse the block comments up front since block comments can go across
			// lines - inefficient way of doing this
			//lineStyler.parseBlockComments(textString);
		}*/
	}

	public void ajoute_fichier(String nom)
	{
		liste.add(nom);
	}

	public void disposeRessource()
	{
		/*lineStyler.disposeColors();
		text.removeLineStyleListener(lineStyler);*/
	}

	public void mouseDoubleClick(MouseEvent e)
	{
		if(e==null)
			return;
		if(e.getSource()==liste)
		{
			int i=liste.getSelectionIndex();
			if(i==-1)
				return;
			String s=liste.getItem(i);
			System.out.println("double click:"+s);
			EiffelViewer.principal.tab2.setSelection(i);
		}
	}

	public void mouseDown(MouseEvent e)
	{

	}

	public void mouseUp(MouseEvent e)
	{

	}

	public void show(boolean aff)
	{
		if(aff!=win_file.getVisible())
		{
			win_file.setVisible(aff);
		}
	}

	public void ferme_fichier(int i)
	{
		assert(i>=0);
		assert(i<liste.getItemCount()):"i="+i;
		//assert(i<tab.getItems().length):"i="+i+";len="+tab.getItems().length;
		liste.remove(i);
		//TabItem ti=tab.getItem(i);
		//ti.dispose();
	}

	/*public String nom;
	public TabItem tab;
	public StyledText text;
	public JavaLineStyler lineStyler;*/
	org.eclipse.swt.widgets.List liste;
	Shell win_file;
	TabFolder tab;
}