package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LineNew {
    private Long id;
    private String name;
    private String color;
    private LineSections sections;

    protected LineNew() {
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
        this.sections = new LineSections(Arrays.asList(new SectionNew(this, upStation, downStation, distance)));
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
        return this.sections.getStations();
    }

    public void setSection(List<SectionNew> sections) {
        this.sections = new LineSections(sections);
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        SectionNew newSection = new SectionNew(this, upStation, downStation, distance);
        this.sections.addSection(newSection);
    }

    public void removeStation(Station station) {
        if (!this.sections.isRemovable()) {
            throw new RuntimeException();
        }

        this.sections.removeStation(this, station);
    }
}
