/* 
 *  Coder - SatyaJit Pradhan
 *  Website - http://SatyaJiit.XyZ
 *  Source will be Available on GitHub : http://GitHub.com/SatyaJiit
 *  This program lets you to control a LED on pin 13 of arduino using a bluetooth module and show msg on the LED
 *  VERSION : 1.0.0
 */

#include<LiquidCrystal.h>


LiquidCrystal lcd(12,11,5,4,3,2);
 
String data = "NO MESSAGES YET!";  
String fetch="";    //Variable for storing received data
int flag=0;
void setup() 
{
  Serial.begin(9600);         //Sets the data rate in bits per second (baud) for serial data transmission
  pinMode(13, OUTPUT);    //Sets digital pin 13 as output pin
  lcd.begin(16,2);
  lcd.clear();



lcd.blink();
  

if(flag==0){
   lcd.clear();
  lcd.print("A PROJECT ON : ");
  lcd.setCursor(0,1);
  lcd.print("HOME AUTOMATION");
 delay(2000);
  lcd.clear();
  lcd.print("By : SatyaJit");
    lcd.clear();
  lcd.print("By : SatyaJit &");
    lcd.setCursor(0,1);
 lcd.print("Satyajit & ");
 lcd.setCursor(0,1);
  lcd.print("Satyajit & Satyajit");
  
 delay(2000);
  lcd.clear();
}
  
}
void loop()
{


lcd.blink();

lcd.clear();

//here
  
  
  if(Serial.available() > 0)  // Send data only when you receive data:
  {
        
          
          
      fetch= Serial.readString();
   
         //Read the incoming data and store it into variable data

     if(fetch!="OFF"&&fetch!="ON"){
    
    data=fetch;

     }
    
    
    
    Serial.print(fetch);        //Print Value inside data in Serial monitor
    Serial.print("\n");        //New line 
    if(fetch == "ON"){            //
      digitalWrite(13, LOW);
      Serial.print("Switched ON"); 
      Serial.print("\n");  
      lcd.print("DEVICE ON");
       delay(1000);
      lcd.clear();
    }
    //If value is 1 then LED turns ON
    else if(fetch == "OFF")       //
      {digitalWrite(13, HIGH);
      Serial.print("Switched LOW"); 
       lcd.print("DEVICE OFF!");
        delay(1000);
      lcd.clear();
      }//

    
  }


if(data.length()>16){ //IF STRING IS TOOOOOO LONG...Apply Some Logic

      String s1=data.substring(0,16);
      String s2=data.substring(16);
      lcd.print(s1);
      lcd.setCursor(0,1);
      lcd.print(s2);
    }

else {}
lcd.print(data);

  
  
  
  delay(1000);



                              
 
}                 
