package map;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.util.List;
import java.util.Random;

public class InnerCircularMap implements InnerMapBuilder{
    @Override
    public Map build(Structs.Mesh aMesh, int outterR, Map m) {
        Random r = new Random();
        Map circularMap = new Map();
        circularMap.setInnerBounds(m);
        circularMap.setRadius(outterR);
        List<Structs.Vertex> verts = aMesh.getVerticesList();
        System.out.println("         "+circularMap.min_x);
        double cx = r.nextDouble(circularMap.min_x,circularMap.max_x)/2 + circularMap.min_x;
        double cy = r.nextDouble(circularMap.min_y,circularMap.max_y)/2 + circularMap.min_y;
        System.out.println(cx + "   " + cy);

        double xDiff, yDiff;
        for(Structs.Polygon p : aMesh.getPolygonsList()){
            Structs.Vertex centroid = verts.get(p.getCentroidIdx());
            if(centroid.getX() > cx){
                xDiff = centroid.getX() - cx;
            }else{
                xDiff = cx - centroid.getX();
            }
            if(centroid.getY() > cy){
                yDiff = centroid.getY() - cy;
            }else{
                yDiff = cy - centroid.getY();
            }
            //just accounts for radius distance from point - does not account for off center.
            if(Math.sqrt(Math.pow(xDiff, 2)+Math.pow(yDiff, 2))<= outterR)//compare to radius
            {
                System.out.println("Tile added");
                circularMap.addLandTile(p);
            }
        }
        return circularMap;
    }
}
