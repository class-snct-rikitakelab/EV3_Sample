package motor;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.utility.Delay;

public class Sample_pwm {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

	    TachoMotorPort motorPortC = MotorPort.C.open(TachoMotorPort.class);

	    motorPortC.setPWMMode(BasicMotorPort.PWM_BRAKE);

	    motorPortC.controlMotor(0, 0);

	    motorPortC.resetTachoCount();

	    while(true){

	    	motorPortC.controlMotor(20, 1);

	    	if(motorPortC.getTachoCount() >= 360){
	    		motorPortC.controlMotor(0, 0);
	    		break;
	    	}

	    	Delay.msDelay(20);

	    }

	    LCD.clear();
	    LCD.drawString("Rotated Degree:"+motorPortC.getTachoCount(), 0, 0);

	    Button.waitForAnyEvent();	//wait by any key is pressed

	}

}
