package nextstep.subway.path.application;

import nextstep.subway.line.collection.Sections;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Station> stations;
    private WeightedMultigraph<Station, DefaultWeightedEdge> optimalPath;

    public Path() {
        this.stations = new ArrayList<>();
        this.optimalPath = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public Path(List<Station> stations) {
        this.stations = stations;
    }

    public Path findOptimalPath(Station sourceStation, Station targetStation, Sections sections) {
        if(sourceStation.equals(targetStation)){
            throw new IllegalArgumentException("출발역과 도착역이 동일합니다.");
        }
        sections.getSections().forEach(section -> {
            addVerTex(section);
            setEdgeWeight(section);
        });
        return new Path(getStationList(sourceStation, targetStation));
    }

    private List<Station> getStationList(Station sourceStation, Station targetStation) {
        try {
            return new DijkstraShortestPath(optimalPath).getPath(sourceStation, targetStation).getVertexList();
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("연결되어 있지 않은 역입니다.");
        }
    }

    private void setEdgeWeight(Section section) {
        optimalPath.setEdgeWeight(optimalPath.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getDistance());
    }

    private void addVerTex(Section section) {
        optimalPath.addVertex(section.getUpStation());
        optimalPath.addVertex(section.getDownStation());
    }

    public List<Station> getStations() {
        return this.stations;
    }
}
