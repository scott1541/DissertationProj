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

      

}

void loop(){
  
  readFrom( DATAX0, BytesToRead, _buff);
  
    int x = (((int)_buff[1]) << 8) | _buff[0];   
  int y = (((int)_buff[3]) << 8) | _buff[2];
  int z = (((int)_buff[5]) << 8) | _buff[4];

  //x = x - 27;
  //y = y - 8;
  //z = z + 35;
  
  Serial.print("x: ");
  Serial.print( x );
  Serial.print(" y: ");
  Serial.print( y );
  Serial.print(" z: ");
  Serial.println( z );

 int res = x + y + z;

    File dataFile = SD.open("datalog.txt", FILE_WRITE);
    if (dataFile){
      dataFile.println(res);
      dataFile.close();
      Serial.println("Data written to card.");
    }
    else{
      Serial.println("Error writing to card!");
    }
  
  
  delay(100);
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
