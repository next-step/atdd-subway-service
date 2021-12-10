package nextstep.subway.path.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Path {
    private SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.class);
    private Station source;
    private Station target;

    public Path(Station source, Station target) {
        checkValidationSourceAndTarget(source, target);
        this.source = source;
        this.target = target;
    }

    public List<Station> stations(List<Line> lines) {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .distinct()
                .collect(toList());
    }

    public void addVertex(List<Line> lines) {
        this.stations(lines)
                .forEach(station -> subwayGraph.addVertex(station));
    }

    public void addEdge(List<Line> lines) {
        lines.stream()
                .forEach(it -> it.getSections()
                        .forEach(section -> makeSectionEdge(section)));
    }

    private void makeSectionEdge(Section section) {
        subwayGraph.addEdge(section, section.getUpStation(), section.getDownStation());
    }

    public List<Station> findShortestPath(List<Line> lines) {
        addVertex(lines);
        addEdge(lines);
        checkValidateStation(lines);
        return findShortestVertexInPath();
    }

    private List<Station> findShortestVertexInPath() {
        return findShortestPaths().getVertexList();
    }

    private void checkValidateStation(List<Line> lines) {
        List<Station> stations = this.stations(lines);
        if (!isMatchSourceAndTarget(stations)) {
            throw new InputDataErrorException(InputDataErrorCode.IT_CAN_NOT_SEARCH_SOURCE_AND_TARGET_ON_LINE);
        }

        if (isNotConnectSourceAndTargetStation()) {
            throw new InputDataErrorException(InputDataErrorCode.IT_DO_NOT_CONNECT_STATIONS_EACH_OTHER);
        }
    }

    private boolean isNotConnectSourceAndTargetStation() {
        GraphPath<Station, SectionEdge> shortestPaths = findShortestPaths();
        return isEmptyPaths(shortestPaths);
    }

    private boolean isEmptyPaths(GraphPath<Station, SectionEdge> shortestPaths) {
        return shortestPaths == null;
    }

    private GraphPath<Station, SectionEdge> findShortestPaths() {
        DijkstraShortestPath<Station, SectionEdge> dijkstraAlg = new DijkstraShortestPath(subwayGraph);
        ShortestPathAlgorithm.SingleSourcePaths<Station, SectionEdge> singleSourcePaths = dijkstraAlg.getPaths(source);
        return singleSourcePaths.getPath(target);
    }

    public Distance findShortestPathDistance(List<Line> lines, List<Station> shortestPaths) {
        List<Station> shortestStations = shortestPaths;
        List<Section> foundSections = findSection(lines, shortestStations);
        int distanceSum = foundSections.stream().
                mapToInt(it -> it.getDistanceValue()).sum();
        return new Distance(distanceSum);
    }

    private List<Section> findSection(List<Line> lines, List<Station> shortestStations) {
        List<Section> allSections = findAllSections(lines);
        List<Section> foundSections = new ArrayList<>();
        for (int i = 0; i < shortestStations.size() - 1; i++) {
            searchFoundSections(shortestStations, allSections, foundSections, i);
        }
        return foundSections;
    }

    private List<Section> findAllSections(List<Line> lines) {
        List<Section> allSections = new ArrayList<>();
        for (Line line : lines) {
            allSections.addAll(line.getSections());
        }
        return allSections;
    }

    private void searchFoundSections(List<Station> shortestStations, List<Section> allSections, List<Section> foundSections, int i) {
        for (Section section : allSections) {
            addFoundSections(shortestStations, foundSections, i, section);
        }
    }

    private void addFoundSections(List<Station> shortestStations, List<Section> foundSections, int i, Section section) {
        if (isMatchSection(shortestStations, i, section)) {
            foundSections.add(section);
        }
    }

    private boolean isMatchSection(List<Station> shortestStations, int i, Section section) {
        return section.getUpStation().getName().equals(shortestStations.get(i).getName())
                && section.getDownStation().getName().equals(shortestStations.get(i + 1).getName());
    }

    private void checkValidationSourceAndTarget(Station source, Station target) {
        if (isNotExistStations(source, target)) {
            throw new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION);
        }

        if (source.equals(target)) {
            throw new InputDataErrorException(InputDataErrorCode.THERE_IS_SAME_STATIONS);
        }
    }

    private boolean isNotExistStations(Station source, Station target) {
        return source == null || target == null;
    }

    private boolean isMatchSourceAndTarget(List<Station> stations) {
        return stations.stream()
                .filter(it -> it.equals(source) || it.equals(target))
                .collect(toList()).size() == 2;
    }
}
