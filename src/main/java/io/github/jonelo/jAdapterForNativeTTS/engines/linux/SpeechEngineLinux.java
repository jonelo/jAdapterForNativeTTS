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

package io.github.jonelo.jAdapterForNativeTTS.engines.linux;

import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineAbstract;
import io.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;

public class SpeechEngineLinux extends SpeechEngineAbstract {

    public SpeechEngineLinux() throws SpeechEngineCreationException {
        super();
    }

    public String getSayExecutable() {
        return "spd-say";
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-L"};
    }

    public String[] getSayOptionsToSayText(String text) {
        return new String[]{"-l", voice, "-r", String.valueOf(rate), text};
    }

    // returns null if the header is found
    public Voice parse(String line) throws ParseException {
        String[] tokens = line.trim().split("  +");

        if (tokens.length != 3) {
            throw new ParseException(String.format("Unexpected line from %s: %s", getSayExecutable(), line));
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
