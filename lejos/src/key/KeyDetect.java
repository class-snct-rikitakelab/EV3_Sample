package key;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

/*押されたキーのID番号をLCDに表示します*/

public class KeyDetect {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		int key = 0;
		while(true){
			key = Button.readButtons();

			LCD.drawString("ID:"+key, 0, 0);

			Delay.msDelay(200);
		}
	}

}
