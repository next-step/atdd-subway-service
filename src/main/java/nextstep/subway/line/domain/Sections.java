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

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }
        return sortedStation();
    }

    public void add(Section newSection) {
        validateNotContainsAny(newSection);
        validateAlreadyContainsAll(newSection);
        updateUpStation(newSection);
        updateDownStation(newSection);
        this.sections.add(newSection);
    }

    public void removeLineStation(Station station) {
        validateSectionSize();
        validateNotContainsStation(station);
        Optional<Section> upSection = findUpSection(station);
        Optional<Section> downSection = findDownSection(station);

        removeSection(upSection, downSection);

        if (upSection.isPresent() && downSection.isPresent()) {
            this.add(merge(upSection.get(), downSection.get()));
        }
    }

    private Section merge(Section upSection, Section downSection) {
        return upSection.merge(downSection);
    }

    private void validateNotContainsAny(Section section) {
        List<Station> stations = this.getStations();
        if (stations.isEmpty()) {
            return;
        }
        if (getStations().stream().noneMatch(station -> section.getStations().contains(station))) {
            throw new IllegalArgumentException("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없습니다.");
        }
    }

    private void validateAlreadyContainsAll(Section section) {
        if (getStations().containsAll(section.getStations())) {
            throw new IllegalArgumentException("상행역과 하행역이 이미 노선에 모두 등록되어 있습니다.");
        }
    }

    private void updateUpStation(Section newSection) {
        sections.stream()
            .filter(it -> it.hasUpStation(newSection.getUpStation()))
            .findFirst()
            .ifPresent(it -> it.updateUpStation(newSection.getDownStation(), newSection.getDistance()));
    }

    private void updateDownStation(Section newSection) {
        sections.stream()
            .filter(it -> it.hasDownStation(newSection.getDownStation()))
            .findFirst()
            .ifPresent(it -> it.updateDownStation(newSection.getUpStation(), newSection.getDistance()));
    }

    private void validateSectionSize() {
        if (getSections().size() <= MINIMUM_SECTIONS_SIZE) {
            throw new IllegalStateException("더 이상 구간을 제거할 수 없습니다.");
        }
    }

    private void validateNotContainsStation(Station station) {
        if (!this.getStations().contains(station)) {
            throw new IllegalArgumentException("노선에 등록되어있지 않은 역입니다.");
        }
    }

    private Optional<Section> findUpSection(Station station) {
        Optional<Section> upSection = this.sections.stream()
            .filter(section -> section.hasDownStation(station))
            .findAny();
        return upSection;
    }

    private Optional<Section> findDownSection(Station station) {
        Optional<Section> downSection = this.sections.stream()
            .filter(section -> section.hasUpStation(station))
            .findAny();
        return downSection;
    }

    private void removeSection(Optional<Section> upSection, Optional<Section> downSection) {
        upSection.ifPresent(section -> this.sections.remove(section));
        downSection.ifPresent(section -> this.sections.remove(section));
    }

    public List<Section> getSections() {
        return sections;
    }

    private List<Station> sortedStation() {
        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (isPresentNextSectionByStation(downStation)) {
            Station finalDownStation = downStation;
            downStation = findNextStation(finalDownStation).getDownStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (isPresentPrevSectionByStation(downStation)) {
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

    private boolean isPresentPrevSectionByStation(Station station) {
        return sections.stream()
            .anyMatch(it -> it.hasDownStation(station));
    }

    private boolean isPresentNextSectionByStation(Station station) {
        return sections.stream()
            .anyMatch(it -> it.hasUpStation(station));
    }

}
