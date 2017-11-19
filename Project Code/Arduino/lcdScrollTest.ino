#include <LiquidCrystal.h>

LiquidCrystal lcd(12, 11, 5, 4, 3, 2);
char str[] = "Hello world, my name is Bob.";
int len = sizeof(str)/sizeof(str[0]) - 1;
int count = 0;

void setup() {
  // put your setup code here, to run once:
  lcd.begin(16, 2);
}

void loop() {
  lcd.setCursor(0, 0);
  
  for (int i = 0; i < len; i++) {
    lcd.print(str[i]);
    count++;
    delay(200);

    if (count > 16) {
      count--;
      lcd.scrollDisplayLeft();
    }
  }
}
