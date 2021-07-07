package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class LineStations {
	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
	private List<LineStation> lineStations = new ArrayList<>();

	protected LineStations() {
	}

	public LineStations(List<LineStation> lineStations) {
		this.lineStations = new ArrayList<>(lineStations);
	}

	public void add(LineStation lineStation) {
		if (!existsFileStationBy(lineStation)) {
			this.lineStations.add(lineStation);
		}
	}

	public boolean existsFileStationBy(LineStation lineStation) {
		return this.lineStations.stream()
				.anyMatch(lineStation::isSameLineAndStation);
	}

	public void remove(LineStation lineStation) {
		this.lineStations.stream()
				.filter(lineStation::isSameLineAndStation)
				.findFirst()
				.ifPresent(lineStations::remove);
	}

	@Override
	public String toString() {
		return lineStations.toString();
	}
}
