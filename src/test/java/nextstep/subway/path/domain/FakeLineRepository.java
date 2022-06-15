package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.FakeStationRepository;
import nextstep.subway.station.domain.Station;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FakeLineRepository implements LineRepository {
    private final Map<Long, Line> elements = new HashMap<>();
    private long lineId = 0L;

    public FakeLineRepository() {
        FakeStationRepository stations = new FakeStationRepository();

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
    public List<Line> findAll() {
        return new ArrayList<>(elements.values());
    }

    @Override
    public <S extends Line> S save(S entity) {
        ReflectionTestUtils.setField(entity, "id", ++lineId);
        elements.put(lineId, entity);
        return entity;
    }

    @Override
    public List<Line> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Line> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Line> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Line entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Line> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Line> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Line> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Line> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Line> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Line> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Line getOne(Long aLong) {
        return null;
    }

    @Override
    public Line getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Line> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Line> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Line> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Line> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Line> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Line> boolean exists(Example<S> example) {
        return false;
    }
}
