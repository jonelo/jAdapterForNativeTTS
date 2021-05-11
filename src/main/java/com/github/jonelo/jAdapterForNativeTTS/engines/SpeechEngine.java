package com.github.jonelo.jAdapterForNativeTTS.engines;

import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;

import java.io.IOException;
import java.util.List;

public interface SpeechEngine {

    // config
    String getSayExecutable();

    String[] getSayOptionsToSayText(String text);

    String[] getSayOptionsToGetSupportedVoices();

    Voice parse(String line) throws ParseException;

    void findAvailableVoices() throws IOException, InterruptedException, ParseException;

    List<Voice> getAvailableVoices();

    Voice findVoiceByPreferences(VoicePreferences voicePreferences);

    void setRate(int rate) throws IllegalArgumentException;

    // actions
    void setVoice(String voice);

    void say(String text) throws IOException;

    void stopTalking();

}
