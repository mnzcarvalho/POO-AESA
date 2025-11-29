package projetoExtra.entities;

import java.io.Serializable;

public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String type;

    public Address() {}

    public Address(String zipCode, String street, String number, String neighborhood, String city, String state) {
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }
        this.zipCode = zipCode;
    }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (street != null && !street.trim().isEmpty()) {
            sb.append(street);
        }

        if (number != null && !number.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(number);
        }

        if (neighborhood != null && !neighborhood.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(neighborhood);
        }

        if (city != null && !city.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(city);
        }

        if (state != null && !state.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(state);
        }

        if (zipCode != null && !zipCode.trim().isEmpty()) {
            if (sb.length() > 0) sb.append(" (");
            sb.append(zipCode);
            if (sb.toString().contains("(")) sb.append(")");
        }

        return sb.toString();
    }
}