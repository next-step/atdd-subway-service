package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Transactional(readOnly = true)
    public PathResponse findPath(Long source, Long target) {

        Station sourceStation = stationRepository.findById(source).orElseThrow(SubwayException::new);
        Station targetStation = stationRepository.findById(target).orElseThrow(SubwayException::new);
        List<Line> lines = lineRepository.findAll();



        return null;
    }

}
