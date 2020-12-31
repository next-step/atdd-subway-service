package nextstep.subway.path.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.FeeCalculator;
import nextstep.subway.station.dto.StationResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PathResponse {

	private List<StationResponse> stations;

	private int distance;

	private int fee;

	@JsonIgnore
	private List<LineResponse> lines;

	@JsonIgnore
	@Setter
	private Integer age;

	private PathResponse(List<StationResponse> stations, int distance, List<LineResponse> lines) {
		this.stations = stations;
		this.distance = distance;
		this.lines = lines;
		this.fee = calculateFee();
	}

	public static PathResponse of(List<StationResponse> stations, int distance, List<LineResponse> lines) {
		return new PathResponse(stations, distance, lines);
	}

	private int calculateFee() {
		FeeCalculator feeCalculator = new FeeCalculator(this.distance, this.age, findMaxExtraFee(lines));
		return feeCalculator.calculate();
	}

	private int findMaxExtraFee(List<LineResponse> lines) {
		return lines.stream()
			.mapToInt(LineResponse::getExtraFee)
			.max()
			.orElse(0);
	}

}
