package nextstep.subway.favorite.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.ErrorMessage;

@Embeddable
public class Favorites {

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Favorite> favorites = new ArrayList<>();

    public void remove(Favorite favorite) {
        if(!favorites.contains(favorite)){
            throw new IllegalStateException(ErrorMessage.CANNOT_REMOVE_FAVORITE_NO_MATCHING_ID);
        }
        favorites.remove(favorite);
    }
    public void add(Favorite favorite){
        if(favorites.contains(favorite)){
            throw new IllegalStateException(ErrorMessage.CANNOT_ADD_FAVORITE_DUPLICATED);
        }
        favorites.add(favorite);
    }
    public List<Favorite> getFavorites() {
        return Collections.unmodifiableList(favorites);
    }

    public boolean contains(Favorite favorite){
        return favorites.contains(favorite);
    }
}
