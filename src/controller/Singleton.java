package controller;

public class Singleton {


    public Controller controller = new Controller();

    private static final Singleton singleton = new Singleton();

    private Singleton(){

    }

    public static Singleton getInstance(){
        return singleton;
    }
}
