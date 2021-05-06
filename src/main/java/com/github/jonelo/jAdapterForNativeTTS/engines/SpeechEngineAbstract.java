package com.github.jonelo.jAdapterForNativeTTS.engines;

import com.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;
import com.github.jonelo.jAdapterForNativeTTS.util.os.ProcessHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SpeechEngineAbstract implements SpeechEngine {

    protected String voice;
    protected Process process;
    protected List<Voice> availableVoices;

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public SpeechEngineAbstract() {
        try {
            findAvailableVoices();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        this.voice = null;
    }


    public SpeechEngineAbstract(String voice) {
        this();
        this.voice = voice;
    }

    public List<Voice> getAvailableVoices() {
        return availableVoices;
    }

    public void findAvailableVoices() throws IOException, InterruptedException, ParseException {
        ArrayList<String> list = ProcessHelper.startApplicationAndGetOutput(getSayExecutable(), getSayOptionsToGetSupportedVoices());
        ArrayList<Voice> voices = new ArrayList<>();
        for (String line : list) {
            Voice v = parse(line);
            if (v != null) {
                voices.add(v);
            }
        }
        availableVoices = voices;
    }

    /**
     * Find a Voice instance by providing voice preferences.
     * @param voicePreferences the preferences for a voice
     * @return an instance of the voice that matches the voicePreferences
     */
    public Voice findVoiceByPreferences(VoicePreferences voicePreferences) {
        for (Voice voice : availableVoices) {
            if (voice.matches(voicePreferences)) return voice;
        }
        return null;
    }

    public void say(String text) throws IOException {
        if (voice != null) {
            process = ProcessHelper.startApplication(getSayExecutable(), getSayOptionsToSayText(text));
        }
    }

    /**
     * Stop talking the engine
     * Actually it kills the say-process
     */
    public void stopTalking() {
        if (process != null) {
            process.destroy();
        }
    }

}
