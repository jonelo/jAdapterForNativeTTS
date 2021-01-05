package com.github.jonelo.jAdapterForNativeTTS.engines;

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
}
