package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void add(Station upStation, Station downStation, int distance) {
        validateAddable(upStation, downStation);
        sections.add(new Section(this, upStation, downStation, distance));
    }

    private void validateAddable(Station upStation, Station downStation) {
        if (contains(upStation) && contains(downStation)) {
            throw new IllegalArgumentException("이미 등록된 구간 입니다.");
        }
        if (!isEmpty() && !contains(upStation) && !contains(downStation)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void remove(Station station) {
        sections.remove(this, station);
    }

    public List<Station> stations() {
        return sections.stations();
    }

    private boolean contains(Station station) {
        return sections.contains(station);
    }

    private boolean isEmpty() {
        return sections.isEmpty();
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

}
