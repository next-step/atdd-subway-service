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
    private WeightedMultigraph<Station, SectionEdge> stationGraph;

    public PathFinder(Lines lines) {
        stationGraph = new WeightedMultigraph<Station, SectionEdge>(SectionEdge.class);
        setStationGraph(lines);
    }

    public GraphPath getGraphPath(Station sourceStation, Station targetStation) {
        DijkstraShortestPath path = new DijkstraShortestPath(stationGraph);
        return Optional.ofNullable(path.getPath(sourceStation, targetStation)).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.NOT_CONNECTED_STATIONS));
    }
    private void setStationGraph(Lines lines) {
        for (Line line : lines.getLines()) {
            line.getSections().forEach(section -> {
                SectionEdge sectionEdge = new SectionEdge(section);
                Station upStation = section.getUpStation();
                Station downStation = section.getDownStation();

                stationGraph.addVertex(upStation);
                stationGraph.addVertex(downStation);

                stationGraph.addEdge(upStation, downStation, sectionEdge);
                stationGraph.setEdgeWeight(sectionEdge, section.getDistance());
            });
        }
    }
}
