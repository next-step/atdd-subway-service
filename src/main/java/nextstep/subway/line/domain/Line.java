package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    public static final String LINE_NEGATIVE_CHARGE_EXCEPTION_MESSAGE = "노선 추가 금액이 0보다 작을 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @Embedded
    private final Sections sections = new Sections();
    private int charge;

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

    public Line(String name, String color, Station upStation, Station downStation, int distance, int charge) {
        this.name = name;
        this.color = color;
        validateCharge(charge);
        this.charge = charge;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    private void validateCharge(int charge) {
        if (charge < 0) {
            throw new IllegalArgumentException(LINE_NEGATIVE_CHARGE_EXCEPTION_MESSAGE);
        }
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

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void removeStation(Station station) {
        sections.removeSection(this, station);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Section> getUnmodifiableSectionList() {
        return sections.getSections();
    }

    public int getCharge() {
        return charge;
    }
}
