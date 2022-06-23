package nextstep.subway.path.domain;

import nextstep.subway.exception.ErrorMessage;
import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class PathFinder {
    private WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;
    private final Lines lines;

    public PathFinder(Lines lines) {
        stationGraph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        this.lines = lines;
        setStationGraph(lines);
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        checkValidateStations(sourceStation, targetStation);
        GraphPath path = getGraphPath(sourceStation, targetStation);

        List<Station> stations = path.getVertexList();
        int distance = (int) path.getWeight();

        return Path.of(stations, distance);
    }

    private GraphPath getGraphPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath path = new DijkstraShortestPath(stationGraph);
        return Optional.ofNullable(path.getPath(sourceStation, targetStation)).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_CONNECTED_STATIONS));
    }

    private void checkValidateStations(Station sourceStation, Station targetStation) {
        checkSameStation(sourceStation, targetStation);
        checkContainStation(lines.hasStation(sourceStation), lines.hasStation(targetStation));
    }

    private void checkSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.SOURCE_EQUALS_TARGET);
        }
    }

    private void checkContainStation(boolean isSourceStationExisted, boolean isTargetStationExisted) {
        if (!isSourceStationExisted | !isTargetStationExisted) {
            throw new NoSuchElementFoundException(ErrorMessage.NOT_FOUND_STATION_FOR_FIND_PATH);
        }
    }

    private void setStationGraph(Lines lines) {
        for (Line line : lines.getLines()) {
            line.getSections().forEach(section -> {
                Station upStation = section.getUpStation();
                Station downStation = section.getDownStation();
                //지하철역 설정
                stationGraph.addVertex(upStation);
                stationGraph.addVertex(downStation);
                //지하철역 연결정보 및 가중치로 거리 설정
                stationGraph.setEdgeWeight(stationGraph.addEdge(upStation, downStation), section.getDistance());
            });
        }
    }
}
