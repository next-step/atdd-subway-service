package nextstep.subway.line.dto;

public class FindPathRequest {
	private Long source;
	private Long target;

	public FindPathRequest(Long sourceStationId, Long destStationId) {
		this.source = sourceStationId;
		this.target = destStationId;
	}

	public Long getSource() {
		return source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	public Long getTarget() {
		return target;
	}

	public void setTarget(Long target) {
		this.target = target;
	}
}
