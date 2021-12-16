package nextstep.subway.common.exception;

public enum Exceptions {
	DISTANCE_TOO_FAR(new InternalServerException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요")),
	ALREADY_EXIST_SECTION(new InternalServerException("이미 등록된 구간 입니다.")),
	NO_CONNECTED_SECTION(new InternalServerException("등록할 수 없는 구간 입니다.")),
	SECTION_MUST_BE_EXIST(new InternalServerException("최소한 1개의 구간이 등록되어 있어야 합니다.")),
	LINE_NOT_EXIST(new ResourceNotFoundException("존재하지 않는 노선입니다.")),
	LINE_ALREADY_EXIST(new ResourceAlreadyExistException("이미 존재하는 노선입니다.")),
	STATION_NOT_EXIST(new ResourceNotFoundException("존재하지 않는 지하철 역입니다."))
	;

	private final BaseException exception;

	Exceptions(BaseException exception) {
		this.exception = exception;
	}

	public BaseException getException() {
		return exception;
	}
}
