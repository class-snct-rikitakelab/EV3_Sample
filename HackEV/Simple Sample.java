import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.port.SensorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

// this is very much still unfinished, it does not trace the line enough and i haven't yet tried PWM

public class Main {
	
	static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
	static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
	static RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	static RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	static RegulatedMotor middleMotor = new EV3LargeRegulatedMotor(MotorPort.A);
	
	private static void InitMotors(){
		int acceleration = 300, speed = 200;
		
	    leftMotor.setAcceleration(acceleration);
	    rightMotor.setAcceleration(acceleration);
	    middleMotor.setAcceleration(acceleration);
	    
	    leftMotor.setSpeed(speed);
	    rightMotor.setSpeed(speed); 
	    
	    middleMotor.rotateTo(25); //sets the fork in optimal position (if as low as possible)
	    
	    MotorsStop();

	}
	
	private static void MotorsStop(){
	    leftMotor.stop(true);
	    rightMotor.stop(true);
	    middleMotor.stop(true);
	}
	
	private static void MoveForward(){
		
		leftMotor.forward();
		rightMotor.forward();
		
	}


	public static void main(String[] args) {
		
		//boolean direction = true, pass = false;
		//int  lSpeed = speed, rSpeed = speed;
		int lineFinding = 0, searchRange = 30; // this is set in 2 places
		
		InitMotors();

		LCD.clear();
		
		SensorMode touch = touchSensor.getMode(0);
		SensorMode color = colorSensor.getMode(1);

		float tValue[] = new float[touch.sampleSize()];
		float cValue[] = new float[color.sampleSize()];
		
	    //middleMotor.rotateTo(25); //sets the fork in optimal position (if as low as possible)
	    //middleMotor.stop(true);
		
		// wait for button		
		while(tValue[0]!=1){
			touch.fetchSample(tValue, 0);
		}
		
		Delay.msDelay(1000);
		
		leftMotor.forward();
		rightMotor.forward();
		
		
		while(true){
			
			color.fetchSample(cValue, 0);
			
			
			for(int k=0;k<color.sampleSize();k++){
				//LCD.drawString("val[" + k + "] : " + cValue[k], 1, k+1);
				LCD.drawString("value: " + cValue[0], 1, 1);
			}
			Delay.msDelay(10);
			LCD.refresh();
			
			
			// musta 0.06 , 0.55 - 0.65 valkoinen , keltainen 65 - 70 , sininen 0.12 , pun 0.41 - 0.51 ,  vihr 0.15 
			
			// TESTING 
			
			
			if(cValue[0] > 0.1){
				MotorsStop();
				lineFinding++;
				if(lineFinding<=searchRange){
					leftMotor.rotate(2,true);
					rightMotor.rotate(-2,true);

				}
				else if(lineFinding>searchRange && lineFinding <= (searchRange*2)+searchRange/2){
					rightMotor.rotate(2,true);
					leftMotor.rotate(-2,true);

				}
				else{
					searchRange = searchRange * 2;
					lineFinding = 0;
				}
				

				

				
			}
			else{
				MotorsStop();
				searchRange = 30;
				lineFinding=0;
				MoveForward();
			}
			
			
			
			
			touch.fetchSample(tValue, 0);
			if(tValue[0]==1){
				break;
			}
			
			
			
		}
		
		rightMotor.stop(true);

		leftMotor.stop(true);
		
	}
}
