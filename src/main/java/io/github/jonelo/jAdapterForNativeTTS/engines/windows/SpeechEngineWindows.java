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

package io.github.jonelo.jAdapterForNativeTTS.engines.windows;

import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineAbstract;
import io.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;

public class SpeechEngineWindows extends SpeechEngineAbstract {

    private final static String
            QUOTE = "\"",
            CODE_TOKEN_TTS_NAME = "##TTS_NAME##",
            CODE_TOKEN_RATE = "##RATE##",
            CODE_TOKEN_TEXT = "##TEXT##",
            POWER_SHELL_CODE_SAY =
                    String.join("",
                            "Add-Type -AssemblyName System.Speech;",
                            "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer;",
                            "$speak.SelectVoice('", CODE_TOKEN_TTS_NAME, "');",
                            "$speak.Rate=", CODE_TOKEN_RATE,";",
                            "$speak.Speak('", CODE_TOKEN_TEXT, "');"),

    POWER_SHELL_CODE_SUPPORTED_VOICES =
            String.join("",
                    "Add-Type -AssemblyName System.Speech;",
                    "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer;",
                    "$speak.GetInstalledVoices() | ",
                    "Select-Object -ExpandProperty VoiceInfo | ",
                    "Select-Object -Property Name, Culture, Gender, Age, Description | ",
                    "ConvertTo-Csv -NoTypeInformation | ",
                    "Select-Object -Skip 1;");

    public SpeechEngineWindows() throws SpeechEngineCreationException {
        super();
    }

    public String getSayExecutable() {
        return "PowerShell";
    }

    private int recalcRate(int rate) {
        return (int)Math.round(rate/10.0);
    }

    public String[] getSayOptionsToSayText(String text) {
        String escapedText = text.replaceAll("'", "''''");
        String code = POWER_SHELL_CODE_SAY
                .replaceAll(CODE_TOKEN_TTS_NAME, voice)
                .replaceAll(CODE_TOKEN_RATE, Integer.toString(recalcRate(rate)))
                .replaceAll(CODE_TOKEN_TEXT, escapedText);
        //System.out.println(code);

        return new String[]{"-Command", String.join("", QUOTE, code, QUOTE)};
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-Command", String.join("", QUOTE, POWER_SHELL_CODE_SUPPORTED_VOICES, QUOTE)};
    }

    private String trimQuotes(String line) {
        return line.replaceAll("^\"|\"$", "");
    }

    public Voice parse(String csvLine) throws ParseException {
        String[] tokens = csvLine.split(",");

        //List<String> tokens = CSVParser.parseLine(csvLine);
        if (tokens.length != 5) {
            throw new ParseException(String.format("Invalid csv line: %s", csvLine));
        }

        Voice voice = new Voice();
        voice.setName(trimQuotes(tokens[0]));
        voice.setCulture(trimQuotes(tokens[1]));
        voice.setGender(trimQuotes(tokens[2]));
        voice.setAge(trimQuotes(tokens[3]));
        voice.setDescription(String.format("%s (%s, %s)", voice.getName(), voice.getCulture().replace('-','_'), voice.getGender()));
        return voice;
    }

}
