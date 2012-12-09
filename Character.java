import java.awt.*;
import java.applet.*;

public abstract class Character
{
	protected int x, y, id;
	protected int width, height;
	
	// default constructor, sets all to zero
	public Character()
	{
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		id=0;
	}
	
	// four parameter constructor - call with super for each
	// new character
	public Character(int id1, int x1, int y1, int w, int h)
	{
		id = id1;
		x = x1;
		y = y1;
		width = w;
		height = h;
	}
	
	//returns the index number of the needed image from the ArrayList
	public abstract int show();
		
	// implement to define how each new character moves 
	public abstract void move();

	public int getId()
	{
		return id;
	}
	
	public void setId(int i)
	{
		id = i;
	}
	
	// accessor methods
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	
	// mutator methods
	public void setX(int x1)
	{
		x = x1;
	}
	public void setY(int y1)
	{
		y = y1;
	}
	public void changeX(int k)
	{
		x += k;
	}
	public void changeY(int k)
	{
		y += k;
	}
	
	// overridden in Fireball to remove from ArrayList
	public boolean inScene()
	{
		return true;
	}
	
	// used to determine if two characters are in the same place
	public boolean equals(Character obj)
	{
		Character other = obj;
		if(getX()+getWidth()>=other.getX()&&
			getX()<=other.getX()+other.getWidth() &&
			getY()+getHeight() >=other.getY() &&
			getY() <= other.getY()+other.getHeight())
		   return true;
		return false;
	}
	
}