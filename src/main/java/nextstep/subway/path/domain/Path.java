package nextstep.subway.path.domain;

import nextstep.subway.common.Excetion.NotConnectStationException;
import nextstep.subway.line.collection.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Map;

public class Path {
    private static List<Station> stations;
    private static WeightedMultigraph<Station, DefaultWeightedEdge> optimalPath;

    private Path() {
    }

    public Path(List<Station> stations) {
        this.stations = stations;
    }

    public static Path findOptimalPath(Station sourceStation, Station targetStation, List<Section> sections) {
        optimalPath = new WeightedMultigraph(DefaultWeightedEdge.class);
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
        sections.forEach(section -> {
            addVerTex(section);
            setEdgeWeight(section);
        });
        return new Path(getStationList(sourceStation, targetStation));
    }

    private static List<Station> getStationList(Station sourceStation, Station targetStation) {
        try {
            return new DijkstraShortestPath(optimalPath).getPath(sourceStation, targetStation).getVertexList();
        } catch (NullPointerException e) {
            throw new NotConnectStationException();
        }
    }

    private static void setEdgeWeight(Section section) {
        Map<Section, Integer> sectionMap = section.ofSectionMap();
        optimalPath.setEdgeWeight(optimalPath.addEdge(section.getUpStation(), section.getDownStation()), sectionMap.get(section));
    }

    private static void addVerTex(Section section) {
        optimalPath.addVertex(section.getUpStation());
        optimalPath.addVertex(section.getDownStation());
    }

    public List<Station> getStations() {
        return this.stations;
    }
}
