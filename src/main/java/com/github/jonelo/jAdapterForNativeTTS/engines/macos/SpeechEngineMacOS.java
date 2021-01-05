package com.github.jonelo.jAdapterForNativeTTS.engines.macos;

import com.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineAbstract;
import com.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;

public class SpeechEngineMacOS extends SpeechEngineAbstract {

    // macOS' say doesn't tell us the gender, so we have to find it out by ourselves
    private final static String maleNames =
            "Alex,Daniel,Diego,Fred,Jorge,Juan,Luca,Maged,Markus,Thomas,Tom,Xander,Yannick,Yuri";

    public String getSayExecutable() {
        return "say";
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-v", "?"};
    }

    public String[] getSayOptionsToSayText(String text) {
        return new String[]{"-v", voice, text};
    }

    public Voice parse(String line) throws ParseException {
        String[] tokensAll = line.split("#");
        String[] tokens = tokensAll[0].split(" +");

        if (tokens.length != 2) {
            throw new ParseException("This is an unexpected line: " + line);
        }

        Voice voice = new Voice();
        voice.setName(tokens[0]);
        voice.setCulture(tokens[1]);
        String gender = getGender(tokens[0]);
        voice.setGender(gender);
        voice.setAge("?");
        voice.setDescription(String.format("%s (%s, %s)", tokens[0], tokens[1], gender));
        return voice;
    }

    private String getGender(String name) {
        if (maleNames.contains(name)) return "male";
        return "female";
    }
}
