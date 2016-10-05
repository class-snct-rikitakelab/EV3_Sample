import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;


public class Motors extends Thread{
	
	private DataExchange DEObj;
	TachoMotorPort leftMotor = MotorPort.C.open(TachoMotorPort.class);
	TachoMotorPort rightMotor = MotorPort.B.open(TachoMotorPort.class);
	
	/*
	static RegulatedMotor leftMotor = Motor.C;
	static RegulatedMotor rightMotor = Motor.B;
	static RegulatedMotor middleMotor = Motor.A;
	*/
	
	
	public Motors(DataExchange DE){
		DEObj = DE;
		
	}
	
	private void FollowLine(int lMotor, int rMotor){ //basically this is the function that moves the robot, robot only turns right now
		
		if(lMotor > 0){
			leftMotor.controlMotor(lMotor,1);
		}
		else if(lMotor > 0){
			leftMotor.controlMotor(lMotor, 1);
		}
		else{
			leftMotor.controlMotor(0, 0);
		}
		
		
		if(rMotor > 0){
			rightMotor.controlMotor(rMotor, 1);
		}
		else if(rMotor < 0){
			rightMotor.controlMotor(rMotor, 1);
		}
		else{
			rightMotor.controlMotor(0, 0);
		}
		
		Delay.msDelay(50);

	}
	
	public void run(){
		

		rightMotor.controlMotor(0, 0);
		rightMotor.resetTachoCount();
		
		leftMotor.controlMotor(0, 0); // power , mode
		leftMotor.resetTachoCount();
		
		while(true){
			
			/*
			leftMotor.controlMotor(40, 1);
			rightMotor.controlMotor(40, 1);
			
			if(leftMotor.getTachoCount() >= 360 && rightMotor.getTachoCount() >= 360){
				leftMotor.controlMotor(0, 0);
				rightMotor.controlMotor(0, 0);
				break;
			}
			Delay.msDelay(10);
			*/
			
			if(DEObj.getColor() < 0.1){
				FollowLine(10,40);
			}
			else{
				FollowLine(40,10);
			}
			
			
			if(DEObj.getStop()){
				break;
			}
		}
		
	}

}
