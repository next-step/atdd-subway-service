package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.InMemoryStationRepository;
import nextstep.subway.station.domain.Station;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

public class InMemoryLineRepository implements LineRepository {
    private final Map<Long, Line> elements = new HashMap<>();
    private long lineId = 0L;

    public InMemoryLineRepository() {
        InMemoryStationRepository stations = new InMemoryStationRepository();

        Station empty = new Station("empty");
        Station 강남역 = stations.findById(1L).orElse(empty);
        Station 양재역 = stations.findById(2L).orElse(empty);
        Station 남부터미널역 = stations.findById(3L).orElse(empty);
        Station 교대역 = stations.findById(4L).orElse(empty);
        Station 사당역 = stations.findById(5L).orElse(empty);
        Station 이수역 = stations.findById(6L).orElse(empty);

        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 4);
        Line 삼호선 = new Line("삼호선", "orange", 교대역, 남부터미널역, 3);
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        Line 사호선 = new Line("사호선", "blue", 사당역, 이수역, 10);

        삼호선.addSection(new Section(남부터미널역, 양재역, 2));

        save(신분당선);
        save(삼호선);
        save(이호선);
        save(사호선);
    }

    @Override
    public Optional<Line> findById(Long id) {
        return Optional.ofNullable(elements.get(id));
    }

    @Override
    public List<Line> findAll() {
        return new ArrayList<>(elements.values());
    }

    @Override
    public Line save(Line line) {
        ReflectionTestUtils.setField(line, "id", ++lineId);
        elements.put(lineId, line);
        return line;
    }

    @Override
    public List<Line> saveAll(Iterable<Line> lines) {
        List<Line> result = new ArrayList<>();

        for (Line line : lines) {
            result.add(save(line));
        }
        return result;
    }

    @Override
    public void deleteById(Long id) {
        elements.remove(id);
    }
}
