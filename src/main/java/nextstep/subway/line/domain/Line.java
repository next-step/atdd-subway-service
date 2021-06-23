package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.line.domain.wrapped.LineColor;
import nextstep.subway.line.domain.wrapped.LineName;
import nextstep.subway.station.domain.Station;
import nextstep.subway.wrapped.Money;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LineName name;
    private LineColor color;

    private Money money;

    private Sections sections = new Sections();

    protected Line() {
    }


    public Line(String name, String color, int money, Station upStation, Station downStation, int distance) {
        this(name, color, money, new Section(upStation, downStation, new Distance(distance)));
    }

    public Line(String name, String color, int money, Section section) {
        this(name, color, money);

        addSection(section);
    }

    public Line(String name, String color) {
        this(name, color, 0);
    }

    public Line(String name, String color, int money) {
        this(new LineName(name), new LineColor(color), new Money(money));
    }

    public Line(LineName name, LineColor color, Money money) {
        this.name = name;
        this.color = color;
        this.money = money;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.money = line.getMoney();
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return name;
    }

    public LineColor getColor() {
        return color;
    }

    public Money getMoney() {
        return money;
    }

    public SortedStations sortedStation() {
        return sections.toSortedStations();
    }

    public void removeStation(Station station) {
        Optional<Section> newSection = sections.removeStation(station);
        newSection.ifPresent(this::addSection);
    }

    public void addSection(Section section) {
        section.changeLine(this);
        sections.add(section);
    }

    public boolean containsStationsExactly(Station ...stations) {
        return sections.containsStationsExactly(stations);
    }

    public boolean containsStation(Station station) {
        return sections.containsStation(station);
    }

    public Sections getSections() {
        return sections;
    }


}
