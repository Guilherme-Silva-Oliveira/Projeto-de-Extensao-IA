package school.sptech.sistema_xingu_ia.dto.ia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroqModelResponse {
    private String object;
    private List<GroqModelData> data;

    public GroqModelResponse() {}

    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }
    public List<GroqModelData> getData() { return data; }
    public void setData(List<GroqModelData> data) { this.data = data; }
}
