package model;


import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


public class Person {

    Gender gender;
    int age;
    Emotions emotion;
    @JsonIgnore
    long demographic_group;
   /* public Person(int age, Gender gender, Emotions emotions){
        this.age = age;
        this.gender = gender;
        this.emotion = emotions;
    }*/

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender.toString();
    }

    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender);
    }

    public Emotions getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotions emotion) {
        this.emotion = emotion;
    }

    public long getDemographic_group() {
        return demographic_group;
    }

    public void setDemographic_group(long demographic_group) {
        this.demographic_group = demographic_group;
    }

    public void print_person(){
        System.out.println("Age "+ age);
        System.out.println("gender "+ gender);
        if(emotion!=null) emotion.print_emotions();
        System.out.println("demographic group "+ demographic_group);
        System.out.println("_______________________________________");
    }

    public String toJSON()  {
        try {
            String result = new ObjectMapper().writeValueAsString(this);
            return result;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
