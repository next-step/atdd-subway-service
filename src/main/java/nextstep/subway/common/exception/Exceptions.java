package nextstep.subway.common.exception;

public enum Exceptions {
	DISTANCE_TOO_FAR(new InternalServerException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요")),
	ALREADY_EXIST_SECTION(new InternalServerException("이미 등록된 구간 입니다.")),
	NO_CONNECTED_SECTION(new InternalServerException("등록할 수 없는 구간 입니다.")),
	SECTION_MUST_BE_EXIST(new InternalServerException("최소한 1개의 구간이 등록되어 있어야 합니다.")),
	LINE_NOT_EXIST(new ResourceNotFoundException("존재하지 않는 노선입니다.")),
	LINE_ALREADY_EXIST(new ResourceAlreadyExistException("이미 존재하는 노선입니다.")),
	STATION_NOT_EXIST(new ResourceNotFoundException("존재하지 않는 지하철 역입니다.")),
	SOURCE_AND_TARGET_EQUAL(new BadParameterException("출발지와 도착지가 같아 경로를 찾을 수 없습니다.")),
	SOURCE_AND_TARGET_NOT_CONNECTED(new BadParameterException("출발지와 도착지가 연결되지 않아 경로를 찾을 수 없습니다.")),
	SOURCE_OR_TARGET_NOT_EXIST(new BadParameterException("출발지 또는 도착지가 존재하지 않아 경로를 찾을 수 없습니다.")),
	MEMBER_NOT_FOUND(new ResourceNotFoundException("존재하지 않는 회원입니다.")),
	SECTION_IS_NOT_ELIGIBLE(new BadParameterException("추가하려는 구간이 유효하지 않습니다.")),
	TOKEN_IS_NOT_ELIGIBLE(new ForbiddenException("토큰이 유효하지 않습니다.")),
	FAVORITE_NOT_FOUND(new ResourceNotFoundException("존재하지 않는 즐겨찾기 입니다."))
	;

	private final CustomExceptionBase exception;

	Exceptions(CustomExceptionBase exception) {
		this.exception = exception;
	}

	public CustomExceptionBase getException() {
		return exception;
	}

	public CustomExceptionBase getException(String additionalMsg) {
		exception.withMsg(additionalMsg);
		return exception;
	}
}
