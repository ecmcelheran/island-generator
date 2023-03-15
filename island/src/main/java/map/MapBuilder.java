package map;
import ca.mcmaster.cas.se2aa4.a2.io.Structs;

public interface MapBuilder{
    public Map build(Structs.Mesh aMesh, int outterR);
    //public Map build(Structs.Mesh aMesh);
}