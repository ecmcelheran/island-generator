package map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.protobuf.Struct;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class Map {
    protected double centerX, centerY;
    public double max_x = Double.MIN_VALUE;
    public double max_y = Double.MIN_VALUE;
    protected double min_x = Double.MIN_VALUE;
    protected double min_y = Double.MIN_VALUE;

    protected HashMap<Integer, Double> elevation;
    protected HashMap<Integer, Double> absorption;
    protected double radius;
    public ArrayList<Structs.Polygon> land;
    public ArrayList<Structs.Polygon> innerLand;
    public ArrayList<Structs.Polygon> ocean;
    public ArrayList<Structs.Polygon> edge;
    public ArrayList<Structs.Polygon> border;

    public ArrayList<Structs.Polygon> lakes;
    public ArrayList<Structs.Polygon> aquifers;
    public ArrayList<ArrayList<Integer>> rivers;

    public Map(){
        this.land =  new ArrayList<Structs.Polygon>();
        this.innerLand =  new ArrayList<Structs.Polygon>();
        this.ocean =  new ArrayList<Structs.Polygon>();
        this.edge =  new ArrayList<Structs.Polygon>();
        this.border =  new ArrayList<Structs.Polygon>();
        this.lakes = new ArrayList<Structs.Polygon>();
        this.aquifers = new ArrayList<Structs.Polygon>();
        this.rivers = new ArrayList<ArrayList<Integer>>();
        this.elevation = new HashMap<>();
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

    public void setElevation(HashMap<Integer,Double> elevation){
        this.elevation = elevation;
    }

    public void setAbsorption(HashMap<Integer,Double> absorption){
        this.absorption=absorption;
    }

    public HashMap<Integer,Double> getElevation(){
        return elevation;
    }

    public HashMap<Integer,Double> getAbsorption(){
        return absorption;
    }


    public void addLandTile(Structs.Polygon tile){
        this.land.add(tile);
    }

    public void removeLandTile(Structs.Polygon tile){
        this.land.removeAll(Collections.singleton(tile));
    }
    public void addOceanTile(Structs.Polygon tile){
        this.ocean.add(tile);
    }

    public void removeOceanTile(Structs.Polygon tile){
        this.ocean.removeAll(Collections.singleton(tile));
    }

    public ArrayList<Structs.Polygon> findEdge(Structs.Mesh aMesh){
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        ArrayList<Structs.Polygon> edges =  new ArrayList<>();
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: verts) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        double margin = 2*Math.sqrt(((max_x*max_y)/aMesh.getPolygonsCount())/Math.PI); // avg diameter of a polygon 
        for(Structs.Polygon p : aMesh.getPolygonsList()){
            Structs.Vertex centroid = verts.get(p.getCentroidIdx());
            if(centroid.getX() < margin || centroid.getX()>(max_x-margin)){
                edges.add(p);
            }else if(centroid.getY() < margin || centroid.getY()>(max_y-margin)){
                edges.add(p);
            }
        }
        this.edge = edges; 
        return this.edge;
    }

    public void findInnerLand(){        
        for(Structs.Polygon p: this.land){
        if(!this.border.contains(p))
            this.innerLand.add(p);

    }}

    public ArrayList<Structs.Polygon> getInnerLand(){
        this.findInnerLand();
        return this.innerLand;
    }

    public void findOcean(Structs.Mesh aMesh){
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        double max_x = Double.MIN_VALUE;
        double max_y = Double.MIN_VALUE;
        for (Structs.Vertex v: verts) {
            max_x = (Double.compare(max_x, v.getX()) < 0? v.getX(): max_x);
            max_y = (Double.compare(max_y, v.getY()) < 0? v.getY(): max_y);
        }
        double margin = 2*Math.sqrt(((max_x*max_y)/aMesh.getPolygonsCount())/Math.PI); // avg diameter of a polygon 
        double x = max_x/margin;
        double y = max_y/margin;
        this.ocean.addAll(edge);
        ArrayList<Structs.Polygon> newOcean = new ArrayList<>();
        for(int n=0; n<Math.sqrt(Math.pow(x,2)+Math.pow(y,2))/2; n++){
            for(Structs.Polygon p : ocean){
                List<Integer> neighbours = p.getNeighborIdxsList();
                for(int i : neighbours){
                    if(!land.contains(aMesh.getPolygons(i)) && !ocean.contains(aMesh.getPolygons(i))){
                        newOcean.add(aMesh.getPolygons(i));
                    }
                } 
            }
            this.ocean.addAll(newOcean);
        }
    }

    public ArrayList<Structs.Polygon> getOcean(){
        return this.ocean;
    }

    public void findBorder(Structs.Mesh aMesh){
        for(Structs.Polygon p : this.land){
            List<Integer> neigh = p.getNeighborIdxsList();
            for(int i : neigh){
                if(this.ocean.contains(aMesh.getPolygons(i)))
                    this.border.add(p);
            }
        }
    } 

    public ArrayList<Structs.Polygon> getBorder(){
        return this.border;
    }


    public void addLakeTile(Structs.Polygon tile){
        this.lakes.add(tile);
    }

    public ArrayList<Structs.Polygon> getLakes(){
        return this.lakes;
    }

    public void addRiver(ArrayList<Integer> river){
        this.rivers.add(river);
    }

    public ArrayList<ArrayList<Integer>> getRivers(){
        return this.rivers;
    }


    public void addAquifTile(Structs.Polygon tile){
        this.aquifers.add(tile);
    }

    public ArrayList<Structs.Polygon> getAquif(){
        return this.aquifers;
    }



  
    public HashMap<Integer, Double> getBiome() {
        return null;
    }
    



}
