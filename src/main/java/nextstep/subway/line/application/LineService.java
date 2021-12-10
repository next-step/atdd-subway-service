package nextstep.subway.line.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
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
		Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
		return LineResponse.of(persistLine);
	}

	public List<LineResponse> findLines() {
		return LineResponse.ofList(lineRepository.findAll());
	}

	private Line findLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(() ->
			new AppException(ErrorCode.NOT_FOUND, "노선({id})을 찾을 수 없습니다", id));
	}

	public LineResponse findLineResponseById(Long id) {
		return LineResponse.of(findLineById(id));
	}

	public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
		Line persistLine = findLineById(id);
		persistLine.update(lineUpdateRequest.toLine());
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long lineId, SectionRequest request) {
		Line line = findLineById(lineId);
		Station upStation = stationService.findStationById(request.getUpStationId());
		Station downStation = stationService.findStationById(request.getDownStationId());
		Section newSection = request.toSection(line, upStation, downStation);
		line.updateSections(newSection);

	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station station = stationService.findStationById(stationId);
		if (line.getSections().size() <= 1) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = line.getSections().stream()
			.filter(it -> it.getUpStation() == station)
			.findFirst();
		Optional<Section> downLineStation = line.getSections().stream()
			.filter(it -> it.getDownStation() == station)
			.findFirst();

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
			line.getSections().add(Section.of(line, newUpStation, newDownStation, newDistance));
		}

		upLineStation.ifPresent(it -> line.getSections().remove(it));
		downLineStation.ifPresent(it -> line.getSections().remove(it));
	}

	public List<Station> getStations(Line line) {
		if (line.getSections().isEmpty()) {
			return Collections.emptyList();
		}

		List<Station> stations = new ArrayList<>();
		Station downStation = findUpStation(line);
		stations.add(downStation);

		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = line.getSections().stream()
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

	private Station findUpStation(Line line) {
		Station downStation = line.getSections().get(0).getUpStation();
		while (downStation != null) {
			Station finalDownStation = downStation;
			Optional<Section> nextLineStation = line.getSections().stream()
				.filter(it -> it.getDownStation() == finalDownStation)
				.findFirst();
			if (!nextLineStation.isPresent()) {
				break;
			}
			downStation = nextLineStation.get().getUpStation();
		}

		return downStation;
	}
}
