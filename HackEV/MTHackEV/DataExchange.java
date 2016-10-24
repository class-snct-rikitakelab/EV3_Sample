
public class DataExchange extends Thread{

	private boolean obstacleDetected = false,stop = false, followLeftSide = true;
		
	private float color = 0, distance = 0, rate=0, middleColor = 0;
	
	private int time = 0;
	
	public DataExchange(){
		
	}

	public void setObstacleDetected(boolean status){
		obstacleDetected = status;
	}
	public boolean getObstacleDetected(){
		return obstacleDetected;
	}
	
	public void setColor(float newColor){
		color = newColor;
	}
	
	public float getColor(){
		return color;
	}
	
	public void SetDistance(float newDistance){
		distance = newDistance;
	}
	
	public float GetDistance(){
		return distance;
	}
	
	public void setStop(boolean bool){
		stop = bool;
	}
	
	public boolean getStop(){
		return stop;
	}
	
	public void FollowLeftSide(boolean bool){
		followLeftSide = bool;
	}
	public boolean GetFollow(){
		return followLeftSide;
	}
	
	public void AddRate(float newRate){
		rate = rate + newRate;
	}
	
	public float GetRate(){
		return rate;
	}
	
	public void IncreaseTime(){
		time++;
	}
	
	public int GetTime(){
		return time;
	}
	
	public void ResetTime(){
		time = 0;
	}
	
	public void SetMiddleColor(double newMiddle){
		middleColor = (float)newMiddle;
	}
	
	public float GetMiddle(){
		return middleColor;
	}
}
