package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    private static final int MIN_SECTION_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validateAddExisted(section);
        validateAddNothingExisted(section);
        updateSection(section);
        sections.add(section);
    }

    private void validateAddExisted(Section section) {
        if (getStations().containsAll(section.getStations())) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
    }

    private void validateAddNothingExisted(Section section) {
        List<Station> stations = getStations();
        if (section.getStations().stream().noneMatch(stations::contains)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateSection(Section section) {
        sections.stream()
                .forEach(it -> it.update(section));
    }

    public List<Section> get() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        return getSortedStations();
    }

    private List<Station> getSortedStations() {
        List<Station> stations = new ArrayList<>();
        Station firstStation = findFirstStation();
        stations.add(firstStation);

        Station currentStation = firstStation;
        while (currentStation != null) {
            Optional<Station> nextStation = findNextStation(currentStation);
            nextStation.ifPresent(stations::add);
            currentStation = nextStation.orElseGet(() -> null);
        }
        return stations;
    }

    private Station findFirstStation() {
        Station upStation = sections.get(0).getUpStation();
        while (upStation != null) {
            Optional<Station> prevStation = findPrevStation(upStation);
            if (!prevStation.isPresent()) {
                break;
            }
            upStation = prevStation.get();
        }
        return upStation;
    }

    private Optional<Station> findPrevStation(Station station) {
        Optional<Section> prevSection = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
        if (prevSection.isPresent()) {
            return Optional.of(prevSection.get().getUpStation());
        }
        return Optional.empty();
    }

    private Optional<Station> findNextStation(Station station) {
        Optional<Section> nextSection = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        if (nextSection.isPresent()) {
            return Optional.of(nextSection.get().getDownStation());
        }
        return Optional.empty();
    }

    public void remove(Station station) {
        validateRemoveLastSection();
        Optional<Section> upSection = findUpSection(station);
        Optional<Section> downSection = findDownSection(station);

        if (isMiddleSection(upSection, downSection)) {
            removeMiddleSection(upSection.get(), downSection.get());
            return;
        }

        removeEndSection(upSection, downSection);
    }

    private void validateRemoveLastSection() {
        if (sections.size() <= MIN_SECTION_SIZE) {
            throw new IllegalArgumentException("더이상 구간을 삭제할 수 없습니다.");
        }
    }

    private Optional<Section> findUpSection(Station station) {
        return sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();
    }

    private Optional<Section> findDownSection(Station station) {
        return sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
    }

    private boolean isMiddleSection(Optional<Section> upSection, Optional<Section> downSection) {
        return upSection.isPresent() && downSection.isPresent();
    }

    private void removeMiddleSection(Section upSection, Section downSection) {
        sections.add(new Section.Builder()
                .line(upSection.getLine())
                .upStation(upSection.getUpStation())
                .downStation(downSection.getDownStation())
                .distance(upSection.getDistance() + downSection.getDistance())
                .build());
        sections.remove(upSection);
        sections.remove(downSection);
    }

    private void removeEndSection(Optional<Section> upSection, Optional<Section> downSection) {
        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }
}
