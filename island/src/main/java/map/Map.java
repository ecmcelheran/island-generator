package map;

import java.util.ArrayList;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class Map {
    public ArrayList<Structs.Polygon> land;

    public Map(){
        this.land =  new ArrayList<Structs.Polygon>();
    }

    public ArrayList<Structs.Polygon> getLand(){
        return this.land;
    }

    public void addTile(Structs.Polygon tile){
        this.land.add(tile);
    }

}
