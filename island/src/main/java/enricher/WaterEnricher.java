package enricher;

import java.util.ArrayList;
import java.util.List;


import com.google.protobuf.Struct;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import configuration.Configuration;
import map.Map;

public class WaterEnricher implements Enricher{
    private int LAKES;
    //private String SHAPE;
    

    public WaterEnricher(Configuration config){
        if(config.export().containsKey(Configuration.LAKES)) 
            this.LAKES = Integer.parseInt(config.export(Configuration.LAKES));
        else
            this.LAKES = 100;
    }


    @Override
    public Mesh process(Mesh aMesh) {


        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'process'");
    }

    public void countLakes(Mesh aMesh, Map map){
        ArrayList<Structs.Polygon> land = map.getLand();
        for(Structs.Polygon p : aMesh.getPolygonsList()){
            if(!land.contains(p)){
                List<Integer> neighbours = p.getNeighborIdxsList();
                for(int i : neighbours){
                    Structs.Polygon n = aMesh.getPolygons(i);

                }
            }
        }


    }

    public void surroundedByLand(Structs.Polygon water, Map map, Mesh aMesh){

        
    }

    
}
