package nextstep.subway.line.domain.wrappers;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

import javax.persistence.Embeddable;
import java.util.*;

@Embeddable
public class Sections {
    private static final String DUPLICATE_SECTION_ERROR_MESSAGE = "%s, %s 구간은 이미 등록된 구간 입니다.";
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void addSection(Section section) {
        checkValidDuplicateSection(section);
        sections.add(section);
    }

    public List<Station> stations() {
        List<Station> stations = new ArrayList<>();
        if (sections.isEmpty()) {
            return stations;
        }
        Section preSection = findFirstSection();
        stations.add(preSection.getUpStation());
        while (preSection != null) {
            Station downStation = preSection.getDownStation();
            stations.add(downStation);
            preSection = findNextSection(preSection);
        }
        return stations;
    }

    private Section findFirstSection() {
        Section section = new Section();
        for (Section st : sections) {
            section = st.calcFirstSection(section);
        }
        return section;
    }

    private Section findNextSection(Section section) {
        return sections.stream().filter(st -> st.isNextSection(section)).findFirst().orElse(null);
    }

    private void checkValidDuplicateSection(Section section) {
        if (sections.contains(section)) {
            throw new IllegalArgumentException(
                    String.format(DUPLICATE_SECTION_ERROR_MESSAGE, section.getUpStation(), section.getDownStation()));
        }
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Sections sections1 = (Sections) object;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}
