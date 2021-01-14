package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.line.dto.ExistedStationValue;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
	private static final int MIN_STATION_SIZE = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private String color;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected Line() {
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

	public Line(LineRequest request, List<Station> findStations) {
		this.name = request.getName();
		this.color = request.getColor();
		sections.add(new Section(this, findStations.get(0), findStations.get(1), request.getDistance()));
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

	public List<Section> getSections() {
		return sections;
	}

	public List<StationResponse> getStationResponses() {
		List<StationResponse> stations = getStations().stream()
				.map(it -> StationResponse.of(it))
				.collect(Collectors.toList());

		return stations;
	}

	public List<Station> getStations() {
		if (this.getSections().isEmpty()) {
			return Arrays.asList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = this.getSections().stream()
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

	public Station findUpStation() {
		Station downStation = this.getSections().get(0).getUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = this.getSections().stream()
					.filter(it -> it.getDownStation() == finalDownStation)
					.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getUpStation();
		}
		return downStation;
	}


	public void addLineStation(Station upStation, Station downStation, int distance) {
		ExistedStationValue existedStationValue = validate(upStation, downStation, distance);

		if (existedStationValue.isUpStationExisted()) {
			this.getSections().stream()
					.filter(it -> it.getUpStation() == upStation)
					.findFirst()
					.ifPresent(it -> it.updateUpStation(downStation, distance));

			this.getSections().add(new Section(this, upStation, downStation, distance));
			return;
		}

		if (existedStationValue.isDownStationExisted()) {
			this.getSections().stream()
					.filter(it -> it.getDownStation() == downStation)
					.findFirst()
					.ifPresent(it -> it.updateDownStation(upStation, distance));

			this.getSections().add(new Section(this, upStation, downStation, distance));
			return;
		}
	}

	public ExistedStationValue validate(Station upStation, Station downStation, int distance) {
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
			this.getSections().add(new Section(this, upStation, downStation, distance));
		}

		return new ExistedStationValue(isUpStationExisted, isDownStationExisted);
	}

	public void validateMinSize() {
		if (this.getSections().size() <= MIN_STATION_SIZE) {
			throw new RuntimeException("구간은 최소 1개 이상이어야 합니다.");
		}
	}

	public void removeLineStation(Station station) {
		this.validateMinSize();

		Optional<Section> upLineStation = this.getSections().stream()
				.filter(it -> it.getUpStation() == station)
				.findFirst();
		Optional<Section> downLineStation = this.getSections().stream()
				.filter(it -> it.getDownStation() == station)
				.findFirst();

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
			this.getSections().add(new Section(this, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> this.getSections().remove(it));
		downLineStation.ifPresent(it -> this.getSections().remove(it));

	}

}
