package Biomes;

public class Biome {
    
    private String name;
    private static int temperature;
    private static int precipitation;

    public Biome(String name, int temperature, int precipitation) {
        this.name = name;
        Biome.temperature = temperature;
        Biome.precipitation = precipitation;
    }

    public String getBiomeName() {
        return name;
    }

   // public static int getTemperature() {
      //  return temperature;
  //  }

    public static int getPrecipitation() {
        return precipitation;
    }

    public static int getTemperature(String biome) {
        return 0;
    }
}


