package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    private static final String ERROR_MESSAGE_EXISTS_SECTION = "이미 등록된 구간 입니다.";
    private static final String ERROR_MESSAGE_NOT_CONNECTED_SECTION = "등록할 수 없는 구간 입니다.";
    private static final String ERROR_MESSAGE_CAN_NOT_REMOVE = "단일 구간은 제거할 수 없습니다.";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return getStationsInternal();
    }

    private List<Station> getStationsInternal() {
        List<Station> stations = new ArrayList<>();
        Station nextUpStation = findFirstUpStation();
        stations.add(nextUpStation);

        while (nextUpStation != null) {
            Optional<Section> nextSection = findNextSection(nextUpStation);
            if (!nextSection.isPresent()) {
                break;
            }
            nextUpStation = nextSection.get().getDownStation();
            stations.add(nextUpStation);
        }

        return stations;
    }

    private Optional<Section> findNextSection(Station station) {
        return sections.stream()
                .filter(section -> section.equalUpStation(station))
                .findFirst();
    }

    public void addSection(Section newSection) {
        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }

        addSectionInternal(newSection);
    }

    private void addSectionInternal(Section newSection) {
        boolean isUpStationExisted = isExistedStation(newSection.getUpStation());
        boolean isDownStationExisted = isExistedStation(newSection.getDownStation());

        validateAddSection(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateRightSection(newSection);
            sections.add(newSection);
            return;
        }

        if (isDownStationExisted) {
            updateLeftSection(newSection);
            sections.add(newSection);
        }
    }

    private boolean isExistedStation(Station station) {
        return sections.stream().anyMatch(section -> section.hasStation(station));
    }

    private void validateAddSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        validateAlreadyExistsSection(isUpStationExisted, isDownStationExisted);
        validateConnectedSection(isUpStationExisted, isDownStationExisted);
    }

    private void validateAlreadyExistsSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalStateException(ERROR_MESSAGE_EXISTS_SECTION);
        }
    }

    private void validateConnectedSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!sections.isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalStateException(ERROR_MESSAGE_NOT_CONNECTED_SECTION);
        }
    }

    private void updateRightSection(Section newSection) {
        sections.stream()
                .filter(it -> it.getUpStation() == newSection.getUpStation())
                .findFirst()
                .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
    }

    private void updateLeftSection(Section newSection) {
        sections.stream()
                .filter(it -> it.getDownStation() == newSection.getDownStation())
                .findFirst()
                .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
    }

    public void removeSection(Station station) {
        validateSectionOnlyOne();
        removeSectionInternal(station);
    }

    private void removeSectionInternal(Station station) {
        Optional<Section> previousSection = findPreviousSection(station);
        Optional<Section> nextSection = findNextSection(station);

        if (previousSection.isPresent() && nextSection.isPresent()) {
            mergeAndRemoveSection(previousSection.get(), nextSection.get());
            return;
        }

        previousSection.ifPresent(it -> sections.remove(it));
        nextSection.ifPresent(it -> sections.remove(it));
    }

    private void mergeAndRemoveSection(Section previousSection, Section nextSection) {
        Station newDownStation = nextSection.getDownStation();
        previousSection.updateForCombine(newDownStation, nextSection.getDistance());
        sections.remove(nextSection);
    }

    private Optional<Section> findPreviousSection(Station station) {
        return sections.stream()
                .filter(section -> section.equalDownStation(station))
                .findFirst();
    }

    private void validateSectionOnlyOne() {
        if (sections.size() <= 1) {
            throw new IndexOutOfBoundsException(ERROR_MESSAGE_CAN_NOT_REMOVE);
        }
    }

    public Station findFirstUpStation() {
        Station nextDownStation = sections.get(0).getUpStation();
        while (nextDownStation != null) {
            Optional<Section> previousSection = findPreviousSection(nextDownStation);
            if (!previousSection.isPresent()) {
                break;
            }

            nextDownStation = previousSection.get().getUpStation();
        }

        return nextDownStation;
    }
}
