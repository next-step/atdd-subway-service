package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private final List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	public Sections(List<Section> sections) {
		this.sections.addAll(sections);
	}

	public Sections(Section... sections) {
		this(Arrays.asList(sections));
	}

	public List<Section> getSections() {
		return sections;
	}

	public List<Station> getStations() {
		return this.sections.stream()
			.sorted()
			.flatMap(section -> section.getStations().stream())
			.distinct()
			.collect(Collectors.toList());
	}

	public void addSection(final Section section) {

		List<Station> stations = this.getStations();
		validate(stations, section);

		boolean isUpStationExisted = stations.stream().anyMatch(it -> it == section.getUpStation());
		boolean isDownStationExisted = stations.stream().anyMatch(it -> it == section.getDownStation());

		if (stations.isEmpty()) {
			this.sections.add(section);
			return;
		}

		if (isUpStationExisted) {
			this.getSections().stream()
				.filter(it -> it.equalsUpStation(section.getUpStation()))
				.findFirst()
				.ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

			this.sections.add(section);
			return;
		}

		if (isDownStationExisted) {
			this.sections.stream()
				.filter(it -> it.equalsDownStation(section.getDownStation()))
				.findFirst()
				.ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

			this.sections.add(section);
			return;
		}

		throw new RuntimeException();
	}

	public Optional<Section> findSection(final Station upStation, final Station downStation) {
		return this.sections.stream()
			.filter(section -> section.matchStation(upStation, downStation))
			.findFirst();
	}

	private void validate(final List<Station> stations, final Section section) {
		if (stations.isEmpty()) {
			return;
		}

		if (isUpAndDownExists(stations, section)) {
			throw new IllegalArgumentException("상행역과 하행역이 이미 존재합니다.");
		}

		if (isUpAndDownNotExists(stations, section)) {
			throw new IllegalArgumentException("상행역과 하행역이 모두 노선에 포함되어 있지 않습니다.");
		}
	}

	private boolean isUpAndDownExists(final List<Station> stations, final Section section) {
		return stations.contains(section.getUpStation()) && stations.contains(section.getDownStation());
	}

	private boolean isUpAndDownNotExists(final List<Station> stations, final Section section) {
		return !stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation());
	}

	public void removeStation(final Line line, final Station station) {
		if (isSizeLessThanOne()) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = this.sections.stream()
			.filter(it -> it.getUpStation() == station)
			.findFirst();
		Optional<Section> downLineStation = this.sections.stream()
			.filter(it -> it.getDownStation() == station)
			.findFirst();

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
			this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> line.getSections().remove(it));
		downLineStation.ifPresent(it -> line.getSections().remove(it));
	}

	private boolean isSizeLessThanOne() {
		return this.sections.size() <= 1;
	}

}
