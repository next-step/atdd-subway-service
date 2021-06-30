package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoriteRequest {

	private String source;
	private String target;

	public FavoriteRequest(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public static Favorite toFavorite(Member member, Station source, Station target) {
		return new Favorite(member, source, target);
	}
}
