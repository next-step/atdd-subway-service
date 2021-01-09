package nextstep.subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Line;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LineRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    private Long upStationId;
    private Long downStationId;
    @Min(1)
    private int distance;

    public Line toLine() {
        return new Line(name, color);
    }
}
