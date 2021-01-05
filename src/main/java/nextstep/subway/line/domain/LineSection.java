package nextstep.subway.line.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class LineSection {

	private Line line;
	private Station upStation;
	private Station downStation;

	public LineSection(Line line, Station upStation, Station downStation) {
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
	}

	public void addLineStation(int distance) {
		List<Station> stations = line.getStations();

		boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
		boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

		validate(stations, isUpStationExisted, isDownStationExisted);
		if (stations.isEmpty()) {
			line.getSections().add(new Section(line, upStation, downStation, distance));
			return;
		}

		if (isUpStationExisted) {
			updateUpStation(distance);
			return;
		}

		if (isDownStationExisted) {
			updateDownStation(distance);
			return;
		}
	}

	private void validate(List<Station> stations, boolean isUpStationExisted,
		  boolean isDownStationExisted) {
		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && !isUpStationExisted &&
			  !isDownStationExisted) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}
	}

	private void updateUpStation(int distance) {
		line.updateUpStation(upStation, downStation, distance);
	}

	private void updateDownStation(int distance) {
		line.updateDownStation(upStation, downStation, distance);
	}
}
