package nextstep.subway.path.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.station.domain.Station;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Path {
    private final List<Station> stations;
    private final int distance;
}
