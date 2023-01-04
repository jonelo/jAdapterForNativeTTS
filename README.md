# jAdapterForNativeTTS

## What the project is for
It is a simple Java library with a Java API that allows you to use the native Text To Speech features provided by your operating system.
Microsoft Windows, GNU/Linux, and macOS are supported.


## What are the system requirements
* Java 11 or later
* PowerShell on Microsoft Windows 10, or 11
* spd-say on GNU/Linux
* say on macOS 10.13, 10.14, 10.15, 11, 12, or 13
* installed voices (see also "How to add more voices")

## How to use it
### Code Example
In your Java code, simply obtain a speechEngine object (actually you don't have to care about what OS you are using),
set the voice of your choice either directly or by providing voice preferences and let the engine say some words. This is a short code snippet in order to demonstrate some API features:

```java
    String text = "The answer to the ultimate question of life, the universe, and everything is 42";
    try {
        SpeechEngine speechEngine = SpeechEngineNative.getInstance();
        List<Voice> voices = speechEngine.getAvailableVoices();

        System.out.println("For now the following voices are supported:\n");
        for (Voice voice : voices) {
            System.out.printf("%s%n", voice);
        }

        // We want to find a voice according our preferences
        VoicePreferences voicePreferences = new VoicePreferences();
        voicePreferences.setLanguage("en"); //  ISO-639-1
        voicePreferences.setCountry("GB"); // ISO 3166-1 Alpha-2 code
        voicePreferences.setGender(VoicePreferences.Gender.FEMALE);
        Voice voice = speechEngine.findVoiceByPreferences(voicePreferences);

        // simple fallback just in case our preferences didn't match any voice
        if (voice == null) {
            System.out.printf("Warning: Voice has not been found by the voice preferences %s%n", voicePreferences);
            voice = voices.get(0); // it is guaranteed that the speechEngine supports at least one voice
            System.out.printf("Using \"%s\" instead.%n", voice);
        }

        speechEngine.setVoice(voice.getName());
        speechEngine.say(text);

    } catch (SpeechEngineCreationException | IOException e) {
        System.err.println(e.getMessage());
    }
```
### Maven

Please stay tuned for an artifact on Maven Central.

### CLI with TUI
For demonstration purposes, you can also call the Text User Interface (see also Main.java):

```
> java -jar jadapter-for-native-tts-0.12.0.jar "The answer to the ultimate question of life, the universe, and everything is 42"
0: name='Microsoft Hedda Desktop', culture='de-DE', gender='Female', age='Adult', description='Microsoft Hedda Desktop (de_DE, Female)'
1: name='Microsoft Hedda', culture='de-DE', gender='Female', age='Adult', description='Microsoft Hedda (de_DE, Female)'
2: name='Microsoft Katja', culture='de-DE', gender='Female', age='Adult', description='Microsoft Katja (de_DE, Female)'
3: name='Microsoft Stefan', culture='de-DE', gender='Male', age='Adult', description='Microsoft Stefan (de_DE, Male)'
4: name='Microsoft George', culture='en-GB', gender='Male', age='Adult', description='Microsoft George (en_GB, Male)'
5: name='Microsoft Hazel', culture='en-GB', gender='Female', age='Adult', description='Microsoft Hazel (en_GB, Female)'
6: name='Microsoft Susan', culture='en-GB', gender='Female', age='Adult', description='Microsoft Susan (en_GB, Female)'
7: name='Microsoft David', culture='en-US', gender='Male', age='Adult', description='Microsoft David (en_US, Male)'
8: name='Microsoft Mark', culture='en-US', gender='Male', age='Adult', description='Microsoft Mark (en_US, Male)'
9: name='Microsoft Zira', culture='en-US', gender='Female', age='Adult', description='Microsoft Zira (en_US, Female)'
10: name='Microsoft Helena', culture='es-ES', gender='Female', age='Adult', description='Microsoft Helena (es_ES, Female)'
11: name='Microsoft Laura', culture='es-ES', gender='Female', age='Adult', description='Microsoft Laura (es_ES, Female)'
12: name='Microsoft Pablo', culture='es-ES', gender='Male', age='Adult', description='Microsoft Pablo (es_ES, Male)'
13: name='Microsoft Hortense', culture='fr-FR', gender='Female', age='Adult', description='Microsoft Hortense (fr_FR, Female)'
14: name='Microsoft Julie', culture='fr-FR', gender='Female', age='Adult', description='Microsoft Julie (fr_FR, Female)'
15: name='Microsoft Paul', culture='fr-FR', gender='Male', age='Adult', description='Microsoft Paul (fr_FR, Male)'
16: name='Microsoft Cosimo', culture='it-IT', gender='Male', age='Adult', description='Microsoft Cosimo (it_IT, Male)'
17: name='Microsoft Elsa', culture='it-IT', gender='Female', age='Adult', description='Microsoft Elsa (it_IT, Female)'
18: name='Microsoft Bengt', culture='sv-SE', gender='Male', age='Adult', description='Microsoft Bengt (sv_SE, Male)'
19: name='Microsoft Karsten', culture='de-CH', gender='Male', age='Adult', description='Microsoft Karsten (de_CH, Male)'
20: name='Microsoft Hazel Desktop', culture='en-GB', gender='Female', age='Adult', description='Microsoft Hazel Desktop (en_GB, Female)'
21: name='Microsoft David Desktop', culture='en-US', gender='Male', age='Adult', description='Microsoft David Desktop (en_US, Male)'
22: name='Microsoft Zira Desktop', culture='en-US', gender='Female', age='Adult', description='Microsoft Zira Desktop (en_US, Female)'
23: name='Microsoft Helena Desktop', culture='es-ES', gender='Female', age='Adult', description='Microsoft Helena Desktop (es_ES, Female)'
24: name='Microsoft Hortense Desktop', culture='fr-FR', gender='Female', age='Adult', description='Microsoft Hortense Desktop (fr_FR, Female)'
25: name='Microsoft Elsa Desktop', culture='it-IT', gender='Female', age='Adult', description='Microsoft Elsa Desktop (it_IT, Female)'
Enter the voice id (1-25) or hit enter to specify voice preferences: 
Language: en
Country: GB
Gender: female
You have selected the following voice preferences:
language='en', country='GB', gender='FEMALE', age=''
I have found the following voice matching your voice preferences:
name='Microsoft Hazel', culture='en-GB', gender='Female', age='Adult', description='Microsoft Hazel (en_GB, Female)'
```

## How to add more voices
### Windows

see also https://support.microsoft.com/en-us/topic/how-to-download-text-to-speech-languages-for-windows-10-d5a6b612-b3ae-423f-afa5-4f6caf1ec5d3
and https://github.com/jonelo/unlock-win-tts-voices

### macOS
see also https://support.apple.com/de-de/guide/mac-help/mchlp2290/mac

## The license

The license that the project is offered under is the [MIT license](https://choosealicense.com/licenses/mit/).

