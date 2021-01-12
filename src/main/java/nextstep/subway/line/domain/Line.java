package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.common.Fare;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Iterator;
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
	private Fare fare;

    @Embedded
    private LineSections sections;

    public Line() {
    }

	public Line(String name, String color, Station upStation, Station downStation, int distance, int fare) {
		this(name, color, fare);
		this.sections = new LineSections(new Section(this, upStation, downStation, distance));
	}

	private Line(String name, String color, int fare) {
		this.name = name;
		this.color = color;
		this.fare = new Fare(fare);
	}

	public void addLineStation(Station upStation, Station downStation, int distance) {
        sections.addLineStation(new Section(this, upStation, downStation, distance));
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public void removeLineStation(Station station) {
        sections.removeLineStation(station);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Iterator<Section> getSections() {
    	return this.sections.iterator();
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

	public Fare getFare() {
		return fare;
	}
}
