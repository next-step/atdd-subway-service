package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;
import org.springframework.util.CollectionUtils;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        if (CollectionUtils.isEmpty(sections)) {
            return Collections.emptyList();
        }

        Optional<Section> firstSection = getFirstSection();

        Set<Station> result = new LinkedHashSet<>();
        while (firstSection.isPresent()) {
            Section currentSection = firstSection.get();
            result.addAll(currentSection.getStations());
            firstSection = sections.stream()
                    .filter(section -> Objects.equals(section.getUpStation(), currentSection.getDownStation()))
                    .findFirst();
        }
        return new ArrayList<>(result);
    }

    private Optional<Section> getFirstSection() {
        Section currentSection = null;
        Optional<Section> anySection = sections.stream()
                .findFirst();

        while (anySection.isPresent()) {
            currentSection = anySection.get();
            anySection = getPreviousSection(currentSection);
        }
        return Optional.ofNullable(currentSection);
    }

    private Optional<Section> getPreviousSection(Section currentSection) {
        return sections.stream()
                .filter(section -> Objects.equals(section.getDownStation(), currentSection.getUpStation()))
                .findFirst();
    }
}
