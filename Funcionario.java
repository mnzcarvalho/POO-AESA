public abstract class Funcionario {
    private String nome;
    private int cpf;

    //Construtor padrão
    public Funcionario() {
    }
    //Construtor com parâmetros
    public Funcionario(String nome, int cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
  
    //metodo com retorno
    public abstract double bonusSalario();

    //metodo sem retorno
    public abstract void imprime();

    //Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCpf() {
        return cpf;
    }

    public void setCpf(int cpf) {
        this.cpf = cpf;
    }

    protected abstract double getSalario();
}
