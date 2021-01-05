package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public PathStation getUpStation() {
        return upStation;
    }

    public PathStation getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PathSection)) return false;
        final PathSection that = (PathSection) o;
        return Objects.equals(upStation, that.upStation) &&
                Objects.equals(downStation, that.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }
}
