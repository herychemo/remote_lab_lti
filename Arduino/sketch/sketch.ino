
String inputString = "";         
boolean stringComplete = false;  
void setup() {
  Serial.begin(9600);
  pinMode(13,OUTPUT);  
  inputString.reserve(200);
}
void loop() {
  if (stringComplete) {
    if( inputString .equals("ping") ){
      digitalWrite(13,  !digitalRead(13));  
      Serial.print("Ping: ");
      Serial.println(inputString);
    }else if( inputString.equals("toggle13") ){
      int state = !digitalRead(13);
      digitalWrite( 13, state );
      Serial.println(state);
    }else{}    
    inputString = "";
    stringComplete = false;
  }
  delay(10);
}
void serialEvent() {
  while (Serial.available()) {
    char inChar = (char)Serial.read();
    if (inChar == '\r')   continue;
    if (inChar == '\n')  
      stringComplete = true;
    else
      inputString += inChar;
  }
}


