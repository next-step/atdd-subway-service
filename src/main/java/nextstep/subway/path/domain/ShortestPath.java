package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPath {

    private static final String NONE_EXISTS_PATH = "경로가 존재해야 합니다";
    private GraphPath<Station, DefaultWeightedEdge> shortestPath;

    public ShortestPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        validateShortestPath(shortestPath);
        this.shortestPath = shortestPath;
    }

    private void validateShortestPath(GraphPath<Station, DefaultWeightedEdge> shortestPath){
        if(shortestPath == null){
            throw new NullPointerException(NONE_EXISTS_PATH);
        }
    }

    public List<Station> getStations(){
        return shortestPath
                .getVertexList()
                .stream()
                .collect(Collectors.toList());
    }

    public int getDistance(){
        return (int) shortestPath.getWeight();
    }
}

