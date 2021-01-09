package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PathSection {

    private final Long lineId;

    private final PathStation upStation;

    private final PathStation downStation;

    private final int distance;

    public PathSection(final PathStation upStation, final PathStation downStation, final int distance) {
        this(null, upStation, downStation, distance);
    }

    public PathSection(final Long lineId, final PathStation upStation, final PathStation downStation, final int distance) {
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public PathSection reverse() {
        return new PathSection(lineId, downStation, upStation, distance);
    }

    public boolean contains(final PathStation source, final PathStation target) {
        return hasSameStations(source, target) || hasReverseStations(source, target);
    }

    private boolean hasSameStations(final PathStation source, final PathStation target) {
        return upStation.equals(source) && downStation.equals(target);
    }

    public boolean hasReverseStations(final PathStation source, final PathStation target) {
        return upStation.equals(target) && downStation.equals(source);
    }

    public Long getLineId() {
        return lineId;
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
