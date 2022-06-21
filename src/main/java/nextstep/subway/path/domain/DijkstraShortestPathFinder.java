package nextstep.subway.path.domain;

import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

public class DijkstraShortestPathFinder implements StationGraphStrategy {
    @Override
    public Path findShortestPath(List<Section> sections, Station source, Station target) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);
        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        setGraph(sections, graph);

        GraphPath graphPath = Optional.ofNullable(path.getPath(source, target))
            .orElseThrow(PathException::new);

        List<SectionEdge> shortestSections = graphPath.getEdgeList();
        int distance = (int)graphPath.getWeight();

        return Path.of(shortestSections, distance);
    }

    private void setGraph(List<Section> sections, WeightedMultigraph<Station, SectionEdge> graph) {
        for(Section section: sections) {
            SectionEdge sectionEdge = new SectionEdge(section);

            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);

            graph.addEdge(upStation, downStation, sectionEdge);
            graph.setEdgeWeight(sectionEdge, section.getDistance());
        }
    }
}
