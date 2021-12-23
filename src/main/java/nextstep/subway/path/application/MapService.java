package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResponseAssembler;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {

    private StationRepository stationRepository;
    private LineRepository lineRepository;
    private PathService pathService;

    public MapService(StationRepository stationRepository, LineRepository lineRepository, PathService pathService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathService = pathService;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationRepository.findById(source).orElseThrow(() -> new NotFoundException("출발역이 존재하지 않습니다. Id : " + source));
        Station targetStation = stationRepository.findById(target).orElseThrow(() -> new NotFoundException("도착역이 존재하지 않습니다. Id : " + target));
        SubwayPath path = pathService.findPath(lines, sourceStation, targetStation);
        path.discountByAge(loginMember.getAge());
        return PathResponseAssembler.assemble(path);
    }
}
