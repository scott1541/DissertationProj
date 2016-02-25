/*
Scott Pollard
POL13390234

CMP3XXXX
Project - Feed Bruce's cats

Arduino Code - C/C++
*/

#include <SPI.h>
#include <SD.h>
#include <Wire.h>

#define DEVICE (0x53) // Device address as specified in data sheet 

byte _buff[6];

int j = 0;

const int chipSel = 4; //Chip Select (CS) Pin

uint8_t BytesToRead = 6;

char POWER_CTL = 0x2D;  //Power Control Register
char DATA_FORMAT = 0x31;
char DATAX0 = 0x32; //X-Axis Data 0
char DATAX1 = 0x33; //X-Axis Data 1
char DATAY0 = 0x34; //Y-Axis Data 0
char DATAY1 = 0x35; //Y-Axis Data 1
char DATAZ0 = 0x36; //Z-Axis Data 0
char DATAZ1 = 0x37; //Z-Axis Data 1

  int ledPin1 = 6;  //Green LED
  int ledPin2 = 7;  //Amber LED
  int ledPin3 = 8;  //Red LED

void setup(){
  Wire.begin();
  Serial.begin(9600);
  while (!Serial) { delay(100); } //Wait for Serial to start for output 
  Serial.print("Starting Serial Output...");
  
  if (!SD.begin(chipSel)) {
    Serial.print("SD Card not present or failed!");
    return;
  }
  
  Serial.print("SD Card is present.");
  
  //Put the ADXL345 into +/- 4G range by writing the value 0x01 to the DATA_FORMAT register.
  writeTo(DATA_FORMAT, 0x01);
  //Put the ADXL345 into Measurement Mode by writing 0x08 to the POWER_CTL register.
  writeTo(POWER_CTL, 0x08);

    pinMode(ledPin1, OUTPUT);
    pinMode(ledPin2, OUTPUT);
    pinMode(ledPin3, OUTPUT);
}

void loop(){
  digitalWrite(ledPin1, LOW);
  digitalWrite(ledPin2, LOW);
  digitalWrite(ledPin3, LOW);
  
  j = j + 1;
  readFrom( DATAX0, BytesToRead, _buff);
  
    int x = (((int)_buff[1]) << 8) | _buff[0];   
  int y = (((int)_buff[3]) << 8) | _buff[2];
  int z = (((int)_buff[5]) << 8) | _buff[4];

  x = x - 27;
  y = y - 8;
  z = z + 35;
  
  Serial.print("x: ");
  Serial.print( x );
  Serial.print(" y: ");
  Serial.print( y );
  Serial.print(" z: ");
  Serial.println( z );

 int res = x + y + z;
  
    if (res > 10 && x < 30) {
    digitalWrite(ledPin1, HIGH);
   }

  if (res > 30 && x < 50) {
    digitalWrite(ledPin2, HIGH);
   }

  if (res > 50 && x < 100) {
    digitalWrite(ledPin3, HIGH);
   }

     if (res > -30 && x < -10) {
    digitalWrite(ledPin1, HIGH);
   }

  if (res > -50 && x < -30) {
    digitalWrite(ledPin2, HIGH);
   }

  if (res > -100 && x < -50) {
    digitalWrite(ledPin3, HIGH);
   }

  
  if (j > 60)
  {
    Serial.print("Dumping to SD Card...");
    
    File dataFile = SD.open("datalog.txt", FILE_WRITE);
    if (dataFile){
      dataFile.println("hello");
      dataFile.close();
      Serial.println("Data written to card.");
    }
    else{
      Serial.println("Error writing to card!");
    }
    
    j = 0;
  }
  
  delay(1000);
}

void writeTo(byte address, byte val){
  Wire.beginTransmission(DEVICE); // start transmission to device 
  Wire.write(address);             // send register address
  Wire.write(val);                 // send value to write
  Wire.endTransmission();         // end transmission
}

void readFrom(byte address, int num, byte _buff[]){
 Wire.beginTransmission(DEVICE); // start transmission to device 
  Wire.write(address);             // sends address to read from
  Wire.endTransmission();         // end transmission

  Wire.beginTransmission(DEVICE); // start transmission to device
  Wire.requestFrom(DEVICE, num);    // request 6 bytes from device

  int i = 0;
  while(Wire.available())         // device may send less than requested (abnormal)
  { 
    _buff[i] = Wire.read();    // receive a byte
    i++;
  }
  Wire.endTransmission();         // end transmission
}
