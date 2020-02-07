int ledConnector = 2;
int ledConnector2 = 4;
int ledConnector3 = 7;
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
}

void blink() {
  if (digitalRead(ledConnector) == HIGH) {
    digitalWrite(ledConnector, LOW);
  } else {
    digitalWrite(ledConnector, HIGH);
  }
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
    if(in == "hallo"){
      digitalWrite(ledConnector3, HIGH);
    }
  }
  digitalWrite(ledConnector2, HIGH);
}
