package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    private static final int MIN_SECTION_NUMBER = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if(!sections.isEmpty()) {
            List<Station> stations = getStationsInOrder();
            relocate(section, stations);
        }
        sections.add(section);
    }

    private void relocate(Section section, List<Station> stations) {
        boolean isUpStationExisted = stations.stream().anyMatch(section::isSameUpStation);
        boolean isDownStationExisted = stations.stream().anyMatch(section::isSameDownStation);

        validateDuplicateSection(isUpStationExisted, isDownStationExisted);
        validateNoneMatchStation(section, stations);

        if(isUpStationExisted) {
            mergeUpStation(section);
        }
        if(isDownStationExisted) {
            mergeDownStation(section);
        }
    }

    private void mergeUpStation(Section addSection) {
        findMergeTargetUpStation(addSection)
                .ifPresent(section -> section.updateUpStation(addSection.getDownStation(), addSection.getDistance()));
    }

    private void mergeDownStation(Section addSection) {
        findMergeTargetDownStation(addSection)
                .ifPresent(section -> section.updateDownStation(addSection.getUpStation(), addSection.getDistance()));
    }

    private Optional<Section> findMergeTargetUpStation(Section addSection) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(addSection.getUpStation()))
                .findFirst();
    }

    private Optional<Section> findMergeTargetDownStation(Section addSection) {
        return sections.stream()
                .filter(section -> section.isSameUpStation(addSection.getUpStation()))
                .findFirst();
    }

    private void validateDuplicateSection(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void validateNoneMatchStation(Section section, List<Station> stations) {
        if (stations.stream().noneMatch(section::isSameAnyStation) ) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findFirstUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .filter(this::isStartStation)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isStartStation(Station upStation) {
        return sections.stream()
                .noneMatch(section -> section.getDownStation().equals(upStation));
    }

    public void deleteStation(Station station, Line line) {
        validateLastSection();

        Optional<Section> upLineStation = sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(section -> section.getDownStation() == station)
                .findFirst();

        validatePresentStation(upLineStation, downLineStation);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            upLineStation.get().getDistance().plus(downLineStation.get().getDistance());
            sections.add(new Section(line, newUpStation, newDownStation, upLineStation.get().getDistance().getValue()));
        }

        upLineStation.ifPresent(section -> sections.remove(section));
        downLineStation.ifPresent(section -> sections.remove(section));
    }

    private void validateLastSection() {
        if (sections.size() <= MIN_SECTION_NUMBER) {
            throw new IllegalArgumentException("마지막 구간은 삭제할 수 없음");
        }
    }

    private void validatePresentStation(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if(!upLineStation.isPresent() && !downLineStation.isPresent()) {
            throw new IllegalArgumentException("노선에 등록되어있지 않은 역입니다.");
        }
    }

}
