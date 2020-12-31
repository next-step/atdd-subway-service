package nextstep.subway.path.domain;

import java.util.Objects;

public class SafeSectionInfo {
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public SafeSectionInfo(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SafeSectionInfo that = (SafeSectionInfo) o;
        return distance == that.distance && Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationId, downStationId, distance);
    }
}
