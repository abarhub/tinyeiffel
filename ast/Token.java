package tinyeiffel.ast;

public class Token implements Traite
{
	public Token(int x,int y,String text,String file)
	{
		this.text=text;
		this.x=x;
		this.y=y;
                this.file=file;
	}

        public Position debut()
        {
          return new Position(file,x,y);
        }

        public Position fin()
        {
          return new Position(file,x+1,y);
        }

	public void set_traite(boolean traite0)
	{
		this.traite=traite0;
	}

	public boolean get_traite()
	{
		return traite;
	}

	public void set_type_token(int n)
	{
		type_token=n;
	}

	public int get_type_token()
	{
		return type_token;
	}

	protected boolean traite=false;

	public String text,file;
	public int x,y;
	protected int type_token;

}