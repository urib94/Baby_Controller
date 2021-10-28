//
// Created by Uri on 10/26/2021.
//
#include <HX711_ADC.h>
#include<BLEDevice.h>
#include<BLEServer.h>
#include<BLEUtils.h>
#include<BLE2902.h>
#include <WiFi.h>
#include "Arduino.h"
#include "BluetoothSerial.h"
#include <LiquidCrystal.h>

BluetoothSerial SerialBT;

HX711_ADC Loadcell(0,2);
LiquidCrystal lcd(4, 16, 17, 5, 18, 19); // Creates an LCD object. Parameters: (rs, enable, d4, d5, d6, d7)
const int GreenPin = 12;
const int RedPin = 14;


void setup() {

    Serial.begin(9600);
    SerialBT.begin("baby-controller");
    lcd.begin(16,2);
    pinMode(GreenPin, OUTPUT);
    pinMode(RedPin, OUTPUT);
    Serial.begin(9600); delay(2);
    Loadcell.begin();
    long stabilisingtime = 100;
    Loadcell.start(stabilisingtime);
    if (Loadcell.getTareTimeoutFlag())
    {
        Serial.println("Tare timeout, check MCU>HX711 wiring and pin designations");
    }
    else{
      Loadcell.setCalFactor(1.0);
      Serial.println("startup + tare is complete");
    }
    Loadcell.setCalFactor(-515);
}


void loop() {
    Loadcell.update();
    float loadval = Loadcell.getData();
    lcd.setCursor(0,0);
    lcd.print("Weight: ");
    if(-loadval < 0)
    {
        lcd.print("0.00");
    }
    else
    {
        lcd.print(-loadval);
    }
    lcd.print(" g ");
    delay(100);
    Loadcell.update();
    float check = Loadcell.getData();
    if (check < loadval+0.1 && check > loadval-0.1)
    {

      digitalWrite(GreenPin, HIGH);
      digitalWrite(RedPin, LOW);
      Loadcell.update();
      loadval = Loadcell.getData();
      if(-loadval > 10)
      {
          Serial.println(-loadval);
          SerialBT.println(-loadval);
          Serial.println(-loadval);
          SerialBT.write(-loadval);
      }
      delay(10);
    }
    else
    {
      digitalWrite(GreenPin, LOW);
      digitalWrite(RedPin, HIGH);
      delay(10);
    }

    if(SerialBT.available() != 0)
    {
        lcd.setCursor(0,1);
        lcd.print("BT is connected!");
    }

    else if (SerialBT.available() == 0)
    {
        lcd.setCursor(0,1);
        lcd.print("BT NOT connected!");
    }
}
