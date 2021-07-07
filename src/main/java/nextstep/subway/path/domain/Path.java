package nextstep.subway.path.domain;

import java.util.List;

import org.jgrapht.GraphPath;

public class Path {
    private final GraphPath graphPath;

    public Path(GraphPath graphPath) {
        this.graphPath = graphPath;
    }

    public List<PathVertex> getPathVertexes() {
        return graphPath.getVertexList();
    }

    public <T extends PathVertex> List<T> getPathVertexes(Class<T> objectType) {
        return graphPath.getVertexList();
    }

    public int getTotalDistance() {
        return (int) graphPath.getWeight();
    }
}
