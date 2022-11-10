package model;

import controller.Controller;

import java.util.ArrayList;

public class Spot {
    private int i, j;
    private int width, height;
    private double f, g, h;
    private Spot previous;

    private boolean isObstacle;

    private ArrayList<Spot> neighbours;

    public void setPos(int i, int j){
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }
    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public Spot getPrevious() {
        return previous;
    }

    public void setPrevious(Spot previous) {
        this.previous = previous;
    }

    public void addNeighbours(Spot[][] spots){
        neighbours = new ArrayList<>();
        if(i > 0) neighbours.add(spots[i-1][j]);
        if( i < Controller.col-1) neighbours.add(spots[i+1][j]);
        if(j > 0) neighbours.add(spots[i][j-1]);
        if(j < Controller.row-1) neighbours.add(spots[i][j+1]);
        if(j > 0 && i > 0) neighbours.add(spots[i-1][j-1]);
        if(i > 0 && j< Controller.row-1) neighbours.add(spots[i-1][j+1]);
        if(i < Controller.col-1 && j > 0) neighbours.add(spots[i+1][j-1]);
        if(i < Controller.col-1 && j < Controller.row-1) neighbours.add(spots[i+1][j+1]);
    }

    public ArrayList<Spot> getNeighbours() {
        return neighbours;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }

    public Spot copy(){
        Spot newSpot = new Spot();
        newSpot.setSize(getWidth(),getHeight());
        newSpot.setPos(getI(), getJ());
        newSpot.setObstacle(false);
        newSpot.setH(getH());
        newSpot.setF(getF());
        newSpot.setG(getG());
        return newSpot;
    }
    public void reset(){
        h = 0;
        f = 0;
        g = 0;
        previous = null;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "i=" + i +
                ", j=" + j +
                ", width=" + width +
                ", height=" + height +
                ", f=" + f +
                ", g=" + g +
                ", h=" + h +
                ", previous=" + previous +
                ", isObstacle=" + isObstacle +
                ", neighbours=" + neighbours +
                '}';
    }
}
