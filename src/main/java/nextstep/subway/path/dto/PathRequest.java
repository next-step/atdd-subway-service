package nextstep.subway.path.dto;

public class PathRequest implements PathCalculateRequest {
	private Long source;
	private Long target;

	public PathRequest(Long source, Long target) {
		this.source = source;
		this.target = target;
	}

	public Long getSource() {
		return source;
	}

	public Long getTarget() {
		return target;
	}

	@Override
	public Long getSourceStationId() {
		return source;
	}

	@Override
	public Long getTargetStationId() {
		return target;
	}
}
