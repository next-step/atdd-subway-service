package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Lines {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public Line findShortDistance(Station source, Station target) {
//        lines.stream()
//                .filter(item -> item.containsStationsExactly(source, target))
//                .min((l1, l2) -> {
//                    l1.calcDistance(source, target, )
//                })
//                .min

        return null;
    }

}
