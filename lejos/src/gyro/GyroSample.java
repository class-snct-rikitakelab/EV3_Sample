package gyro;

import java.util.Timer;

import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class GyroSample {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		Port  SENSORPORT_GYRO      = SensorPort.S4;  // ジャイロセンサーポート
		Port  SENSORPORT_TOUCH     = SensorPort.S1;  // タッチセンサポート

	    // ジャイロセンサ
	    EV3GyroSensor gyro;
	    SampleProvider rate;		// 角速度検出モード[deg/s]
	    SampleProvider angle;		//角度検出[deg]
	    SampleProvider rate_angle;	//角度・角速度両方[deg][deg/s]
	    float[] rateSample;
	    float[] angleSample;
	    float[] rate_angleSample;

	    gyro = new EV3GyroSensor(SENSORPORT_GYRO);
        rate = gyro.getRateMode();              // 角速度検出モード
        angle = gyro.getAngleMode();
        rateSample = new float[rate.sampleSize()];
        angleSample = new float[angle.sampleSize()];

        //Hardware calibration of the Gyro sensor and reset off accumulated angle to zero.
        //The sensor should be motionless during calibration. (lejosドキュメントから引用)
        gyro.reset();

        Timer touchTimer = new Timer();
        TouchSensorTask tsTask = new TouchSensorTask(SENSORPORT_TOUCH);

        touchTimer.scheduleAtFixedRate(tsTask, 0, 20);

        for(;;){

        	rate.fetchSample(rateSample, 0);
        	angle.fetchSample(angleSample, 0);

        	LCD.clear();
        	LCD.drawString("Angle:"+angleSample[0]+"[deg]", 0, 0);
        	LCD.drawString("Rate:"+rateSample[0]+"[deg/sec]", 0, 1);
        	Delay.msDelay(100);

        	if(tsTask.GetFlag() == true){
        		break;
        	}
        }

        touchTimer.cancel();

        gyro.close();

	}

}
