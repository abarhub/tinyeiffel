package tinyeiffel.test_unitaire;

import java.io.*;

public class FiltreFileName implements FilenameFilter
{
	public String filtre;

	public FiltreFileName(String filtre)
	{
		assert(filtre!=null);
		this.filtre=filtre;
	}

	public boolean accept(File dir,String name)
	{
		if(name==null)
			return false;
		if(name.endsWith(filtre))
			return true;
		else
			return false;
	}
}