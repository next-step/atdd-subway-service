package nextstep.subway.common.exception;

public class NonMatchStationException extends  IllegalArgumentException {
	public static final String NON_MATCH_STATION_EXCEPTION = "등록할 수 없는 구간 입니다.";

	public NonMatchStationException() {
		super(NON_MATCH_STATION_EXCEPTION);
	}
}
