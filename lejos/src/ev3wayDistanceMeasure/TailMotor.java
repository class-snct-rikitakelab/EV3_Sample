package ev3wayDistanceMeasure;

import lejos.hardware.port.BasicMotorPort;
import lejos.hardware.port.TachoMotorPort;

public class TailMotor {

	private TachoMotorPort motorPortT;

	private float P = 2.5F;
	private int PWM_MAX = 60;

	public TailMotor(TachoMotorPort tail){
		motorPortT = tail;
		motorPortT.setPWMMode(BasicMotorPort.PWM_BRAKE);
		motorPortT.resetTachoCount();
	}

	public void resetEncord(){
		motorPortT.resetTachoCount();
	}

	public void pwmControlTail(int pwm){
		motorPortT.controlMotor(pwm, 1);
	}

	public void controlTail(int angle) {
		float pwm = (float)(angle - motorPortT.getTachoCount()) * P; // 比例制御
        // PWM出力飽和処理
        if (pwm > PWM_MAX) {
            pwm = PWM_MAX;
        } else if (pwm < -PWM_MAX) {
            pwm = -PWM_MAX;
        }
        motorPortT.controlMotor((int)pwm, 1);
	}

	public int getEncord(){
		return this.motorPortT.getTachoCount();
	}

}
