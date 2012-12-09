import java.awt.*;
import java.applet.*;
import java.util.*;

public class Platform extends Character
{
   private int stepy;
   private int id,hv,vv,vcount;
   private boolean brownAnimation=false;
	
	// default constructor, sets all to zero
	public Platform(int id, int x, int y, int w, int h)
	{
		super(id,x,y,w,h);
		
		int hv1 = (int) (Math.random()*2)+1;
		if(hv1==1)
			hv=-2;
		if(hv1==2)
			hv=2;
			
		int vv1 = (int) (Math.random()*2)+1;
		if(vv1==1)
			vv=-1;
		if(vv1==2)
			vv=1;
		
		
		vcount=0;
	}
	
	
	public void changeStepY(int y)
	{
		stepy += y;
	}

	//returns the index number of the needed image from the ArrayList
	public int show()
	{
		return id;
	}
	
	public int getVcount()
	{
		return vcount;
	}
	
	public void setVcount(int v)
	{
		vcount = v;
	}
	
	public boolean getBrownAnimation()
	{
		return brownAnimation;
	}
	
	public void setBrownAnimation(boolean b)
	{
		brownAnimation=b;
	}
	
	public void setHV(int h2)
	{
		hv = h2;
	}
	
	public int getHV()
	{
		return hv;
	}
	
	public int getVV()
	{
		return vv;
	}

	public void setVV(int v)
	{
		vv =v;
	}
	
	public String toString()
	{
		return x + " " + y;
	}
	

	public void move()
	{
	
		changeY(stepy);
		
		stepy = 0;
		
	}
	public boolean inScene()
	{
		boolean scene = true;
		if(getX() > 600)
			scene = false;
		return scene;
	}


	
}