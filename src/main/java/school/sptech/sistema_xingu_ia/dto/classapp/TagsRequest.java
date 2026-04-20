package school.sptech.sistema_xingu_ia.dto.classapp;

import java.util.List;

public record TagsRequest(
        Integer totalItems,
        List<Tag> tags
) {}
