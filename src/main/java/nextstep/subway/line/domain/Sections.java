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

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
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
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateAddNothingExisted(Section section) {
        List<Station> stations = getStations();
        if (section.getStations().stream().noneMatch(stations::contains)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateSection(Section section) {
        sections.stream()
                .forEach(it -> it.update(section.getUpStation(), section.getDownStation(), section.getDistance()));
    }

    public List<Section> get() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
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

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
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
            throw new RuntimeException("더이상 구간을 삭제할 수 없습니다.");
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
        sections.add(
                new Section(upSection.getLine(), upSection.getUpStation(), downSection.getDownStation(), upSection.getDistance() + downSection.getDistance()));
        sections.remove(upSection);
        sections.remove(downSection);
    }

    private void removeEndSection(Optional<Section> upSection, Optional<Section> downSection) {
        upSection.ifPresent(sections::remove);
        downSection.ifPresent(sections::remove);
    }
}
