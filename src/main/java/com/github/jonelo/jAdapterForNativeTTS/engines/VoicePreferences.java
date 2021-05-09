package com.github.jonelo.jAdapterForNativeTTS.engines;

public class VoicePreferences {

    public enum Gender {
        FEMALE, MALE
    }

    public enum Age {
        CHILD, ADULT
    }

    private String language;
    private String country;
    private Gender gender;
    private Age age;

    public VoicePreferences() {
        reset();
    }

    public void reset() {
        this.language = null;
        this.country = null;
        this.gender = null;
        this.age = null;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Age getAge() {
        return age;
    }

    public void setAge(Age age) {
        this.age = age;
    }

    public String toString() {
        return String.format ("language='%s', country='%s', gender='%s', age='%s'",
                language!=null?language:"",
                country!=null?country:"",
                gender!=null?gender:"",
                age!=null?age:"");
    }


}
