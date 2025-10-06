public class Desenvolvedor extends Funcionario{
        private double salario;
        private double bonus;

    public Desenvolvedor(String nome,int cpf, double salario) {
        super(nome, cpf);
        this.salario = salario;
    }

    @Override
    public double bonusSalario() {
        return this.bonus = (this.salario * 0.10);
    }

    @Override
    public void imprime() {
        this.bonusSalario();
        System.out.println("\nDados do desenvolvedor: ");
        System.out.println("Nome: " + this.getNome());
        System.out.println("CPF: " + this.getCpf());
        System.out.println("Salário: " + this.getSalario());
        System.out.printf("Bonus do salário: %.2f%n", this.getBonus());
        System.out.printf("Salário + bônus: %.2f%n", (this.getSalario() + this.getBonus()));
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getBonus() {
        return bonus;
    }
}