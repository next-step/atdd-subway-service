package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PathService {
    private StationRepository stationRepository;
    private PathFinder pathFinder;

    public PathService(StationRepository stationRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        checkSameStation(source, target);

        ShortestPath shortestPath = pathFinder.findShortestPath(source, target);

//        Station sourceStation = stationRepository.findById(source).orElseThrow(
//                () -> new IllegalArgumentException("등록되지 않은 역입니다."));
//
//        Station targetStation = stationRepository.findById(target).orElseThrow(
//                () -> new IllegalArgumentException("등록되지 않은 역입니다."));
//
//        Station linkStation = pathFinder.getTransferStation(sourceStation,targetStation).orElseThrow(
//                () -> new IllegalArgumentException("연결된 경로가 없습니다."));


        return new PathResponse(shortestPath.getStations(), shortestPath.getDistance());
    }




    private void checkSameStation(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }


}
