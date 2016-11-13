package ev3wayDistanceMeasure;

import java.util.TimerTask;

import ev3Sample.balancer.Balancer;
import lejos.hardware.Battery;

public class DriveTask extends TimerTask {

	TurnCalc turncalc;
	TailMotor tail;
	WheelMotor wheel;

	GyroSensor gyro;

	DriveTask(TurnCalc turn, WheelMotor wheel, TailMotor tail, GyroSensor gyro){
		turncalc = turn;
		this.tail = tail;
		this.wheel = wheel;
		this.gyro = gyro;
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

		tail.controlTail(0);

		float turn = turncalc.getTurn();
		float forward = 100.0F;

		float battery = Battery.getVoltageMilliVolt();
		float gyroValue = (int)gyro.getGyro_deg_per_sec();
		float tachoR = wheel.getEncordR();
		float tachoL = wheel.getEncordL();

		Balancer.control(forward,turn,gyroValue * (-1),0.0F, tachoL, tachoR, battery);

		wheel.controlWheel(Balancer.getPwmR(), Balancer.getPwmL());
	}

}
