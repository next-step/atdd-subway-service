package nextstep.subway.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	IS_SAME_UP_DOWN_STATION_ERROR(HttpStatus.BAD_REQUEST, "동일한 상, 하행 종점을 포함하는 구간은 추가할 수 없습니다."),
	IS_NOT_IN_STATIONS_ERROR(HttpStatus.BAD_REQUEST, "상, 하행 종점 하나를 포함해야 합니다."),
	NO_SUCH_LINE_ERROR(HttpStatus.BAD_REQUEST, "존재하지 않은 노선입니다."),
	VALID_CAN_NOT_REMOVE_LAST_STATION(HttpStatus.BAD_REQUEST, "구간이 하나인 노선의 마지막 구간은 제거할 수 없습니다."),
	VALID_CAN_NOT_REMOVE_NOT_IN_STATIONS(HttpStatus.BAD_REQUEST, "구간에 존재하지 않는 역은 제거할 수 없습니다."),
	VALID_SAME_SOURCE_TARGET_STATION(HttpStatus.BAD_REQUEST, "출발역과 도착역이 같습니다."),
	NO_SUCH_STATION_ERROR(HttpStatus.BAD_REQUEST, "존재하지 않는 역입니다."),
	NO_SUCH_FAVORITE_ERROR(HttpStatus.BAD_REQUEST, "존재하지 않는 즐겨찾기입니다."),
	VALID_CAN_NOT_REMOVE_FAVORITE(HttpStatus.UNAUTHORIZED, "본인의 즐겨찾기만 삭제 가능합니다."),
	UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
	INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.");

	private HttpStatus status;
	private String errorMessage;

	ErrorCode(HttpStatus status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
