package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathVertexStation;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathFinder {

    public static final int BASE_FARE = 1250;                       // 기본 요금
    public static final int SALE_RATE_FOR_TEENAGER = 20;            // 청소년 할인율
    public static final int SALE_RATE_FOR_CHILDREN = 50;            // 어린이 할인율
    public static final int ADULT_BOUND_AGE = 19;                   // 성인 나이 기준
    public static final int TEENAGER_BOUND_AGE = 13;                // 청소년 나이 기준
    public static final int CHILDREN_BOUND_AGE = 6;                 // 어린이 나이 기준
    public static final int ADDITIONAL_OVER_DISTANCE_BOUND = 50;    // 추가 운임 거리 기준 : 최대 구간
    public static final int OVER_DISTANCE_BOUND = 10;               // 추가 운임 거리 기준 : 중간 구간
    
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathFinder(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    /**
     * 주어진 출발-도착 역 ID로 지하철역을 찾고, 등록된 전체 구간에서 최단 경로를 구합니다.
     * @param sourceStationId
     * @param targetStationId
     * @return
     */
    public PathResponse getShortestPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
        Optional<Station> sourceStation = this.stationRepository.findById(sourceStationId);
        Optional<Station> targetStation = this.stationRepository.findById(targetStationId);

        if(sourceStation.isPresent() && targetStation.isPresent()) {
            return this.getShortestPath(loginMember, sourceStation.get(), targetStation.get());
        }

        throw new IllegalArgumentException("존재하지 않은 지하철역이 있습니다.");
    }

    /**
     * 주어진 출발-도착 역으로 지금까지 등록된 전체 구간에서 최단 경로를 구합니다.
     * @param sourceStation
     * @param targetStation
     * @return 최단 경로
     */
    public PathResponse getShortestPath(LoginMember loginMember, Station sourceStation, Station targetStation) {
        this.equalsSourceAndTargetOccurredException(sourceStation, targetStation);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);;

        this.addGraphAllLines(graph);

        GraphPath<Station, DefaultWeightedEdge> shortestPath
                = new DijkstraShortestPath<Station, DefaultWeightedEdge>(graph)
                    .getPath(sourceStation, targetStation);

        int distance = (int) shortestPath.getWeight();
        return new PathResponse(shortestPath.getVertexList().stream()
                                    .map(PathVertexStation::of).collect(Collectors.toList())
                                , distance
                                , this.calculateFare(loginMember, shortestPath.getVertexList(), distance));
    }

    /**
     * 주어진 두 지하철 역이 같은 경우 최단 경로를 구하지 않고 예외를 발생합니다.
     * @param sourceStation
     * @param targetStation
     */
    private void equalsSourceAndTargetOccurredException(Station sourceStation, Station targetStation) {
        if(sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같은 경우 경로를 구할 수 없습니다.");
        }
    }

    /**
     * 저장된 모든 노선의 구간 정보를 추가합니다.
     * @param graph
     */
    private void addGraphAllLines(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Line> persistLines = lineRepository.findAll();

        persistLines.stream().map(Line::getSections)
                .forEach(sections -> sections
                        .forEach(section -> this.addGraph(graph, section)));
    }

    /**
     * 구간정보와 역 정보를 추가합니다.
     * @param section
     */
    private void addGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.addVertex(section.getUpStation());
        graph.addVertex(section.getDownStation());
        this.addEdgeWeight(graph, section);
    }

    /**
     * 구간 정보를 추가합니다.
     * @param section
     */
    private void addEdgeWeight(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.setEdgeWeight(
                graph.addEdge(section.getUpStation(), section.getDownStation())
                , section.getDistance());
    }

    /**
     * 최단 경로를 기반으로 운임을 계산합니다.
     * @param loginMember
     * @param vertexList
     * @param distance
     * @return
     */
    public int calculateFare(LoginMember loginMember, List<Station> vertexList, int distance) {
        Integer age = loginMember.getAge();
        return this.saleForAge(PathFinder.BASE_FARE + this.getMaxLineFare(vertexList) + this.getOverFareByDistance(distance), age);
    }

    /**
     * 사용자가 할인 받을 수 있는 나이면 주어진 운임에 할인 값을 적용 합니다.
     * @param fare
     * @param age
     * @return
     */
    private int saleForAge(int fare, int age) {
        if(this.isTeenager(age)) {
            return this.calculateSaleFare(fare, PathFinder.SALE_RATE_FOR_TEENAGER);
        }

        if(this.isChildren(age)) {
            return this.calculateSaleFare(fare, PathFinder.SALE_RATE_FOR_CHILDREN);
        }

        return fare;
    }

    /**
     * 사용자가 어린이인지 확인 합니다.
     * @param age
     * @return
     */
    private boolean isChildren(int age) {
        return age >= PathFinder.CHILDREN_BOUND_AGE && age < PathFinder.TEENAGER_BOUND_AGE;
    }

    /**
     * 사용자가 청소년인지 확인 합니다.
     * @param age
     * @return
     */
    private boolean isTeenager(int age) {
        return age >= PathFinder.TEENAGER_BOUND_AGE && age < PathFinder.ADULT_BOUND_AGE;
    }

    /**
     * 비율에 따라 할인 값을 적용하여 계산합니다.
     * @param fare
     * @param saleRate
     * @return
     */
    private int calculateSaleFare(int fare, int saleRate) {
        return (int) (((100 - saleRate) * 0.01) * fare);
    }

    /**
     * 노선의 추가 요금을 구합니다.
     * 단, 여러 노선을 거치는 경우 가장 큰 추가요금을 반환합니다.
     * @param vertexList
     * @return
     */
    private int getMaxLineFare(List<Station> vertexList) {
        List<Line> persistLines = this.lineRepository.findAll();
        List<Integer> lineFares = getLineFares(vertaxListToFindSectionInfos(vertexList), persistLines);

        return lineFares.stream().max(Integer::compareTo).orElse(0);
    }

    /**
     * 최단경로에 포함 되어 있는 구간의 노선 추가 요금을 구합니다.
     * @param findSectionInfos
     * @param persistLines
     * @return
     */
    private List<Integer> getLineFares(List<Map.Entry<Station, Station>> findSectionInfos, List<Line> persistLines) {
        List<Integer> lineFares = new ArrayList<>();
        for (Line line : persistLines) {
            this.addLineFareFromSections(findSectionInfos, lineFares, line);
        }
        return lineFares;
    }

    /**
     * 최단경로를 찾는 구간의 상행역, 하행역 정보로 바꿉니다.
     * @param vertexList
     * @return
     */
    private List<Map.Entry<Station, Station>> vertaxListToFindSectionInfos(List<Station> vertexList) {
        List<Map.Entry<Station, Station>> findSections = new ArrayList<>();

        vertexList.stream().reduce((upStation, downStation)
                -> { findSections.add(new AbstractMap.SimpleEntry<Station, Station>(upStation, downStation));
                     return downStation;
        });

        return findSections;
    }

    /**
     * 노선의 구간에 주어진 구간이 있으면, 해당 노선의 추가 운임을 목록에 추가합니다.
     * @param findSectionInfos
     * @param lineFares
     * @param line
     */
    private void addLineFareFromSections(List<Map.Entry<Station, Station>> findSectionInfos
            , List<Integer> lineFares, Line line) {
        Sections sections = new Sections(line.getSections());

        findSectionInfos.stream().filter(findSectionInfo
                -> sections.existSection(findSectionInfo.getKey(), findSectionInfo.getValue()))
                .findAny()
                .ifPresent(yes -> lineFares.add(line.getSurcharge()));
    }


    /**
     * 최단 경로의 거리 값으로 거리에 따른 추가 운임을 구합니다.
     * @param distance
     * @return
     */
    private int getOverFareByDistance(int distance) {
        if(this.isOverFareDistance(distance)) {
            return this.calculateOverFareByDistance(distance);
        }

        if(this.isAdditionalOverFareDistance(distance)) {
            return this.calculateAdditionalOverFareDistance(distance);
        }

        return 0;
    }


    /**
     * 추가 운임 구간인 경우의 추가 운임을 계산합니다.
     * @param distance
     * @return
     */
    private int calculateAdditionalOverFareDistance(int distance) {
        return this.calculateOverFareByDistance(PathFinder.ADDITIONAL_OVER_DISTANCE_BOUND)
                + (int) ((Math.ceil(((distance - PathFinder.OVER_DISTANCE_BOUND) - 1) / 8) + 1) * 100);
    }

    /**
     * 추가 운임 기본 구간인 경우 추가 운임을 계산합니다.
     * @param distance
     * @return
     */
    private int calculateOverFareByDistance(int distance) {
        return (int) ((Math.ceil(((distance - PathFinder.OVER_DISTANCE_BOUND) - 1) / 5) + 1) * 100);
    }

    /**
     * 추가 운임 구간인지 반환합니다.
     * @param distance
     * @return
     */
    private boolean isOverFareDistance(int distance) {
        return distance > PathFinder.OVER_DISTANCE_BOUND
                && distance <= PathFinder.ADDITIONAL_OVER_DISTANCE_BOUND;
    }

    /**
     * 추가 운임 기본 구간인지 반환합니다
     * @param distance
     * @return
     */
    private boolean isAdditionalOverFareDistance(int distance) {
        return distance > PathFinder.ADDITIONAL_OVER_DISTANCE_BOUND;
    }
}
