package nextstep.subway.path.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(of = {"upStation", "downStation"})
@Getter
public class PathSection {

    private final PathStation upStation;

    private final PathStation downStation;

    private final int distance;

    public PathSection(final PathStation upStation, final PathStation downStation, final int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public List<PathStation> getStations() {
        return Arrays.asList(upStation, downStation);
    }
}
