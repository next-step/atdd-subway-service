package nextstep.subway.path.infrastructure;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph extends WeightedMultigraph<Station, SectionEdge> {

    public SubwayGraph(List<SectionEdge> sectionEdges) {
        super(SectionEdge.class);

        mapGraph(sectionEdges);
    }

    public void mapGraph(List<SectionEdge> sectionEdges) {
        sectionEdges.stream()
            .map(SectionEdge::getVertexes)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList())
            .forEach(this::addVertex);

        sectionEdges.forEach(this::addEdgeWeight);
    }

    private void addEdgeWeight(SectionEdge sectionEdge) {
        addEdge(sectionEdge.getSource(), sectionEdge.getTarget(), sectionEdge);
        setEdgeWeight(sectionEdge, sectionEdge.getWeight());
    }
}
