package nextstep.subway.path.application;

import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PathFinder {

    public Optional<Station> getTransferStation(Station sourceStation, Station targetStation) {
        Station station = new Station(4L,"결과역");
        return Optional.of(station);
    }
}
