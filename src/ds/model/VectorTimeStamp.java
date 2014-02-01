package ds.model;

public class VectorTimeStamp extends TimeStamp {
	
	private int[] vectorData;
	
	
	public VectorTimeStamp(int num)
	{
		if (num != 0)
		{
			vectorData = new int[num];
		}
	}
	
	public int[] getVector()
	{
		return this.vectorData;
	}
	
	public int getVectorLength()
	{
		return this.vectorData.length;
	}
	
	public void setVector(int index, int data)
	{
		this.vectorData[index] = data;
	}
}