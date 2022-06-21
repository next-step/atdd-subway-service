package nextstep.subway.line.domain;

public interface OperationCostPolicy<T> {
    T normal();
    T special(long item);
}
