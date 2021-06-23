package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public enum CustomExceptionMessage {
	NONE(HttpStatus.INTERNAL_SERVER_ERROR, ""),
	NOT_FOUND_LINE(HttpStatus.BAD_REQUEST, "해당 라인을 찾을 수 없습니다."),
	NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "해당 멤버를 찾을 수 없습니다."),
	NOT_FOUND_STATION(HttpStatus.BAD_REQUEST, "해당 지하철 역을 찾을 수 없습니다."),
	OVER_DISTANCE(HttpStatus.INTERNAL_SERVER_ERROR, "역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
	IMPOSSIBLE_MIN_SECTION_SIZE(HttpStatus.INTERNAL_SERVER_ERROR,"구간이 한개 일때는 삭제할 수 없습니다."),
	EXIST_ALL_STATION_IN_SECTIONS(HttpStatus.INTERNAL_SERVER_ERROR, "이미 등록된 구간 입니다."),
	NOT_EXIST_ALL_STATION_IN_SECTIONS(HttpStatus.INTERNAL_SERVER_ERROR, "등록할 수 없는 구간 입니다.");

	private final HttpStatus status;
	private final String message;

	CustomExceptionMessage(final HttpStatus status, final String message) {
		this.status = status;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}
}
