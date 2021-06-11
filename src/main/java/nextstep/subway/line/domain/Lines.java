package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = new ArrayList<>(lines);
    }

    public List<LineResponse> toResponses() {
        return lines.stream()
                .map(item -> LineResponse.of(item, item.sortedStation2().toResponses()))
                .collect(Collectors.toList());
    }
}
