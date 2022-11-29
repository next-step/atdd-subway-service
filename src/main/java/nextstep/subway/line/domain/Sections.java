package nextstep.subway.line.domain;

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
    private static final int MINIMUM_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void add(Section newSection) {
        this.sections.add(newSection);
    }

    public void removeLineStation(Station station) {
        validate();

        Optional<Section> upLineStation = removeUpSection(station);
        Optional<Section> downLineStation = removeDownSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            this.add(mergeSection(upLineStation, downLineStation));
        }
    }

    private Section mergeSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        Station newUpStation = downLineStation.get().getUpStation();
        Station newDownStation = upLineStation.get().getDownStation();
        int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
        Section newSection = new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance);
        return newSection;
    }

    private Optional<Section> removeUpSection(Station station) {
        Optional<Section> upSection = this.sections.stream()
            .filter(section -> section.hasUpStation(station))
            .findAny();
        upSection.ifPresent(section -> this.sections.remove(section));
        return upSection;
    }

    private Optional<Section> removeDownSection(Station station) {
        Optional<Section> downSection = this.sections.stream()
            .filter(section -> section.hasDownStation(station))
            .findAny();
        downSection.ifPresent(section -> this.sections.remove(section));
        return downSection;
    }

    private void validate() {
        if (getSections().size() <= MINIMUM_SECTIONS_SIZE) {
            throw new IllegalStateException("더 이상 구간을 제거할 수 없습니다.");
        }
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }
        return sortedStation();
    }

    public void updateUpStation(Station upStation, Station downStation, int distance) {
        getSections().stream()
            .filter(it -> it.getUpStation() == upStation)
            .findFirst()
            .ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    public void updateDownStation(Station upStation, Station downStation, int distance) {
        getSections().stream()
            .filter(it -> it.getDownStation() == downStation)
            .findFirst()
            .ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private List<Station> sortedStation() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (isPresentNextSection(downStation)) {
            Station finalDownStation = downStation;
            downStation = findNextStation(finalDownStation).getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (isPresentPreSection(downStation)) {
            Station finalDownStation = downStation;
            downStation = findPrevStation(finalDownStation).getUpStation();
        }
        return downStation;
    }

    private Section findPrevStation(Station station) {
        return this.sections.stream()
            .filter(it -> it.hasDownStation(station))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("이전 구간이 없습니다."));
    }

    private Section findNextStation(Station station) {
        return this.sections.stream()
            .filter(it -> it.hasUpStation(station))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("다음 구간이 없습니다."));
    }

    private boolean isPresentPreSection(Station station) {
        return sections.stream()
            .filter(Section::existDownStation)
            .anyMatch(it -> it.hasDownStation(station));
    }

    private boolean isPresentNextSection(Station station) {
        return sections.stream()
            .filter(Section::existUpStation)
            .anyMatch(it -> it.hasUpStation(station));
    }

    public List<Section> getSections() {
        return sections;
    }
}
