package nextstep.subway.path.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class FakeLineRepository implements LineRepository {
    private List<Line> lines = new ArrayList<>();

    @Override
    public List<Line> findAll() {
        return lines;
    }

    @Override
    public <S extends Line> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public <S extends Line> S save(S entity) {
        lines.add(entity);
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
