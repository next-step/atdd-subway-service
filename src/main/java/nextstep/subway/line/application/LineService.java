package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class LineService {
	private final LineRepository lineRepository;
	private final StationService stationService;

	public LineService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	public LineResponse saveLine(LineRequest request) {
		Station upStation = findStationById(request.getUpStationId());
		Station downStation = findStationById(request.getDownStationId());
		Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
		return LineResponse.of(persistLine);
	}

	@Transactional(readOnly = true)
	public List<LineResponse> findLines() {
		List<Line> persistLines = lineRepository.findAll();
		return persistLines.stream()
			.map(LineResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public LineResponse findLineResponseById(Long id) {
		return LineResponse.of(findLineById(id));
	}

	@Transactional
	public LineResponse updateLine(Long id, LineRequest lineRequest) {
		Line persistLine = findLineById(id);
		persistLine.update(lineRequest.toLine());
		return LineResponse.of(persistLine);
	}

	@Transactional
	public void deleteLineById(Long id) {
		Line persistLine = findLineById(id);
		lineRepository.delete(persistLine);
	}

	@Transactional
	public LineResponse addLineStation(Long lineId, SectionRequest request) {
		Line line = findLineById(lineId);
		Station upStation = findStationById(request.getUpStationId());
		Station downStation = findStationById(request.getDownStationId());

		line.addSection(upStation, downStation, request.getDistance());
		Line persistLine = lineRepository.save(line);
		return LineResponse.of(persistLine);
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station station = findStationById(stationId);

		line.deleteSection(station);
	}

	private Line findLineById(Long LindId) {
		return lineRepository.findById(LindId).orElseThrow(RuntimeException::new);
	}

	private Station findStationById(Long stationId) {
		return stationService.findById(stationId);
	}
}
