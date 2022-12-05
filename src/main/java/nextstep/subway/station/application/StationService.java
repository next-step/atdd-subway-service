package nextstep.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@Service
public class StationService {
	private final StationRepository stationRepository;

	public StationService(StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public StationResponse saveStation(StationRequest stationRequest) {
		validateDuplicateName(stationRequest.name());
		Station persistStation = stationRepository.save(stationRequest.toStation());
		return StationResponse.of(persistStation);
	}

	public List<StationResponse> findAllStations() {
		List<Station> stations = stationRepository.findAll();

		return stations.stream()
			.map(StationResponse::of)
			.collect(Collectors.toList());
	}

	public void deleteStationById(Long id) {
		stationRepository.deleteById(id);
	}

	public Station findById(Long id) {
		return stationRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(String.format("해당 ID(%d) 에 해당하는 역을 찾을 수 없습니다.", id)));
	}

	public void validateDuplicateName(Name name) {
		if (stationRepository.existsByName(name)) {
			throw new DuplicateDataException(String.format("해당 이름(%s) 은 이미 존재합니다.", name));
		}
	}
}
