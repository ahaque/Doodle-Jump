import java.awt.*;
import java.applet.*;

public class Bullet extends Character
{
   private int stepy;
   private int id;
   private int legStep,heightStep;
	
	// default constructor, sets all to zero
	public Bullet(int id, int x, int y, int w, int h)
	{
		super(id,x,y,w,h);
		stepy=5;
	}
	
	public void move()
	{
		changeX(legStep);
		changeY(heightStep);
	}
	
	public void changeStepY(int y)
	{
		stepy += y;
	}
	
	public void setLegStep(int ls)
	{
		legStep=ls;
	}
	
	public void setHeightStep(int hs)
	{
		heightStep=hs;
	}
	
	public int getHeightStep()
	{
		return heightStep;
	}
	
	public int getLegStep()
	{
		return legStep;
	}

	public int show()
	{
		return id;
	}
	
	public String toString()
	{
		return x + " " + y;
	}
}