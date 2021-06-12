package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class TopSection {
    private Section topSection;

    public TopSection(List<Section> sections) {
        this.topSection = findTopSection(sections);
    }

    private Section findTopSection(List<Section> sections) {
        if (sections.isEmpty()) {
            return null;
        }

        Section lastTopSection = sections.get(0);

        Section section = null;
        do {
            section = lastTopSection;

            final Section finalSection = section;

            lastTopSection = sections.stream()
                    .filter(item -> item.isDownStationEqualsUpStationBy(finalSection))
                    .findFirst()
                    .orElse(section);
        } while(section != lastTopSection);

        return section;
    }

    public Section getTopSection() {
        return topSection;
    }

    public boolean hasTopSection() {
        return topSection != null;
    }
}
