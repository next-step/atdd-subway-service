package nextstep.subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.BaseRequest;
import nextstep.subway.line.domain.Line;

@Getter
@NoArgsConstructor
public class LineRequest extends BaseRequest<Line> {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    @Builder
    private LineRequest(final String name, final String color, final Long upStationId, final Long downStationId, final int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    @Override
    public Line toEntity() {
        return new Line(name, color);
    }

    public SectionRequest toSectionRequest() {
        return SectionRequest.builder()
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
