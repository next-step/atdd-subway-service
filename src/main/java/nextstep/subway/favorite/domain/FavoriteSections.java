package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class FavoriteSections {
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteSection> favoriteSections;

    public FavoriteSections() {
        this.favoriteSections = new ArrayList<>();
    }

    public FavoriteSections(List<FavoriteSection> favoriteSections) {
        this.favoriteSections = favoriteSections;
    }

    public List<FavoriteSection> getFavoriteSections() {
        return favoriteSections;
    }

    public void addFavoriteSection(Member member, FavoriteSection favoriteSection) {
        favoriteSections.add(favoriteSection);
        favoriteSection.setMember(member);
    }

    public void remove(FavoriteSection favoriteSection) {
        favoriteSections.remove(favoriteSection);
    }
}
