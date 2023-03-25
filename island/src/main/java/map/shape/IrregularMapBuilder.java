package map.shape;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import map.Map;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.google.protobuf.Struct;

public class IrregularMapBuilder implements MapBuilder{

    @Override
    public Map build(Structs.Mesh aMesh, int R) {
        Map irregMap = new Map();
        List<Structs.Polygon> edges = irregMap.findEdge(aMesh);
        //List<Structs.Vertex> verts = aMesh.getVerticesList();
        // List<Structs.Polygon> edges =  new ArrayList<>();
        List<Structs.Polygon> border = new ArrayList<>();
        for(Structs.Polygon p : aMesh.getPolygonsList()){
            if(!edges.contains(p)){
                border.add(p);
            }
        }
        Random rand = new Random();
        for(int i=0; i<edges.size(); i++){
            Structs.Polygon targetPoly = border.get(rand.nextInt(border.size()));
            irregMap.addLandTile(targetPoly);
            List<Integer> neighbours = targetPoly.getNeighborIdxsList();
            for(int n: neighbours){
                if(!edges.contains(aMesh.getPolygons(n)))
                    irregMap.addLandTile(aMesh.getPolygons(n));
            }
        }
        irregMap.findOcean(aMesh);
        irregMap.findBorder(aMesh);
        ArrayList<Structs.Polygon> ocean = irregMap.getOcean();
        ArrayList<Structs.Polygon> land = irregMap.getLand();

        for(Structs.Polygon p : aMesh.getPolygonsList()){
            if(!land.contains(p)&& !ocean.contains(p)){
                irregMap.addLandTile(p);
            }
        }
        return irregMap;
    }

}