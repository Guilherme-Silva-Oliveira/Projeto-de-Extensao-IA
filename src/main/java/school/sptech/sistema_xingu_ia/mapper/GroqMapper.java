package school.sptech.sistema_xingu_ia.mapper;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import school.sptech.sistema_xingu_ia.dto.ia.GroqPedidoMaterial;

@Component
public class GroqMapper {
    public GroqPedidoMaterial toGroqPedidoMaterial(String json){
        JSONObject obj = new JSONObject(json);
        GroqPedidoMaterial response = new GroqPedidoMaterial();
        response.setNome_professor(obj.optString("nome_professor",null));
        response.setNome_material(obj.optString("nome_material",null));
        response.setQuantidade(obj.optInt("quantidade",0));
        response.setData_solicitacao(obj.optString("data_solicitacao",null));
        response.setMotivo(obj.optString("motivo",null));
        response.setAlerta(obj.optString("alerta",null));
        response.setDeveDevolver(obj.optBoolean("deve_devolver",false));
        return response;
    }
}
