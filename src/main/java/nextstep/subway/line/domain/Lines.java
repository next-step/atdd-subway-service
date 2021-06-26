package nextstep.subway.line.domain;

import java.util.List;
import java.util.function.Consumer;

public class Lines {

    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public void handleLinesSection(Consumer<Section> consumer) {
        lines.forEach(line -> line.getSections().forEach(consumer));
    }

    public int maximumAdditionalFee() {
        return lines.stream()
                .mapToInt(Line::getAdditionalFee)
                .max()
                .orElse(0);
    }
}
