package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.WeightedMultigraph;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.path.common.PathConstants.*;

public class Path {
    private Station source;
    private Station target;
    private WeightedMultigraph<Station, LineSection> graph;
    private DijkstraShortestPath dijkstraShortestPath;

    public Path(Station source, Station target) {
        if (source.equals(target)){
            throw new RuntimeException(SAME_SOURCE_TARGET_EXCEPTION);
        }

        this.source = source;
        this.target = target;
        this.graph = new WeightedMultigraph(LineSection.class);
    }

    public List<Station> findShortPath(List<Line> lines) {
        setUpPath(lines);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    private void setUpPath(List<Line> lines) {
        List<Station> stations = assembleStations(lines);
        validStations(stations);
        addVertex(lines);
        addEdge(lines);
    }

    public void addVertex(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.assembleStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(station -> graph.addVertex(station));
    }

    public void addEdge(List<Line> lines) {
        for (Line line : lines) {
            line.getSections().stream()
                    .forEach(it -> addEdgeWeight(it, line));
        }
    }

    private void addEdgeWeight(Section section, Line line) {
        LineSection lineSection = new LineSection(section, line);
        graph.addEdge(section.upStation(), section.downStation(), lineSection);
        graph.setEdgeWeight(lineSection, section.distance());
    }

    public void validStations(List<Station> stations) {
        boolean hasSourceStation = hasStations(stations, source);
        boolean hasTargetStation = hasStations(stations, target);

        if (!hasSourceStation || !hasTargetStation) {
            throw new RuntimeException(SOURCE_TARGET_EXCEPTION);
        }
    }

    private boolean hasStations(List<Station> stations, Station inputStation) {
        return stations.stream()
                .anyMatch(station -> station.equals(inputStation));
    }

    public List<Station> assembleStations(List<Line> lines) {
        return lines.stream()
                .flatMap(it -> it.assembleStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public int calculateDistance(int pathNumber) {
        List<GraphPath> paths = new KShortestPaths(graph, pathNumber).getPaths(source, target);
        return paths.stream()
                .mapToInt(it -> (int)it.getWeight())
                .sum();
    }

    public int calculateFare(int distance, int age) {
        int totalFare= calculateDistanceFare(distance);
        totalFare = calculateAagFare(age, totalFare);
        return totalFare + findMaxLineFares();
    }

    private int calculateDistanceFare(int distance) {
        int totalFare = DEFAULT_FARE;
        if (isLessFiftyKm(distance)) {
            return DEFAULT_FARE + calculateOverFare(distance - DEFAULT_KM, FIVE_KM);
        }
        if (isMoreFiftyKm(distance)) {
            return DEFAULT_FARE + calculateOverFare(distance - DEFAULT_KM, EIGHT_KM);
        }
        return totalFare;
    }


    private int calculateAagFare(int age, int totalFare) {
        if (isBetweenThirteenAndNineteenAge(age)) {
            return (int) ((totalFare-DEDUCTIBLE_FARE) * DISCOUNT_TEENAGER);
        }
        if (isBetweenSixAndThirteenAge(age)) {
            return (int) ((totalFare-DEDUCTIBLE_FARE) * DISCOUNT_CHILDREN);
        }
        return totalFare;
    }
    private boolean isLessFiftyKm(int distance) {
        if (distance >= DEFAULT_KM && distance <= FIFTY_KM) {
            return true;
        }
        return false;
    }

    private boolean isMoreFiftyKm(int distance) {
        if (distance > FIFTY_KM) {
            return true;
        }
        return false;
    }

    private boolean isBetweenThirteenAndNineteenAge(int age) {
        if (THIRTEEN_AGE <= age && age < NINETEEN_AGE) {
            return true;
        }
        return false;
    }

    private boolean isBetweenSixAndThirteenAge(int age) {
        if (SIX_AGE <= age && age < THIRTEEN_AGE) {
            return true;
        }
        return false;
    }

    private int calculateOverFare(int distance, int km) {
        return (int) ((Math.ceil((distance - ONE) / km) + ONE) * DEFAULT_ADD_FARE);
    }

    private int findMaxLineFares() {
        List<LineSection> lineSections = dijkstraShortestPath.getPath(source, target).getEdgeList();
        return lineSections.stream()
                .mapToInt(it -> it.getLineFare())
                .max()
                .orElse(ZEOR);
    }
}
