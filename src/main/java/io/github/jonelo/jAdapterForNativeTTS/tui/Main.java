/*
 * MIT License
 *
 * Copyright (c) 2021 Johann N. Löfflmann
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.jonelo.jAdapterForNativeTTS.tui;

import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngine;
import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineNative;
import io.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import io.github.jonelo.jAdapterForNativeTTS.engines.VoicePreferences;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * A simple text user interface in order to demonstrate the jAdapterForNativeTTS.
 */
public class Main {

    private SpeechEngine speechEngine;

    /**
     * Prints the usage to the standard output.
     */
    public static void usage() {
        System.out.println("java -jar jadapter-for-native-tts-0.12.0.jar [word]...");
    }


    private void printVoices(List<Voice> voices) {
        // print all voices
        int id = 0;
        for (Voice voice : voices) {
            System.out.printf("%d: %s%n", id++, voice);
        }
    }

    private int readRateFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Rate [-100..100]: ");
        String rate = scanner.nextLine();
        if (rate.strip().length() == 0) {
            return 0;
        }
        try {
            return Integer.parseInt(rate);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            return 0;
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
        } else if (gender.equalsIgnoreCase("male")) {
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
     * @throws IOException          in case of an I/O error
     * @throws InterruptedException in case of an interrupt
     * @throws ParseException       in case of an parse error
     */
    private Voice selectVoice(SpeechEngine speechEngine) throws InterruptedException, IOException {

        List<Voice> voices = speechEngine.getAvailableVoices();
        printVoices(voices);

        int id;
        // select a voice
        do {
            System.out.printf("Enter the voice id (0-%d) or hit enter to specify voice preferences: ", voices.size() - 1);
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

    private void sayText(Voice voice, String text) {
        try {
            speechEngine.setVoice(voice.getName());
            speechEngine.setRate(readRateFromUser());
            System.out.printf("Playing the following text: \"%s\"%n", text);
            speechEngine.say(text);
            // Thread.sleep(1000);
            // speechEngine.stopTalking();
        } catch (IOException ex) {
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
            try {
                speechEngine = SpeechEngineNative.getInstance();

                // the speechEngine has been created with at least one voice,
                // but if there were parsing errors, we should need to know.
                // Usually that shouldn't happen if you use the latest and greatest release.
                if (speechEngine.getParseExceptions().size() > 0) {
                    System.err.printf("Warning: the SpeechEngine supports %d voice(s), but there were %d unexpected parsing errors.%n",
                            speechEngine.getAvailableVoices().size(), speechEngine.getParseExceptions().size());
                    int errorId = 0;
                    for (ParseException parseException : speechEngine.getParseExceptions()) {
                        System.err.printf("    Parsing Error (%d): %s%n", errorId++, parseException.getMessage());
                    }
                    System.err.printf("    Please report the above errors at https://github.com/jonelo/jAdapterForNativeTTS/issues%n%n");
                }

                Voice voice = selectVoice(speechEngine);
                sayText(voice, concatenateArgs(args));
            } catch (SpeechEngineCreationException | IOException | InterruptedException e) {
                System.err.printf("Error: %s%n", e.getMessage());
            }
        } else {
            usage();
        }
    }

    public static void main(String[] args) {
        Main main = new Main(args);
    }
}
