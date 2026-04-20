package school.sptech.sistema_xingu_ia.dto.classapp;

import java.util.List;

public record MensagemRequest(
        Integer id,
        String subject,
        String content,
        String type,
        List<Integer> recipients
) {}
