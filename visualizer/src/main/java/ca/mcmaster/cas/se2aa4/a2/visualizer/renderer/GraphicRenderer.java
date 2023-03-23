package ca.mcmaster.cas.se2aa4.a2.visualizer.renderer;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Mesh;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Property;
import ca.mcmaster.cas.se2aa4.a2.io.Structs.Vertex;
import ca.mcmaster.cas.se2aa4.a2.visualizer.renderer.properties.ColorProperty;

import java.awt.Graphics2D;
import java.util.List;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;

public class GraphicRenderer implements Renderer {

    private static final int THICKNESS = 3;
    public void render(Mesh aMesh, Graphics2D canvas) {
        canvas.setColor(Color.BLACK);
        Stroke stroke = new BasicStroke(0.2f);
        canvas.setStroke(stroke);
        drawPolygons(aMesh,canvas);
        drawSegments(aMesh, canvas);
    }

    private void drawPolygons(Mesh aMesh, Graphics2D canvas) {
        for(Structs.Polygon p: aMesh.getPolygonsList()){
            drawAPolygon(p, aMesh, canvas);
        }
    }

    private void drawAPolygon(Structs.Polygon p, Mesh aMesh, Graphics2D canvas) {
        Hull hull = new Hull();
        for(Integer segmentIdx: p.getSegmentIdxsList()) {
            hull.add(aMesh.getSegments(segmentIdx), aMesh);
        }
        Path2D path = new Path2D.Float();
        Iterator<Vertex> vertices = hull.iterator();
        Vertex current = vertices.next();
        path.moveTo(current.getX(), current.getY());
        while (vertices.hasNext()) {
            current = vertices.next();
            path.lineTo(current.getX(), current.getY());
        }
        path.closePath();
        canvas.draw(path);
        Optional<Color> fill = new ColorProperty().extract(p.getPropertiesList());
        if(fill.isPresent()) {
            Color old = canvas.getColor();
            canvas.setColor(fill.get());
            canvas.fill(path);
            canvas.setColor(old);
        }
        
    }

    private void drawSegments(Mesh aMesh, Graphics2D canvas){
        canvas.setColor(Color.BLACK);
        
        List<Vertex> verts = aMesh.getVerticesList();
        for (Structs.Segment s: aMesh.getSegmentsList()) {
            double x1 = verts.get(s.getV1Idx()).getX();
            double y1 = verts.get(s.getV1Idx()).getY();
            double x2 = verts.get(s.getV2Idx()).getX();
            double y2 = verts.get(s.getV2Idx()).getY();

            Stroke strokeSeg = new BasicStroke(extractThickness(s.getPropertiesList()));
            canvas.setStroke(strokeSeg);

            Color old = canvas.getColor();
            canvas.setColor(extractColor(s.getPropertiesList()));

            //canvas.setColor(extractColor(s.getPropertiesList()));
            Line2D line = new Line2D.Double(x1, y1, x2, y2);
            //canvas.fill(line);
            canvas.draw(line);
            canvas.setColor(old);
        }
    }

    private Color extractColor(List<Property> properties) {
        String val = null;
        for(Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                //System.out.println(p.getValue());
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }

    private float extractThickness(List<Property> props){
        float val = 0;
        for(Property p: props){
            if(p.getKey().equals("thickness")){
                val = Float.parseFloat(p.getValue()); 
                break;
            }
        }
        return (float) val;
    }

}
