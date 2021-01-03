package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LineNew {
    private Long id;
    private String name;
    private String color;
    private LineSections sections;

    public LineNew() {
    }

    public LineNew(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public LineNew(Long id, String name, String color, List<SectionNew> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new LineSections(sections);
    }

    public LineNew(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new SectionNew(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<SectionNew> getSections() {
        return sections.getSections();
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalUpStation = downStation;
            Optional<SectionNew> nextLineStation = this.sections.findByUpStation(finalUpStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public void setSection(List<SectionNew> sections) {
        this.sections = new LineSections(sections);
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        List<Station> stations = this.getStations();
        boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
        boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
            stations.stream().noneMatch(it -> it == downStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (stations.isEmpty()) {
            this.sections.add(new SectionNew(this, upStation, downStation, distance));
            return;
        }

        if (isUpStationExisted) {
            this.sections.findByUpStation(upStation)
                .ifPresent(it -> it.updateUpStation(downStation, distance));
            SectionNew newSection = new SectionNew(this, upStation, downStation, distance);
            this.sections.add(newSection);
        } else if (isDownStationExisted) {
            this.sections.findByDownStation(downStation)
                .ifPresent(it -> it.updateDownStation(upStation, distance));

            this.sections.add(new SectionNew(this, upStation, downStation, distance));
        } else {
            throw new RuntimeException();
        }
    }

    private Station findUpStation() {
        Station upStation = this.sections.findFirstUpstation();
        while (upStation != null) {
            Station finalDownStation = upStation;
            Optional<SectionNew> nextLineStation = this.sections.findByDownStation(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            upStation = nextLineStation.get().getUpStation();
        }

        return upStation;
    }
}
