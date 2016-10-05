/**
 ******************************************************************************
 ** ファイル名 : app.cpp
 **
 ** タッチセンサを押すごとに輝度値を取得してくれるサンプルプログラム
 **
 ** 注記 : タッチセンサ：ポートⅠ カラーセンサ：ポート2
 ******************************************************************************
 **/

#include "ev3api.h"
#include "app.h"
#include "TouchSensor.h"
#include "ColorSensor.h"
#include "Clock.h"

using namespace ev3api;

#define DEBUG

#ifdef DEBUG
#define _debug(x) (x)
#else
#define _debug(x)
#endif

/* LCDフォントサイズ */
#define CALIB_FONT (EV3_FONT_SMALL)
#define CALIB_FONT_WIDTH (6/*TODO: magic number*/)
#define CALIB_FONT_HEIGHT (8/*TODO: magic number*/)

/* オブジェクトへのポインタ定義 */
TouchSensor*    touchSensor;
ColorSensor*    colorSensor;
Clock*          clock;


/* メインタスク */
void main_task(intptr_t unused)
{
    bool PressedFlag = false;
    char buff[256] = {'\0'};

    /* 各オブジェクトを生成・初期化する */
    touchSensor = new TouchSensor(PORT_1);
    colorSensor = new ColorSensor(PORT_2);
    clock       = new Clock();

    ev3_speaker_play_tone(NOTE_C4,20);

    while(1){

      while(1){
        if(touchSensor->isPressed()){
          PressedFlag = true;
        }

        if(PressedFlag == true){
          PressedFlag = false;
          sprintf(buff,"%i",colorSensor->getBrightness());
          ev3_lcd_fill_rect(0,0,EV3_LCD_WIDTH,EV3_LCD_HEIGHT,EV3_LCD_WHITE);
          ev3_lcd_draw_string(buff,0,0);
          break;
        }

        clock->sleep(20);
      }
      ev3_speaker_play_tone(NOTE_C4,20);
    }

    ext_tsk();
}
