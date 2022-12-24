package nextstep.subway.line.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fee.domain.StationFee;
import nextstep.subway.fee.domain.StationFeeRepository;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.SectionEdge;
import nextstep.subway.utils.SubwayGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.fee.domain.AgePriceStrategy.calculatePrice;
import static nextstep.subway.fee.domain.StationFeeStrategy.STANDARD_FEE;

@Service
@Transactional
public class PathService {

    private StationRepository stationRepository;
    private SectionRepository sectionRepository;
    private StationFeeRepository stationFeeRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository
            , StationFeeRepository stationFeeRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.stationFeeRepository = stationFeeRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse getStationPathInfo(final String sourceStationId, final String targetStationid, LoginMember loginMember) {
        int totalDistance = 0;
        int totalFee = STANDARD_FEE;
        List<StationResponse> shortestPath = getShortestPath(sourceStationId, targetStationid);
        totalDistance = findTotalDistance(shortestPath);

        Optional<StationFee> stationFee = stationFeeRepository.findStationFee(totalDistance);
        if (stationFee.isPresent()) {
            totalFee += stationFee.get().getAdditionalFee(totalDistance);
        }

        return PathResponse.of(totalFee + findLineMaxAdditionalFee(shortestPath) - calculatePrice(loginMember.getAge()), totalDistance, shortestPath);
    }

    private int findLineMaxAdditionalFee(List<StationResponse> stationResponses) {
        int maxAdditionFee = 0;
        for (int i = 0; i < stationResponses.size() - 1; i++) {
            maxAdditionFee = Math.max(maxAdditionFee, findLineAdditionalFee(stationResponses.get(i).getId(), stationResponses.get(i+1).getId()));
        }
        return maxAdditionFee;
    }

    private int findLineAdditionalFee(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(RuntimeException::new);
        Section section = sectionRepository.findByUpStationAndDownStation(sourceStation, targetStation)
                .orElseThrow(RuntimeException::new);

        return section.getLine().getAdditionalFee();
    }

    private int findTotalDistance(List<StationResponse> stationResponses) {
        int totalDistance = 0;
        for (int i = 0; i < stationResponses.size() - 1; i++) {
            totalDistance += findDistance(stationResponses.get(i).getId(), stationResponses.get(i+1).getId());
        }
        return totalDistance;
    }

    private int findDistance(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(RuntimeException::new);

        Optional<Section> section = sectionRepository.findByUpStationAndDownStation(sourceStation, targetStation);

        return section.isPresent() ? section.get().getDistance() : 0;
    }

    public List<StationResponse> getShortestPath(final String sourceStationId, final String targetStationid) {
        Station sourceStation = stationFind(sourceStationId);
        Station targetStation = stationFind(targetStationid);

        validCheckForSameStation(sourceStationId, targetStationid);

        List<Station> stations = stationRepository.findAll();
        List<Station> shortestPath = getShortestPath(sourceStation, targetStation, stations);
        return getShortestPathResponse(stations, shortestPath);
    }

    private Station stationFind(String stationId) {
        return stationRepository.findById(Long.valueOf(stationId))
                .orElseThrow(RuntimeException::new);
    }

    private void validCheckForSameStation(final String sourceStationId, final String targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new RuntimeException();
        }
    }

    List<Station> getShortestPath(Station sourceStation, Station targetStation, List<Station> stations) {
        List<Section> sections = sectionRepository.findAll();
        List<Station> shortestPath;

        try {
            shortestPath = getShortestPath(sections, stations)
                    .getPath(sourceStation, targetStation)
                    .getVertexList();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return shortestPath;
    }

    private DijkstraShortestPath<Station, SectionEdge> getShortestPath(List<Section> sections, List<Station> stations) {
        SubwayGraph<Station, SectionEdge> graph = new SubwayGraph<>(SectionEdge.class);

        graph.addGraphVertex(stations);
        graph.addEdgeWeight(sections);

        return new DijkstraShortestPath<>(graph);
    }

    private List<StationResponse> getShortestPathResponse(List<Station> stations, List<Station> shortestPath) {
        List<StationResponse> shortestStations = new ArrayList<>();

        for (Station innerStation : shortestPath) {
            Optional<Station> station = stations.stream()
                    .filter(it -> it.equals(innerStation))
                    .findFirst();

            station.ifPresent(it -> shortestStations.add(StationResponse.of(it)));
        }

        return shortestStations;
    }
}
