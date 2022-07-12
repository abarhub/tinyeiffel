package tinyeiffel.genere_c;

import java.io.*;
// OutputStream out
public class PrintStreamTest extends PrintStream
{
	public PrintStreamTest(String nom) throws FileNotFoundException,SecurityException
	{
		super(new FileOutputStream("temp.txt"));
		buf=new StringBuffer();
		nl=new StringBuffer("\r\n");
		this.out=new PrintStream(new FileOutputStream(nom));
	}

	public boolean checkError() { if(vide) return true; else return out.checkError();} 
	public void close(){if(!vide){out.print(buf);out.close();}}
	public void flush(){}
	public void print(boolean b){if(!vide) buf.append(b);}
	public void print(char c){if(!vide) buf.append(c);}
	public void print(char[] s){if(!vide) buf.append(s);}
	public void print(double d){if(!vide) buf.append(d);}
	public void print(float f){if(!vide) buf.append(f);}
	public void print(int i){if(!vide) buf.append(i);}
	public void print(long l){if(!vide) buf.append(l);}
	public void print(Object obj){if(!vide) buf.append(obj);}
	public void print(String s){if(!vide) buf.append(s);}
	public void println(){if(!vide) buf.append(nl);}
	public void println(boolean x){if(!vide) {print(x);println();}}
	public void println(char x){if(!vide) {print(x);println();}}
	public void println(char[] x){if(!vide) {print(x);println();}}
	public void println(double x){if(!vide) {print(x);println();}}
	public void println(float x){if(!vide) {print(x);println();}}
	public void println(int x){if(!vide) {print(x);println();}}
	public void println(long x){if(!vide) {print(x);println();}}
	public void println(Object x){if(!vide) {print(x);println();}}
	public void println(String x){if(!vide) {print(x);println();}}
	protected  void setError(){/*out.setError();*/}
	public void write(byte[] buf0, int off, int len){/*this.buf.append((char[])buf0,off,len);*/}
	public void write(int b){if(!vide) buf.append(b);}
	protected StringBuffer buf;
	protected StringBuffer nl;
	protected PrintStream out;
	protected static boolean vide=true;
}
