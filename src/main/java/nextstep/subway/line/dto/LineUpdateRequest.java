package nextstep.subway.line.dto;

import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.Line;

/**
 * packageName : nextstep.subway.line.dto
 * fileName : LineUpdateRequest
 * author : haedoang
 * date : 2021-12-02
 * description : 노선 업데이트 관련 Dto
 */
@NoArgsConstructor
public class LineUpdateRequest {
    private String name;
    private String color;

    public Line toLine() {
        return Line.of(name, color);
    }

}
