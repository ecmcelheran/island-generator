package map.waterBodies;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class Lake {
    public ArrayList<Structs.Polygon> lakeTiles;

    public Lake(){
        this.lakeTiles = new ArrayList<>();
    }

    public void addTile(Structs.Polygon tile){
        this.lakeTiles.add(tile);
    }

    public ArrayList<Structs.Polygon> getLakeTiles(){
        return this.lakeTiles;
    }

}
