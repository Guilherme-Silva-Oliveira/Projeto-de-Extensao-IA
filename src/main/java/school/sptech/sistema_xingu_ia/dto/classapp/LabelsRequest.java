package school.sptech.sistema_xingu_ia.dto.classapp;

import java.util.List;

public record LabelsRequest(
        Integer totalItems,
        List<Label> labels
){}
