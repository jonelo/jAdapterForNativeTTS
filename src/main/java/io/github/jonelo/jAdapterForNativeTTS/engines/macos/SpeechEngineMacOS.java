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
import io.github.jonelo.jAdapterForNativeTTS.engines.exceptions.SpeechEngineCreationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechEngineMacOS extends SpeechEngineAbstract {

    // macOS' say doesn't tell us the gender, so we have to find it out by ourselves
    private final static String maleNames =
            // male names from say in macOS 13 that are not already in earlier releases
            "Albert,Eddy,Grandpa,Jester,Jacques,Majed,Reed,Rishi,Rocko,Sinji," +
            // male names from say in macOS 10.13, 10.14, 10.15, 11, and 12
            "Alex,Bruce,Carlos,Cem,Daniel,Diego,Felipe,Fred,Henrik,Jorge,Juan,Junior,Juri,Lee,Luca,Maged,Magnus,Markus,Neel,Nicolas,Nicos,Oliver,Oskar,Otoya,Ralph,Tarik,Thomas,Tom,Xander,Yannick,Yuri,";

    private final static String femaleNames =
            // female names from say in macOS 13 that are not already in earlier releases
            "Amélie,Amira,Daria,Grandma,Lana,Lesya,Linh,Tünde,Meijia,Mónica,Montse,Sandy,Shelley,Tingting," +
            // female names from say in macOS 10.13, 10.14, 10.15, 11, and 12
            "Alva,Agnes,Alice,Allison,Andrea,Angelica,Anna,Amelie,Aurelie,Ava,Catarina,Carmit,Chantal,Claire,Damayanti,Ellen,Ewa,Fiona,Frederica,Ioana,Iveta,Joana,Kanya,Karen,Kate,Kathy,Katja,Klara,Kyoko,Laila,Laura,Lekha,Luciana,Mariska,Milena,Mei-Jia,Melina,Moira,Monica,Nora,Paola,Paulina,Petra,Princess,Samantha,Sara,Satu,Serena,Sin-ji,Soledad,Susan,Tessa,Ting-Ting,Veena,Vicki,Victoria,Yelda,Yuna,Zosia,Zuzana,";

    public SpeechEngineMacOS() throws SpeechEngineCreationException {
        super();
    }

    public String getSayExecutable() {
        return "say";
    }

    public String[] getSayOptionsToGetSupportedVoices() {
        return new String[]{"-v", "?"};
    }

    private String formatRate() {
        // we do not need to specify the rate if rate has been set to zero.
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
        /*
            Example lines:
            > say -v "?" | grep it_IT
            returns on macOS 10.13, 10.14, 10.15, 11, 12:
            Alice               it_IT    # Salve, mi chiamo Alice e sono una voce italiana.
            Luca                it_IT    # Salve, mi chiamo Luca e sono una voce italiana.

            but returns on macOS 13:
            Alice (Enhanced)    it_IT    # Ciao! Mi chiamo Alice.
            Alice               it_IT    # Salve, mi chiamo Alice e sono una voce italiana.
            Eddy (Italian (Italy)) it_IT    # Ciao! Mi chiamo Eddy.
            ...
        */
        Pattern pattern = Pattern.compile("^(.+?)\\s+([^ ]+)\\s+#.*$");
        Matcher matcher = pattern.matcher(line);

        if (matcher.find() && matcher.groupCount() == 2) {
            String name = matcher.group(1);
            String culture = matcher.group(2);

            Voice voice = new Voice();
            voice.setName(name);
            voice.setCulture(culture);
            String gender = getGender(stripBrackets(name));
            voice.setGender(gender);
            voice.setAge("?");
            voice.setDescription(String.format("%s (%s, %s)", name, culture, gender));
            return voice;
        } else {
            throw new ParseException(String.format("Unexpected line from %s: %s", getSayExecutable(), line));
        }
    }

    // removes the brackets that is following the wanted string,
    // e.g. "Eddy (Italian (Italy))" would become "Eddy"
    private static String stripBrackets(String string) {
        Pattern pattern = Pattern.compile("^([^(]+)\\s+\\(.*\\).*$");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return string;
    }

    private static String getGender(String name) {
        // appending a comma for the search in order to avoid false positives for Fred/Frederica for example.
        if (femaleNames.contains(name + ",")) return "female";
        if (maleNames.contains(name + ",")) return "male";
        return "?";
    }
}
