package nextstep.subway.station.infrastructure;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class InMemoryStationRepository implements StationRepository {
    private final Map<Long, Station> elements = new HashMap<>();
    private long stationId = 0L;

    public InMemoryStationRepository() {
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");
        Station 교대역 = new Station("교대역");
        Station 사당역 = new Station("사당역");
        Station 이수역 = new Station("이수역");

        save(강남역);
        save(양재역);
        save(남부터미널역);
        save(교대역);
        save(사당역);
        save(이수역);
    }

    @Override
    public Optional<Station> findById(Long id) {
        return Optional.ofNullable(elements.get(id));
    }

    @Override
    public Station save(Station station) {
        ReflectionTestUtils.setField(station, "id", ++stationId);
        elements.put(stationId, station);
        return station;
    }

    @Override
    public List<Station> findAll() {
        return new ArrayList<>(elements.values());
    }

    @Override
    public void deleteById(Long id) {
        elements.remove(id);
    }
}
