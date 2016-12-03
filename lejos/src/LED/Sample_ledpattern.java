import lejos.hardware.Button;
import lejos.utility.Delay;

public class Sample_ledpattern {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		//0から6までのLEDパターンを3秒ごとに切り替える
		//0から順に、無点灯,緑,赤,オレンジ,緑点滅,赤点滅,オレンジ点滅
		for(int i = 0;i<=6;i++)
		{
			Button.LEDPattern(i);
			Delay.msDelay(3000);//3000ms停止
		}
	}

}
