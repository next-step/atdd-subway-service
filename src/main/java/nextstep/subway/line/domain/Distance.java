package nextstep.subway.line.domain;

import java.util.Objects;

public class Distance {

  private static final int MIN_DISTANCE = 1;
  private static final String DISTANCE_SHOULD_BE_LARGER_THAN_ZERO = "역 간 거리는 %d 이상이어야 합니다.";
  private final int number;

  private Distance(int distanceNumber) {
    this.number = distanceNumber;
  }

  public static Distance from(int distanceNumber) {
    validateDistanceNumber(distanceNumber);
    return new Distance(distanceNumber);
  }

  private static void validateDistanceNumber(int distanceNumber) {
    if(distanceNumber < 1) {
      throw new IllegalArgumentException(String.format(DISTANCE_SHOULD_BE_LARGER_THAN_ZERO, MIN_DISTANCE));
    }
  }

  public Distance add(Distance other) {
    return Distance.from(this.number + other.number);
  }

  public Distance subtract(Distance other) {
    return Distance.from(this.number - other.number);
  }

  public boolean isFartherThan(Distance other) {
    return this.number > other.number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Distance distance = (Distance) o;
    return number == distance.number;
  }

  @Override
  public int hashCode() {
    return Objects.hash(number);
  }
}
