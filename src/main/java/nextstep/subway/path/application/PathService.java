package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathService {
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findRouteSearch(Long source, Long target, LoginMember user) {
        Station station1 = stationRepository.findById(source).orElseThrow(IllegalArgumentException::new);
        Station station2 = stationRepository.findById(target).orElseThrow(IllegalArgumentException::new);
        PathFinder pathFinder = new PathFinder();
        pathFinder.findRouteSearch(station1, station2, lineRepository.findAll(), user.getAge());
        List<Station> stations = new ArrayList();
        for(Long id: pathFinder.getStation()){
            stations.add(stationRepository.findById(id).orElseThrow(IllegalArgumentException::new));
        }
        return new PathResponse(stations, pathFinder.getDistance(), pathFinder.getTotalFee());
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

}
