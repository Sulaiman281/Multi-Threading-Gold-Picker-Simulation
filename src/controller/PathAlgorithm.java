package controller;

import model.Spot;

import java.util.ArrayList;

public class PathAlgorithm {


    public ArrayList<Spot> generatePath(Spot start, Spot goal){
        ArrayList<Spot> path = new ArrayList<>();
        ArrayList<Spot> openSet = new ArrayList<>();
        ArrayList<Spot> closeSet = new ArrayList<>();
        openSet.add(start);
//        System.out.println("added the start into open set");
        int winner = 0;

        for (int i = 0; i< openSet.size(); i++){
//            System.out.println("inside loop algorithm");
            if(openSet.get(i).getF() < openSet.get(winner).getF()){
                winner = i;
            }

            Spot current = openSet.get(winner);

            if(current == goal){
//                System.out.println("found the goal.");
                Spot temp = current;
                path.add(temp);
                while(temp.getPrevious() != null){
//                    System.out.println("spot previous ");
                    path.add(temp.getPrevious());
                    temp = temp.getPrevious();
                }
            }
            openSet.remove(current);
            closeSet.add(current);

            for (Spot neighbour : current.getNeighbours()) {
//                System.out.println("Neighbours: "+neighbour.getI()+","+neighbour.getJ());
                if(closeSet.contains(neighbour) || neighbour.isObstacle()) continue;
                double tempG = current.getG()+1;
                boolean newPath = false;
                if(openSet.contains(neighbour)){
                    if(tempG < neighbour.getG()){
                        neighbour.setG(tempG);
                        newPath = true;
                    }
                }else{
                    neighbour.setG(tempG);
                    openSet.add(neighbour);
                    newPath = true;
                }
                if(newPath){
                    neighbour.setH(heuristic(neighbour,goal));
                    neighbour.setF(neighbour.getG()+neighbour.getH());
                    neighbour.setPrevious(current);
                }
            }
        }
        return path;
    }

    private double heuristic(Spot a, Spot b){
        return dist(a.getI(),a.getJ(),b.getI(),b.getJ());
    }

    private double dist(float x1,float y1, float x2, float y2){
        float x = (x2-x1)*(x2-x1);
        float y = (y2-y1)*(y2-y1);
        return Math.sqrt(x+y);
    }
}
