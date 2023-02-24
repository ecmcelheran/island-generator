package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;

import java.text.DecimalFormat;
import java.util.Random; 

class VertexV{
    
    private double xCord;
    private double yCord;
    //private Color color;
    private String color; 


    private static final DecimalFormat df = new DecimalFormat("#.00"); //precision to 2 digits after decimal 
    public VertexV(double xCord, double yCord) {
        this.xCord = Double.parseDouble(df.format(xCord));
        this.yCord = Double.parseDouble(df.format(yCord));
        Random bag = new Random();
        int red = bag.nextInt(255);
        int green = bag.nextInt(255);
        int blue = bag.nextInt(255);
        //int alpha = 10;
        int alpha = 128;
        this.color = red + "," + green + "," + blue + "," + alpha;

    }

    public Vertex makeVertex(){
        return Vertex.newBuilder().setX(xCord).setY(yCord).build(); 
    }

    public double getX() {
        return xCord;
    }

    public double getY() {
        return yCord;
    }

    public void setX(double xCord) {
        this.xCord = Double.parseDouble(df.format(xCord));
    }

    public void setY(double yCord) {
        this.yCord = Double.parseDouble(df.format(yCord));
    }

    public String getColor() {
        return color;
    }

    // public void randomColor() {
        
    // }

}