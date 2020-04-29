package model;


import com.google.gson.GsonBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import com.google.gson.Gson;
import org.codehaus.jettison.json.JSONString;


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

    public Gender getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender);
    }

  /*  public void setGender(Gender gender){
        this.gender=gender;
    }*/

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

    public String toJSON(){
        return new Gson().toJson(this);
    }
}
