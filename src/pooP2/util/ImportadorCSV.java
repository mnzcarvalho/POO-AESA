package pooP2.util;

import pooP2.model.Produto;
import java.io.*;
import java.util.*;

public class ImportadorCSV {
    public static List<Produto> importarProdutosCSV(String arquivo) {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 3) {
                    String nome = dados[0];
                    double preco = Double.parseDouble(dados[1]);
                    int estoque = Integer.parseInt(dados[2]);
                    produtos.add(new Produto(nome, preco, estoque));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
        return produtos;
    }
}