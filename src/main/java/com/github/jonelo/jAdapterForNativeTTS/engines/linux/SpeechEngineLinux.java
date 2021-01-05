package com.github.jonelo.jAdapterForNativeTTS.engines.linux;

import com.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineAbstract;
import com.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;

public class SpeechEngineLinux extends SpeechEngineAbstract {


    public String getSayExecutable() {
        return "spd-say";
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-L"};
    }

    public String[] getSayOptionsToSayText(String text) {
        return new String[]{"-l", voice, text};
    }

    // returns null if the header is found
    public Voice parse(String line) throws ParseException {
        String[] tokens = line.trim().split("  +");

        if (tokens.length != 3) {
            throw new ParseException("This is an unexpected line: " + line);
        }

        // we don't want the header of the output
        if (tokens[0].equalsIgnoreCase("NAME")) {
            return null;
        }
        Voice voice = new Voice();
        voice.setName(tokens[1]);
        voice.setCulture(tokens[1]);
        voice.setGender("?");
        voice.setAge("?");
        voice.setDescription(tokens[0]);
        return voice;
    }
}
