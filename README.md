# Mesh Generator (Assignment #2 Walkthrough)

  - Author: Sébastien Mosser

## How to install?

```
mosser@azrael A2 % mvn install
```

It creates two jars:

  1. `generator/generator.jar` to generate meshes
  2. `visualizer/visualizer.jar` to visualize such meshes as SVG files

## Examples of execution

### Generating a mesh, grid or irregular

```
mosser@azrael A2 % java -jar generator/generator.jar -k grid -h 1080 -w 1920 -p 1000 -s 20 -o img/grid.mesh
mosser@azrael A2 % java -jar generator/generator.jar -k irregular -h 1080 -w 1920 -p 1000 -s 20 -o img/irregular.mesh
```

One can run the generator with `-help` as option to see the different command line arguments that are available

### Visualizing a mesh, (regular or debug mode)

```
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/grid.mesh -o img/grid.svg          
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/grid.mesh -o img/grid_debug.svg -x
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/irregular.mesh -o img/irregular.svg   
mosser@azrael A2 % java -jar visualizer/visualizer.jar -i img/irregular.mesh -o img/irregular_debug.svg -x
```

Note: PDF versions of the SVG files were created with `rsvg-convert`.



### A3 - Using Command Line to Make Islands
The generator subproject can be used to generate a mesh. In general, our island subproject can support any sized mesh, but one should generate relatively small meshes in order to keep the runtime of our program to be acceptable. A well sized mesh can be implemented using the following command: 
 java -jar generator.jar -k irregular -h 500 -w 500 -p 1000 -s 15 -o ../img/input.mesh


Island shapes can be implemented via the tag "-shape" followed by one of our implemented shapes, including circle, irregular (irreg), (add more). Water bodies as added via the commands -rivers -lakes - auqifers and the input is the number of units of that water body implemented in the map(-rivers 5 means 5 rivers are added). Elevation types can be added via "-elevations" tag to implement mountains, peaks (peak), plataues (plateau) and flat lands (flat). Example command that operated on the mesh created above:
java -jar island.jar -i ../img/input.mesh -o ../img/ouput.mesh -shape irreg -elevation mountain -lakes 5 -rivers 4 -auqifers 0

To visualize this mesh, run the command below:
java -jar visualizer.jar -i ../img/output.mesh -o ../img/output.svg




