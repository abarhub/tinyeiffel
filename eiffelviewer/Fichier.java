package tinyeiffel.eiffelviewer;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
//import org.eclipse.swt.events.*;
//import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
//import java.util.*;
import java.io.*;
import java.text.*;

public class Fichier
{
	public Fichier(String nom,String textString,TabFolder tab2,String path)
	{
		tab=new TabItem(tab2,0);
		final StyledText text1=create_text(tab2/*shell/*t[0]*/);
		tab.setControl(text1);
		text=text1;
		if(nom==null)
		{
			est_fichier=false;
			this.nom="Sans titre";
		}
		else
		{
			this.nom=nom;
			est_fichier=true;
			open(path,nom);
		}
		tab.setText(this.nom);
		//final String txt=textString;
		
		/*if(txt!=null)
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

	private StyledText create_text(Composite/*Shell*/ win)
	{
		StyledText t = new StyledText (win, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData spec = new GridData();
		spec.horizontalAlignment = GridData.FILL;
		spec.grabExcessHorizontalSpace = true;
		spec.verticalAlignment = GridData.FILL;
		spec.grabExcessVerticalSpace = true;
		t.setLayoutData(spec);
		lineStyler=new JavaLineStyler();
		t.addLineStyleListener(lineStyler);
		//lineStyler_fichier.add(l);
		//text_fichier.add(t);
		return t;
	}

	public void disposeRessource()
	{
		lineStyler.disposeColors();
		text.removeLineStyleListener(lineStyler);
	}

	public void open(String path,String name)
	{
		final String textString;
		File file = new File(/*fileDialog.getFilterPath()*/path, name);
		if (!file.exists()) {
			String message = MessageFormat.format("Err_file_no_exist", new String[] {file.getName()});
			//displayError(message);
			System.out.println("Erreur:"+message);
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
				//displayError(message);
				System.out.println("Erreur:"+message);
				return;
			}
		}
		catch (FileNotFoundException e) {
			String message = MessageFormat.format("Err_not_found", new String[] {file.getName()});
			//displayError(message);
			System.out.println("Erreur:"+message);
			return;
		}
		//if(txt!=null)
		{
			// Guard against superfluous mouse move events -- defer action until later
			Display display = text.getDisplay();
			display.asyncExec(new Runnable() {
				public void run() {
					text.setText(textString);
				}
			});
			
			// parse the block comments up front since block comments can go across
			// lines - inefficient way of doing this
			//lineStyler.parseBlockComments(textString);
		}
	}

	public String nom;
	public TabItem tab;
	public StyledText text;
	public JavaLineStyler lineStyler;
	public boolean est_fichier;
}