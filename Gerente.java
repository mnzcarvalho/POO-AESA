public class Gerente extends Funcionario {
    private double salario;
    private double bonus;

    public Gerente (String nome, int cpf, double salario){
        super(nome, cpf);
        this.salario = salario;
    }

    @Override
    public double bonusSalario(){
        return this.bonus = (this.salario * 0.10);
    }

    @Override
    public void imprime() {
        this.bonusSalario();
        System.out.println("\nDados do gerente: ");
        System.out.println("nome " + this.getNome());
        System.out.println("CPF: " + this.getCpf());
        System.out.println("Salário: " + this.getSalario());
        System.out.println("Bonus do salário: %.2f%n" + this.bonus);
        System.out.println("Salário + bonus: %.2f%n" + (this.getSalario() + this.bonus));
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public double getBonus(){
        return bonus;
    }

}
