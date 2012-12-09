
public class Person
{
	private String name;
	private int score;
	
	public Person()
	{
		name="";
		score=0;
	}
	
	public Person(String n, int s)
	{
		name=n;
		score=s;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public void setScore(int s)
	{
		score=s;
	}
	
	public String toString()
	{
		String te = name+" - "+score;
		return te;
	}
	
}