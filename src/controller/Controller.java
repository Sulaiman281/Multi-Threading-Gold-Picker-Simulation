package controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.Coins;
import model.GoldPicker;
import model.Spot;

import java.util.ArrayList;
import java.util.Random;

public class Controller {

    public static final int width = 600;
    public static final int height = 600;

    public static final int col = 15, row = 15;
    public static final int cell_w = width/col, cell_h = height/row;

    private Spot[][] spots = new Spot[col][row];
    private ArrayList<Coins> untarget_coins = new ArrayList<>();
    private ArrayList<Coins> targeted_coins = new ArrayList<>();

    private ArrayList<GoldPicker> goldPickers = new ArrayList<>();
    public class Pos{
        IntegerProperty x, y;
        Pos(int i, int j){
            x = new SimpleIntegerProperty(i);
            y = new SimpleIntegerProperty(j);
        }

        public int getX() {
            return x.get();
        }

        public int getY() {
            return y.get();
        }
    }
    private ArrayList<Pos> pos = new ArrayList<>();
    public Controller(){

    }

    //TODO: initialize spots
    public void setup_background(){
        for(int i = 0; i<col; i++){
            for(int j = 0; j< row; j++){
                Spot spot = new Spot();
                spot.setPos(i,j);
                spot.setSize(cell_w,cell_h);
                spots[i][j] = spot;
            }
        }
        for(int i = 0; i<col; i++){
            for(int j = 0; j< row; j++){
                spots[i][j].addNeighbours(spots);
            }
        }
    }

    //TODO: create list of foods
    public void spawn_foods(){
        int random_foods_count = new Random().nextInt(20)+10;
        int x = 0, y = 1;
        for(int i = 0; i < random_foods_count; i++){
            x += new Random().nextInt(4)+2;
            if(x >= col){
                x = 0;
                y += new Random().nextInt(4)+2;
            }
            if(y >= row) return;
            Coins coins = new Coins(x,y);
//            spots[coins.getX()][coins.getY()].setObstacle(true);
            untarget_coins.add(coins);
            targeted_coins.add(coins);
        }
    }

    public Spot[][] getSpots() {
        return spots;
    }

    public ArrayList<Coins> getUntarget_food() {
        return untarget_coins;
    }

    public ArrayList<Coins> getTargeted_foods() {
        return targeted_coins;
    }

    public void startThreads(int num) {

        for(int i = 0; i< num; i++){
            Pos pos = new Pos(0,0);
            GoldPicker goldPicker = new GoldPicker();
            goldPicker.bindInteger(pos.x,pos.y);
            goldPicker.setName("goldPicker_"+(i+1));
            this.pos.add(pos);
            goldPickers.add(goldPicker);
            goldPicker.getThread().start();
        }
    }

    public ArrayList<GoldPicker> getGoldPickers() {
        return goldPickers;
    }

    public ArrayList<Pos> getPickers() {
        return pos;
    }
}
