package nextstep.subway.line.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exception.LineException;
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
		Station upStation = stationService.findById(request.getUpStationId());
		Station downStation = stationService.findById(request.getDownStationId());
		Line persistLine = lineRepository.save(
			Line.of(request.getName(), request.getColor(), upStation, downStation,
				request.getDistance()));
		return LineResponse.from(persistLine);
	}

	@Transactional(readOnly = true)
	public List<LineResponse> findLines() {
		List<Line> persistLines = lineRepository.findAll();
		return LineResponse.listOf(persistLines);
	}

	private Line findLineById(Long id) {
		return lineRepository.findById(id)
			.orElseThrow(() -> new LineException(ErrorCode.NO_SUCH_LINE_ERROR));
	}

	@Transactional(readOnly = true)
	public LineResponse findLineResponseById(Long id) {
		Line persistLine = findLineById(id);
		return LineResponse.from(persistLine);
	}

	public void updateLine(Long id, LineRequest lineUpdateRequest) {
		Line persistLine = lineRepository.findById(id)
			.orElseThrow(RuntimeException::new);
		persistLine.update(lineUpdateRequest.toLine());
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long lineId, SectionRequest request) {
		Line line = findLineById(lineId);
		Station upStation = stationService.findStationById(request.getUpStationId());
		Station downStation = stationService.findStationById(request.getDownStationId());
		line.addSection(Section.of(line, upStation, downStation, request.getDistance()));
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station station = stationService.findStationById(stationId);
		line.removeStation(station);
	}
}
