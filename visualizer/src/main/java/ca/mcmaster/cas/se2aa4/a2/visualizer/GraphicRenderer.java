package ca.mcmaster.cas.se2aa4.a2.visualizer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Segment;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Polygon;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;


import java.util.List;

public class GraphicRenderer {

    private static final int THICKNESS = 3;
    public void render(Mesh aMesh, Graphics2D canvas, boolean visualize) {
        canvas.setColor(Color.BLACK);
        Stroke strokeVer = new BasicStroke(0.5f);
        canvas.setStroke(strokeVer);
        List<Vertex> vertices = aMesh.getVerticesList();
        for (Vertex v: vertices) {
            double centre_x = v.getX() - (THICKNESS/2.0d);
            double centre_y = v.getY() - (THICKNESS/2.0d);
            Color old = canvas.getColor();
//            canvas.setColor(visualize? Color.BLACK : extractColor(v.getPropertiesList(), 10));
            canvas.setColor(visualize? Color.BLACK : extractColor(v.getPropertiesList()));

            //canvas.setColor(extractColor(v.getPropertiesList()));
            Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
            canvas.fill(point);
            canvas.setColor(old);
        }
        canvas.setColor(Color.BLACK);
        Stroke strokeSeg = new BasicStroke(1.0f);
        canvas.setStroke(strokeSeg);
        for (Segment s: aMesh.getSegmentsList()) {
            double x1 = vertices.get(s.getV1Idx()).getX();
            double y1 = vertices.get(s.getV1Idx()).getY();
            double x2 = vertices.get(s.getV2Idx()).getX();
            double y2 = vertices.get(s.getV2Idx()).getY();
            Color old = canvas.getColor();
            canvas.setColor(visualize? Color.BLACK : extractColor(s.getPropertiesList()));
            //canvas.setColor(extractColor(s.getPropertiesList()));
            Line2D line = new Line2D.Double(x1, y1, x2, y2);
            //canvas.fill(line);
            canvas.draw(line);
            canvas.setColor(old);
        }

        canvas.setColor(Color.BLACK);
        canvas.setStroke(strokeSeg);
        for (Polygon p: aMesh.getPolygonsList()) {
            List<Integer> neighbours = p.getNeighborIdxsList();
            int centroid = p.getCentroidIdx();
            for(Integer n: neighbours){
                double x1 = vertices.get(centroid).getX();
                double y1 = vertices.get(centroid).getY();
                double x2 = vertices.get(n).getX();
                double y2 = vertices.get(n).getY();
                Color old = canvas.getColor();
                canvas.setColor(visualize? Color.GRAY : extractColor(p.getPropertiesList()));
                Line2D line = new Line2D.Double(x1, y1, x2, y2);
                canvas.draw(line);
                canvas.setColor(old);
            }
        }

        canvas.setColor(Color.BLACK);
        canvas.setStroke(strokeVer);
        List<Polygon> polygons = aMesh.getPolygonsList();
        for (Polygon p: polygons) {
            double centre_x = vertices.get(p.getCentroidIdx()).getX() - (THICKNESS/2.0d);
            double centre_y = vertices.get(p.getCentroidIdx()).getY() - (THICKNESS/2.0d);
            Color old = canvas.getColor();
            canvas.setColor(visualize? Color.RED : extractColor(p.getPropertiesList()));
            Ellipse2D point = new Ellipse2D.Double(centre_x, centre_y, THICKNESS, THICKNESS);
            canvas.fill(point);
            canvas.setColor(old);
        }
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                System.out.println(p.getValue());
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        int alphaValue = Integer.parseInt(raw[3]);
        return new Color(red, green, blue, alphaValue);
       /* if (useAlpha){
            int alphaValue = Integer.parseInt(raw[3]);
        return new Color(red, green, blue, alphaValue);
        } else {
            return new Color(red, green, blue);
        }*/
    }

}
