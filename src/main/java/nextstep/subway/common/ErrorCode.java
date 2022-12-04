package nextstep.subway.common;

public enum ErrorCode {
	SAME_UP_STATION_OR_DOWN_STATION_ERROR("동일한 상, 하행 종점을 포함하는 구간은 추가할 수 없습니다."),
	NOT_IN_STATIONS_ERROR("상, 하행 종점 하나를 포함해야 합니다."),
	NOT_FOUND_LINE_ERROR("존재하지 않은 노선입니다."),
	CAN_NOT_REMOVE_LAST_STATION("구간이 하나인 노선의 마지막 구간은 제거할 수 없습니다."),
	CAN_NOT_REMOVE_NOT_IN_STATIONS("구간에 존재하지 않는 역은 제거할 수 없습니다.");

	private String errorMessage;

	ErrorCode(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
