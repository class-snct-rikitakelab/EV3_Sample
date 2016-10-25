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
	
	
	public void run(){
		
		// must 0.06 , valk = 0.6
		
		double correction=0,value = 0,kp = 1.1;
		double midpoint = (white - black ) / 2 + black;
		DEObj.SetMiddleColor(midpoint);
		int right=0,left=0,forward=38, turn=0, stage=1;
		
		DEObj.ResetTime(); //to make sure that there is no time counted
		
		while(true){
			
			value = DEObj.getColor(); //get the color value
			
			switch(stage){
			
			//will follow the line and continue trying to find it unless little while has passed on white.
			case 1:
				//this moves code to stage 2 if only white is detected for a while
				if(DEObj.GetTime() > 5000 && DEObj.GetFollow()){ 
					stage=2;
				}
				
				//calculations for the turn is calculated here
				correction = kp * ( midpoint - value);
				turn = (int)(correction*100);
				left = forward - turn;
				right = forward + turn;
				break;
				
				// will search for non-white line
			case 2:
				//if no longer on only white, will go to stage 3
				if(value < (DEObj.GetMiddle() * 1.2)){
					stage = 3;
				}
				//starts turning to left
				value = midpoint*0.7;
				
				//calculations for the turn is calculated here
				correction = kp * ( midpoint - value);
				turn = (int)(correction*100);
				left = forward - turn;
				right = forward + turn;
				break;
				
				// only stops it for a little while
			case 3:
				left = 0;
				right = 0;
				Delay.msDelay(500);
				stage = 4;
			
			//will try to find the next color.
			case 4:
				
				break;
			}

			LCD.drawString("stage = " + stage, 1, 6);
			LCD.refresh();
			//makes the robot move
			Forward(left,right);

			
			//if button is pressed, this will stop the loop.
			if(DEObj.getStop()){
				break;
			}
		}
		
	}

}
