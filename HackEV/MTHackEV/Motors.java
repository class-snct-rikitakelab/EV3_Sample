import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.utility.Delay;

// yks tapa, forward menee eteepäi ja turn vaikuttaa forwardii vaa
// kattoo arvon mukaa ja nopeuttaa vauhtia jos arvo pysyy tasasesti alhasena

public class Motors extends Thread{
	
	double white = 0.6, black = 0.06;	
	
	private DataExchange DEObj;
	TachoMotorPort leftMotor = MotorPort.C.open(TachoMotorPort.class);
	TachoMotorPort rightMotor = MotorPort.B.open(TachoMotorPort.class);
	
	TachoMotorPort middleMotor = MotorPort.A.open(TachoMotorPort.class);
		
	public Motors(DataExchange DE){
		DEObj = DE;
	}
	
	public void SetBlack(){
		black = DEObj.getColor();
	}
	
	public void SetWhite(){
		white = DEObj.getColor();
	}

	public void MotorInit(){
		
		rightMotor.controlMotor(0, 0);
		rightMotor.resetTachoCount();
		
		leftMotor.controlMotor(0, 0); 
		leftMotor.resetTachoCount();
		
		middleMotor.controlMotor(0, 0);
		middleMotor.resetTachoCount();
		
	}
	
	private void Forward(int left, int right){
		

		rightMotor.controlMotor(right, 1);
		leftMotor.controlMotor(left, 1);
		
	}
	
	private void CheckForNewColor(int color){
		
		
		
	}
	
	public void run(){
		
		// musta 0.06 , valk = 0.6
		
		double correction=0,value = 0,kp = 1.1;
		double midpoint = (white - black ) / 2 + black;
		int right=0,left=0,forward=38, turn=0;
		
		while(true){
			
			value = DEObj.getColor();
			correction = kp * ( midpoint - value);
			turn = (int)(correction*100);
			
			left = forward - turn;
			right = forward + turn;
			
			LCD.drawString("value "+ (int)(value*100), 0, 4);

			LCD.refresh();
			
			Forward(left,right);
			
			if(DEObj.getStop()){
				break;
			}
		}
		
	}

}
