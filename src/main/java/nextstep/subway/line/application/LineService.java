package nextstep.subway.line.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import nextstep.subway.station.dto.StationResponse;

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
		Line persistLine = lineRepository.save(
			new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
		List<StationResponse> stations = extractStationToStationResponse(persistLine);
		return LineResponse.of(persistLine, stations);
	}

	public List<LineResponse> findLines() {
		List<Line> persistLines = lineRepository.findAll();
		return persistLines.stream()
			.map(line -> lineToLineResponse(line))
			.collect(Collectors.toList());
	}

	private LineResponse lineToLineResponse(Line line) {
		List<StationResponse> stations = extractStationToStationResponse(line);
		return LineResponse.of(line, stations);
	}

	public Line findLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(RuntimeException::new);
	}

	public LineResponse findLineResponseById(Long id) {
		Line persistLine = findLineById(id);
		List<StationResponse> stations = extractStationToStationResponse(persistLine);
		return LineResponse.of(persistLine, stations);
	}

	private List<StationResponse> extractStationToStationResponse(Line persistLine) {
		return persistLine.getStations().stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}

	public void updateLine(Long id, LineRequest lineUpdateRequest) {
		Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		persistLine.update(new Line(lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long lineId, SectionRequest request) {
		Line line = findLineById(lineId);
		Station upStation = stationService.findStationById(request.getUpStationId());
		Station downStation = stationService.findStationById(request.getDownStationId());
		List<Station> stations = line.getStations();

		validateLineStation(stations, upStation, downStation);

		if (stations.isEmpty()) {
			line.addSection(line, upStation, downStation, request.getDistance());
			return;
		}

		if (upStation.isExisted(stations)) {
			line.updateUpStation(upStation, downStation, request.getDistance());
		} else if (downStation.isExisted(stations)) {
			line.updateDownStation(upStation, downStation, request.getDistance());
		} else {
			throw new RuntimeException();
		}

		line.addSection(line, upStation, downStation, request.getDistance());
	}

	private void validateLineStation(List<Station> stations, Station upStation, Station downStation) {
		if (upStation.isExisted(stations) && downStation.isExisted(stations)) {
			throw new RuntimeException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
			stations.stream().noneMatch(it -> it == downStation)) {
			throw new RuntimeException("등록할 수 없는 구간 입니다.");
		}

	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station station = stationService.findStationById(stationId);
		if (line.getSections().size() <= 1) {
			throw new RuntimeException();
		}

		line.removeLineStation(station);
	}
}
