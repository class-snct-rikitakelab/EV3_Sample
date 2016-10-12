/**
 ******************************************************************************
 ** ファイル名 : app.cpp
 **
 ** 概要 : マルチタスクを実装するためのライントレース。EV3waｙの左右モータをpwm=20で回転し続け、1秒ごとにそれぞれのエンコーダ値をLCDに出力
 **
 ** 注記 : sample_cpp (ライントレース/尻尾モータ/超音波センサ/リモートスタート)
 ******************************************************************************
 **/

#include "ev3api.h"
#include "app.h"
#include "Motor.h"
#include "Clock.h"
#include "TouchSensor.h"

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

Motor*          leftMotor;
Motor*          rightMotor;
TouchSensor*    touchSensor;
Clock*          clock;


/* メインタスク */
void main_task(intptr_t unused)
{

    /* 各オブジェクトを生成・初期化する */
    leftMotor   = new Motor(PORT_C);
    rightMotor  = new Motor(PORT_B);
    clock       = new Clock();
    touchSensor = new TouchSensor(PORT_1);

    /* LCD表示タスクの起動 */
    act_tsk(LCD_TASK);

    /* 走行モーターエンコーダーリセット */
    leftMotor->reset();
    rightMotor->reset();

    /**
    * Main loop for the self-balance control algorithm
    */
    while(1)
    {
        leftMotor->setPWM(20);
        rightMotor->setPWM(20);

        if (touchSensor->isPressed())
        {
            break; /* タッチセンサが押された */
        }

        clock->sleep(20); /* 20msec周期起動 */
    }

    leftMotor->setPWM(0);
    rightMotor->setPWM(0);
    leftMotor->reset();
    rightMotor->reset();

    ter_tsk(LCD_TASK);

    ext_tsk();
}

//*****************************************************************************
// 関数名 : lcd_task
// 引数 : unused
// 返り値 : なし
// 概要 : 1秒ごとにエンコーダ値を取得。LCDに出力
//*****************************************************************************
void lcd_task(intptr_t unused)
{
  int32_t left = 0;
  int32_t right = 0;
  char buff_left[256] = {'\0'};
  char buff_right[256] = {'\0'};
  while(1){
    left = leftMotor->getCount();
    right = rightMotor->getCount();

    sprintf(buff_left,"%d",(int)left);
    sprintf(buff_right,"%d",(int)right);

    ev3_lcd_fill_rect(0, 0, EV3_LCD_WIDTH, EV3_LCD_HEIGHT, EV3_LCD_WHITE);
    ev3_lcd_draw_string("Left Encoder:", 0, CALIB_FONT_HEIGHT*1);
    ev3_lcd_draw_string(buff_left, 0, CALIB_FONT_HEIGHT*2);
    ev3_lcd_draw_string("Right Encoder:", 0, CALIB_FONT_HEIGHT*3);
    ev3_lcd_draw_string(buff_right, 0, CALIB_FONT_HEIGHT*4);

    clock->sleep(1000);
  }
}
