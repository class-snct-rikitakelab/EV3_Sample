/**
 ******************************************************************************
 ** ファイル名 : app.cpp
 **
 ** 概要 : シリアル通信でキーボードの値を読み込むプログラム
 **
 ** 注記 : sample_cpp (ライントレース/尻尾モータ/超音波センサ/リモートスタート)
 ******************************************************************************
 **/

#include <string>
#include <stdlib.h>
#include "ev3api.h"
#include "app.h"
#include "Clock.h"

using namespace ev3api;

#define DEBUG

#ifdef DEBUG
#define _debug(x) (x)
#else
#define _debug(x)
#endif

/* Bluetooth */
static int32_t   bt_cmd = 0;      /* Bluetoothコマンド 1:リモートスタート */
static FILE     *bt = NULL;      /* Bluetoothファイルハンドル */
static uint8_t  character = 0;         /*受信文字列*/

/* 下記のマクロは個体/環境に合わせて変更する必要があります */
#define CMD_START         '1'    /* リモートスタートコマンド */

/* LCDフォントサイズ */
#define CALIB_FONT (EV3_FONT_SMALL)
#define CALIB_FONT_WIDTH (6/*TODO: magic number*/)
#define CALIB_FONT_HEIGHT (8/*TODO: magic number*/)

/* オブジェクトへのポインタ定義 */
Clock*          clock;

/* メインタスク */
void main_task(intptr_t unused)
{
    /* LCD画面表示 */
    ev3_lcd_fill_rect(0, 0, EV3_LCD_WIDTH, EV3_LCD_HEIGHT, EV3_LCD_WHITE);//矩形を書いて色を塗る
    ev3_lcd_draw_string("EV3way-ET sample_cpp", 0, CALIB_FONT_HEIGHT*1);

    /* Open Bluetooth file */
    bt = ev3_serial_open_file(EV3_SERIAL_BT);
    assert(bt != NULL);

    /* Bluetooth通信タスクの起動 */
    act_tsk(BT_TASK);

    //ビープ音を鳴らす
    ev3_speaker_play_tone(NOTE_C4,20);

    //入力用のバッファ
    char buff = '';

    //入力待機
    while(1){
      buff = character;

      if(bt_cmd == 1){
        break;
      }

      /* LCD画面表示 */
      ev3_lcd_fill_rect(0, 0, EV3_LCD_WIDTH, EV3_LCD_HEIGHT, EV3_LCD_WHITE);//矩形を書いて色を塗る
      ev3_lcd_draw_string(&buff, 0, CALIB_FONT_HEIGHT*1);
      clock->sleep(20);
    }

    ev3_speaker_play_tone(NOTE_C4,20);

    ev3_lcd_fill_rect(0, 0, EV3_LCD_WIDTH, EV3_LCD_HEIGHT, EV3_LCD_WHITE);//矩形を書いて色を塗る
    ev3_lcd_draw_string("END", 0, CALIB_FONT_HEIGHT*1);

    ter_tsk(BT_TASK);
    fclose(bt);

    //エスケープボタンを1秒間ほど押下してプログラムを終了(おそらく)
    ext_tsk();
}

//*****************************************************************************
// 関数名 : bt_task
// 引数 : unused
// 返り値 : なし
// 概要 : Bluetooth通信によるリモートスタート。 Tera Termなどのターミナルソフトから、
//       ASCIIコードで1を送信すると、リモートスタートする。
//*****************************************************************************
void bt_task(intptr_t unused)
{
    while(1)
    {
        uint8_t c = fgetc(bt); /* 受信 */
        character = c;
        switch(c)
        {
        case '1':
            bt_cmd = 1;
            break;
        default:
            break;
        }
        fputc(c, bt); /* エコーバック */
    }
}
