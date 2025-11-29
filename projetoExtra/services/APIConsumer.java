package projetoExtra.services;

import projetoExtra.entities.Endereco;
import projetoExtra.exceptions.CEPInvalidoException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIConsumer {

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    public Endereco consultarCEP(String cep) throws CEPInvalidoException {
        // Estruturas lógicas: validação do CEP
        if (cep == null || cep.trim().isEmpty()) {
            throw new CEPInvalidoException("CEP não pode ser vazio");
        }

        String cepLimpo = cep.replaceAll("[^0-9]", "");

        if (cepLimpo.length() != 8) {
            throw new CEPInvalidoException("CEP deve ter 8 dígitos");
        }

        try {
            URL url = new URL(VIA_CEP_URL + cepLimpo + "/json/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new CEPInvalidoException("Erro na consulta do CEP. Código: " + responseCode);
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                return parseEnderecoFromJSON(response.toString(), cep);
            }

        } catch (Exception e) {
            throw new CEPInvalidoException("Erro ao consultar CEP: " + e.getMessage());
        }
    }

    private Endereco parseEnderecoFromJSON(String json, String cepOriginal) throws CEPInvalidoException {
        if (json.contains("\"erro\": true")) {
            throw new CEPInvalidoException("CEP não encontrado: " + cepOriginal);
        }

        try {
            String logradouro = extractValue(json, "logradouro");
            String bairro = extractValue(json, "bairro");
            String cidade = extractValue(json, "localidade");
            String estado = extractValue(json, "uf");

            Endereco endereco = new Endereco();
            endereco.setCep(cepOriginal);
            endereco.setLogradouro(logradouro);
            endereco.setBairro(bairro);
            endereco.setCidade(cidade);
            endereco.setEstado(estado);

            return endereco;

        } catch (Exception e) {
            throw new CEPInvalidoException("Erro ao processar resposta da API: " + e.getMessage());
        }
    }

    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return "";

        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);

        if (endIndex == -1) return "";
        return json.substring(startIndex, endIndex);
    }
}