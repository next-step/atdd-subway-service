package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.InvalidLineInPathException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LineOfStationInPath {
    private static final int MULTI_LINE_BOUNDARY = 1;

    private final List<Long> lineIds;

    public LineOfStationInPath(List<Long> lineIds) {
        validate(lineIds);
        this.lineIds = new ArrayList<>(lineIds);
    }

    private void validate(List<Long> lineIds) {
        if (lineIds.isEmpty()) {
            throw new InvalidLineInPathException("최소 한개 이상의 노선이 있어야 합니다.");
        }
    }

    public boolean isMultiLine() {
        return this.lineIds.size() > MULTI_LINE_BOUNDARY;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineOfStationInPath that = (LineOfStationInPath) o;
        return this.lineIds.equals(that.lineIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineIds);
    }
}
