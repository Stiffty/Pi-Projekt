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

//list<byte[]> cardUIDs;

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

void printLcd(int line) {
  lcd.setCursor(0, line);
  lcd.print("                     ");
  lcd.setCursor(0, line);

  while (Serial.available() == 0);
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
    if (in == "print0") {
      printLcd(0);
    }
    if (in == "print1") {
      printLcd(1);
    }
  }
  digitalWrite(ledConnector2, HIGH);

  if (distanceSensor.measureDistanceCm() < 4) {
    schranke(2);
    // Serial.println("_____________________________________________");
  }

  if ( (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial())) {
    schranke(1);
    Serial.write("rifd");
    for(int i = 0; i<4; i++){
     // byte b = Serial.read();
     while(Serial.available() == 0);
      Serial.print("HI");
      delay(90);
     
    }
    mfrc522.PICC_HaltA();
  }
  // Serial.println(distanceSensor.measureDistanceCm());

  //  lcd.setCursor(0, 0);//Hier wird die Position des ersten Zeichens festgelegt. In diesem Fall bedeutet (0,0) das erste Zeichen in der ersten Zeile.
  //  lcd.print("Freie Plaetze:");
  //  lcd.setCursor(0, 1);// In diesem Fall bedeutet (0,1) das erste Zeichen in der zweiten Zeile.
  //  lcd.print("   400");
}
