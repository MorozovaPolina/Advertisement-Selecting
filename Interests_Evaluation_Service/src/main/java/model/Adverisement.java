package model;

import java.util.ArrayList;

public class Adverisement {
    public int demographical_group;
    ArrayList<String> subjects;

    public int getDemographical_group() {
        return demographical_group;
    }

    public void setDemographical_group(int demographical_group) {
        this.demographical_group = demographical_group;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }
}
