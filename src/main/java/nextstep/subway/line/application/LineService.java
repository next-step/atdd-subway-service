package nextstep.subway.line.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	public LineService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	@Transactional
	public LineResponse saveLine(LineCreateRequest request) {
		validateDuplicateName(request.getName());
		Line line = lineRepository.save(savedLine(request));
		return lineResponse(line);
	}

	private void validateDuplicateName(String name) {
		if (lineRepository.existsByName(name)) {
			throw new DuplicateDataException(String.format("%s는 이미 존재하는 노선 이름입니다.", name));
		}
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
		validateDuplicateName(request.getName());
		Line line = findById(id);
		line.update(request.name(), request.color());
	}

	@Transactional
	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	private Line findById(Long id) {
		return lineRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(String.format("ID(%d) 에 해당하는 노선을 찾을 수 없습니다.", id)));
	}

	public void addLineStation(Long lineId, SectionRequest request) {
		Line line = findById(lineId);
		Station upStation = stationService.findById(request.getUpStationId());
		Station downStation = stationService.findById(request.getDownStationId());
		List<Station> stations = getStations(line);
		boolean isUpStationExisted = stations.stream().anyMatch(it -> it == upStation);
		boolean isDownStationExisted = stations.stream().anyMatch(it -> it == downStation);

		if (isUpStationExisted && isDownStationExisted) {
			throw new DuplicateDataException("이미 등록된 구간 입니다.");
		}

		if (!stations.isEmpty() && stations.stream().noneMatch(it -> it == upStation) &&
			stations.stream().noneMatch(it -> it == downStation)) {
			throw new InvalidDataException("등록할 수 없는 구간 입니다.");
		}

		if (stations.isEmpty()) {
			line.getSections().add(Section.of(line, upStation, downStation, request.distance()));
			return;
		}

		if (isUpStationExisted) {
			line.getSections().stream()
				.filter(it -> it.getUpStation() == upStation)
				.findFirst()
				.ifPresent(it -> it.updateUpStation(downStation, request.distance()));

			line.getSections().add(Section.of(line, upStation, downStation, request.distance()));
		} else if (isDownStationExisted) {
			line.getSections().stream()
				.filter(it -> it.getDownStation() == downStation)
				.findFirst()
				.ifPresent(it -> it.updateDownStation(upStation, request.distance()));

			line.getSections().add(Section.of(line, upStation, downStation, request.distance()));
		} else {
			throw new InvalidDataException("등록할 수 없는 구간 입니다.");
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

	public List<Station> getStations(Line line) {
		if (line.getSections().isEmpty()) {
			return Arrays.asList();
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
