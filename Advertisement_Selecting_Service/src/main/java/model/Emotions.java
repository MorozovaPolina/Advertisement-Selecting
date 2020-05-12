package model;

public class Emotions {
    double anger, contempt, disgust, fear, happiness, neutral, sadness, surprise;

    public double getAnger() {
        return anger;
    }

    public void setAnger(double anger) {
        this.anger = anger;
    }

    public double getContempt() {
        return contempt;
    }

    public void setContempt(double contempt) {
        this.contempt = contempt;
    }

    public double getDisgust() {
        return disgust;
    }

    public void setDisgust(double disgust) {
        this.disgust = disgust;
    }

    public double getFear() {
        return fear;
    }

    public void setFear(double fear) {
        this.fear = fear;
    }

    public double getHappiness() {
        return happiness;
    }

    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    public double getNeutral() {
        return neutral;
    }

    public void setNeutral(double neutral) {
        this.neutral = neutral;
    }

    public double getSadness() {
        return sadness;
    }

    public void setSadness(double sadness) {
        this.sadness = sadness;
    }

    public double getSurprise() {
        return surprise;
    }

    public void setSurprise(double surprise) {
        this.surprise = surprise;
    }

    public void print_emotions() {
        System.out.println("    anger " + anger);
        System.out.println("    contempt " + contempt);
        System.out.println("    disgust " + disgust);
        System.out.println("    fear " + fear);
        System.out.println("    happiness " + happiness);
        System.out.println("    neutral " + neutral);
        System.out.println("    sadness " + sadness);
        System.out.println("    surprise " + surprise);
    }
}
