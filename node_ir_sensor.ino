#include<ESP8266WiFi.h>
#include <Firebase.h>  
#include <FirebaseArduino.h>  
#define FIREBASE_HOST "arduino-90c09.firebaseio.com"  
#define FIREBASE_AUTH "T1pTwebJ4UW9LJbbZtwPgvDxkoRBPpD51KTJ7RaY"  
const char* ssid="Honor";
const char* pwd="1234567890";
int inputPin = 13; // choose input pin (for Infrared sensor) 
int laststate= 1;
int val = 0; // variable for reading the pin status
int count=0;
String c="";

void setup() 
{ 
   pinMode(inputPin, INPUT); // declare Infrared sensor as input
   Serial.begin(115200);
   
  WiFi.begin(ssid,pwd);
   Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop()
{ 
   val = digitalRead(inputPin); // read input value 
  if(val != laststate)
  {
     if(digitalRead(inputPin)== LOW)
     {
        
        Serial.println("LED Turned OFF");
        count=count+1;
        c=count;
         Firebase.setString("1/dripcount",c); 
        delay(30);
       }

   }
   laststate = val;
}
