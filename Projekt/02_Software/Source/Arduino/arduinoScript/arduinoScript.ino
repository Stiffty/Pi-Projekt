#include <Servo.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <HCSR04.h>
#include <SPI.h>
#include <MFRC522.h>

int ledConnector = 24;
int ledConnector2 = 22;
int ledConnector3 = 26;

int servo_Pin = 28;

int ultrasonic_Trigger_Pin = 32;
int ultrasonic_Echo_Pin = 30;

int rifd_SS_Pin = 53;
int rifd_RST_Pin = 5;

byte s[] = {0x80, 0x07, 0xA3, 0xA3};

LiquidCrystal_I2C lcd(0x27, 16, 2);
UltraSonicDistanceSensor distanceSensor(ultrasonic_Trigger_Pin, ultrasonic_Echo_Pin);
MFRC522 mfrc522(rifd_SS_Pin, rifd_RST_Pin);

Servo myservo;

void setup() {
  // put your setup code here, to run once:
  Serial.setTimeout(20);
  Serial.begin(9600);
  //wartet bis die Serielle Verbindung aufgebaut wurde
  while (!Serial);

  myservo.write(90);

  pinMode(ledConnector, OUTPUT);
  pinMode(ledConnector2, OUTPUT);
  pinMode(ledConnector3, OUTPUT);

  myservo.attach(servo_Pin);

  lcd.init();
  lcd.backlight();

  SPI.begin();         // Init SPI bus
  mfrc522.PCD_Init(); // Init MFRC522 card
}

void schranke(int state) {
  if (state == 1) {
    myservo.write(90);
  } else {
    myservo.write(180);
  }
}

void printLcd(int line) {
  lcd.setCursor(0, line);
  lcd.print("                     ");
  lcd.setCursor(0, line);

  while (Serial.available() == 0);
  lcd.print(Serial.readString());
}

boolean pruefeKey(byte keyIn[]) {
  Serial.write("rifd");
  for (int i = 0; i < 4; i++) {
    while (Serial.available() == 0);
    byte b = Serial.read();
    if (b != keyIn[i]) {
      return false;
    }

  }
  return true;
}

void setzteStatus() {
  while (Serial.available() == 0);
  String in = Serial.readString();

  if (in == "Voll") {
    digitalWrite(ledConnector2, HIGH);
    digitalWrite(ledConnector3, LOW);
    digitalWrite(ledConnector, LOW);
  } else if (in == "Mittel") {
    digitalWrite(ledConnector2, LOW);
    digitalWrite(ledConnector3, LOW);
    digitalWrite(ledConnector, HIGH);
  } else if (in == "Leer") {
    digitalWrite(ledConnector2, LOW);
    digitalWrite(ledConnector3, HIGH);
    digitalWrite(ledConnector, LOW);
  }

}

void loop() {
  // put your main code here, to run repeatedly:
  //wenn ein input über den Seriellen Port kommt

  while (Serial.available() > 0) {
    String in = Serial.readString();

    if (in == "open") {
      schranke(1);
    }
    if (in == "close") {
      schranke(2);
    }
    if (in == "print0") {
      printLcd(0);
    }
    if (in == "print1") {
      printLcd(1);
    }
    if (in == "Status") {
      setzteStatus();
    }
  }
  
  if (distanceSensor.measureDistanceCm() < 4) {
    schranke(2);
    // Serial.println("_____________________________________________");
  }

  if ( (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial())) {

    if (pruefeKey(mfrc522.uid.uidByte) == true) {
      Serial.write("access");
      schranke(1);
    }

  }
  mfrc522.PICC_HaltA();
}
