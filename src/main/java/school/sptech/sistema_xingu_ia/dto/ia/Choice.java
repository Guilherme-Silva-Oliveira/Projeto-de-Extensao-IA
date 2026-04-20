package school.sptech.sistema_xingu_ia.dto.ia;

public class Choice {
    private int index; // POSIÇÃO DA RESPOSTA
    private GroqMessageStruct message; // RESPOSTA EM QUESTÃO, CONTEÚDO
    private String finish_reason; // MOTIVO DA IA TER PARADO OU FINALIZAÇÃO

    public Choice(int index, GroqMessageStruct message, String finish_reason) {
        this.index = index;
        this.message = message;
        this.finish_reason = finish_reason;
    }
    public Choice() {}

    public int getIndex() {return index;}
    public void setIndex(int index) {this.index = index;}
    public GroqMessageStruct getMessage() {return message;}
    public void setMessage(GroqMessageStruct message) {this.message = message;}
    public String getFinish_reason() {return finish_reason;}
    public void setFinish_reason(String finish_reason) {this.finish_reason = finish_reason;}
}
