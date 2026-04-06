package school.sptech.sistema_xingu_ia.dto;

public class GroqMessageStruct {
    private String role; // TIPO DE SOLICITAÇÃO (SISTEMA OU USUÁRIO)
    private String content; // CONTEÚDO DA SOLICITAÇÃO

    public GroqMessageStruct(String role, String content) {
        this.role = role;
        this.content = content;
    }
    public GroqMessageStruct() {}

    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}
}
