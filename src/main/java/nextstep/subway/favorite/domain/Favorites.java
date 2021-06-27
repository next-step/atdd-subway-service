package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {
	@OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Favorite> favorites = new ArrayList<>();

	public Favorites() {
	}

	public List<Favorite> getFavorites() {
		return favorites;
	}

	public void addFavorite(Favorite favorite) {
		this.favorites.add(favorite);
	}

	public void removeFavorite(Favorite favorite) {
		this.favorites.remove(favorite);
	}
}
