#include <Servo.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

int ledConnector = 24;
int ledConnector2 = 22;
int ledConnector3 = 26;

LiquidCrystal_I2C lcd(0x27, 16, 2);

Servo myservo;
void setup() {
  // put your setup code here, to run once:
  Serial.setTimeout(20);
  Serial.begin(9600);
  //wartet bis die Serielle Verbindung aufgebaut wurde
  while (!Serial) {
    ;
  }
  
  pinMode(ledConnector, OUTPUT);
  pinMode(ledConnector2, OUTPUT);
  pinMode(ledConnector3, OUTPUT);

  myservo.attach(28);

  lcd.init();
  lcd.backlight();
}

void blink() {
  if (digitalRead(ledConnector) == HIGH) {
    digitalWrite(ledConnector, LOW);
  } else {
    digitalWrite(ledConnector, HIGH);
  }
}

void schranke(int state) {
  if (state == 1) {
    myservo.write(90);
  } else {
    myservo.write(180);
  }
}

void printLcd(int line){
  Serial.write("Hi Welt");
  lcd.setCursor(0, line);
  lcd.print("                     ");
  lcd.setCursor(0, line);
  
  while(Serial.available() == 0);
  lcd.print(Serial.readString());
}

void loop() {
  // put your main code here, to run repeatedly:
  //wenn ein input über den Seriellen Port kommt

  while (Serial.available() > 0) {
    digitalWrite(ledConnector2, LOW);
    String in = Serial.readString();

    if (in == "blink") {
      blink();
    }
    if (in == "open") {
      schranke(1);
    }
    if (in == "close") {
      schranke(2);
    }
    if (in == "print0"){
      printLcd(0);
    }
    if (in == "print1"){
      printLcd(1);
    }
  }
  digitalWrite(ledConnector2, HIGH);
  
//  lcd.setCursor(0, 0);//Hier wird die Position des ersten Zeichens festgelegt. In diesem Fall bedeutet (0,0) das erste Zeichen in der ersten Zeile.
//  lcd.print("Freie Plaetze:");
//  lcd.setCursor(0, 1);// In diesem Fall bedeutet (0,1) das erste Zeichen in der zweiten Zeile.
//  lcd.print("   400");
}
