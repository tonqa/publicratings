#include <ESP8266WiFi.h>
#include <ESP8266WiFiAP.h>
#include <ESP8266WiFiGeneric.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266WiFiScan.h>
#include <ESP8266WiFiSTA.h>
#include <ESP8266WiFiType.h>
#include <WiFiClient.h>
#include <WiFiClientSecure.h>
#include <WiFiServer.h>
#include <WiFiUdp.h>
#include <string.h>
#include <ESP8266HTTPClient.h>

#define USE_SERIAL Serial

// GLOBAL VARIABLES

// Zugangsdaten für WLAN
const char* ssid = "Private";
const char* password = "capgemini";

WiFiServer server(80);
WiFiClient client;

String serverpublic = "172.20.10.9/api";

const int ledPin = BUILTIN_LED;
const int switchPin1 = 5;
const int switchPin2 = 4;

int buttonState1 = 0;
int buttonState2 = 0;

// Deklaration der startWiFi Methode
void startWiFi();

void setup() {
  Serial.begin(115200);
  delay(10);

  WiFi.mode(WIFI_STA);
  startWiFi();
  
  pinMode(ledPin, OUTPUT);
  pinMode(switchPin1, INPUT);
  pinMode(switchPin2, INPUT);
}

void loop(){

  if (WiFi.status() != WL_CONNECTED) {
    startWiFi();
  }

   buttonState1 = digitalRead(switchPin1);
   buttonState2 = digitalRead(switchPin2);

  if (buttonState1 == HIGH){
    digitalWrite(ledPin, LOW);
    Serial.println("Schalter 1");
    
    postData(1);
   delay(2000);
  }
  else if (buttonState2 == HIGH){
    digitalWrite(ledPin, LOW);
    Serial.println("Schalter 2");
    
    postData(0);
   delay(2000);
  }else{
    digitalWrite(ledPin, HIGH);
  }

  
}

void startWiFi() {
  Serial.println();
  Serial.println();
  Serial.println("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println();
  }

  Serial.println("");
  Serial.println("WiFi connected");

  server.begin();
  Serial.println("Server started");
  Serial.println(WiFi.localIP());
}

void postData(int val) {
  

        HTTPClient http;

        USE_SERIAL.print("[HTTP] begin...\n");
        
        // SERVICE URL
        String address = "http://";
        address += serverpublic;
        address += "/clicks";
        http.begin(address); //HTTP

        String body = "{\"place\": \"/places/1\",\"type\": \"";
        String bodys = body + val + "\",\"timestamp\":\"2016-12-05 15:23:10\"}";

        USE_SERIAL.print("[HTTP] POST...\n");
        // start connection and send HTTP header
        http.addHeader("Content-Type", "application/json");
        int httpCode = http.POST(bodys);

        // httpCode will be negative on error
        if(httpCode >= 0) {
            // HTTP header has been send and Server response header has been handled
            USE_SERIAL.printf("[HTTP] GET... code: %d\n", httpCode);

            http.end();
      }
}

