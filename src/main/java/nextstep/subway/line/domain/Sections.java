package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

@Getter
@Embeddable
@NoArgsConstructor
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public void initSection(Section section) {
		sections.add(section);
	}

	public void addSection(Section section) {
		throw new RuntimeException("메소드 작성필요");
	}

	public List<Station> getStations() {
		throw new RuntimeException("메소드 작성 필요");
	}

	public void removeLineStation(Line line, Station station) {
		throw new RuntimeException("메소드 작성 필요");
	}
}
