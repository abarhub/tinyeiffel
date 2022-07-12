package tinyeiffel.eiffelviewer;
//package org.eclipse.swt.examples.javaviewer;


import org.eclipse.swt.*;
//import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
//import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import java.util.*;
//import java.io.*;
//import java.text.*;

/**
 */
public class EiffelViewer {  
	Shell shell/*,win_file*/;
	//StyledText text;
	//JavaLineStyler lineStyler = new JavaLineStyler();
	FileDialog fileDialog;
	//static ResourceBundle resources = ResourceBundle.getBundle("examples_javaviewer");
	public TabFolder /*tab,*/tab2;
	Vector /*nom_fichier=new Vector(),tab_fichier=new Vector(),
		text_fichier=new Vector(),lineStyler_fichier=new Vector(),*/
		fichier=new Vector();
	//org.eclipse.swt.widgets.List liste;
	ListeFichier liste_fichier;

Menu createFileMenu() {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	MenuItem item;

	// New 
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText ("New");
	item.setAccelerator(SWT.MOD1 + 'N');
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			add_fichier("");
		}
	});

	// Open 
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText ("Open");
	item.setAccelerator(SWT.MOD1 + 'O');
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			openFile();
		}
	});

	item=new MenuItem (menu, SWT.PUSH);
	item.setText ("Save");
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuSave();
		}
	});

	// Exit
	item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Exit");
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuFileExit ();
		}
	});
	return menu;
}

Menu createEditMenu() {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	MenuItem item;

	// New 
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText ("Copie");
	item.setAccelerator(SWT.MOD1 + 'C');
	/*item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			add_fichier("");
		}
	});*/

	// Open 
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText ("Coupe");
	item.setAccelerator(SWT.MOD1 + 'O');
	/*item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			openFile();
		}
	});*/

	item=new MenuItem (menu, SWT.PUSH);
	item.setText ("Colle");
	/*item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuSave();
		}
	});*/

	// Exit
	/*item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Exit");
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuFileExit ();
		}
	});*/
	return menu;
}

Menu createProjetMenu() {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	MenuItem item;

	// New 
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText ("Nouveau");
	item.setAccelerator(SWT.MOD1 + 'N');
	/*item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			add_fichier("");
		}
	});*/

	// Open 
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText ("Configure");
	item.setAccelerator(SWT.MOD1 + 'O');
	/*item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			openFile();
		}
	});*/

/*	item=new MenuItem (menu, SWT.PUSH);
	item.setText ("Save");
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuSave();
		}
	});

	// Exit
	item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Exit");
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuFileExit ();
		}
	});*/
	return menu;
}

static MenuItem fenetre_liste_fichier;

Menu createWindowMenu() {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	MenuItem item;

	// New 
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText ("Fermer");
	item.setAccelerator(SWT.MOD1 + 'N');
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			fermer_fichier();
		}
	});

	// Open 
	item = new MenuItem (menu, SWT.CHECK);
	fenetre_liste_fichier=item;
	item.setText ("Liste fichiers");
	item.setAccelerator(SWT.MOD1 + 'L');
	fenetre_liste_fichier.setSelection(true);
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			if(fenetre_liste_fichier.getSelection())
			{
				//System.out.println("1");
				liste_fichier.show(true);
			}
			else
			{
				//System.out.println("0");
				liste_fichier.show(false);
			}
		}
	});

/*	item=new MenuItem (menu, SWT.PUSH);
	item.setText ("Save");
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuSave();
		}
	});

	// Exit
	item = new MenuItem (menu, SWT.PUSH);
	item.setText ("Exit");
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuFileExit ();
		}
	});*/
	return menu;
}

void createMenuBar () {
	Menu bar = new Menu (shell, SWT.BAR);
	shell.setMenuBar (bar);

	MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
	fileItem.setText ("File");
	fileItem.setMenu (createFileMenu ());

	fileItem = new MenuItem (bar, SWT.CASCADE);
	fileItem.setText ("Edit");
	fileItem.setMenu (createEditMenu ());
	
	fileItem = new MenuItem (bar, SWT.CASCADE);
	fileItem.setText ("Projet");
	fileItem.setMenu (createProjetMenu ());
	
	fileItem = new MenuItem (bar, SWT.CASCADE);
	fileItem.setText ("Windows");
	fileItem.setMenu (createWindowMenu ());
}

void createShell (Display display) {
	shell = new Shell (display);
	shell.setText ("Edit");	
	GridLayout layout = new GridLayout();
	layout.numColumns = 1;
	shell.setLayout(layout);
	shell.addShellListener (new ShellAdapter () {
		public void shellClosed (ShellEvent e) {
			//lineStyler.disposeColors();
			//text.removeLineStyleListener(lineStyler);
			int i;
			i=0;
			/*while(i<text_fichier.size())
			{
				((JavaLineStyler)lineStyler_fichier.elementAt(i)).disposeColors();
				((StyledText)text_fichier.elementAt(i)).removeLineStyleListener((JavaLineStyler)lineStyler_fichier.elementAt(i));
				i++;
			}*/
			for(i=0;i<fichier.size();i++)
			{
				((Fichier)fichier.elementAt(i)).disposeRessource();
			}
		}
	});
}

void displayError(String msg) {
	MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
	box.setMessage(msg);
	box.open();
}

public static void main (String [] args) {
	Display display = new Display();
	EiffelViewer example = new EiffelViewer ();
	EiffelViewer.principal=example;
	Shell shell = example.open (display);
	while (!shell.isDisposed ())
		if (!display.readAndDispatch ()) display.sleep ();
	display.dispose ();
}


public void add_fichier(String nom)
{
	add_fichier(nom,null,null);
}

public void add_fichier(String nom,String textString,String path)
{
	Fichier f;
	if(nom==null||nom=="")
	{
		/*TabItem t=new TabItem(tab2,0);
		t.setText("sans titre");
		StyledText text1=create_text(tab2/*shell/*t[0]*//*);
		t.setControl(text1);
		nom_fichier.add("sans titre");*/
		f=new Fichier(null,null,tab2,null);
	}
	else
	{
		/*TabItem t=new TabItem(tab2,0);
		t.setText(nom);
		final StyledText text1=create_text(tab2/*shell/*t[0]*//*);
		t.setControl(text1);
		nom_fichier.add(nom);
		final String txt=textString;
		
		if(textString!=null)
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
		f=new Fichier(nom,textString,tab2,path);
	}
	fichier.add(f);
	/*if(f.nom==null)
	{
		System.out.println("Erreur:nom null");
		liste.add("Sans nom");
	}
	else
		liste.add(f.nom);*/
	liste_fichier.ajoute_fichier(f.nom);
	TabItem[] t=new TabItem[1];
	t[0]=f.tab;
	tab2.setSelection(t);
}

public void fermer_fichier()
{
	int i=tab2.getSelectionIndex(),j;
	if(i+1<tab2.getItemCount())
		j=i+1;
	else if(i-1>=0)
		j=i-1;
	else
		j=-1;
	tab2.setSelection(j);
	fermer_fichier(i);
}

public void fermer_fichier(int no)
{
	assert(no>=0);
	assert(no<fichier.size());
	//assert(no<liste_fichier.liste.ge)
	assert(no<tab2.getItemCount());
	fichier.remove(no);
	liste_fichier.ferme_fichier(no);
	tab2.getItem(no).dispose();
}

public static EiffelViewer principal;

public void createTab()
{
	tab2=new TabFolder(shell,SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
	GridData spec = new GridData();
	spec.horizontalAlignment = GridData.FILL;
	spec.grabExcessHorizontalSpace = true;
	spec.verticalAlignment = GridData.FILL;
	spec.grabExcessVerticalSpace = true;
	tab2.setLayoutData(spec);
	/*TabItem[] t= new TabItem[2];
	t[0]=new TabItem(tab2,0);
	t[0].setText("test 3");
	StyledText text1=create_text(tab2/*shell/*t[0]*//*);
	t[0].setControl(text1);
	t[1]=new TabItem(tab2,0);
	t[1].setText("test 4");*/
	add_fichier("");
	//add_fichier("salut");
}

public Shell open (Display display) {
	createShell (display);
	createMenuBar ();
	//createStyledText ();
	open_win_file();
	createTab();
	shell.setSize(500, 400);
	shell.open ();
	return shell;
}

public void open_win_file()
{
	//TabFolder
/*	win_file=new Shell(shell,SWT.SHELL_TRIM);
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
/*	liste=new org.eclipse.swt.widgets.List(win_file,SWT.SINGLE);
	//liste.add("a");
	//liste.add("b");
	//tab.setTabList(t);
	GridData spec = new GridData();
	spec.horizontalAlignment = GridData.FILL;
	spec.grabExcessHorizontalSpace = true;
	spec.verticalAlignment = GridData.FILL;
	spec.grabExcessVerticalSpace = true;
	liste.setLayoutData(spec);
	win_file.open ();*/
	liste_fichier=new ListeFichier(shell);
}

void openFile() {	
	final String textString;
	if (fileDialog == null) {
		fileDialog = new FileDialog(shell, SWT.OPEN);
	}

	fileDialog.setFilterExtensions(new String[] {"*.e","*.java", "*.*"});
	fileDialog.open();
	String name = fileDialog.getFileName();
	
	if ((name == null) || (name.length() == 0)) return;

	/*File file = new File(fileDialog.getFilterPath(), name);
	if (!file.exists()) {
		String message = MessageFormat.format("Err_file_no_exist", new String[] {file.getName()});
		displayError(message);
		return;
	}

	try {
		FileInputStream stream= new FileInputStream(file.getPath());
		try {
			Reader in = new BufferedReader(new InputStreamReader(stream));
			char[] readBuffer= new char[2048];
			StringBuffer buffer= new StringBuffer((int) file.length());
			int n;
			while ((n = in.read(readBuffer)) > 0) {
				buffer.append(readBuffer, 0, n);
			}
			textString = buffer.toString();
			stream.close();
		}
		catch (IOException e) {
			// Err_file_io
			String message = MessageFormat.format("Err_file_io", new String[] {file.getName()});
			displayError(message);
			return;
		}
	}
	catch (FileNotFoundException e) {
		String message = MessageFormat.format("Err_not_found", new String[] {file.getName()});
		displayError(message);
		return;
	}
	// Guard against superfluous mouse move events -- defer action until later
	/*Display display = text.getDisplay();
	display.asyncExec(new Runnable() {
		public void run() {
			text.setText(textString);
		}
	});
	
	// parse the block comments up front since block comments can go across
	// lines - inefficient way of doing this
	lineStyler.parseBlockComments(textString);*/
	add_fichier(name,null/*textString*/,fileDialog.getFilterPath());
}

void newFile() {	
	final String textString;
	

	String name = "Sans nom";
	
	if ((name == null) || (name.length() == 0)) return;

	add_fichier(name);
}

void menuFileExit () {
	shell.close ();
}

void menuSave () {
	//shell.close ();
	int i=tab2.getSelectionIndex();
	if(i>-1)
	{
		String n=((Fichier)fichier.elementAt(i)).nom;
		System.out.println("nom="+n);
	}
}

}
