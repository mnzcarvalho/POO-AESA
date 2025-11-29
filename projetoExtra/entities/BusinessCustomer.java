package projetoExtra.entities;

public class BusinessCustomer extends Customer {
    private static final long serialVersionUID = 1L;

    private String cnpj;
    private String companyName;

    public BusinessCustomer() {
        super();
    }

    public BusinessCustomer(String name, String email, String phone, String cnpj, String companyName) {
        super(name, email, phone);
        this.cnpj = cnpj;
        this.companyName = companyName;
    }

    @Override
    public String getDocument() {
        return cnpj;
    }

    @Override
    public void validateDocument() throws Exception {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ não pode ser vazio");
        }

        String cleanCnpj = cnpj.replaceAll("[^0-9]", "");

        if (cleanCnpj.length() != 14) {
            throw new IllegalArgumentException("CNPJ deve ter 14 dígitos");
        }

        if (cleanCnpj.matches("(\\d)\\1{13}")) {
            throw new IllegalArgumentException("CNPJ inválido (dígitos repetidos)");
        }

        System.out.println("✅ CNPJ validado com sucesso: " + formatCnpj());
    }

    @Override
    public String getCustomerType() {
        return "Pessoa Jurídica";
    }

    public String getCnpj() { return cnpj; }

    public void setCnpj(String cnpj) throws Exception {
        String oldCnpj = this.cnpj;
        this.cnpj = cnpj;

        try {
            validateDocument();
        } catch (Exception e) {
            this.cnpj = oldCnpj;
            throw e;
        }
    }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) {
        if (companyName != null && companyName.length() < 2) {
            throw new IllegalArgumentException("Razão social deve ter pelo menos 2 caracteres");
        }
        this.companyName = companyName;
    }

    public String formatCnpj() {
        if (cnpj == null) return "";
        String cleanCnpj = cnpj.replaceAll("[^0-9]", "");
        if (cleanCnpj.length() == 14) {
            return cleanCnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        return cnpj;
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %d, CNPJ: %s, Razão Social: %s]",
                getName(), getId(), formatCnpj(), companyName != null ? companyName : "N/A");
    }

    @Override
    public String getContactInfo() {
        String baseInfo = super.getContactInfo();
        if (companyName != null && !companyName.isEmpty()) {
            return "Razão Social: " + companyName + ", " + baseInfo;
        }
        return baseInfo;
    }
}