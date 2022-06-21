package nextstep.subway.line.domain;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Lines implements Iterable<Line> {
    private final List<Line> lineList;

    public Lines(List<Line> lineList) {
        this.lineList = lineList;
    }

    public Line mostExpensive() {
        return this.lineList.stream().max((o1, o2) -> Math.toIntExact((o1.getExtraFare() - o2.getExtraFare())))
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public Iterator<Line> iterator() {
        return this.lineList.iterator();
    }
}
