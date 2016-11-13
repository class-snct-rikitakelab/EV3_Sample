package ev3wayDistanceMeasure;

import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public class GyroSensor {

    // ジャイロセンサ
    public EV3GyroSensor gyro;
    public SampleProvider rate;          // 角速度検出モード
    public float[] sampleGyro;

	public GyroSensor(EV3GyroSensor gyro){
		this.gyro = gyro;
		this.rate = gyro.getRateMode();
		this.sampleGyro = new float[rate.sampleSize()];
		gyro.reset();
	}

	float getGyro_deg_per_sec(){
		rate.fetchSample(sampleGyro, 0);
		return this.sampleGyro[0];
	}

}
