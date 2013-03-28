AndroidRCCar
============

Remote control of a model vehicle by using two Android-powered mobile phones

More Info:
http://androidrccar.sven.to/

Short overview about the directories
-------
- Android Software
	- (Each directory is an Eclipse Project)
	- [AndroidRCCar.Client/](AndroidRCCar.Client/)
		- Client Software -- The "Remote Controller"
	- [AndroidRCCar.Host/](AndroidRCCar.Host/)
		- Host Software for the device on the car
	- [AndroidRCCar.Common/](AndroidRCCar.Common/)
		- Shared Library for Host and Client
	- [AndroidRCCar.Common.Test/](AndroidRCCar.Common.Test/), ...
	- [AndroidRCCar.Host.Test](/AndroidRCCar.Host.Test/), ...
	- [AndroidRCCar.Client.Test/](AndroidRCCar.Client.Test/), ...
	- and [AndroidRCCar.Test/](AndroidRCCar.Test/)
		- Unit Tests
	- [AndroidRCCar.Arduino.Test/](AndroidRCCar.Arduino.Test/)
		- For testing the Microcontroller Software
	- [RtspCamera/](RtspCamera/)
		- Video-Streaming Library
		- See https://github.com/spex66/RTSP-Camera-for-Android
	- [_APK/](_APK/)
		- Binaries
- Microcontroller Software
	- [AndroidRCCar.Arduino/AndriodRCCarSketch/](AndroidRCCar.Arduino/AndriodRCCarSketch/)
		- The Arduino Sketch -- "The main function"
	- [AndroidRCCar.Arduino/AndriodRCCar/](AndroidRCCar.Arduino/AndriodRCCar/)
		- Implements the communication protocol between µController and car
		- Library (Link or copy to library/ in your Android IDE installation)
	- [AndroidRCCar.Arduino/AndroidRCReferenceCar/](AndroidRCCar.Arduino/AndroidRCReferenceCar/)
		- Specific implementation for the car shown in the picture
		- Extends AndroidRCCarCommunication from AndriodRCCar/
		- Library (Link or copy to library/ in your Android IDE installation)
	- [ADKArduinoLibs/](ADKArduinoLibs/)
		- Fixed ADK-Libraries
		- From: http://developer.android.com/tools/adk/adk.html
		- (Link or copy to library/ in your Android IDE installation)
	- See also: [Class Diagram](/_SoftwareDesign/AndroidRCCarArduinoClass.pdf)

Getting Started
-------

##### You need:
- An Arduino ADK board
	- like this: http://arduino.cc/en/Main/ArduinoBoardADK
- A motor driver
	- like this: http://www.adafruit.com/products/81
- Some robotic platform and motors
	- like this: http://www.dfrobot.com/index.php?route=product/product&product_id=97
- Two Android devices
	- Android 2.3.3 or newer
	- The host needs ADK support
	
##### Microcontroller Software:
- Link or copy these Libraries to library/ in your Android IDE installation:
	- [AndroidRCCar.Arduino/AndriodRCCar/](AndroidRCCar.Arduino/AndriodRCCar/)
	- [ADKArduinoLibs/](ADKArduinoLibs/)
	- ( [AndroidRCCar.Arduino/AndroidRCReferenceCar/](AndroidRCCar.Arduino/AndroidRCReferenceCar/) )
- Implement the abstract class AndroidRCCarCommunication
	- See [AndroidRCCar.Arduino/AndroidRCReferenceCar/](AndroidRCCar.Arduino/AndroidRCReferenceCar/) for an example implementation
	- And [AndroidRCCar.Arduino/AndriodRCCarSketch/](AndroidRCCar.Arduino/AndriodRCCarSketch/) for an example sketch
- Compile and Upload
	- You can test your software with the [AndroidRCCar.Arduino.Test](_APK/AndroidRCCar.Arduino.Test.apk) Android Application
		- Note: The test suite is optimized for the reference car, so if you have other features configured the command GET_FEATURES will fail as example
		- The test suite is located here [AndroidRCCar.Arduino.Test/res/raw/test_suite](AndroidRCCar.Arduino.Test/res/raw/test_suite)
- The protocol is documented here:
	- [RequestCommands](AndroidRCCar.Host/src/to/sven/androidrccar/host/accessorycommunication/model/RequestCommand.java)
	- [ResponseMessages](AndroidRCCar.Host/src/to/sven/androidrccar/host/accessorycommunication/model/ResponseMessage.java)
	
##### Android Software:
- Note: There are precompiled binaries in [_APK/](_APK/) 
- Import the Eclipse projectes:
	- [AndroidRCCar.Client/][AndroidRCCar.Client/]
	- [AndroidRCCar.Host/](AndroidRCCar.Host/)
	- [AndroidRCCar.Common/](AndroidRCCar.Common/)
	- [RtspCamera/](RtspCamera/)
	- and the *.Test projects (optional)
- See source code documentation for further information
- Have fun!

System Design
-------

System Design:

![System Design](http://androidrccar.sven.to/downloads/system.png)

Android-App Design:

![Android App Design](http://androidrccar.sven.to/downloads/architecture.png)


For mor diagrams see: [/_SoftwareDesign/](/_SoftwareDesign/)

Live
-------

![Screenshot 1](http://androidrccar.sven.to/style/images/top.jpg) ![Screenshot 2](http://androidrccar.sven.to/style/images/androidrccar.jpg)

![Screenshot 3](http://androidrccar.sven.to/style/images/ggle.jpg)

Video
-------

http://www.youtube.com/watch?v=mPUK-vqFtEA