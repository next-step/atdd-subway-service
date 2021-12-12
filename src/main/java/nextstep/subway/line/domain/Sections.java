package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.line.exception.CannotSectionAddException;
import nextstep.subway.line.exception.DuplicateSectionException;
import nextstep.subway.line.exception.SectionNotRemovableException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int MINIMUM_SECTION_COUNT_LIMIT = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void removeStation(final Line line, final Station station) {
        validateMinimumSectionSize();
        validateRemovable(station);

        Optional<Section> upSection = getUpStationMatchSection(station);
        Optional<Section> downSection = getDownStationMatchSection(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            sections.add(Section.merge(line, upSection.get(), downSection.get()));
        }

        upSection.ifPresent(it -> sections.remove(it));
        downSection.ifPresent(it -> sections.remove(it));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findUpStation();
        stations.add(station);
        Optional<Section> matchSection = getUpStationMatchSection(station);

        while (matchSection.isPresent()) {
            station = matchSection.get().getDownStation();
            matchSection = getUpStationMatchSection(station);
            stations.add(station);
        }

        return stations;
    }

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        validateNotDuplicate(section);
        validateAddable(section);

        updateUpStationMatchSection(section);
        updateDownStationMatchSection(section);

        sections.add(section);
    }

    private Station findUpStation() {
        Station station = sections.get(0).getUpStation();
        Optional<Section> upSection = getDownStationMatchSection(station);

        while (upSection.isPresent()) {
            station = upSection.get().getUpStation();
            upSection = getDownStationMatchSection(station);
        }

        return station;
    }

    private Optional<Section> getDownStationMatchSection(final Station downStation) {
        return sections.stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst();
    }

    private Optional<Section> getUpStationMatchSection(final Station upStation) {
        return sections.stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst();
    }

    private void validateRemovable(final Station station) {
        if (!isStationExists(station)) {
            throw new SectionNotRemovableException("노선에 등록되어 있지 않은 역을 제거할 수 없습니다.");
        }
    }

    private void validateMinimumSectionSize() {
        if (sections.size() <= MINIMUM_SECTION_COUNT_LIMIT) {
            throw new SectionNotRemovableException(
                String.format("노선에는 최소 %d개의 구간이 있어야 합니다.", MINIMUM_SECTION_COUNT_LIMIT));
        }
    }

    private void updateUpStationMatchSection(final Section targetSection) {
        Optional<Section> matchSection = getUpStationMatchSection(targetSection.getUpStation());
        matchSection.ifPresent(
            it -> it.updateUpStation(targetSection.getDownStation(), targetSection.getDistance())
        );
    }

    private void updateDownStationMatchSection(final Section targetSection) {
        Optional<Section> matchSection = getDownStationMatchSection(targetSection.getDownStation());
        matchSection.ifPresent(
            it -> it.updateDownStation(targetSection.getUpStation(), targetSection.getDistance())
        );
    }

    private boolean isStationExists(final Station station) {
        return sections.stream().anyMatch(section -> section.hasAnyMatchStation(station));
    }

    private void validateAddable(final Section section) {
        boolean matchStationsNotExists =
            !isStationExists(section.getUpStation()) && !isStationExists(section.getDownStation());

        if (matchStationsNotExists) {
            throw new CannotSectionAddException();
        }
    }

    private void validateNotDuplicate(final Section targetSection) {
        sections.stream()
            .filter(section -> section.hasSameStations(targetSection))
            .findFirst()
            .ifPresent(section -> {
                throw new DuplicateSectionException();
            });
    }
}
