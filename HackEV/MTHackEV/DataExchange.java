
public class DataExchange extends Thread{

	private boolean obstacleDetected = false,stop = false, followLeftSide = true;
		
	private float color = 0, distance = 0;
	
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
}
