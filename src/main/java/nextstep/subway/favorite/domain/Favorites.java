package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {
	@OneToMany(mappedBy = "member")
	private List<Favorite> favorites = new ArrayList<>();

	public Favorites() {
	}

	public List<Favorite> getFavorites() {
		return favorites;
	}

	public void addFavorite(Favorite favorite) {
		this.favorites.add(favorite);
	}
}
