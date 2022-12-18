package nextstep.subway.utils;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph<V, E> extends WeightedMultigraph<Station, SectionEdge> {

    public SubwayGraph(EdgeFactory<Station, SectionEdge> ef) {
        super(ef);
    }

    public SubwayGraph(Class<? extends SectionEdge> edgeClass) {
        super(edgeClass);
    }

    public void addGraphVertex(List<Station> stations) {
        stations.forEach(station ->  addVertex(station));
    }

    public void addEdgeWeight(List<Section> sections) {
        sections.forEach(section -> setEdgeWeight(addEdge(section.getUpStation()
                        , section.getDownStation())
                , section.getDistance()));
    }
}
