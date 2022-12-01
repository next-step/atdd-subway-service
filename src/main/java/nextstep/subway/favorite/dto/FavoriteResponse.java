package nextstep.subway.favorite.dto;

public class FavoriteResponse {
    Long id;
    Long source;
    Long target;
    Long member;

    public FavoriteResponse(Long id, Long source, Long target, Long member) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Long getMember() {
        return member;
    }
}
