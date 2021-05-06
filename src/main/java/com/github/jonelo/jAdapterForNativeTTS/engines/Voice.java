package com.github.jonelo.jAdapterForNativeTTS.engines;

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
