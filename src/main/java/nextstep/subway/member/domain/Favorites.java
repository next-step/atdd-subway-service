package nextstep.subway.member.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>();

    public Favorites() {
    }

    public Favorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    /**
     * 주어진 역 정보로 자신의 즐겨찾기중에 정보가 일치하는 즐겨찾기를 반환합니다.
     * @param source
     * @param target
     * @return favorite
     */
    public Favorite getFavorite(Station source, Station target) {
        return this.favorites.stream()
                .filter(memberFavorite -> memberFavorite.equalsSource(source)
                        && memberFavorite.equalsTarget(target))
                .findAny().orElseThrow(() -> new IllegalArgumentException("주어진 지하철역으로 등록된 즐겨찾기를 찾을 수 없습니다."));
    }

    public void add(Favorite favorite) {
        this.favorites.add(favorite);
    }

    /**
     * 즐겨찾기를 삭제합니다
     * @param favoriteId 
     */
    public void remove(Long favoriteId) {
        Favorite favorite = this.favorites.stream()
                .filter(memberFavorite -> memberFavorite.equalsId(favoriteId))
                .findAny().orElseThrow(() -> new IllegalArgumentException("다른 유저의 즐겨찾기는 지울 수 없습니다."));
        this.favorites.remove(favorite);
    }
}
