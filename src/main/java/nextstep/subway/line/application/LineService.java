package nextstep.subway.line.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class LineService {
	private LineRepository lineRepository;
	private StationService stationService;

	public LineService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	public LineResponse saveLine(LineRequest request) {
		Station upStation = stationService.findById(request.getUpStationId());
		Station downStation = stationService.findById(request.getDownStationId());
		Line persistLine = lineRepository.save(Line.of(request, upStation, downStation));
		return LineResponse.of(persistLine);
	}

	public List<LineResponse> findLines() {
		return LineResponse.of(lineRepository.findAll());
	}

	private Line findLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(RuntimeException::new);
	}

	public LineResponse findLineResponseById(Long id) {
		return LineResponse.of(findLineById(id));
	}

	public void updateLine(Long id, LineRequest lineUpdateRequest) {
		Line persistLine = findLineById(id);
		persistLine.update(Line.of(lineUpdateRequest));
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long lineId, SectionRequest request) {
		Line line = findLineById(lineId);
		Station upStation = stationService.findStationById(request.getUpStationId());
		Station downStation = stationService.findStationById(request.getDownStationId());
		List<Station> stations = getStations(line);
		boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
		boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

		if (isUpStationExisted && isDownStationExisted) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation)
			&& stations.stream().noneMatch(it -> it == downStation)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}

		if (stations.isEmpty()) {
			line.sections().add(new Section(line, upStation, downStation, request.getDistance()));
			return;
		}

		if (isUpStationExisted) {
			line.sections().stream()
				.filter(it -> it.upStation() == upStation)
				.findFirst()
				.ifPresent(it -> it.updateUpStation(downStation, request.getDistance()));

			line.sections().add(new Section(line, upStation, downStation, request.getDistance()));
		} else if (isDownStationExisted) {
			line.sections().stream()
				.filter(it -> it.downStation() == downStation)
				.findFirst()
				.ifPresent(it -> it.updateDownStation(upStation, request.getDistance()));

			line.sections().add(new Section(line, upStation, downStation, request.getDistance()));
		} else {
			throw new RuntimeException();
		}
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station station = stationService.findStationById(stationId);
		if (line.sections().size() <= 1) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = line.sections().stream()
			.filter(it -> it.upStation() == station)
			.findFirst();
		Optional<Section> downLineStation = line.sections().stream()
			.filter(it -> it.downStation() == station)
			.findFirst();

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().upStation();
			Station newDownStation = upLineStation.get().downStation();
			int newDistance = upLineStation.get().distance() + downLineStation.get().distance();
			line.sections().add(new Section(line, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> line.sections().remove(it));
		downLineStation.ifPresent(it -> line.sections().remove(it));
	}

	public List<Station> getStations(Line line) {
		if (line.sections().isEmpty()) {
			return Arrays.asList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation(line);
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = line.sections().stream()
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

	private Station findUpStation(Line line) {
		Station downStation = line.sections().get(0).upStation();
		while (downStation != null) {
			Station finalUpStation = downStation;
			Optional<Section> nextLineStation = line.sections().stream()
				.filter(it -> it.downStation() == finalUpStation)
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().upStation();
		}

		return downStation;
	}
}
