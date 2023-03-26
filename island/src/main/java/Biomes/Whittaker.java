package Biomes;

import java.util.ArrayList;
import java.util.HashMap;
import map.elevation.Elevation;
import map.elevation.FlatBuilder;
import map.elevation.MountainBuilder;
import map.elevation.PeakBuilder;
import map.elevation.PlateauBuilder;
import map.soil.Absorption;
import map.Map;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

public class Whittaker {
    
    private double FinalTemperature = 0;

    public HashMap<Integer, HashMap<String, Double>> whittaker(Map map, String biome){
        HashMap<Integer,Double> absorption = map.getAbsorption();
        HashMap<Integer,Double> elevation = map.getElevation();
        ArrayList<Structs.Polygon> land = map.getLand();
        HashMap<Integer, HashMap<String, Double>> results = new HashMap<Integer, HashMap<String, Double>>();

        for(Structs.Polygon p : land) {
            double abs = absorption.get(land.indexOf(p));
            double elev = elevation.get(land.indexOf(p));
            double elevTemperature;
            double absorp = 0;
            if (elev > 150) {
                elevTemperature = -7;
            } else if (elev > 100) {
                elevTemperature = -2;
            } else if (elev > 50) {
                elevTemperature = 5;
            } else if (elev > 25) {
                elevTemperature = 10;
            } else {
                elevTemperature = 0;
            }


            double finalTemperature = (elevTemperature) + Biome.getTemperature();
            double moisture = (absorp) + Biome.getPrecipitation();
            HashMap<String, Double> values = new HashMap<String, Double>();
            values.put("temperature", finalTemperature);
            values.put("moisture", moisture);
            results.put(land.indexOf(p), values);
        }

        return results;
    }
 


    public void setFinalTemperature (double FinalTemperature){
        this.FinalTemperature=FinalTemperature;
    }

    public double getFinalTemperature(){
        return FinalTemperature;
    }

}
/*public void setElevationTemperature() {
    for (Structs.Polygon land : this.land) {
        List<Integer> vertices = land.getVertexList();
        for (int vertex : vertices) {
            double elevation = this.elevation.get(vertex);
            double elevTemperature;
            if (elevation > 150) {
                elevTemperature = -7;
            } else if (elevation > 100) {
                elevTemperature = -2;
            } else if (elevation > 50) {
                elevTemperature = 5;
            } else if (elevation > 25) {
                elevTemperature = 10;
            } else {
                elevTemperature = 0;
            }
            this.elevation.put(vertex, elevTemperature);
        }
    }
}*/
/*

package Biomes;

import java.util.ArrayList;
import java.util.HashMap;
import map.elevation.Elevation;
import map.elevation.FlatBuilder;
import map.elevation.MountainBuilder;
import map.elevation.PeakBuilder;
import map.elevation.PlateauBuilder;
import map.soil.Absorption;
import map.Map;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;

public class Whittaker {
    
    public void whittaker(Map map, String biome){
        HashMap<Integer,Double> absorption = map.getAbsorption();
        HashMap<Integer,Double> elevation = map.getElevation();
        Map genMap = new Map();
        ArrayList<Structs.Polygon> land = genMap.getLand();
        Mesh aMesh;
        Structs.Mesh givenMesh = aMesh;

        HashMap<Integer, String> polygonData = new HashMap<Integer, Double, Double>();
       
        int temperature = 0;
        // return moisture;
        //int temperature = Elevation.assignTemp(0)+ Biome.getTemperature();

      
       // for (Polygon p : aMesh.getLand()) {
            int moisture = Absorption.getHumidity() + Biome.getPrecipitation();
            String polygonValue = temperature + ":" + moisture;
            polygonData.put(aMesh.getPolygonsList().indexOf(p), polygonValue);}
        
    }
    for(Structs.Polygon land: aMesh.getLand()) {
        Structs.Polygon.Builder pc = Structs.Polygon.newBuilder(land);
        // if(border.contains(poly)){
        //     color = "135,99,41";
        // }
        if (land.contains(land)){
            System.out.println("land poly");
            int elevTemperature;
            if(elevation.get(aMesh.getLand().indexOf(land))>150){
                elevTemperature = -7 ;
            }
            else if(elevation.get(aMesh.getLand().indexOf(land))>100){
                elevTemperature = -2 ;
            }
            else if(elevation.get(aMesh.getLand().indexOf(land))>50){
                elevTemperature = 5 ;
            }
            else if(elevation.get(aMesh.getLand().indexOf(land))>25){
                elevTemperature = 10 ;
            }
            else{
                elevTemperature = 0 ;

            rgb = color.split(",");
            blue = Integer.parseInt(rgb[2]);
            try{
                blue+=(absorption.get(aMesh.getPolygonsList().indexOf(poly)));
            }catch(Exception e){
                System.out.print("This one doesn't work");
            }
            if(blue>255){
                blue = 255;
            }
            color = (rgb[0] + "," + rgb[1] + "," + blue);
      
        //else if(aquifers.contains(poly)){ // for debug
        //     color = "0,0,0";
        // }
        else {
            color = "0,0,0";
        }
        Structs.Property c = Structs.Property.newBuilder()
                    .setKey("rgb_color")
                    .setValue(color)
                    .build();
        pc.addProperties(c);
        clone.addPolygons(pc);
    }
    return clone.build();
}


}



return HashMap<Polygon index, final temp, final moisture>





}
*/