package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> value = new ArrayList<>();

    public Lines(Collection<Line> lines) {
        this.value.addAll(lines);
    }

    public List<LineResponse> toResponse() {
        return this.value.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public void forEach(Consumer<Line> action) {
        this.value.forEach(action);
    }

    public LineFare getMaxAdditionalFare() {
        return this.value.stream()
                .max(Comparator.comparing(line -> line.getAdditionalFare().getFare()))
                .orElseThrow(() -> new IllegalStateException("노선 정보가 없습니다."))
                .getAdditionalFare();
    }

    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    public List<Line> getValue() {
        return value;
    }
}
