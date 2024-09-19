import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graph {
    private Set<Vertex> vertices;
    private List<Edge> edges;  // Change Set to List to allow for parallel edges
    private Map<Edge, Set<Vertex>> edgeToVertices;

    public Graph() {
        vertices = new HashSet<>();
        edges = new ArrayList<>();  // Use ArrayList to allow parallel edges
        edgeToVertices = new HashMap<>();
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    public void addEdge(Edge e) {
        edges.add(e);  // Simply add the edge to the list
        Set<Vertex> connectedVertices = new HashSet<>();
        connectedVertices.add(e.getVertex1());
        connectedVertices.add(e.getVertex2());
        edgeToVertices.put(e, connectedVertices);
    }

    public Set<Vertex> getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {  // Return List instead of Set
        return edges;
    }

    public Set<Vertex> getVerticesForEdge(Edge e) {
        return edgeToVertices.get(e);
    }

    public String toString() {
        return String.format("Graph{vertices=%s, edges=%s}", vertices, edges);
    }


    // Method to generate HTML with vertex and edge visualization
    public void toHTML(String filename) throws IOException {
        StringBuilder htmlContent = new StringBuilder();

        // Build the basic HTML structure
        htmlContent.append("<!DOCTYPE html>\n<html>\n<head>\n")
                .append("<title>Graph</title>\n")
                .append("<style>\n")
                .append("svg { border: 1px solid black; }\n")
                .append("</style>\n</head>\n<body>\n")
                .append("<h1>Graph Visualization</h1>\n") // Added H1 tag
                .append("<svg width='800' height='600'>\n");

        // Vertex coordinates mapping
        Map<String, int[]> vertexPositions = new HashMap<>();
        vertexPositions.put("A", new int[]{100, 250});
        vertexPositions.put("B", new int[]{300, 250});
        vertexPositions.put("C", new int[]{200, 400});
        vertexPositions.put("D", new int[]{500, 300});

        int radius = 20;


        for (Map.Entry<String, int[]> entry : vertexPositions.entrySet()) {
            String vertex = entry.getKey();
            int[] pos = entry.getValue();

            htmlContent.append("<circle cx='").append(pos[0]).append("' cy='").append(pos[1])
                    .append("' r='").append(radius).append("' fill='lightgreen' stroke='black' stroke-width='2'/>\n");

            htmlContent.append("<text x='").append(pos[0]).append("' y='").append(pos[1])
                    .append("' font-size='12' text-anchor='middle' dominant-baseline='central' fill='black'>")
                    .append(vertex).append("</text>\n");
        }


        Map<String, List<Edge>> edgeGroups = new HashMap<>();
        for (Edge edge : edges) {
            String key = edge.getVertex1().getName() + "-" + edge.getVertex2().getName();
            edgeGroups.computeIfAbsent(key, k -> new ArrayList<>()).add(edge);
        }


        for (List<Edge> edgeGroup : edgeGroups.values()) {
            int edgeCount = edgeGroup.size();
            for (int i = 0; i < edgeCount; i++) {
                Edge edge = edgeGroup.get(i);
                int[] pos1 = vertexPositions.get(edge.getVertex1().getName());
                int[] pos2 = vertexPositions.get(edge.getVertex2().getName());


                double angle = Math.atan2(pos2[1] - pos1[1], pos2[0] - pos1[0]);


                double startX = pos1[0] + radius * Math.cos(angle);
                double startY = pos1[1] + radius * Math.sin(angle);
                double endX = pos2[0] - radius * Math.cos(angle);
                double endY = pos2[1] - radius * Math.sin(angle);


                double midX = (startX + endX) / 2;
                double midY = (startY + endY) / 2;
                double normalX = -(endY - startY);
                double normalY = endX - startX;
                double normalLength = Math.sqrt(normalX * normalX + normalY * normalY);
                normalX /= normalLength;
                normalY /= normalLength;

                double offset = (i - (edgeCount - 1) / 2.0) * 30;
                double controlX = midX + normalX * offset;
                double controlY = midY + normalY * offset;


                htmlContent.append("<path d='M").append(startX).append(",").append(startY)
                        .append(" Q").append(controlX).append(",").append(controlY)
                        .append(" ").append(endX).append(",").append(endY)
                        .append("' fill='none' stroke='black' stroke-width='2'/>\n");


                double labelX = midX + normalX * offset * 1.2;
                double labelY = midY + normalY * offset * 1.2;
                htmlContent.append("<text x='").append(labelX).append("' y='").append(labelY)
                        .append("' font-size='12' text-anchor='middle' dominant-baseline='central' fill='black'>")
                        .append(edge.getId()).append("</text>\n");
            }
        }

        htmlContent.append("</svg>\n");
        htmlContent.append("</body>\n</html>");

        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(htmlContent.toString());
        }
    }



}
