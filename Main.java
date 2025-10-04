public class Main {
    public static void main(String[] args) {
        Desenvolvedor dev1 = new Desenvolvedor("Jao", 9999999, 1400);
        Desenvolvedor dev2 = new Desenvolvedor("uoooowww", 1111111, 2000);
        Desenvolvedor[] desenvolvedores = {dev1, dev2};
        Gerente gerente1 = new Gerente("Nome1", 888444, 5000);

        System.out.println("\nImprimindo dados dos desenvolvedores");

        for (Desenvolvedor desenvolvedor : desenvolvedores) {
            desenvolvedor.imprime();
        }

        gerente1.imprime();

    }
}
