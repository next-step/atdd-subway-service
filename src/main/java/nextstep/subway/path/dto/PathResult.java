package nextstep.subway.path.dto;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.SubwayWeightedEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.stream.Collectors;

public class PathResult {
    private final List<Station> vertexList;
    private final double weight;
    private final Sections sections;

    public PathResult(List<Station> vertexList, double weight, Sections sections) {
        this.vertexList = vertexList;
        this.weight = weight;
        this.sections = sections;
    }

    public static PathResult of(GraphPath<Station, SubwayWeightedEdge> path) {
        List<Section> sections = path.getEdgeList()
                .stream()
                .map(SubwayWeightedEdge::getSection)
                .collect(Collectors.toList());
        return new PathResult(path.getVertexList(), path.getWeight(), new Sections(sections));
    }

    public int getMaxExtraFare() {
        return sections.getMaxExtraFare();
    }

    public List<Station> getVertexList() {
        return vertexList;
    }

    public double getWeight() {
        return weight;
    }
}
