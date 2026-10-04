package school.sptech.sistema_xingu_ia.mapper;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import school.sptech.sistema_xingu_ia.dto.ia.GroqPedidoMaterial;

@Component
public class GroqMapper {
    public GroqPedidoMaterial toGroqPedidoMaterial(String json){
        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("A resposta retornada pela IA veio vazia.");
        }

        String limpo = json.trim();
        if (limpo.startsWith("```json")) {
            limpo = limpo.substring(7);
        } else if (limpo.startsWith("```")) {
            limpo = limpo.substring(3);
        }
        if (limpo.endsWith("```")) {
            limpo = limpo.substring(0, limpo.length() - 3);
        }
        limpo = limpo.trim();

        int inicio = limpo.indexOf('{');
        int fim = limpo.lastIndexOf('}');
        if (inicio != -1 && fim != -1 && fim > inicio) {
            limpo = limpo.substring(inicio, fim + 1);
        }

        JSONObject obj = new JSONObject(limpo);
        GroqPedidoMaterial response = new GroqPedidoMaterial();
        response.setNome_professor(obj.optString("nome_professor", null));
        response.setNome_material(obj.optString("nome_material", null));
        response.setQuantidade(obj.optString("quantidade", null));
        response.setData_solicitacao(obj.optString("data_solicitacao", null));
        response.setMotivo(obj.optString("motivo", null));
        response.setAlerta(obj.optString("alerta", null));
        response.setDeveDevolver(obj.optString("deve_devolver", null));
        return response;
    }
}