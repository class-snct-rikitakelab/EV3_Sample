import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;

public class Sensors extends Thread{
	
	private DataExchange DEObj;
	
	static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
	
	public Sensors(DataExchange DE){
		DEObj = DE;
		

	}
	
	public void run(){
		
		SensorMode color = colorSensor.getMode(1);

		float cValue[] = new float[color.sampleSize()];
		
		while(true){
			
			color.fetchSample(cValue, 0);
			
			DEObj.setColor(cValue[0]);
			
			if(DEObj.getStop()){
				break;
			}
			
		}
		
	}

}
