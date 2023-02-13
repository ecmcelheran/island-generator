package ca.mcmaster.cas.se2aa4.a2.generator;

import java.awt.Color;

public class Vertex {
    private float xCord;
    private float yCord;
    private Color color;
    private int precision;

    public Vertex(float xCord, float yCord) {
        this.xCord = xCord;
        this.yCord = yCord;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
}
