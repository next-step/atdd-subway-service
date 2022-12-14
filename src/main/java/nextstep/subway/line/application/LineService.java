package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.LineCreateRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineUpdateRequest;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class LineService {
	private final LineRepository lineRepository;
	private final StationService stationService;
	private final SectionService sectionService;

	public LineService(
		LineRepository lineRepository,
		StationService stationService,
		SectionService sectionService
	) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
		this.sectionService = sectionService;
	}

	@Transactional
	public LineResponse saveLine(LineCreateRequest request) {
		validateDuplicateName(request.name());
		Line line = lineRepository.save(savedLine(request));
		return lineResponse(line);
	}

	@Transactional(readOnly = true)
	public List<LineResponse> findLines() {
		return lineRepository.findAll().stream()
			.map(this::lineResponse)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Lines findAll() {
		return Lines.from(lineRepository.findAll());
	}

	@Transactional(readOnly = true)
	public LineResponse findLineResponseById(Long id) {
		return LineResponse.from(findById(id));
	}

	@Transactional
	public void updateLine(Long id, LineUpdateRequest request) {
		validateDuplicateName(request.name());
		Line line = findById(id);
		line.update(request.name(), request.color());
	}

	@Transactional
	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	@Transactional
	public void addLineStation(Long lineId, SectionRequest request) {
		Line line = findById(lineId);

		Station upStation = station(request.getUpStationId());
		Station downStation = station(request.getDownStationId());
		List<Section> sectionsToUpdate = sectionService.findSectionsToUpdate(upStation, downStation, line);

		Section section = Section.of(line, upStation, downStation, Distance.from(request.getDistance()));
		line.connectSection(section, sectionsToUpdate);
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Section sectionByUpStation = sectionService.findSectionByUpStation(stationId);
		Section sectionByDownStation = sectionService.findSectionByDownStation(stationId);
		Line line = findById(lineId);

		line.removeSection(sectionByUpStation, sectionByDownStation);
	}

	private Line findById(Long id) {
		return lineRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(String.format("ID(%d) 에 해당하는 노선을 찾을 수 없습니다.", id)));
	}

	private void validateDuplicateName(Name name) {
		if (lineRepository.existsByName(name)) {
			throw new DuplicateDataException(String.format("%s는 이미 존재하는 노선 이름입니다.", name));
		}
	}

	private Line savedLine(LineCreateRequest request) {
		return Line.of(
			request.name(),
			request.color(),
			Sections.from(section(request.getUpStationId(),
					request.getDownStationId(),
					request.getDistance())),
			request.fare());
	}

	private LineResponse lineResponse(Line line) {
		return LineResponse.from(line);
	}

	private Section section(Long upStationId, Long downStationId, int distance) {
		return Section.of(station(upStationId), station(downStationId), Distance.from(distance));
	}

	private Station station(Long id) {
		return stationService.findById(id);
	}
}
