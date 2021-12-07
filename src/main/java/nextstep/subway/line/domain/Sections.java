package nextstep.subway.line.domain;

import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.CannotUpdateException;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findFirstUpStation();
        stations.add(station);

        while (isExistNextSection(station)) {
            Section nextSection = findNextSection(station);
            station = nextSection.getDownStation();
            stations.add(station);
        }
        return stations;
    }

    private Station findFirstUpStation() {
        Station upStation = sections.get(0).getUpStation();
        while (isExistPreSection(upStation)) {
            Section preSection = findPreSection(upStation);
            upStation = preSection.getUpStation();
        }
        return upStation;
    }

    private boolean isExistPreSection(Station upStation) {
        return sections.stream()
                .anyMatch(section -> section.equalsDownStation(upStation));
    }

    private Section findPreSection(Station upStation) {
        return sections.stream()
                .filter(section -> section.equalsDownStation(upStation))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("이전 구간 없습니다."));
    }

    private boolean isExistNextSection(Station downStation) {
        return sections.stream()
                .anyMatch(section -> section.equalsUpStation(downStation));
    }

    private Section findNextSection(Station downStation) {
        return sections.stream()
                .filter(section -> section.equalsUpStation(downStation))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("다음 구간 없습니다."));
    }

    void addLineSection(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStationsInOrder();
        boolean isUpStationExisted = isStationExisted(upStation, stations);
        boolean isDownStationExisted = isStationExisted(downStation, stations);

        validateStation(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            updateUpStation(upStation, downStation, distance);
        }

        if (isDownStationExisted) {
            updateDownStation(upStation, downStation, distance);
        }

        sections.add(new Section(line, upStation, downStation, distance));
    }

    private boolean isStationExisted(Station upStation, List<Station> stations) {
        return stations.stream().anyMatch(it -> it == upStation);
    }

    private void validateStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new CannotUpdateException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new CannotUpdateException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.equalsUpStation(upStation))
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance) {
        sections.stream()
                .filter(it -> it.equalsDownStation(downStation))
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    void removeLineSection(Line line, Station station) {
        validateRemoveSection();
        Section downStationSection = getDownStationSection(station);
        Section upStationSection = getUpStationSection(station);

        if(downStationSection != null && upStationSection != null) {
            Station newUpStation = downStationSection.getUpStation();
            Station newDownStation = upStationSection.getDownStation();
            int newDistance = upStationSection.getDistance() + downStationSection.getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        sections.remove(downStationSection);
        sections.remove(upStationSection);
    }

    private void validateRemoveSection() {
        if (sections.size() <= 1) {
            throw new CannotDeleteException("구간이 하나인 노선은 역을 제거할 수 없음");
        }
    }

    private Section getDownStationSection(Station station) {
        Section downStationSection = sections.stream()
                .filter(it -> it.equalsDownStation(station))
                .findFirst()
                .orElse(null);
        return downStationSection;
    }

    private Section getUpStationSection(Station station) {
        Section upStationSection = sections.stream()
                .filter(it -> it.equalsUpStation(station))
                .findFirst()
                .orElse(null);
        return upStationSection;
    }
}
