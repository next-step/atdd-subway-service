package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Lines {

    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public List<Line> getLines() {
        return lines;
    }

    public int calculateAddPrice(List<Station> stations) {
        int maxAddPrice = 0;
        for (Line line : lines) {
            maxAddPrice = Math.max(maxAddPrice, line.getAddPriceIfContains(stations));
        }
        return maxAddPrice;
    }
}
