public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;
    private String name;  // New field to differentiate parallel edges

    // Constructor
    public Edge(String name, Vertex v1, Vertex v2) {
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.name = name;
    }

    // Getter for vertex1
    public Vertex getVertex1() {
        return vertex1;
    }

    // Getter for vertex2
    public Vertex getVertex2() {
        return vertex2;
    }

    // Getter for edge id
    public String getId() {
        return name;
    }


    public String toString() {
        return String.format("Edge{vertex1=%s, vertex2=%s, name='%s'}",
                vertex1.getName(), vertex2.getName(), name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Edge)) {
            return false;
        }

        Edge otherEdge = (Edge) obj;
        return vertex1.equals(otherEdge.vertex1) &&
                vertex2.equals(otherEdge.vertex2) &&
                name.equals(otherEdge.name);
    }


}
