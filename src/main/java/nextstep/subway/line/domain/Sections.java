package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.common.CannotSectionAddException;
import nextstep.subway.common.DuplicateSectionException;
import nextstep.subway.common.SectionNotRemovableException;
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

    public Optional<Section> getDownStationMatchSection(final Station downStation) {
        return sections.stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst();
    }

    public Optional<Section> getUpStationMatchSection(final Station upStation) {
        return sections.stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst();
    }

    public void removeStation(final Line line, final Station station) {
        validateMinimumSectionSize();

        Optional<Section> upLineStation = getUpStationMatchSection(station);
        Optional<Section> downLineStation = getDownStationMatchSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            Distance newDistance = upLineStation.get().getDistance()
                .add(downLineStation.get().getDistance());
            sections.add(Section.of(line, newUpStation, newDownStation, newDistance.getDistance()));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void validateMinimumSectionSize() {
        if (sections.size() <= MINIMUM_SECTION_COUNT_LIMIT) {
            throw new SectionNotRemovableException(
                String.format("노선에는 최소 %d개의 구간이 있어야 합니다.", MINIMUM_SECTION_COUNT_LIMIT));
        }
    }

    public Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextLineStation = getDownStationMatchSection(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextLineStation = getUpStationMatchSection(downStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
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
