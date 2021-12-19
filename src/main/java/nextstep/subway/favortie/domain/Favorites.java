package nextstep.subway.favortie.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		Optional<Favorite> founded = this.favorites.stream()
			.filter(favorite -> favorite.isSameMember(member))
			.filter(favorite -> favorite.isSamePath(source, target))
			.findFirst();
		return founded.isPresent();
	}
}
