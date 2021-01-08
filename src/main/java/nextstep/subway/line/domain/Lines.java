package nextstep.subway.line.domain;

import nextstep.subway.path.dto.PathSection;
import nextstep.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    public Lines(Line... lines) {
        this.lines = Arrays.asList(lines);
    }

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<PathSection> allSection() {
        return lines.stream()
                .flatMap(line -> line.getSection().stream())
                .map(PathSection::of)
                .collect(Collectors.toList());
    }

    public Station searchStationById(long source) {
        return this.lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList())
                .stream()
                .filter(station -> station.getId().equals(source))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 출발역이나 도착역입니다."));
    }

    public List<Line> getLines() {
        return lines;
    }
}

