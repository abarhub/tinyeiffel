package tinyeiffel.compiler;

//import java.util.*;

public class Log
{
	public Log(int no,String text)
	{
		this.no=no;
		this.text=text;
	}

	final int no;
	final String text;

	public String toString()
	{
		return text;
	}

}