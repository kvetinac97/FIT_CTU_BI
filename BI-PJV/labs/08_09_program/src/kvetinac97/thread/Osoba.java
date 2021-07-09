package kvetinac97.thread;

public abstract class Osoba {

    protected String first_name;
    protected String  last_name;
    protected    int        age;

    public Osoba ( String first, String last, int age ) {
        this.first_name = first;
        this.last_name = last;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Osoba{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", age=" + age +
                '}';
    }

}
