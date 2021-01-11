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
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public List<Section> getSections() {
		return sections;
	}

	public void updateUpStation(Station upStation, Station downStation, int distance) {
		sections.stream()
			.filter(it -> it.getUpStation() == upStation)
			.findFirst()
			.ifPresent(it -> it.updateUpStation(downStation, distance));
	}

	public void updateDownStation(Station upStation, Station downStation, int distance) {
		sections.stream()
			.filter(it -> it.getDownStation() == downStation)
			.findFirst()
			.ifPresent(it -> it.updateDownStation(upStation, distance));
	}

	public Section getSectionEqualUpStation(Station station) {
		return sections.stream()
			.filter(it -> it.getUpStation() == station)
			.findFirst()
			.orElse(null);
	}

	public Section getSectionEqualDownStation(Station station) {
		return sections.stream()
			.filter(it -> it.getDownStation() == station)
			.findFirst()
			.orElse(null);
	}

	public void addSection(Line line, Station upStation, Station downStation, int distance) {
		sections.add(new Section(line, upStation, downStation, distance));
	}

	public Station getFirstUpStation() {
		return sections.get(0).getUpStation();
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
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Section nextLineStation = getSectionEqualUpStation(finalDownStation);
			if (nextLineStation == null) {
				break;
			}
			downStation = nextLineStation.getDownStation();
			stations.add(downStation);
		}
		stations.forEach(station -> System.out.println(station.getName()));
		return stations;
	}

	private Station findUpStation() {
		Station downStation = getFirstUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Section nextLineStation = getSectionEqualDownStation(finalDownStation);
			if (nextLineStation == null) {
				break;
			}
			downStation = nextLineStation.getUpStation();
		}

		return downStation;
	}
}
