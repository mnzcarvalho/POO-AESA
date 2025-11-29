package projetoExtra.entities;


import projetoExtra.exceptions.CPFInvalidoException;

public class ClientePessoaFisica extends Cliente {
    private static final long serialVersionUID = 1L;

    private String cpf;


    public ClientePessoaFisica() {
        super();
    }

    public ClientePessoaFisica(String nome, String email, String telefone, String cpf) {
        super(nome, email, telefone);
        this.cpf = cpf;
    }


    @Override
    public String getDocumento() {
        return cpf;
    }

    @Override
    public void validarDocumento() throws CPFInvalidoException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new CPFInvalidoException("CPF não pode ser vazio");
        }

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");


        if (cpfLimpo.length() != 11) {
            throw new CPFInvalidoException("CPF deve ter 11 dígitos");
        }


        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new CPFInvalidoException("CPF inválido (dígitos repetidos)");
        }


        System.out.println("✅ CPF validado com sucesso: " + formatarCPF());
    }

    @Override
    public String getTipoCliente() {
        return "Pessoa Física";
    }


    public String getCpf() { return cpf; }

    public void setCpf(String cpf) throws CPFInvalidoException {
        String cpfAntigo = this.cpf;
        this.cpf = cpf;

        try {
            validarDocumento();
        } catch (CPFInvalidoException e) {
            this.cpf = cpfAntigo; // Reverte se inválido
            throw e;
        }
    }


    public String formatarCPF() {
        if (cpf == null) return "";
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        if (cpfLimpo.length() == 11) {
            return cpfLimpo.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        return cpf;
    }


    @Override
    public String toString() {
        return String.format("%s [CPF: %s]", getNome(), formatarCPF());
    }
}