package nextstep.subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest request) {
        validateDuplicateName(request.name());
        return StationResponse.from(savedStation(request));
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll()
            .stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationRepository.delete(findById(id));
    }

    public Station findById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() ->
                new NotFoundException(String.format("station(%d) is not exist", id)));
    }

    private Station savedStation(StationRequest stationRequest) {
        return stationRepository.save(stationRequest.toStation());
    }

    private void validateDuplicateName(Name name) {
        if (stationRepository.existsByName(name)) {
            throw new DuplicateDataException(String.format("지하철 역 이름(%s)은 이미 존재합니다.", name));
        }
    }
}
