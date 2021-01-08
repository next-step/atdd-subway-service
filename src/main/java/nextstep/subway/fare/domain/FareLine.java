package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathStation;

import java.util.List;
import java.util.stream.IntStream;

public class FareLine {
    private final List<Line> lines;

    public FareLine(List<Line> lines) {
        this.lines = lines;
    }

    public int getAmountFare(List<PathStation> stations) {
        return findExchangeLine(stations);
    }

    private int findExchangeLine(List<PathStation> stations) {
        return IntStream.range(1, stations.size())
                .mapToObj(index -> findLineOverFare(stations.get(index - 1).getId(), stations.get(index).getId()))
                .mapToInt(v -> v)
                .max()
                .orElse(0);
    }

    private int findLineOverFare(long source, long target) {
        return this.lines.stream()
                .filter(line -> line.hasSection(source, target))
                .map(Line::getOverFare)
                .findFirst()
                .orElse(0);
    }
}
