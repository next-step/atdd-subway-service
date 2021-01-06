package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections(Section section) {
        sections.add(section);
    }

    protected Sections() {
    }

    public void add(Section section) {
        validateSection(section);
        updateSection(section);
    }

    private void updateSection(Section section) {
        findDownSectionBy(section.getUpStation())
                .ifPresent(it -> it.updateUpStation(section));
        findUpSectionBy(section.getDownStation())
                .ifPresent(it -> it.updateDownStation(section));
        sections.add(section);
    }

    public List<Station> getStations() {
        Station station = findFirstUpStation();
        List<Station> result = new ArrayList<>(Collections.singletonList(station));
        Optional<Section> nextSection = findNextSection(station);
        while (nextSection.isPresent()) {
            Station nextStation = nextSection.get().getDownStation();
            result.add(nextStation);
            nextSection = findNextSection(nextStation);
        }
        return result;
    }

    public void deleteStation(Station station) {
        if (isDeletable()) {
            throw new IllegalStateException("구간이 1개이면 역을 삭제할 수 없습니다.");
        }
        Optional<Section> upSection = findDownSectionBy(station);
        Optional<Section> downSection = findUpSectionBy(station);
        if (upSection.isPresent() && downSection.isPresent()) {
            sections.add(upSection.get().merge(downSection.get()));
        }
        upSection.ifPresent(this::remove);
        downSection.ifPresent(this::remove);
    }

    public Optional<Section> findDownSectionBy(Station baseStation) {
        return sections.stream()
                .filter(it -> it.getUpStation() == baseStation)
                .findFirst();
    }

    public Optional<Section> findUpSectionBy(Station baseStation) {
        return sections.stream()
                .filter(it -> it.getDownStation() == baseStation)
                .findFirst();
    }

    private void validateSection(Section section) {
        List<Station> stations = getStations();
        if (isAlreadyRegistered(section, stations)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
        if (isNotRegistered(section)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean isAlreadyRegistered(Section section, List<Station> stations) {
        return stations.contains(section.getUpStation()) && stations.contains(section.getDownStation());
    }

    private boolean isNotRegistered(Section section) {
        List<Station> stations = getStations();
        return !stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation());
    }

    private boolean isDeletable() {
        return sections.size() <= 1;
    }

    private void remove(Section section) {
        sections.remove(section);
    }

    private Station findFirstUpStation() {
        return sections.stream()
                .map(Section::getUpStation)
                .filter(this::matchUpStation)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("상행역이 존재하지 않습니다."));
    }

    private boolean matchUpStation(Station upStation) {
        return sections.stream()
                .noneMatch(section -> section.getDownStation() == upStation);
    }

    private Optional<Section> findNextSection(Station station) {
        return sections.stream()
                .filter(s -> s.getUpStation() == station)
                .findAny();
    }
}
