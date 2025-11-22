package pooP2.util;

import pooP2.model.Produto;
import java.io.*;
import java.util.*;

public class ImportadorCSV {
    public static List<Produto> importarProdutosCSV(String arquivo) {
        List<Produto> produtos = new ArrayList<>();
        int linhasProcessadas = 0;
        int linhasComErro = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linhasProcessadas++;


                if (linha.trim().isEmpty()) {
                    continue;
                }

                String[] dados = linha.split(";");


                if (linhasProcessadas == 1 && !ehNumero(dados[1].trim())) {
                    System.out.println("Cabeçalho detectado e ignorado: " + linha);
                    continue;
                }

                if (dados.length >= 3) {
                    try {
                        String nome = dados[0].trim();
                        double preco = Double.parseDouble(dados[1].trim().replace(",", "."));
                        int estoque = Integer.parseInt(dados[2].trim());


                        if (nome.isEmpty() || preco <= 0 || estoque < 0) {
                            System.out.println("Linha " + linhasProcessadas + " ignorada - dados inválidos: " + linha);
                            linhasComErro++;
                            continue;
                        }

                        produtos.add(new Produto(nome, preco, estoque));
                        System.out.println("✓ Produto importado: " + nome + " - R$" + preco + " - Estoque: " + estoque);

                    } catch (NumberFormatException e) {
                        System.out.println("✗ Erro na linha " + linhasProcessadas + " - formato numérico inválido: " + linha);
                        linhasComErro++;
                    } catch (Exception e) {
                        System.out.println("✗ Erro na linha " + linhasProcessadas + ": " + e.getMessage());
                        linhasComErro++;
                    }
                } else {
                    System.out.println("✗ Linha " + linhasProcessadas + " ignorada - formato inválido: " + linha);
                    linhasComErro++;
                }
            }

            System.out.println("\n=== RESUMO DA IMPORTAÇÃO ===");
            System.out.println("Linhas processadas: " + linhasProcessadas);
            System.out.println("Produtos importados: " + produtos.size());
            System.out.println("Linhas com erro: " + linhasComErro);

        } catch (FileNotFoundException e) {
            System.out.println("❌ Arquivo não encontrado: " + arquivo);
            System.out.println("Certifique-se que o arquivo está na pasta do projeto.");
        } catch (IOException e) {
            System.out.println("❌ Erro ao ler arquivo: " + e.getMessage());
        }

        return produtos;
    }


    private static boolean ehNumero(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str.replace(",", "."));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}