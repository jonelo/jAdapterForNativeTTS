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

import java.util.Locale;

public class Voice {
    private String name;
    private String culture;
    private String gender;
    private String age;
    private String description;

    public Voice() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return String.format ("name='%s', culture='%s', gender='%s', age='%s', description='%s'", name, culture, gender, age, description);
    }

    public String toJSONString() {
        return String.format("{\"name\" : \"%s\", \"culture\" : \"%s\", \"gender\" : \"%s\", \"age\" : \"%s\", \"description\" : \"%s\"}", name, culture, gender, age, description);
    }


    private VoicePreferences toVoicePreferences() {
        VoicePreferences voicePreferences = new VoicePreferences();
        String[] cultureTokens = culture.toLowerCase(Locale.US)
                .replaceAll("_", "-").split("-");

        if (cultureTokens.length > 0) {
            voicePreferences.setLanguage(cultureTokens[0]);
        }
        if (cultureTokens.length > 1) {
            voicePreferences.setCountry(cultureTokens[1].toUpperCase(Locale.US));
        }

        switch (gender.toLowerCase(Locale.US)) {
            case "male": voicePreferences.setGender(VoicePreferences.Gender.MALE);
                         break;
            case "female": voicePreferences.setGender(VoicePreferences.Gender.FEMALE);
                           break;
        }
        switch (age.toLowerCase(Locale.US)) {
            case "adult":voicePreferences.setAge(VoicePreferences.Age.ADULT);
                         break;
            case "child":voicePreferences.setAge(VoicePreferences.Age.CHILD);
                         break;
        }

        return voicePreferences;
    }

    public boolean matches(VoicePreferences voicePreferences) {
        boolean match = true;
        VoicePreferences myVoicePreferences = toVoicePreferences();
        if (voicePreferences.getLanguage() != null && myVoicePreferences.getLanguage() != null) {
            match = voicePreferences.getLanguage().equalsIgnoreCase(myVoicePreferences.getLanguage());
        }
        if (!match) return false;

        if (voicePreferences.getCountry() != null && myVoicePreferences.getCountry() != null) {
            match = voicePreferences.getCountry().equalsIgnoreCase(myVoicePreferences.getCountry());
        }
        if (!match) return false;

        if (voicePreferences.getGender() != null && myVoicePreferences.getGender() != null) {
            match = voicePreferences.getGender().equals(myVoicePreferences.getGender());
        }
        if (!match) return false;

        if (voicePreferences.getAge() != null && myVoicePreferences.getAge() != null) {
            match = voicePreferences.getAge().equals(myVoicePreferences.getAge());
        }
        return match;
    }
}
