package nextstep.subway.line.domain;

import java.util.List;
import java.util.Optional;
import nextstep.subway.station.domain.Station;

public class LineSection {

	private Line line;

	public LineSection(Line line) {
		this.line = line;
	}

	public void addLineStation(Station upStation, Station downStation, int distance) {
		List<Station> stations = line.getStations();

		boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
		boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

		validate(stations, isUpStationExisted, isDownStationExisted);
		if (stations.isEmpty()) {
			line.getSections().add(new Section(line, upStation, downStation, distance));
			return;
		}

		if (isUpStationExisted) {
			updateUpStation(upStation, downStation, distance);
			return;
		}

		if (isDownStationExisted) {
			updateDownStation(upStation, downStation, distance);
			return;
		}
	}

	public void removeStation(Station station) {
		if (line.emptyOrHasOneSection()) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = line.upLineStation(station);
		Optional<Section> downLineStation = line.downLineStation(station);

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().sumDistance(downLineStation.get());
			line.addSection(newUpStation, newDownStation, newDistance);
		}

		upLineStation.ifPresent(it -> line.removeSection(it));
		downLineStation.ifPresent(it -> line.removeSection(it));
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

	private void updateUpStation(Station upStation, Station downStation, int distance) {
		line.updateUpStation(upStation, downStation, distance);
	}

	private void updateDownStation(Station upStation, Station downStation, int distance) {
		line.updateDownStation(upStation, downStation, distance);
	}
}
