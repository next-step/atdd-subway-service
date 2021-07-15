package nextstep.subway.common.exception;

public class SameStationException extends IllegalArgumentException {
	public static final String SAME_STATION_EXCEPTION = "출,도착지가 동일합니다.";

	public SameStationException() {
		super(SAME_STATION_EXCEPTION);
	}
}
