package nextstep.subway.line.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SectionRequest {
	@NotNull
	private Long upStationId;
	@NotNull
	private Long downStationId;
	@Min(1)
	private int distance;
}
