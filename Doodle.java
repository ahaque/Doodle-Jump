import java.awt.*;
import java.applet.*;

public class Doodle extends Character
{
   private int stepy;
   private int id;
   private int velocity;
   private int hVelocity;
   private int count;
   private int hFacing;

	// default constructor, sets all to zero
	public Doodle(int id, int x, int y, int w, int h)
	{
		super(id,x,y,w,h);
		velocity=0;
		// L and R velocity
		hVelocity=10;
		count=0;
	}
	
	public int getVelocity()
	{
		return velocity;
	}
	
	public void setVelocity(int v)
	{
		velocity = v;
	}
	
	public void setHFacing(int hf)
	{
		hFacing = hf;
	}

	//returns the index number of the needed image from the ArrayList
	// at location 1 in ArrayList of images
	public int show()
	{
		return id;
	}
	
	public String toString()
	{
		return "Doodle x: "+x+"|y: "+y;
	}
	

	public void move()
	{
		// ################# Y AXIS #####################
		// negative acceleration (gravity factor)
		int acceleration = 1;
		
		if(velocity!=0){
			if(velocity<=-1)
				acceleration=1;
			if(velocity>=1)
				acceleration=1;
		// gravity, count slows down the gravity so it doesnt reduce velocity every frame update
		if(count>2){
			// set this number for max falling speed
			if(velocity<20){
				/* if velocity = 0 (means game has not started), dont do anything
				     if velocity with velocity ==0, prevent it from being 0 so program is
				     not confused with game start (because it does nothing on game start) */
				if(velocity+acceleration==0){
					velocity=0;
				}
				velocity=velocity+acceleration;
			}

			count=0;
		}		
		count++;	
		changeY(velocity);
		}
		
		
		
		// ################# X AXIS #####################
		// horizontal velocity
			
		switch (hFacing)	{
			case -1: changeX(-hVelocity); break;
			case 0: changeX(0); break;
			case 1: changeX(hVelocity); break;
		}	

	//	System.out.println("Doodle x: "+super.getX()+" | y: "+super.getY()+ " | v: "+velocity + " | a: "+acceleration);
		
		checkLRBounds();
	}
	
	public boolean checkHitPlatform(Object obj)
	{
		Platform other = (Platform) obj;
		
		if(getX()+getWidth()>=other.getX()&&
			getX()<=other.getX()+other.getWidth() &&
			getY()+getHeight() >=other.getY() &&
			getY()+getHeight() <= other.getY()+other.getHeight())
		   return true;
		return false;
	}
	
	public void checkLRBounds()
	{
		if(getX()>445)
			setX(-60);
			
		if(getX()<-60)
			setX(445);
			
		if(getY()<10)
			setY(10);	
	}
		
}