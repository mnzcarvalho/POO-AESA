package projetoExtra.entities;

public class ClientePessoaJuridica extends Cliente {
    private static final long serialVersionUID = 1L;

    private String cnpj;
    private String razaoSocial;


    public ClientePessoaJuridica() {
        super();
    }

    public ClientePessoaJuridica(String nome, String email, String telefone, String cnpj, String razaoSocial) {
        super(nome, email, telefone);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }

    @Override
    public String getDocumento() {
        return cnpj;
    }

    @Override
    public void validarDocumento() throws Exception {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ não pode ser vazio");
        }

        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");

        if (cnpjLimpo.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve ter 14 dígitos");
        }

        if (cnpjLimpo.matches("(\\d)\\1{13}")) {
            throw new IllegalArgumentException("CNPJ inválido (dígitos repetidos)");
        }

        System.out.println("✅ CNPJ validado com sucesso: " + formatarCNPJ());
    }

    @Override
    public String getTipoCliente() {
        return "Pessoa Jurídica";
    }

    public String getCnpj() { return cnpj; }

    public void setCnpj(String cnpj) throws Exception {
        String cnpjAntigo = this.cnpj;
        this.cnpj = cnpj;

        try {
            validarDocumento();
        } catch (Exception e) {
            this.cnpj = cnpjAntigo; // Reverte se inválido
            throw e;
        }
    }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) {
        if (razaoSocial != null && razaoSocial.length() < 2) {
            throw new IllegalArgumentException("Razão social deve ter pelo menos 2 caracteres");
        }
        this.razaoSocial = razaoSocial;
    }

    public String formatarCNPJ() {
        if (cnpj == null) return "";
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        if (cnpjLimpo.length() == 14) {
            return cnpjLimpo.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        return cnpj;
    }

    @Override
    public String toString() {
        return String.format("%s [CNPJ: %s, Razão Social: %s]",
                getNome(), formatarCNPJ(), razaoSocial != null ? razaoSocial : "N/A");
    }

    @Override
    public String getInformacoesContato() {
        String infoBase = super.getInformacoesContato();
        if (razaoSocial != null && !razaoSocial.isEmpty()) {
            return "Razão Social: " + razaoSocial + ", " + infoBase;
        }
        return infoBase;
    }
}