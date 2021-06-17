package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponses {
    private final List<LineResponse> lineResponses;

    public LineResponses(List<Line> lines) {
        this.lineResponses = convertToDTO(lines);
    }

    public int size() {
        return lineResponses.size();
    }

    public List<LineResponse> toCollection() {
        return Collections.unmodifiableList(lineResponses);
    }

    private List<LineResponse> convertToDTO(List<Line> lines) {
        return lines.stream()
                .map(item -> LineResponse.of(item, new StationResponses(item.sortedStation())))
                .collect(Collectors.toList());
    }
}
