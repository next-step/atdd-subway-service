package nextstep.subway.line.application;

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
		Line persistLine = lineRepository.save(
			new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
		return LineResponse.of(persistLine);
	}

	public List<LineResponse> findLines() {
		List<Line> persistLines = lineRepository.findAll();
		return LineResponse.of(persistLines);
	}

	public Line findLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(RuntimeException::new);
	}

	public LineResponse findLineResponseById(Long id) {
		Line persistLine = findLineById(id);
		return LineResponse.of(persistLine);
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

		Section section = Section.createSection(line, upStation, downStation, request.getDistance());
		line.addSection(section);
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findLineById(lineId);
		Station station = stationService.findStationById(stationId);
		if (line.isSectionsSizeLessThanOrEqualTo(1)) {
			throw new RuntimeException();
		}

		Optional<Section> upLineStation = line.findSectionByUpStation(station);
		Optional<Section> downLineStation = line.findSectionByDownStation(station);

		upLineStation.ifPresent(line::removeSection);
		downLineStation.ifPresent(line::removeSection);

		if (upLineStation.isPresent() && downLineStation.isPresent()) {
			Station newUpStation = downLineStation.get().getUpStation();
			Station newDownStation = upLineStation.get().getDownStation();
			int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
			line.addSection(Section.createSection(line, newUpStation, newDownStation, newDistance));
		}

	}
}
