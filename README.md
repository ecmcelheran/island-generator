# Mesh Generator (Assignment #2 Walkthrough)

  - Authors: Emily McElheren, Luna Aljammal, Rawan Mahdi


### A3 - Using Command Line to Make Islands
The generator subproject can be used to generate a mesh. In general, our island subproject can support any sized mesh, but one should generate relatively small meshes in order to keep the runtime of our program to be acceptable. A well sized mesh can be implemented using the following command: 
 java -jar generator.jar -k irregular -h 500 -w 500 -p 500 -s 10 -o ../img/input.mesh

Island shapes can be implemented via the tag "-shape" followed by one of our implemented shapes, including circle, irregular (irreg), (add more). Water bodies as added via the commands -rivers -lakes - auqifers and the input is the number of units of that water body implemented in the map(-rivers 5 means 5 rivers are added). Elevation types can be added via "-elevations" tag to implement mountains, peaks (peak), plataues (plateau) and flat lands (flat). Example command that operated on the mesh created above:
java -jar island.jar -i ../img/input.mesh -o ../img/ouput.mesh -shape irreg -elevation mountain -lakes 5 -rivers 4 -auqifers 0

To visualize this mesh, run the command below:
java -jar visualizer.jar -i ../img/output.mesh -o ../img/output.svg


# Breakdown of command line arguments

-shape: circle, irreg
-rivers: int
-lakes: int 
-auqifer: int
-soil: clay, sand, silt
-seed: enter number printed to reproduce island
-elevation: mountain, peak, plateau, flat
-biome: canada, australia, latvia