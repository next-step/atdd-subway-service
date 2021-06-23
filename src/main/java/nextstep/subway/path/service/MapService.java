package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MapService {

    public Optional<Station> findPaths(List<Line> lines, Station upStation, Station downStation) {


        return null;
    }
}
