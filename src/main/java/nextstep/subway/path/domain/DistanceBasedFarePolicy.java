package nextstep.subway.path.domain;

public interface DistanceBasedFarePolicy {
	int calculate(int basicFare, int distance);
}
