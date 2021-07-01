package nextstep.subway.path.dto;

import java.time.LocalDateTime;

import nextstep.subway.path.domain.PathVertex;

public class VertexResponse {

    private Long id;
    private String name;
    private LocalDateTime createdDate;

    protected VertexResponse() {
    }

    private VertexResponse(Long id, String name, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
    }

    public static VertexResponse of(PathVertex pathVertex) {
        return new VertexResponse(pathVertex.getId(), pathVertex.getName(), pathVertex.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
