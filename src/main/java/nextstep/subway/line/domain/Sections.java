package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {
	}

	public Sections(List<Section> sections) {
		this.sections = sections;
	}

	public List<Station> getOrderedStations() {
		if (isEmptySections()) {
			return Arrays.asList();
		}

		Section nextSection = findFirstSection();
		return traversalSections(nextSection);
	}

	private List<Station> traversalSections(Section startSection) {
		List<Station> stations = new ArrayList<>();
		stations.add(startSection.getUpStation());

		Section nextSection = startSection;
		while (nextSection != null) {
			final Station downStation = nextSection.getDownStation();
			stations.add(downStation);
			Optional<Section> findNextSection = sections.stream()
				.filter(it -> it.isEqualToUpStation(downStation))
				.findFirst();
			nextSection = findNextSection.orElse(null);
		}

		return stations;
	}

	private boolean isEmptySections() {
		return sections.isEmpty();
	}

	private Section findFirstSection() {
		Section candiSection = pickCandidateDownStation();
		Section lastSection = candiSection;
		while (candiSection != null) {
			lastSection = candiSection;
			Station prevDownStation = lastSection.getUpStation();
			Optional<Section> nextLineStation = sections.stream()
				.filter(it -> it.isEqualToDownStation(prevDownStation))
				.findFirst();
			candiSection = nextLineStation.orElse(null);
		}

		return lastSection;
	}

	private Section pickCandidateDownStation() {
		return sections.get(0);
	}

	public void add(Section section) {
		this.sections.add(section);
	}

	private boolean isEmpty() {
		return sections.isEmpty();
	}

	public void addSection(Line line, Station upStation, Station downStation, int distance) {
		if (isEmpty()) {
			add(new Section(line, upStation, downStation, distance));
			return;
		}

		List<Station> stations = getOrderedStations();
		validateAddableSection(stations, upStation, downStation);

		sections.stream()
			.filter(it -> it.isEqualToUpStation(upStation))
			.findFirst()
			.ifPresent(it -> it.updateUpStation(downStation, distance));

		sections.stream()
			.filter(it -> it.isEqualToDownStation(downStation))
			.findFirst()
			.ifPresent(it -> it.updateDownStation(upStation, distance));

		add(new Section(line, upStation, downStation, distance));
	}

	private void validateAddableSection(List<Station> stations, Station upStation, Station downStation) {
		boolean isUpStationExisted = stations.stream()
			.anyMatch(it -> it.equals(upStation));
		boolean isDownStationExisted = stations.stream()
			.anyMatch(it -> it.equals(downStation));

		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!isUpStationExisted && !isDownStationExisted) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	public void removeStation(Line line, Station station) {
		validateMinSectionSizeToRemove();
		Optional<Section> upLineStation = sections.stream()
			.filter(it -> it.isEqualToUpStation(station))
			.findFirst();
		Optional<Section> downLineStation = sections.stream()
			.filter(it -> it.isEqualToDownStation(station))
			.findFirst();

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
			add(new Section(line, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> remove(it));
		downLineStation.ifPresent(it -> remove(it));
	}

	private void remove(Section section) {
		sections.remove(section);
	}

	private void validateMinSectionSizeToRemove() {
		if (sections.size() <= 1) {
			throw new RuntimeException();
		}
	}
}
