package model;

import controller.Controller;
import controller.PathAlgorithm;
import controller.Singleton;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Random;

public class GoldPicker {

    private Thread thread;
    private IntegerProperty x, y;
    private StringProperty score_str;
    private String name;
    private int score = 0;

    ArrayList<Spot> path;
    
    Coins target;
    boolean noCoins;

    Spot[][] threads_spot;

    public GoldPicker(){
        x = new SimpleIntegerProperty();
        y = new SimpleIntegerProperty();
        score_str = new SimpleStringProperty();
        this.threads_spot = new Spot[Controller.col][Controller.row];
        Spot[][] spots = Singleton.getInstance().controller.getSpots();

        for(int i = 0; i< Controller.col; i++){
            for(int j = 0; j< Controller.row; j++){
                this.threads_spot[i][j] = spots[i][j].copy();
            }
        }
        for(int i = 0; i< Controller.col; i++){
            for(int j = 0; j< Controller.row; j++){
                this.threads_spot[i][j].addNeighbours(threads_spot);
            }
        }
        score_str.set("Score: 00");
        thread = new Thread(new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                try {
                    do {
                        if(target != null) {
                            if (x.get() == target.getX() && y.get() == target.getY()) {
                                //TODO: reached to the target.
                                Singleton.getInstance().controller.getTargeted_foods().remove(target);
                                noCoins = Singleton.getInstance().controller.getUntarget_food().isEmpty();
                                score++;
                                score_str.set("Score: "+score);
                            }
                        }
                        if (path == null || path.isEmpty()) {
                            try {
                                if(Singleton.getInstance().controller.getUntarget_food().size() != 0) {
                                    target = Singleton.getInstance().controller.getUntarget_food().get(new Random().nextInt(
                                            Singleton.getInstance().controller.getUntarget_food().size()
                                    ));
                                    //TODO: reset the spots previous to null.
                                    for (int i = 0; i < Controller.col; i++) {
                                        for (int j = 0; j < Controller.row; j++) {
                                            threads_spot[i][j].reset();
                                        }
                                    }
                                    for (Coins coins : Singleton.getInstance().controller.getUntarget_food()) {
                                        if (target == coins)
                                            threads_spot[coins.getX()][coins.getY()].setObstacle(false);
                                        else
                                            threads_spot[coins.getX()][coins.getY()].setObstacle(true);
                                    }
                                    System.out.println(name + ":\t" + Singleton.getInstance().controller.getUntarget_food().size() + " coins available");
                                    path = new PathAlgorithm().generatePath(
                                            threads_spot[x.get()][y.get()],
                                            threads_spot[target.getX()][target.getY()]
                                    );
                                    System.out.println(name + ":\t" + "Path Size: " + path.size());
                                    if (!path.isEmpty())
                                        Singleton.getInstance().controller.getUntarget_food().remove(target);
                                    else target = null;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Spot nextMove = path.remove(path.size()-1);
                            x.set(nextMove.getI());
                            y.set(nextMove.getJ());
                        }
                        Thread.sleep(300);
                    } while (true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public void bindInteger(IntegerProperty i, IntegerProperty j){
        i.bind(x);
        j.bind(y);
    }

    public Thread getThread() {
        return thread;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public StringProperty score_strProperty() {
        return score_str;
    }
}
