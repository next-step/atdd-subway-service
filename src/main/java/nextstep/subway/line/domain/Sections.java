package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    protected Sections() {
    }

    public static Sections create() {
        return new Sections();
    }

    public void addSection(Section section) {
        boolean isUpStationExisted = isExisted(section.getUpStation());
        boolean isDownStationExisted = isExisted(section.getDownStation());

        valid(isUpStationExisted, isDownStationExisted);

        values.add(section);
    }

    private boolean isExisted(Station upStation) {
        return orderBySection().stream().anyMatch(it -> it == upStation);
    }

    private void valid(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }

        if (!isEmpty() && !isUpStationExisted && !isDownStationExisted) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public List<Station> orderBySection() {
        List<Station> stations = new ArrayList<>();
        Station station = findFirstStation();
        stations.add(station);

        while (isPresentNextSection(station)) {
            Section nextSection = findNextSection(station);
            station = nextSection.getDownStation();
            stations.add(station);
        }
        return stations;
    }

    private Station findFirstStation() {
        Station upStation = values.get(0).getUpStation();
        while (isPresentPreSection(upStation)) {
            Section nextSection = findPreSection(upStation);
            upStation = nextSection.getUpStation();
        }
        return upStation;
    }

    private boolean isPresentPreSection(Station station) {
        return values.stream()
                .filter(Section::existDownStation)
                .anyMatch(it -> it.equalDownStation(station));
    }

    private boolean isPresentNextSection(Station station) {
        return values.stream()
                .filter(Section::existUpStation)
                .anyMatch(it -> it.equalUpStation(station));
    }

    private Section findPreSection(Station station) {
        return values.stream()
                .filter(it -> it.equalDownStation(station))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이전 구간이 없습니다."));
    }

    private Section findNextSection(Station down) {
        return values.stream()
                .filter(it -> it.equalUpStation(down))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("다음 구간이 없습니다."));
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public List<Section> getValues() {
        return values;
    }
}
