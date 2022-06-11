package nextstep.subway.line.domain;

import nextstep.subway.line.consts.ErrorMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MINIMUM_SECTION_LENGTH = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(Section section) {
        this.sections.add(section);
    }

    public static Sections from(Section section) {
        return new Sections(section);
    }

    public List<Station> getStations() {
        sortSections();
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        if(!stations.isEmpty()) {
            stations.add(getLastDownStation());
        }
        return stations;
    }

    private Station getLastDownStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public void addSection(Section sectionToAdd) {
        validateSectionToAdd(sectionToAdd);
        updateAdjacentSection(sectionToAdd);
        sections.add(sectionToAdd);
    }

    private void validateSectionToAdd(Section sectionToAdd) {
        List<Station> stations = getStations();
        boolean upStationExists = stations.contains(sectionToAdd.getUpStation());
        boolean downStationExists = stations.contains(sectionToAdd.getDownStation());
        validateSectionDuplicated(upStationExists, downStationExists);
        validateSectionAddable(stations, upStationExists, downStationExists);
    }

    private void validateSectionDuplicated(boolean upStationExists, boolean downStationExists) {
        if (upStationExists && downStationExists) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_SECTION_ADD_ALREADY_REGISTERED);
        }
    }

    private void validateSectionAddable(List<Station> stations, boolean upStationExists, boolean downStationExists) {
        if (!stations.isEmpty() && !upStationExists && !downStationExists) {
            throw new IllegalArgumentException(ErrorMessage.ERROR_SECTION_ADD_UNKNOWN_STATIONS);
        }
    }

    private void updateAdjacentSection(Section sectionToAdd) {
        sections.stream().forEach(section -> section.updateWith(sectionToAdd));
    }

    public void removeStation(Station station) {
        validateStationToRemove(station);
        removeValidStation(station);
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

    private void removeValidStation(Station station) {
        Optional<Section> postSection = findPostSection(station);
        Optional<Section> previousSection = findPreviousSection(station);
        if (isMiddleStation(postSection.orElse(null), previousSection.orElse(null))) {
            removeMiddleStation(postSection.get(), previousSection.get());
            return;
        }
        removeEndStation(postSection.orElse(null), previousSection.orElse(null));
    }

    private boolean isMiddleStation(Section postSection, Section previousSection) {
        return postSection != null && previousSection != null;
    }

    private void removeMiddleStation(Section postSection, Section previousSection) {
        previousSection.mergeWith(postSection);
        sections.remove(postSection);
    }

    private void removeEndStation(Section postSection, Section previousSection) {
        if(postSection != null){
            sections.remove(postSection);
            return;
        }
        if (previousSection != null) {
            sections.remove(previousSection);
        }
    }

    private Optional<Section> findPreviousSection(Station station) {
        return sections.stream()
                .filter(section -> section.isDownStationEqualTo(station))
                .findFirst();
    }

    private Optional<Section> findPostSection(Station station) {
        return sections.stream()
                .filter(section -> section.isUpStationEqualTo(station))
                .findFirst();
    }

    private void sortSections() {
        sections.sort((Section section1, Section section2) -> {
            if (section1.isDownStationEqualTo(section2.getUpStation())) {
                return -1;
            }
            return 1;
        });
    }
}
