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

package io.github.jonelo.jAdapterForNativeTTS.engines;

import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;
import io.github.jonelo.jAdapterForNativeTTS.util.os.ProcessHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SpeechEngineAbstract implements SpeechEngine {

    protected String voice;
    protected Process process;
    protected List<Voice> availableVoices;
    protected int rate;

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
        rate = 0; // normal voice speed
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
            try {
                Voice v = parse(line);
                if (v != null) {
                    voices.add(v);
                }
            } catch (ParseException e) { e.printStackTrace() }
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

    /**
     * Sets the rate at which the words should be spoken.
     * @param rate Valid range is [-100..100]. -100 is to slow the voice to a maximum, and 100 is to speed up the voice to a maximum.
     * @throws IllegalArgumentException is thrown if rate is out of range
     */
    public void setRate(int rate) throws IllegalArgumentException {
        if (rate < -100 || rate > 100) throw new IllegalArgumentException("Invalid range for rate, valid range is [-100..100]");
        this.rate = rate;
    }


}
