import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

// mitä pidempää ollu valkosella / mustalla nii sitä enemmän kääntyy vastaa

public class Motors extends Thread{
	
	private DataExchange DEObj;
	TachoMotorPort leftMotor = MotorPort.C.open(TachoMotorPort.class);
	TachoMotorPort rightMotor = MotorPort.B.open(TachoMotorPort.class);
	
	TachoMotorPort middleMotor = MotorPort.A.open(TachoMotorPort.class);
	
	double black = 0.1;
	
	public Motors(DataExchange DE){
		DEObj = DE;
	}
	
	
	private void FollowLine(int lMotor, int rMotor){
		
		if(lMotor > 0 || lMotor > 0){
			leftMotor.controlMotor(lMotor,1);
		}
		else{
			leftMotor.controlMotor(0, 0);
		}
		
		
		if(rMotor > 0 || rMotor < 0){
			rightMotor.controlMotor(rMotor, 1);
		}
		else{
			rightMotor.controlMotor(0, 0);
		}
		
		Delay.msDelay(50);

	}

	
	private void Forward(double value){
		
		int power = (int)(value * 300);
		
		LCD.drawInt(power, 0, 5);
		LCD.refresh();
		
		if(value < 0){
			rightMotor.controlMotor(power/2,1);
			leftMotor.controlMotor(power,1);
		}
		else if(value > 0){
			rightMotor.controlMotor(power, 1);
			leftMotor.controlMotor(power/2, 1);
		}
		else{
			rightMotor.controlMotor(0, 0);
			leftMotor.controlMotor(0, 0);
		}
		
	}
	
	public void run(){
		
		// musta 0.06 , valk = 0.6
		
		double white = 0.6, black = 0.06;
		
		double midpoint = (white - black ) / 2 + black;
		int kp = 1;
		double correction=0,value = 0;
		

		rightMotor.controlMotor(0, 0);
		rightMotor.resetTachoCount();
		
		leftMotor.controlMotor(0, 0); // power , mode
		leftMotor.resetTachoCount();
		
		middleMotor.controlMotor(0, 0);
		middleMotor.resetTachoCount();
		
		middleMotor.controlMotor(15, 1);
		
		Delay.msDelay(500);
		
		middleMotor.controlMotor(0, 0);
		
		while(true){
			
			
			value = DEObj.getColor();
			correction = kp * ( midpoint - value);
			Forward(correction);
			/*
			if(DEObj.getColor() < black){
				FollowLine(30,40);
			}
			else{
				FollowLine(40,30);
			}
			*/
			
			if(DEObj.getStop()){
				break;
			}
		}
		
	}

}
