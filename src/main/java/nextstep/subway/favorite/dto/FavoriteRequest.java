package nextstep.subway.favorite.dto;

public class FavoriteRequest {
	private Long source;
	private Long target;

	public FavoriteRequest() {
	}

	public FavoriteRequest(Long source, Long target) {
		this.source = source;
		this.target = target;
	}

	public Long getSourceId() {
		return source;
	}

	public Long getTargetId() {
		return target;
	}
}
