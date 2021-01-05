package com.github.jonelo.jAdapterForNativeTTS.tui;

import com.github.jonelo.jAdapterForNativeTTS.engines.*;
import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.NotSupportedOperatingSystemException;
import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * A simple text user interface in order to demonstrate the jAdapterForNativeTTS.
 */
public class Main {

    /**
     * Prints the usage to the standard output.
     */
    public static void usage() {
        System.out.println("java -jar JTextToSpeech.jar [word]...");
    }


    public void printVoices(List<Voice> voices) {
        // print all voices
        int id = 0;
        for (Voice voice : voices) {
            System.out.printf("%d: %s%n", id++, voice);
        }
    }


    public int selectVoice(List<Voice> voices) {
        int id;
        // select a voice
        do {
            System.out.print("Enter the voice id: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.length() == 0) {
                id = 0;
            } else {
                try {
                    id = Integer.parseInt(input);
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid input. Please try again.");
                    id = -1;
                }
            }
        } while (id < 0 || id >= voices.size());
        return id;
    }

    /**
     * Let the user select a voice from a speech engine
     */
    public String selectVoice(SpeechEngine engine) throws InterruptedException, ParseException, IOException {
        List<Voice> voices = engine.getAvailableVoices();
        printVoices(voices);
        int id = selectVoice(voices);
        return voices.get(id).getName();
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

    public void sayText(String text) {
        try {
            SpeechEngine speechEngine = SpeechEngineNative.getInstance();
            String name = selectVoice(speechEngine);
            speechEngine.setVoice(name);
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
