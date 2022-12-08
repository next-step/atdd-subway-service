package nextstep.subway.line.domain;

public class LineId {
    private final long lineId;

    private LineId(long lineId) {
        this.lineId = lineId;
    }

    public static LineId from(Long lineId) {
        return new LineId(lineId);
    }

    public Long getLong() {
        return lineId;
    }

    public String getString() {
        return Long.toString(lineId);
    }
}
