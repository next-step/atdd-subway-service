package nextstep.subway.path.domain.impl;

import java.util.List;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.exception.PathNotFoundException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

public class ShortestPath implements Path {

    GraphPath<Station, SectionEdge> graphPath;

    public ShortestPath(GraphPath<Station, SectionEdge> graphPath) {
        if (graphPath == null) {
            throw new PathNotFoundException();
        }
        this.graphPath = graphPath;
    }

    public List<Station> getStations() {
        return graphPath.getVertexList();
    }

    public int getDistance() {
        return (int) graphPath.getWeight();
    }

    public int getSurcharge() {
        return graphPath.getEdgeList().stream()
            .map(SectionEdge::getSurcharge)
            .max(Integer::compareTo)
            .orElse(0);
    }

}
