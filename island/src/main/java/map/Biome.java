package map;

public class Biome {
    
    private String name;
    private int temperature;
    private int precipitation;

    public Biome(String name, int temperature, int precipitation) {
        this.name = name;
        this.temperature = temperature;
        this.precipitation = precipitation;
    }

    public String getName() {
        return name;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getPrecipitation() {
        return precipitation;
    }
}


