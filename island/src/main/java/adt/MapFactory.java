import ca.mcmaster.cas.se2aa4.a2.generator;
// import ca.mcmaster.cas.se2aa4.a2.io.MeshFactory;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public class MapFactory {
    private String input;
    private String output;
    private String mode; 

    public MapFactory(Configuration config){
        Map<String, String> options = config.export();
        this.input = options.get(Configuration.INPUT);
        this.output = options.get(Configuration.Output);
        this.mode = options.get(Configuration.MODE);
    }

    public void create(){
        if(mode == "lagoon"){

        }
    }

    public void createLagoon(int outterR){
        Mesh inMesh = input;
        List<Vertex> verts = inMesh.getVerticesList;
        int centerX = inMesh.width/2;
        int centerY= inMesh.height/2;
        for(Polygon p : inMesh.getPoly){
            Vertex centroid = vert.get(p.getCentroidIdx);
            if(centroid.getX() > centerX){
                float xDiff = centroid.getX() - centerX;
            }else{
                float xDiff = centerX - centroid.getX();
            }
            if(centroid.getY() > centerY){
                float yDiff = centroid.getY() - centerY;
            }else{
                float yDiff = centerY - centroid.getY();
            }
            if()//compare to radius
            {
                
            }
            
        }
    }


}
