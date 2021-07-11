package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Favorites {
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Favorite> favorites = new ArrayList<>();

	public void addFavorite(Favorite favorite) {
		favorites.add(favorite);
	}

	public void deleteFavorite(Long favoriteId) {
		Favorite favorite = getMatchFavorite(favoriteId);
		favorites.remove(favorite);
		favorite.toMember(null);
	}

	private Favorite getMatchFavorite(Long favoriteId) {
		return favorites.stream()
			.filter(f -> f.equalsById(favoriteId))
			.findFirst()
			.orElseThrow(RuntimeException::new);
	}

	public Favorite getLatestFavorite() {
		return favorites.stream()
			.max((x1, x2) -> (int) (x1.getId() - x2.getId()))
			.orElseThrow(RuntimeException::new);
	}

	public boolean isEmpty() {
		return favorites.isEmpty();
	}

	public List<Favorite> getList() {
		return favorites;
	}
}
