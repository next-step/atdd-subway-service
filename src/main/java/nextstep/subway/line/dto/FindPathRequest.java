package nextstep.subway.line.dto;

public class FindPathRequest {
	private Long source;
	private Long target;

	public FindPathRequest(Long sourceStationId, Long destStationId) {
		this.source = sourceStationId;
		this.target = destStationId;
	}
}
