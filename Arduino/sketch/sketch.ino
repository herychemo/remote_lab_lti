
const int LED1 = 13;
const int LED2 = 12;
const int LED3 = 11;
const int LED4 = 10;
boolean stringComplete = false;
String inString;

void setup() {
  // put your setup code here, to run once:
  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);
  pinMode(LED4, OUTPUT);
  Serial.begin(9600);
  inString.reserve(200);
}
void loop() {
  // put your main code here, to run repeatedly:
  if (stringComplete) {
    //inString = Serial.readString();
    if (inString.equals("toggle0")) 
      toggle_n_print_status(  LED1  );
    else if (inString.equals("toggle1")) 
      toggle_n_print_status(  LED2  );
    else if (inString.equals("toggle2")) 
      toggle_n_print_status(  LED3  );
    else if (inString.equals("toggle3"))
      toggle_n_print_status(  LED4  );
    else if (inString.equals("reset")) {
      digitalWrite(LED1, LOW);
      digitalWrite(LED2, LOW);
      digitalWrite(LED3, LOW);
      digitalWrite(LED4, LOW);
      Serial.println("1");
    }else if( inString.equals("query-leds") ){
      String output = ""
        + String( digitalRead( LED1 ) )
        + String( digitalRead( LED2 ) )
        + String( digitalRead( LED3 ) )
        + String( digitalRead( LED4 ) )
      ;
      Serial.println( output );
    }
    inString = "";
    stringComplete = false;
  }
  delay(10);
}
void toggle_n_print_status(int pin){
  int nStatus = !digitalRead(pin);
  digitalWrite(pin, nStatus );
  Serial.println( String( nStatus ) );
}
void serialEvent(){
  while(Serial.available()){
    char inChar = (char)Serial.read();
    if (inChar == '\r') continue;
    if(inChar == '\n'){
      stringComplete = true;
    } else {
      inString += inChar;
    }
  }
}

