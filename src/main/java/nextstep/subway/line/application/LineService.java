package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.exception.NothingException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
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

	public List<LineResponse> findLines() {
		List<Line> persistLines = findLineAll();
		return persistLines.stream()
			.map(LineResponse::of)
			.collect(Collectors.toList());
	}

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

	@Transactional
	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station station = findStationById(stationId);

		line.deleteSection(station);
	}

	public Station findStationById(Long stationId) {
		return stationService.findStationById(stationId);
	}

	public List<Line> findLineAll() {
		return lineRepository.findAll();
	}

	private Line findLineById(Long lineId) {
		return lineRepository.findById(lineId).orElseThrow(NothingException::new);
	}
}
