package school.sptech.sistema_xingu_ia.dto.ia;

import java.util.List;

public class GroqResponse {
    private List<Choice> choices;
    private Usage usage;

    public GroqResponse() {}
    public GroqResponse(List<Choice> choices) {this.choices = choices;}
    public List<Choice> getChoices() {return choices;}
    public void setChoices(List<Choice> choices) {this.choices = choices;}

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
