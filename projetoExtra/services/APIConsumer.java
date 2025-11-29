package projetoExtra.services;

import projetoExtra.entities.Address;
import projetoExtra.exceptions.InvalidZipCodeException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class APIConsumer {

    public Address consultarCEP(String cep) throws InvalidZipCodeException {
        if (cep == null || cep.trim().isEmpty() || !cep.matches("\\d{5}-?\\d{3}")) {
            throw new InvalidZipCodeException("CEP inválido: " + cep);
        }

        cep = cep.replace("-", "");
        String urlString = "https://viacep.com.br/ws/" + cep + "/json/";

        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new InvalidZipCodeException("Erro ao consultar CEP. Código: " + responseCode);
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("🔍 Resposta da API: " + response.toString()); // DEBUG
            return parseResponse(response.toString());

        } catch (Exception e) {
            throw new InvalidZipCodeException("Erro ao consultar CEP: " + e.getMessage());
        }
    }

    private Address parseResponse(String jsonResponse) throws InvalidZipCodeException {
        if (jsonResponse.contains("\"erro\": true") ||
                jsonResponse.contains("\"erro\":true") ||
                jsonResponse.trim().isEmpty() ||
                jsonResponse.equals("{}")) {
            throw new InvalidZipCodeException("CEP não encontrado na base de dados");
        }

        try {
            String logradouro = extractValue(jsonResponse, "logradouro");
            String bairro = extractValue(jsonResponse, "bairro");
            String cidade = extractValue(jsonResponse, "localidade");
            String estado = extractValue(jsonResponse, "uf");
            String cep = extractValue(jsonResponse, "cep");

            System.out.println("🔍 Dados extraídos - Cidade: " + cidade + ", Estado: " + estado + ", CEP: " + cep); // DEBUG

            if (cidade == null || estado == null || cep == null) {
                throw new InvalidZipCodeException("Dados do CEP incompletos. Resposta: " + jsonResponse);
            }

            Address address = new Address();
            address.setZipCode(cep);
            address.setStreet(logradouro != null ? logradouro.trim() : "Não informado");
            address.setNeighborhood(bairro != null ? bairro.trim() : "Não informado");
            address.setCity(cidade);
            address.setState(estado);

            return address;

        } catch (Exception e) {
            throw new InvalidZipCodeException("Erro ao processar resposta da API: " + e.getMessage() + " | Resposta: " + jsonResponse);
        }
    }

    private String extractValue(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":\"";
            int start = json.indexOf(searchKey);
            if (start == -1) {
                searchKey = "\"" + key + "\":";
                start = json.indexOf(searchKey);
                if (start == -1) return null;
                start += searchKey.length();

                int end = json.indexOf(",", start);
                if (end == -1) end = json.indexOf("}", start);
                if (end == -1) return null;

                String value = json.substring(start, end).trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                return value;
            }

            start += searchKey.length();
            int end = json.indexOf("\"", start);
            if (end == -1) return null;

            return json.substring(start, end);
        } catch (Exception e) {
            return null;
        }
    }
}