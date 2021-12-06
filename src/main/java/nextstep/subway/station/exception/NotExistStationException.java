package nextstep.subway.station.exception;

public class NotExistStationException extends RuntimeException {

	private static String ERROR_MESSAGE = "존재하지 않는 지하철역 입니다.";

	public NotExistStationException() {
		super(ERROR_MESSAGE);
	}
}
