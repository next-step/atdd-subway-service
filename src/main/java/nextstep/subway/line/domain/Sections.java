package nextstep.subway.line.domain;

import nextstep.subway.line.consts.ErrorMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    private static final int MINIMUM_SECTION_LENGTH = 1;

    protected Sections() {
    }

    private Sections(Section section) {
        this.sections.add(section);
    }

    public static Sections from(Section section) {
        return new Sections(section);
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findPostSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findFirstStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findPreviousSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addSection(Section sectionToAdd) {
        validateSectionToAdd(sectionToAdd);
        updateAdjacentSection(sectionToAdd);
        sections.add(sectionToAdd);
    }

    private void updateAdjacentSection(Section sectionToAdd) {
        sections.stream().forEach(section -> section.updateSection(sectionToAdd));
    }

    private void validateSectionToAdd(Section sectionToAdd) {
        List<Station> stations = getStations();
        boolean upStationExists = stations.contains(sectionToAdd.getUpStation());
        boolean downStationExists = stations.contains(sectionToAdd.getDownStation());

        if (upStationExists && downStationExists) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_SECTION_ADD_ALREADY_REGISTERED);
        }
        if (!stations.isEmpty() && !upStationExists && !downStationExists) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_SECTION_ADD_UNKNOWN_STATIONS);
        }
    }

    public void removeStation(Station station) {
        validateStationToRemove(station);
        removeValidStation(station);
    }

    private void removeValidStation(Station station) {
        Optional<Section> postSection = findPostSection(station);
        Optional<Section> previousSection = findPreviousSection(station);
        if (isMiddleStation(postSection, previousSection)) {
            removeMiddleStation(postSection.get(), previousSection.get());
            return;
        }
        removeEndStation(postSection, previousSection);
    }

    private boolean isMiddleStation(Optional<Section> postSection, Optional<Section> previousSection) {
        return postSection.isPresent() && previousSection.isPresent();
    }

    private void removeMiddleStation(Section postSection, Section previousSection) {
        previousSection.mergeWith(postSection);
        sections.remove(postSection);
    }

    private void removeEndStation(Optional<Section> postSection, Optional<Section> previousSection) {
        postSection.ifPresent(it -> sections.remove(it));
        previousSection.ifPresent(it -> sections.remove(it));
    }

    private Optional<Section> findPreviousSection(Station station) {
        return sections.stream()
                .filter(section -> section.downStationEquals(station))
                .findFirst();
    }

    private Optional<Section> findPostSection(Station station) {
        return sections.stream()
                .filter(section -> section.upStationEquals(station))
                .findFirst();
    }

    private void validateStationToRemove(Station station) {
        validateSectionLength();
        validateStationExists(station);
    }

    private void validateSectionLength() {
        if (sections.size() <= MINIMUM_SECTION_LENGTH) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessage.ERROR_SECTION_DELETE_MINIMUM_LENGTH, MINIMUM_SECTION_LENGTH)
            );
        }
    }

    private void validateStationExists(Station station) {
        if (!getStations().contains(station)) {
            throw new IllegalArgumentException(
                    String.format(ErrorMessage.ERROR_SECTION_DELETE_UNKNOWN_STATION, station.getName())
            );
        }
    }
}
