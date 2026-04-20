package school.sptech.sistema_xingu_ia.dto.ia;

import java.util.List;

public class GroqResponse {
    private List<Choice> choices;
    public GroqResponse() {}
    public GroqResponse(List<Choice> choices) {this.choices = choices;}
    public List<Choice> getChoices() {return choices;}
    public void setChoices(List<Choice> choices) {this.choices = choices;}
}
