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

import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;
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
    protected List<ParseException> parseExceptions;

    public void setVoice(String voice) {
        this.voice = voice;
    }

    /**
     * Creates a SpeechEngine for your platform.
     * After construction, it is guaranteed that at least one voice can be provided.
     * @throws SpeechEngineCreationException if the SpeechEngine cannot be created or if not even one voice can be found.
     */
    public SpeechEngineAbstract() throws SpeechEngineCreationException {
        parseExceptions = new ArrayList<>();
        try {
            findAvailableVoices();
        } catch (IOException | InterruptedException e) {
            throw new SpeechEngineCreationException(e.getMessage());
        }
        if (availableVoices.size() == 0) {
            throw new SpeechEngineCreationException(String.format("Not even one voice has been found. There were %s parse error(s).%n", getParseExceptions().size()));
        }
        this.voice = null;
        rate = 0; // normal voice speed
    }

    public SpeechEngineAbstract(String voice) throws SpeechEngineCreationException {
        this();
        this.voice = voice;
    }

    /**
     * Returns all available voices that are supported by this SpeechEngine instance.
     * @return all available voices that are supported by this SpeechEngine instance.
     */
    public List<Voice> getAvailableVoices() {
        return availableVoices;
    }

    /**
     * Returns all ParseExceptions during creation of this SpeechEngine instance.
     * @return all ParseExceptions during creation of this SpeechEngine instance.
     */
    public List<ParseException> getParseExceptions() {
        return parseExceptions;
    }

    public void findAvailableVoices() throws IOException, InterruptedException {
        ArrayList<String> list = ProcessHelper.startApplicationAndGetOutput(getSayExecutable(), getSayOptionsToGetSupportedVoices());
        ArrayList<Voice> voices = new ArrayList<>();
        for (String line : list) {
            try {
                Voice v = parse(line);
                if (v != null) {
                    voices.add(v);
                }
            } catch (ParseException e) {
                parseExceptions.add(e);
            }
        }
        availableVoices = voices;
    }

    /**
     * Find a Voice instance by providing voice preferences.
     * @param voicePreferences the preferences for a voice
     * @return an instance of the voice that matches the voicePreferences, null if voice is not found by the voicePreferences
     */
    public Voice findVoiceByPreferences(VoicePreferences voicePreferences) {
        for (Voice voice : availableVoices) {
            if (voice.matches(voicePreferences)) return voice;
        }
        return null;
    }

    public Process say(String text) throws IOException {
        if (voice != null) {
            process = ProcessHelper.startApplication(getSayExecutable(), getSayOptionsToSayText(text));
            return process;
        }
        return null;
    }

    /**
     * Stop talking the engine
     * Actually it kills the say-process
     */
    public void stopTalking() {
        if (process != null) {
            process.destroy();
            process = null;
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
