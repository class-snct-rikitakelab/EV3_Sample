package ev3wayDistanceMeasure;

import java.util.TimerTask;

public class DistanceTask extends TimerTask{

	WheelMotor wheel;

	private float curposRight = 0.0F;
	private float curposLeft = 0.0F;

	DistanceTask(WheelMotor wheel){

		this.wheel = wheel;

	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		curposRight = this.wheel.getEncordR();
		curposLeft = this.wheel.getEncordL();

	}

	public float getDistanceCMeter(){
		return (((this.curposLeft + this.curposRight) / 2.0F ) / 360.0F) * 26.2F;
	}

	public float getDistanceMeter(){
		return (((this.curposLeft + this.curposRight) / 2.0F ) / 360.0F) * 0.262F;
	}

}
