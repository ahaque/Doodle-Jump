import java.awt.*;
import java.applet.*;

public class Monster extends Character
{
   private int stepy;
   private int id;
   private int hv,vv;
   private int hcount,vcount;
	
	// default constructor, sets all to zero
	public Monster(int id, int x, int y, int w, int h)
	{
		super(id,x,y,w,h);
		hv=1;
		vv=1;
	}
	
	
	public void changeStepY(int y)
	{
		stepy += y;
	}

	//returns the index number of the needed image from the ArrayList
	// at location 1 in ArrayList of images
	public int show()
	{
		return id;
	}
	
	public String toString()
	{
		return x + " " + y;
	}
	

	public void move()
	{
	
		changeY(vv);
		changeX(hv);
	}
	
	public void setHV(int h)
	{
		hv=h;
	}
	
	public void setVV(int v)
	{
		vv=v;
	}
	
	public int getHV()
	{
		return hv;
	}
	
	public int getVV()
	{
		return vv;
	}
	
	public void setVcount(int v)
	{
		vcount=v;
	}	
	
	public void setHcount(int h)
	{
		hcount=h;
	}
	
	public int getVcount()
	{
		return vcount;
	}
	
	public int getHcount()
	{
		return hcount;
	}
		
}