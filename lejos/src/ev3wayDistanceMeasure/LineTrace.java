package ev3wayDistanceMeasure;

import java.util.Timer;

import ev3Sample.balancer.Balancer;
import ev3Viewer.LogSender;
import lejos.hardware.Battery;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class LineTrace {

	static float BLACK = 0.0F;
	static float WHITE = 0.0F;

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		TachoMotorPort motorPortL = MotorPort.C.open(TachoMotorPort.class); // 左モータ
	    TachoMotorPort motorPortR = MotorPort.B.open(TachoMotorPort.class); // 右モータ
	    TachoMotorPort motorPortT = MotorPort.A.open(TachoMotorPort.class); // 尻尾モータ

	    TouchSensor touch = new TouchSensor(new EV3TouchSensor(SensorPort.S1));
		GyroSensor gyro = new GyroSensor(new EV3GyroSensor(SensorPort.S4));
		WheelMotor wheel = new WheelMotor(motorPortR,motorPortL);
		BrightSensor bright = new BrightSensor(new EV3ColorSensor(SensorPort.S3));
		TailMotor tail = new TailMotor(motorPortT);

		TurnCalc turn = new TurnCalc(bright,50.0F,300.0F);

		LogSender ls = new LogSender();;
		LCD.clear();
		LCD.drawString("Connect to LogSender", 0, 0);

		ls.connect();

		LCD.clear();
        LCD.drawString("Please Wait...  ", 0, 4);

        // Java の初期実行性能が悪く、倒立振子に十分なリアルタイム性が得られない。
        // 走行によく使うメソッドについて、HotSpot がネイティブコードに変換するまで空実行する。
        // HotSpot が起きるデフォルトの実行回数は 1500。
        for (int i=0; i < 1500; i++) {
        	turn.getTurn();
        	gyro.getGyro_deg_per_sec();
        	wheel.controlWheel(0, 0);
        	wheel.getEncordL();
        	wheel.getEncordR();
        	bright.getBright();
            Battery.getVoltageMilliVolt();
            Balancer.control(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8000);
        }
        Delay.msDelay(10000);       // 別スレッドで HotSpot が完了するだろう時間まで待つ。

        wheel.controlWheel(0, 0);
        tail.pwmControlTail(0);
        wheel.resetEncord();
        tail.resetEncord();
        Balancer.init();
            // 倒立振子制御初期化

        Sound.beep();
        calibration(bright, touch);
        turn.setTarget(WHITE, BLACK);

        // リモート接続
        Timer rcTimer = new Timer();
        CommandTask rcTask = new CommandTask(touch);

        // 距離計測タスク
        Timer disTimer = new Timer();
        DistanceTask disTask = new DistanceTask(wheel);

        // 走行タスク
        Timer driveTimer = new Timer();
        DriveTask driveTask = new DriveTask(turn, wheel, tail, gyro);

        rcTimer.scheduleAtFixedRate(rcTask, 0, 20);

        while(true){
        	if(rcTask.checkStartCommand() == true){
            	break;
            }else{
            }

        	tail.controlTail(92);
        	Delay.msDelay(20);
        }

        long hoge = System.currentTimeMillis();

        while(true){
        	tail.controlTail(100);

        	if(tail.getEncord() >= 96){
        		tail.controlTail(0);
        		break;
        	}

        	Delay.msDelay(20);
        }

        driveTimer.scheduleAtFixedRate(driveTask, 0, 4);
        disTimer.scheduleAtFixedRate(disTask, 0, 100);

        float Distance = 0;
        long dammy = 0;

        while(true){

        	if(rcTask.measureFlag == true){
        		Distance = disTask.getDistanceMeter();
        		ls.addLog("Distance", Distance, dammy);
        		dammy += 100;
        		rcTask.measureFlag = false;
        	}

        	if(rcTask.checkstopFlag() == true){
        		break;
        	}

        	Delay.msDelay(20);
        }

        Sound.beep();

        ls.send();

        LCD.clear();
        LCD.drawString("END", 0, 0);

        driveTimer.cancel();
        disTimer.cancel();
        rcTimer.cancel();
        wheel.controlWheel(0, 0);

        Button.waitForAnyEvent();

	}

	//黒と白と階段の輝度値を取得して記録しておく
	private static void calibration(BrightSensor bright,TouchSensor touch){

		boolean flag = false;

		LCD.drawString("Detect BLACK", 0, 0);
		while(true){
			if(touch.isButtonPressed() == true){
				flag = true;
			}else{
				if(flag == true){
					break;
				}
			}
			Delay.msDelay(100);
		}
		BLACK = bright.getBright();
		LCD.clear();
		Sound.beep();
		flag = false;

		LCD.drawString("Detect WHITE", 0, 0);
		while(true){
			if(touch.isButtonPressed() == true){
				flag =true;
			}else{
				if(flag == true){
					break;
				}
			}
			Delay.msDelay(100);
		}
		WHITE = bright.getBright();
		LCD.clear();
		Sound.beep();
		flag = false;
	}

}
