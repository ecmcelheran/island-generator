package Biomes;

import map.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class Whittaker {
    
    public static List<List<Double>> whittaker(int polygonIndex, Map map, String biome){


        HashMap<Integer,Double> absorption = map.getAbsorption();
        HashMap<Integer,Double> elevation = map.getElevation();
        ArrayList<Structs.Polygon> land = map.getLand();
        List<List<Double>> results = new ArrayList<>();

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

            if (abs > 80) {
                absorp = 310;
            } else if (abs > 60) {
                absorp = 230;
            } else if (abs > 40) {
                absorp = 150;
            } else if (abs > 20) {
                absorp = 70;
            } else {
                absorp = 0;
            }


            double temperature = (elevTemperature) + Biome.getTemperature();
            double moisture = (absorp) + Biome.getPrecipitation();
            
            List<Double> polygonValues = new ArrayList<>();
            polygonValues.add(temperature);
            polygonValues.add(moisture);
            results.add(polygonValues);
        }

        return results;
    }
  
    
}
