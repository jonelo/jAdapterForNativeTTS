package com.github.jonelo.jAdapterForNativeTTS.tui;

import com.github.jonelo.jAdapterForNativeTTS.engines.*;
import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.NotSupportedOperatingSystemException;
import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * A simple text user interface in order to demonstrate the jAdapterForNativeTTS.
 */
public class Main {

    /**
     * Prints the usage to the standard output.
     */
    public static void usage() {
        System.out.println("java -jar jAdapterForNativeTTS-*.jar [word]...");
    }


    private void printVoices(List<Voice> voices) {
        // print all voices
        int id = 0;
        for (Voice voice : voices) {
            System.out.printf("%d: %s%n", id++, voice);
        }
    }

    private VoicePreferences readVoicePreferences() {
        Scanner scanner = new Scanner(System.in);
        VoicePreferences voicePreferences = new VoicePreferences();

        System.out.print("Language: ");
        String language = scanner.nextLine();
        if (language.length() > 0) {
            voicePreferences.setLanguage(language.toLowerCase(Locale.US));
        }

        System.out.print("Country: ");
        String country = scanner.nextLine();
        if (country.length() > 0) {
            voicePreferences.setCountry(country.toUpperCase(Locale.US));
        }

        System.out.print("Gender: ");
        String gender = scanner.nextLine();
        if (gender.equalsIgnoreCase("female")) {
            voicePreferences.setGender(VoicePreferences.Gender.FEMALE);
        } else
        if (gender.equalsIgnoreCase("male")) {
            voicePreferences.setGender(VoicePreferences.Gender.MALE);
        }


        System.out.println("You have selected the following voice preferences:");
        System.out.println(voicePreferences);

        return voicePreferences;
    }

    /**
     * Let the user select a voice from a speech engine.
     *
     * @param speechEngine an instance of a SpeechEngine
     * @return the name of the selected voice
     * @throws IOException in case of an I/O error
     * @throws InterruptedException in case of an interrupt
     * @throws ParseException in case of an parse error
     */
    private Voice selectVoice(SpeechEngine speechEngine) throws InterruptedException, ParseException, IOException {

        List<Voice> voices = speechEngine.getAvailableVoices();
        printVoices(voices);

        int id;
        // select a voice
        do {
            System.out.printf("Enter the voice id (1-%d) or hit enter to specify voice preferences: ", voices.size()-1);
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.length() == 0) {
                VoicePreferences voicePreferences = readVoicePreferences();
                Voice voice = speechEngine.findVoiceByPreferences(voicePreferences);
                if (voice == null) {
                    System.out.println("I didn't find a matching voice by applying your voice preferences. Using the default one.");
                    voice = voices.get(0);
                } else {
                    System.out.println("I have found the following voice matching your voice preferences:");
                }
                System.out.println(voice);
                return voice;
            } else {
                try {
                    id = Integer.parseInt(input);
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid input. Please try again.");
                    id = -1;
                }
            }
        } while (id < 0 || id >= voices.size());
        return voices.get(id);
    }


    private String concatenateArgs(String[] args) {
        // get the text from the command line
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            text.append(args[i]);
            if (i < args.length - 1) {
                text.append(" ");
            }
        }
        return text.toString();
    }

    private void sayText(String text) {
        try {
            SpeechEngine speechEngine = SpeechEngineNative.getInstance();
            Voice voice = selectVoice(speechEngine);
            speechEngine.setVoice(voice.getName());
            speechEngine.say(text);
            //Thread.sleep(1000);
            //speechEngine.stopTalking();
        } catch (NotSupportedOperatingSystemException | IOException | InterruptedException | ParseException ex) {
            System.err.printf("Error: %s%n", ex.getMessage());
        }
    }

    /**
     * The main method.
     *
     * @param args the values passed from the command line
     */
    public Main(String[] args) {
        if (args.length > 0) {
            sayText(concatenateArgs(args));
        } else {
            usage();
        }
    }

    public static void main(String[] args) {
        Main main = new Main(args);
    }
}
