package nextstep.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.subway.exception.domain.SubwayException;
import nextstep.subway.exception.domain.SubwayExceptionMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final Lines lines;

    public PathFinder(Lines lines) {
        graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.lines = lines;
        lines.foreach(this::addVertexStations);
        lines.foreach(this::setEdgeWeightSections);
    }

    private void addVertexStations(Line line) {
        line.getStations()
                .forEach(station -> graph.addVertex(station));
    }

    private void setEdgeWeightSections(Line line) {
        line.foreachSections(section ->
                graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                        section.getDistance().getValue()));
    }

    public Sections find(Station sourceStation, Station targetStation) {
        validateStations(sourceStation, targetStation);
        final GraphPath<Station, DefaultWeightedEdge> path = new DijkstraShortestPath<>(graph).getPath(sourceStation,
                targetStation);
        validatePath(path);

        return lines.bindDistance(pathToSections(path));
    }

    private void validateStations(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayException(SubwayExceptionMessage.EQUALS_START_AND_END_STATION);
        }

        if (!lines.hasStation(sourceStation) || !lines.hasStation(targetStation)) {
            throw new SubwayException(SubwayExceptionMessage.NOT_FOUND_STATION);
        }
    }

    private void validatePath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new SubwayException(SubwayExceptionMessage.NOT_LINKED_STATION);
        }
    }

    private Sections pathToSections(GraphPath<Station, DefaultWeightedEdge> path) {
        final List<Station> stations = path.getVertexList();
        List<Section> sectionList = new ArrayList<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            sectionList.add(new Section(stations.get(i), stations.get(i + 1)));
        }
        return new Sections(sectionList);
    }

}
