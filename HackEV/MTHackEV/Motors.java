import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.utility.Delay;

// yks tapa, forward menee eteepäi ja turn vaikuttaa forwardii vaa
// kattoo arvon mukaa ja nopeuttaa vauhtia jos arvo pysyy tasasesti alhasena

public class Motors extends Thread{
	
	private DataExchange DEObj;
	TachoMotorPort leftMotor = MotorPort.C.open(TachoMotorPort.class);
	TachoMotorPort rightMotor = MotorPort.B.open(TachoMotorPort.class);
	
	TachoMotorPort middleMotor = MotorPort.A.open(TachoMotorPort.class);
	
	double black = 0.1;
	
	public Motors(DataExchange DE){
		DEObj = DE;
	}

	public void MotorInit(){
		
		rightMotor.controlMotor(0, 0);
		rightMotor.resetTachoCount();
		
		leftMotor.controlMotor(0, 0); 
		leftMotor.resetTachoCount();
		
		middleMotor.controlMotor(0, 0);
		middleMotor.resetTachoCount();
		
		middleMotor.controlMotor(15, 1);
		
		Delay.msDelay(400);
		
		middleMotor.controlMotor(0, 3);
		
		Delay.msDelay(500);
		
	}
	
	private void Forward(int left, int right){
		
		rightMotor.controlMotor(right, 1);
		leftMotor.controlMotor(left, 1);
		
	}
	
	public void run(){
		
		// musta 0.06 , valk = 0.6
		
		double white = 0.6, black = 0.06, correction=0,value = 0;
		double midpoint = (white - black ) / 2 + black,kp = 1.25;
		int right=0,left=0,forward=40, correctionValue=0;
		
		
		
		while(true){
			
			
			value = DEObj.getColor();
			correction = kp * ( midpoint - value);
			correctionValue = (int)(correction*100);
			
			left = forward - correctionValue;
			right = forward + correctionValue;
			
			Forward(left,right);
			
			if(DEObj.getStop()){
				break;
			}
		}
		
	}

}
