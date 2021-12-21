package nextstep.subway.path.dto;


public class PathRequest {

	private int sourceId;
	private int targetId;

	public PathRequest(int sourceId, int targetId) {
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public int getSourceId() {
		return sourceId;
	}

	public int getTargetId() {
		return targetId;
	}
}
