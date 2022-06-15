package nextstep.subway.station.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeStationRepository implements StationRepository {
    private final Map<Long, Station> elements = new HashMap<>();
    private long stationId = 0L;

    public FakeStationRepository() {
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
    public <S extends Station> S save(S entity) {
        ReflectionTestUtils.setField(entity, "id", ++stationId);
        elements.put(stationId, entity);
        return entity;
    }

    @Override
    public List<Station> findAll() {
        return null;
    }

    @Override
    public List<Station> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Station> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Station> findAllById(Iterable<Long> longs) {
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
    public void delete(Station entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Station> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Station> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Station> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Station> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Station> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Station getOne(Long aLong) {
        return null;
    }

    @Override
    public Station getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Station> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Station> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Station> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Station> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Station> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Station> boolean exists(Example<S> example) {
        return false;
    }
}
