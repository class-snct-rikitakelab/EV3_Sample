package gyro;

import java.util.TimerTask;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class TouchSensorTask extends TimerTask{

    // タッチセンサ
    private EV3TouchSensor touch;
    private SensorMode touchMode;
    private float[] sampleTouch;

    boolean touchedFlag = false;

	TouchSensorTask(Port TouchPort){
		touch = new EV3TouchSensor(TouchPort);
		touchMode = touch.getTouchMode();
		sampleTouch = new float[touchMode.sampleSize()];
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

		if(Pressed() == true){
			touchedFlag = true;
		}else{

		}

	}

	boolean GetFlag(){

		if(touchedFlag == true){
			touchedFlag = false;
			return true;
		}else{
			return false;
		}
	}

	boolean Pressed(){
		touchMode.fetchSample(sampleTouch, 0);
        return ((int)sampleTouch[0] != 0);
	}


}
