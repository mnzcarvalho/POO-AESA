package projetoExtra.entities;

import projetoExtra.exceptions.InvalidCPFException;

public class IndividualCustomer extends Customer {
    private static final long serialVersionUID = 1L;

    private String cpf;

    public IndividualCustomer() {
        super();
    }

    public IndividualCustomer(String name, String email, String phone, String cpf) {
        super(name, email, phone);
        this.cpf = cpf;
    }

    @Override
    public String getDocument() {
        return cpf;
    }

    @Override
    public void validateDocument() throws InvalidCPFException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new InvalidCPFException("CPF não pode ser vazio");
        }

        String cleanCpf = cpf.replaceAll("[^0-9]", "");

        if (cleanCpf.length() != 11) {
            throw new InvalidCPFException("CPF deve ter 11 dígitos");
        }

        if (cleanCpf.matches("(\\d)\\1{10}")) {
            throw new InvalidCPFException("CPF inválido (dígitos repetidos)");
        }

        System.out.println("✅ CPF validado com sucesso: " + formatCpf());
    }

    @Override
    public String getCustomerType() {
        return "Pessoa Física";
    }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) throws InvalidCPFException {
        String oldCpf = this.cpf;
        this.cpf = cpf;

        try {
            validateDocument();
        } catch (InvalidCPFException e) {
            this.cpf = oldCpf;
            throw e;
        }
    }

    public String formatCpf() {
        if (cpf == null) return "";
        String cleanCpf = cpf.replaceAll("[^0-9]", "");
        if (cleanCpf.length() == 11) {
            return cleanCpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        return cpf;
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %d, CPF: %s]", getName(), getId(), formatCpf());
    }
}