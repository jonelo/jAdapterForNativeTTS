jAdapterForNativeTTS
====================

What the project is for
-----------------------
It is a simple Java library that allows you to call the native Text To Speech features provided by your operating system.
Microsoft Windows, GNU/Linux, and macOS are supported.


What are the system requirements
--------------------------------
* Java 11 or later
* Powershell on Windows 10
* spd-say on GNU/Linux
* say on macOS


How to use it
-------------
In your Java code, simply obtain a speechEngine object (actually you don't have to care about what OS you are using),
set the voice of your choice and let the engine say some words:
```
    SpeechEngine speechEngine = SpeechEngineNative.getInstance();
    String name = selectVoice(speechEngine);
    speechEngine.setVoice(name);
    speechEngine.say(text);
```

Please stay tuned for Maven.

For demonstration purposes, you can also call the Text User Interface (see also com.github.jonelo.jAdapterForNativeTTS.tui.Main.java):

```
java -jar jAdapterForNativeTTS.jar "Schön, daß Du das ausprobierst!" bla bla 123
0: name='Microsoft Hedda Desktop', culture='de-DE', gender='Female', age='Adult', description='Microsoft Hedda Desktop (de_DE, Female)'
1: name='Microsoft Zira Desktop', culture='en-US', gender='Female', age='Adult', description='Microsoft Zira Desktop (en_US, Female)'
Enter the voice id: 0    
```

The license
-----------
The license that the project is offered under is the [MIT license](https://choosealicense.com/licenses/mit/).

