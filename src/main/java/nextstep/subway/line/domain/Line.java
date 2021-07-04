package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

@Entity
public class Line extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

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

	public static Line of(LineRequest request, Station upStation, Station downStation) {
		return new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance());
	}

	public static Line of(LineRequest lineUpdateRequest) {
		return new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
	}

	public void update(Line line) {
		this.name = line.name();
		this.color = line.color();
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String color() {
		return color;
	}

	public List<Section> sections() {
		return sections;
	}

	public List<Station> stations() {
		if (sections.isEmpty()) {
			return Arrays.asList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = sections().stream()
				.filter(it -> it.upStation() == finalDownStation)
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().downStation();
			stations.add(downStation);
		}

		return stations;
	}

	private Station findUpStation() {
		Station downStation = sections.get(0).upStation();
		while (downStation != null) {
			Station finalUpStation = downStation;
			Optional<Section> nextLineStation = sections.stream()
				.filter(it -> it.downStation() == finalUpStation)
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().upStation();
		}

		return downStation;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Line)) {
			return false;
		}
		Line line = (Line)object;
		return Objects.equals(id, line.id)
			&& Objects.equals(name, line.name)
			&& Objects.equals(color, line.color)
			&& sections.containsAll(line.sections)
			&& line.sections.containsAll(sections);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, color, sections);
	}
}
