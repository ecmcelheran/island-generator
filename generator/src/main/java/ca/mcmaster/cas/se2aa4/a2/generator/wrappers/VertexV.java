package ca.mcmaster.cas.se2aa4.a2.generator.wrappers;
import java.awt.Color;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import java.util.Random; 

class VertexV{
    
    private float xCord;
    private float yCord;
    //private Color color;
    private String color; 


    public VertexV(float xCord, float yCord) {
        this.xCord = xCord;
        this.yCord = yCord;
        Random bag = new Random();
        int red = bag.nextInt(255);
        int green = bag.nextInt(255);
        int blue = bag.nextInt(255);
        this.color = red + "," + green + "," + blue;
    }

    public Vertex makeVertex(){
        return Vertex.newBuilder().setX(xCord).setY(yCord).build(); 
    }

    public float getX() {
        return xCord;
    }

    public float getY() {
        return yCord;
    }

    public void setX(float xCord) {
        this.xCord = xCord;
    }

    public void setY(float yCord) {
        this.yCord = yCord;
    }

    public String getColor() {
        return color;
    }

    // public void randomColor() {
        
    // }

}