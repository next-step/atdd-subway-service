package nextstep.subway.path.dto;

public class PathRequest {
	private Long source;
	private Long target;

	protected PathRequest() {
	}

	public PathRequest(Long source, Long target) {
		this.source = source;
		this.target = target;
	}

	public boolean isSourceEqualToTarget() {
		return this.source.equals(this.target);
	}

	public Long getSource() {
		return this.source;
	}

	public Long getTarget() {
		return this.target;
	}
}
