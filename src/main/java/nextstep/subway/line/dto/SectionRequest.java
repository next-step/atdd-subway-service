package nextstep.subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Section;

@Getter
@NoArgsConstructor
public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    @Builder
    public SectionRequest(final Long upStationId, final Long downStationId, final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    protected Section toSection() {
        return new Section();
    }
}
