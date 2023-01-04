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

import java.io.IOException;
import java.util.List;

public interface SpeechEngine {

    // config
    String getSayExecutable();

    String[] getSayOptionsToSayText(String text);

    String[] getSayOptionsToGetSupportedVoices();

    Voice parse(String line) throws ParseException;

    void findAvailableVoices() throws IOException, InterruptedException;

    List<Voice> getAvailableVoices();

    List<ParseException> getParseExceptions();

    Voice findVoiceByPreferences(VoicePreferences voicePreferences);

    void setRate(int rate) throws IllegalArgumentException;

    // actions
    void setVoice(String voice);

    Process say(String text) throws IOException;

    void stopTalking();

}
