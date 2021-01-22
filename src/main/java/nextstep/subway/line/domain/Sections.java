package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/11
 * @description :
 **/
@Embeddable
public class Sections {
	private static final int MINIMAL_SECTION_COUNT = 2;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public List<Section> getSections() {
		return sections;
	}

	public void updateUpStation(Station upStation, Station downStation, int distance) {
		sections.stream()
			.filter(it -> it.isEqualUpStation(upStation))
			.findFirst()
			.ifPresent(it -> it.updateUpStation(downStation, distance));
	}

	public void updateDownStation(Station upStation, Station downStation, int distance) {
		sections.stream()
			.filter(it -> it.isEqualDownStation(downStation))
			.findFirst()
			.ifPresent(it -> it.updateDownStation(upStation, distance));
	}

	public Section getSectionEqualUpStation(Station station) {
		return sections.stream()
			.filter(it -> it.isEqualUpStation(station))
			.findFirst()
			.orElse(null);
	}

	public Section getSectionEqualDownStation(Station station) {
		return sections.stream()
			.filter(it -> it.isEqualDownStation(station))
			.findFirst()
			.orElse(null);
	}

	public void addSection(Line line, Station upStation, Station downStation, int distance) {
		sections.add(new Section(line, upStation, downStation, distance));
	}

	public void remove(Section section) {
		if (section != null) {
			sections.remove(section);
		}
	}

	public List<Station> getStations() {
		if (sections.isEmpty()) {
			return Collections.emptyList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation();

		while (downStation != null) {
			stations.add(downStation);
			Station finalDownStation = downStation;
			downStation = findDownStationSectionContainUpStation(finalDownStation);
		}
		return stations;
	}

	private Station findDownStationSectionContainUpStation(Station station) {
		return sections.stream()
			.filter(section -> section.isEqualUpStation(station))
			.findFirst()
			.map(Section::getDownStation)
			.orElse(null);
	}

	private Station findUpStation() {
		return sections.stream()
			.map(section -> section.getUpStation())
			.filter(this::isSectionsContainDownStation)
			.findFirst()
			.orElseThrow(() -> new RuntimeException("지하철역 정보가 올바르지 않습니다."));
	}

	private boolean isSectionsContainDownStation(Station station) {
		return sections.stream()
			.noneMatch(it -> it.isEqualDownStation(station));
	}

	public boolean isExist() {
		return sections.size() > MINIMAL_SECTION_COUNT;
	}
}
