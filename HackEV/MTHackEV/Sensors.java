import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class Sensors extends Thread{
	
	public EV3GyroSensor gyroSensor = new EV3GyroSensor(SensorPort.S4);
	public SampleProvider rate;
	public float[] sampleGyro;
	
	static EV3UltrasonicSensor sonicSensor = new EV3UltrasonicSensor(SensorPort.S3);
		
	private float min = 0.0F;
	private float max = 1.0F;
	
	public float min_api = 0.0F;
	public float max_api = 1.0F;
	
	private DataExchange DEObj;
	
	static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
	
	public Sensors(DataExchange DE){
		DEObj = DE;
		

	}
	
	public void run(){
		
		SensorMode rate = (SensorMode) gyroSensor.getRateMode();
		
		SensorMode sonic = sonicSensor.getMode(0);
		float sValue[] = new float[sonic.sampleSize()];
		
		SensorMode color = colorSensor.getMode(1);
		float cValue[] = new float[color.sampleSize()];
		
		sampleGyro = new float[rate.sampleSize()];
		gyroSensor.reset();
		
		
		
		while(true){
			
			//gets the values from the sensors
			rate.fetchSample(sampleGyro, 0);
			sonic.fetchSample(sValue,0);
			color.fetchSample(cValue, 0);
			
			//determines which side is followed
			if(DEObj.GetFollow()){
				cValue[0]=cValue[0]*(-1);
			}
			
			//puts the values to DataExchange class
			DEObj.AddRate(sampleGyro[0]);
			DEObj.setColor(((cValue[0] - min_api)/(max_api - min_api))*(min-max));
			DEObj.SetDistance(sValue[0]);
			
			//negates the color value if its negative
			if(cValue[0] < 0){
				cValue[0]=cValue[0]*(-1);
			}
			
			//if the robot is on white, will increase the time. Will reset the time if its not
			if(cValue[0] > (DEObj.GetMiddle() * 1.2) ){
				DEObj.IncreaseTime();
			}
			else{
				DEObj.ResetTime();
			}
			
			
			//will stop the loop if touchsensor is pressed
			if(DEObj.getStop()){
				break;
			}
			
		}
		
	}

}
