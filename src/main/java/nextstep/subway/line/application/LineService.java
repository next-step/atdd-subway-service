package nextstep.subway.line.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
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
		List<Section> sectionsToUpdate = sectionService.findSectionsToUpdate(upStation, downStation);

		Section section = Section.of(line, upStation, downStation, Distance.from(request.getDistance()));
		line.connectSection(section, sectionsToUpdate);
	}

	public void new_removeLineStation(Long lineId, Long stationId) {
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

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = findById(lineId);
		Station station = stationService.findById(stationId);
		if (line.getSections().size() <= 1) {
			throw new InvalidDataException("구간이 하나인 노선에서는 역을 제거할 수 없습니다.");
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
			line.getSections().add(Section.of(line, newUpStation, newDownStation, Distance.from(newDistance)));
		}

		upLineStation.ifPresent(it -> line.getSections().remove(it));
		downLineStation.ifPresent(it -> line.getSections().remove(it));
	}

	private Line savedLine(LineCreateRequest request) {
		return Line.of(request.name(), request.color(),
			Sections.from(section(request.getUpStationId(), request.getDownStationId(), request.getDistance())));
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
