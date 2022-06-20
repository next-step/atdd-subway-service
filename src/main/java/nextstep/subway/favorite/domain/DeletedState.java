package nextstep.subway.favorite.domain;

public enum DeletedState {
    YES, NO;

    public boolean isDeleted() {
        return this == YES;
    }
}
