package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortedSections {
    private List<Section> sections;

    public SortedSections(List<Section> sections) {
        this.sections = sort(sections);
    }

    private List<Section> sort(List<Section> sections) {
        List<Section> results = new ArrayList<>();

        if (sections == null || sections.isEmpty()) {
            return results;
        }

        List<Section> copiedSections = new ArrayList<>(sections);

        TopSection topSection = new TopSection(copiedSections);

        while (topSection.hasTopSection()) {
            Section section = topSection.getTopSection();
            results.add(section);
            copiedSections.remove(section);

            topSection = new TopSection(copiedSections);
        }

        return results;
    }

    List<Section> toCollection() {
        return Collections.unmodifiableList(sections);
    }
}
