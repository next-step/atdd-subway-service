package nextstep.subway.path.domain.strategy;

import nextstep.subway.station.domain.Station;

import java.util.List;

public interface PathStrategy {
    List<Station> find();
}
