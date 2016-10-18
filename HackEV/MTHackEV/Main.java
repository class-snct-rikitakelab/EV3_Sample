import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;


public class Main{
	
	static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);
	
	private static DataExchange DE;
	private static Motors MObj;
	private static Sensors SObj;
	
	public static void main(String[] args) {
		
		DE = new DataExchange();
		SObj = new Sensors(DE);
		MObj = new Motors(DE);
		
		float colorValue=0;
		
		SensorMode touch = touchSensor.getMode(0);
		float tValue[] = new float[touch.sampleSize()];
		
		MObj.MotorInit();
		SObj.start();
		
		
		//following is for the calibration
		while(tValue[0]!=1){
			LCD.clear();
			LCD.drawString("set white", 0, 7);
			LCD.drawInt((int)(100*DE.getColor()),0,5);
			LCD.refresh();
			touch.fetchSample(tValue, 0);
		}
		MObj.SetWhite();
		Delay.msDelay(500);
		tValue[0]=0;
		while(tValue[0]!=1){
			LCD.clear();
			LCD.drawString("set black", 0, 7);
			LCD.drawInt((int)(100*DE.getColor()),0,5);
			LCD.refresh();
			touch.fetchSample(tValue, 0);
		}
		MObj.SetBlack();
		Delay.msDelay(500);
		

		
		while(tValue[0]!=1){
			touch.fetchSample(tValue, 0);
		}
		
		LCD.drawString("Started", 0, 7);
		LCD.refresh();
		
		Delay.msDelay(500);
		
		
		MObj.start();

		LCD.clear();
		tValue[0]=0;

		while(tValue[0]!=1){
			touch.fetchSample(tValue, 0);
			colorValue = DE.getColor();
			LCD.drawString("value: " + colorValue, 1, 1);
			Delay.msDelay(10);
			LCD.refresh();
		}
		LCD.drawString("Finished", 0, 7);
		LCD.refresh();
		DE.setStop(true);
		Delay.msDelay(2000);
		
	}
}
