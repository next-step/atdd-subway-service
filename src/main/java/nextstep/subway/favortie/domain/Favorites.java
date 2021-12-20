package nextstep.subway.favortie.domain;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class Favorites {

	private List<Favorite> favorites;

	private Favorites(List<Favorite> favorites) {
		this.favorites = new ArrayList<>(favorites);
	}

	public static Favorites of(List<Favorite> favorites) {
		return new Favorites(favorites);
	}

	public boolean hasSamePath(Member member, Station source, Station target) {
		return this.favorites.stream()
			.anyMatch(favorite ->
				favorite.isSameMember(member) && favorite.isSamePath(source, target));
	}
}
