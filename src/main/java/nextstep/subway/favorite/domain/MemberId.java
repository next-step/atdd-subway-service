package nextstep.subway.favorite.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import nextstep.subway.common.CanNotDeleteException;

@Embeddable
public class MemberId {

    private Long memberId;

    public MemberId(Long id) {
        this.memberId = id;
    }

    protected MemberId() {
    }


    public void equalsId(MemberId memberId) {
        if (!this.memberId.equals(memberId.memberId)) {
            throw new CanNotDeleteException();
        }
    }

    public boolean hasMember() {
        return !Objects.isNull(memberId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberId memberId = (MemberId) o;
        return Objects.equals(this.memberId, memberId.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}
