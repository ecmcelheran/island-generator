package map;

import java.util.ArrayList;
import java.util.List;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class Map {
    protected double centerX, centerY;
    protected double max_x = Double.MIN_VALUE, max_y = Double.MIN_VALUE, min_x = Double.MIN_VALUE, min_y = Double.MIN_VALUE;

    protected double radius;
    public ArrayList<Structs.Polygon> land;

    public Map(){
        this.land =  new ArrayList<Structs.Polygon>();
    }

    public ArrayList<Structs.Polygon> getLand(){
        return this.land;
    }

    public void setRadius(double rad){
        this.radius = rad;
    }


    public void setCenterX(double centerX){this.centerX = centerX;}
    public void setCenterY(double centerY){this.centerY = centerY;}
    public double getCenterX(){
       return centerX;
    }
    public double getCenterY(){
        return centerY;
    }
    public void setStrictBounds( Structs.Mesh aMesh){
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        for (Structs.Vertex v: verts) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
    }

    public void setInnerBounds(Map m){
        max_x = m.getCenterX()+m.radius;
        max_y = m.getCenterY()+m.radius;
        min_x = m.getCenterX()-m.radius;
        min_y = m.getCenterY()-m.radius;

    }

    public void addTile(Structs.Polygon tile){
        this.land.add(tile);
    }

    public void removeTile(Structs.Polygon tile){
        this.land.remove(tile);
    }

}
