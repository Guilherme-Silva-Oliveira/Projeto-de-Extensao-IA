package school.sptech.sistema_xingu_ia.dto;

import java.util.List;

public class GroqRequest {
    private String model; // MODELO DA IA
    private List<GroqMessageStruct> messages; // CONTEXTO + SOLICITAÇÃO DO USUÁRIO
    private Double temperature; // NÍVEL DE CRIATIVIDADE DA IA - O QUANTO ELA VAI VARIAR
    private Integer max_completion_tokens; // LIMITAÇÃO DE CONSUMO DE TOKENS E COMPLEXIDADE DE RETORNO
    private Boolean stream; // FORMATO DE RESPOSTA DA IA

    public GroqRequest(String model, List<GroqMessageStruct> messages, Double temperature, Integer max_completion_tokens, Boolean stream) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
        this.max_completion_tokens = max_completion_tokens;
        this.stream = stream;
    }
    public GroqRequest() {}

    public String getModel() {return model;}
    public void setModel(String model) {this.model = model;}
    public List<GroqMessageStruct> getMessages() {return messages;}
    public void setMessages(List<GroqMessageStruct> messages) {this.messages = messages;}
    public Double getTemperature() {return temperature;}
    public void setTemperature(Double temperature) {this.temperature = temperature;}
    public Integer getMax_completion_tokens() {return max_completion_tokens;}
    public void setMax_completion_tokens(Integer max_completion_tokens) {this.max_completion_tokens = max_completion_tokens;}
    public Boolean getStream() {return stream;}
    public void setStream(Boolean stream) {this.stream = stream;}
}
