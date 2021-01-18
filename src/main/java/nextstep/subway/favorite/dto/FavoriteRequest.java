package nextstep.subway.favorite.dto;

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
public class FavoriteRequest {
	private Long source;
	private Long target;

	public FavoriteRequest() {
	}

	public FavoriteRequest(Long source, Long target) {
		this.source = source;
		this.target = target;
	}

	public Long getSource() {
		return source;
	}

	public Long getTarget() {
		return target;
	}
}
