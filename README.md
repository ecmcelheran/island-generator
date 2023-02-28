# Assignment A2: Mesh Generator

  - Emily McElheran [mcelhere@mcmaster.ca]
  - Rawan Mahdi [mahdir3@mcmaster.ca]
  - Luna Aljammal  [aljammal@mcmaster.ca]
  - Luna Aljammal  [aljammal@mcmaster.ca]

## How to run the product

_This section needs to be edited to reflect how the user can interact with thefeature released in your project_

### Installation instructions

This product is handled by Maven, as a multi-module project. We assume here that you have cloned the project in a directory named `A2`

To install the different tooling on your computer, simply run:

```
mosser@azrael A2 % mvn install
```

After installation, you'll find an application named `generator.jar` in the `generator` directory, and a file named `visualizer.jar` in the `visualizer` one. 

### Generator

To run the generator, go to the `generator` directory, and use `java -jar` to run the product. The product takes one single argument (so far), the name of the file where the generated mesh will be stored as binary.

```
mosser@azrael A2 % cd generator 
mosser@azrael generator % java -jar generator.jar sample.mesh
mosser@azrael generator % ls -lh sample.mesh
-rw-r--r--  1 mosser  staff    29K 29 Jan 10:52 sample.mesh
mosser@azrael generator % 
```

### Visualizer

To visualize an existing mesh, go the the `visualizer` directory, and use `java -jar` to run the product. The product take two arguments (so far): the file containing the mesh, and the name of the file to store the visualization (as an SVG image).

```
mosser@azrael A2 % cd visualizer 
mosser@azrael visualizer % java -jar visualizer.jar ../generator/sample.mesh sample.svg

... (lots of debug information printed to stdout) ...

mosser@azrael visualizer % ls -lh sample.svg

-rw-r--r--  1 mosser  staff    56K 29 Jan 10:53 sample.svg
mosser@azrael visualizer %
```
To viualize the SVG file:


  - Open it with a web browser
  - Convert it into something else with tool slike `rsvg-convert`

## How to contribute to the project

When you develop features and enrich the product, remember that you have first to `package` (as in `mvn package`) it so that the `jar` file is re-generated by maven.

## Backlog

### Definition of Done

-- A feature is done when the code implementing is compiles correctly in maven, is fully supported by all subprojects (generator and visualizer) and its tested based on the requirments defined by the client --
-- A feature is done when the code implementing is compiles correctly in maven, is fully supported by all subprojects (generator and visualizer) and its tested based on the requirments defined by the client --

### Product Backlog

| Id | Feature title | Who? | Start | End | Status |
|:--:|---------------|------|-------|-----|--------|
|  F01  | draw segments between vertices to visualize the squares | Rawan | 02/02/23 | 02/04/23 |    D    |
|  F02   | construct minimal and precise mesh | Luna | 02/16/23 | | D |
|  F03    | displays mesh according to assigned colour, transparency and thickness | | | | |
|  F04    | generate full meshes |Luna & Rawan| 02/05/23 | 02/21/23 | D |
|  F05    | switch to debug mode from the command line | Emily | 02/07/23 | 02/21/23 | D |
|  F07    | generate irregular meshes | Emily | 02/23/23 | 02/23/23 | D |
|  F08    | apply Lloyd relaxation x times | Rawan | 02/24/23 | 02/24/23 | D |
|  F09    | determine neighbours using Delaunay triangle | Emily | 02/24/23 | 02/24/23 | D |
|  F10    | generate meshes according to user inputted command line args | Luna | 02/24/23 02/26/23| | D |
|  F11    | crop meshes to size | Luna | 02/24/23 | 02/26/23 | D |

## Part 2 Demo:
After compiling, when we run in generator `java -jar generatr.jar sample.mesh` and then in visualizer `java -jar visualizer.jar ../generator/sample mesh sample svg`, the outputted svg file is the image under \a2---mesh-generator-team-15\images\grid.svg

Note that transparency can be changed, here we set the transparency to 50% on the segments and vertices

If instead, in visualizer, we run the command `java -jar visualizer.jar ../generator/sample mesh sample svg -X`, we will enter debug mode and the image will be the output as in \a2---mesh-generator-team-15\images\debugGrid.svg

To confirm the meshs are minimal, we observe the magnitude of vertices and segments of our mesh ouputted by the generator.jar, below is a sample of magnitudes for a mesh with width = 500, height = 500, and square size = 20:
Mesh Mesh Metrics: 
|Vertices| = 1301
|Segments| = 1300
|Polygons| = 625

## Part 3 Demo: 
Regular grids: running the command ` java -jar generator.jar c.mesh -G -p 9,900,900` and  `java -jar visualizer.jar ../generator/r.mesh customSizeGrid.svg` produces a custom sized mesh as with images\customSizeGrid.svg

Irregular grids: running the command `java -jar generator.jar r.mesh -I -r 10` and ` java -jar visualizer.jar ../generator/r.mesh uncropped10Relaxations.svg ` results in the irregular grid relaxed via 10 iterations of the Lloyd relaxation as seen in images\uncropped10Relaxations.svg

Irregular grids: running the command `java -jar generator.jar r.mesh -I -r 10` and ` java -jar visualizer.jar ../generator/r.mesh uncropped10Relaxations.svg ` results in the irregular grid relaxed via 10 iterations of the Lloyd relaxation as seen in images\uncropped10Relaxations.svg
