package nextstep.subway.line.domain;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.BaseEntity;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StringUtils;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Color color;
    @Embedded
    private Sections sections;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = Name.from(name);
        this.color = Color.from(color);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        validateUpStation(upStation);
        validateDownStation(downStation);

        Section section = Section.of(this, upStation, downStation, distance);
        this.name = Name.from(name);
        this.color = Color.from(color);
        this.sections = Sections.from(singletonList(section));
    }

    private void validateUpStation(Station upStation) {
        if(upStation == null) {
            throw new IllegalArgumentException(ErrorCode.상행종착역은_비어있을_수_없음.getErrorMessage());
        }
    }

    private void validateDownStation(Station downStation) {
        if(downStation == null) {
            throw new IllegalArgumentException(ErrorCode.하행종착역은_비어있을_수_없음.getErrorMessage());
        }
    }

    public void update(Line line) {
        this.name = line.name;
        this.color = line.color;
    }

    public void updateLineAndColor(String name, String color) {
        if(!StringUtils.isNullOrEmpty(name)) {
            updateLineName(name);
        }
        if(!StringUtils.isNullOrEmpty(color)) {
            updateLineColor(color);
        }
    }

    private void updateLineName(String name) {
        this.name = Name.from(name);
    }

    private void updateLineColor(String color) {
        this.color = Color.from(color);
    }

    public void addSection(Section section) {
        sections.addSection(section);
    }

    public List<Station> findStations() {
        return sections.findStations();
    }

    public List<Station> findInOrderStations() {
        return sections.findInOrderStations();
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.getSections(); // TODO 리팩토링 필요
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(getName(), line.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
