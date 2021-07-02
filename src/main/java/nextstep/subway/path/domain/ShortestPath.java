package nextstep.subway.path.domain;

public interface ShortestPath<T> {
    Path<T> getPath(T source, T target);
}
