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

package io.github.jonelo.jAdapterForNativeTTS.engines.macos;

import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineAbstract;
import io.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.ParseException;

public class SpeechEngineMacOS extends SpeechEngineAbstract {

    // macOS' say doesn't tell us the gender, so we have to find it out by ourselves
    private final static String maleNames =
            "Alex,Bruce,Carlos,Cem,Daniel,Diego,Felipe,Fred,Henrik,Jorge,Juan,Junior,Juri,Lee,Luca,Maged,Magnus,Markus,Neel,Nicolas,Nicos,Oliver,Oskar,Otoya,Ralph,Tarik,Thomas,Tom,Xander,Yannick,Yuri";

    private final static String femaleNames =
            "Alva,Agnes,Alice,Allison,Andrea,Angelica,Anna,Amelie,Aurelie,Ava,Catarina,Carmit,Chantal,Claire,Damayanti,Ellen,Ewa,Fiona,Frederica,Ioana,Iveta,Joana,Kanya,Karen,Kate,Kathy,Katja,Klara,Kyoko,Laila,Laura,Lekha,Luciana,Mariska,Milena,Mei-Jia,Melina,Moira,Monica,Nora,Paola,Paulina,Petra,Princess,Samantha,Sara,Satu,Serena,Sin-ji,Soledad,Susan,Tessa,Ting-Ting,Veena,Vicki,Victoria,Yelda,Yuna,Zosia,Zuzana";

    public String getSayExecutable() {
        return "say";
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-v", "?"};
    }

    private String formatRate() {
        // we do not need specify the rate if rate has been set to zero.
        if (rate==0) return "";
        // We have a rage of [-100..100] which needs to be mapped to [50..310],
        // This is (310-50)/200=1,3 for each step, and the mid (default rate) is 180
        // which leads to the formula mappedRate=rate*1,3+180
        return String.format("[[rate %s]]", (int) Math.round(rate * 1.3 + 180));
    }

    public String[] getSayOptionsToSayText(String text) {
        return new String[]{"-v", voice, formatRate()+text};
    }

    public Voice parse(String line) throws ParseException {
        String[] tokensAll = line.split("#");
        String[] tokens = tokensAll[0].split(" {2,}");

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
        if (femaleNames.contains(name)) return "female";
        if (maleNames.contains(name)) return "male";
        return "?";
    }
}
