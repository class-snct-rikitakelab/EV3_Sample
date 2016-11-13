package ev3wayDistanceMeasure;

import java.util.TimerTask;

public class CommandTask extends TimerTask{

	SerialConnect serial = new SerialConnect();
	TouchSensor touch;

	boolean startFlag = false;
	boolean stopFlag = false;
	public boolean measureFlag = false;

	CommandTask(TouchSensor touch){
		this.touch = touch;
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ

		serial.checkCommand();

		if(serial.getCommand() == 71){
			startFlag = true;
		}else if(touch.isButtonPressed() == true){
			startFlag = true;
		}else{

		}

		if(serial.getCommand() == 83){
			this.stopFlag = true;
		}else{

		}

		if(serial.getCommand() == 77){
			measureFlag = true;
		}else{

		}

	}

	public boolean checkStartCommand(){
		return startFlag;
	}

	public boolean checkstopFlag(){
		return this.stopFlag;
	}

}
