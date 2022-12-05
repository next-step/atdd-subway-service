package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
    private static final int SINGLE_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    public void addSection(Section section) {
        validate(section);
        for (Section target : sections) {
            target.rebase(section);
        }
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

    public void removeStation(Station station) {
        Optional<Section> upSection = getUpSection(station);
        Optional<Section> downSection = getDownSection(station);
        validateSectionSize();
        if (isAllTrue(upSection.isPresent(), downSection.isPresent())) {
            mergeMiddleSection(upSection.get(), downSection.get());
        }
        if (!isAllTrue(upSection.isPresent(), downSection.isPresent())) {
            upSection.ifPresent(sections::remove);
            downSection.ifPresent(sections::remove);
        }
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

    private void validate(Section section) {
        if (sections.isEmpty()) {
            return;
        }
        Set<Station> stations = new HashSet<>(getStations());
        Set<Station> newStations = new HashSet<>(section.getStations());
        boolean isDuplicatedStations = stations.containsAll(newStations);
        boolean isNotMatchedStation = newStations.stream().noneMatch(stations::contains);
        if (isDuplicatedStations) {
            throw new IllegalArgumentException("추가 하고자 하는 상행역과 하행역이 이미 노선에 등록되어 있습니다");
        }
        if (isNotMatchedStation) {
            throw new IllegalArgumentException("추가 하고자 하는 상행역과 하행역 중 매칭되는 역이 없습니다");
        }
    }

    private Optional<Section> getUpSection(Station station) {
        return sections.stream()
                .filter(upSection -> upSection.getDownStation().equals(station))
                .findFirst();
    }

    private Optional<Section> getDownSection(Station station) {
        return sections.stream()
                .filter(downSection -> downSection.getUpStation().equals(station))
                .findFirst();
    }

    private void validateSectionSize() {
        if (sections.size() == SINGLE_SECTION) {
            throw new IllegalArgumentException("하나 남은 구간의 종점은 삭제할 수 없습니다.");
        }
    }

    private void mergeMiddleSection(Section upSection, Section downSection) {
        downSection.mergeUpStation(upSection);
        sections.remove(upSection);
    }

    private boolean isAllTrue(Boolean... booleans) {
        return Arrays.stream(booleans).allMatch(bool -> Boolean.TRUE == bool);
    }
}
