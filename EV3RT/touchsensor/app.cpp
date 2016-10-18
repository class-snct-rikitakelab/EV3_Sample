#include "ev3api.h"
#include "app.h"
#include "TouchSensor.h"
#include "Clock.h"

using namespace ev3api;

#define DEBUG

#ifdef DEBUG
#define _debug(x) (x)
#else
#define _debug(x)
#endif

#define CALIB_FONT (EV3_FONT_SMALL)
#define CALIB_FONT_WIDTH (6/*TODO: magic number*/)
#define CALIB_FONT_HEIGHT (8/*TODO: magic number*/)

TouchSensor*    touchSensor;
Clock*          clock;

/* ���C���^�X�N */
void main_task(intptr_t unused)
{
    touchSensor = new TouchSensor(PORT_1);
    clock       = new Clock();

    while(1)
    {
      if(touchSensor->isPressed() == true){
        ev3_lcd_fill_rect(0, 0, EV3_LCD_WIDTH, EV3_LCD_HEIGHT, EV3_LCD_WHITE);
        ev3_lcd_draw_string("TRUE", 0, CALIB_FONT_HEIGHT*1);
      }else{
        ev3_lcd_fill_rect(0, 0, EV3_LCD_WIDTH, EV3_LCD_HEIGHT, EV3_LCD_WHITE);
        ev3_lcd_draw_string("FALSE", 0, CALIB_FONT_HEIGHT*1);
      }
        clock->sleep(1000);
    }

    ext_tsk();
}
