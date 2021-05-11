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
