package projetoExtra.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime registrationDate;
    private List<Address> addresses;
    private List<Category> categories;

    public Customer() {
        this.addresses = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.registrationDate = LocalDateTime.now();
    }

    public Customer(String name, String email, String phone) {
        this();
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public abstract String getDocument();
    public abstract void validateDocument() throws Exception;
    public abstract String getCustomerType();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (name.length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        this.name = name;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email != null && !email.isEmpty() && !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = email;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }

    public void addAddress(Address address) {
        if (address != null && !addresses.contains(address)) {
            addresses.add(address);
        }
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
    }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }

    public void addCategory(Category category) {
        if (category != null && !categories.contains(category)) {
            categories.add(category);
        }
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    public boolean hasCategory(String categoryName) {
        for (Category cat : categories) {
            if (cat.getName().equalsIgnoreCase(categoryName)) {
                return true;
            }
        }
        return false;
    }

    public String getContactInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(name);
        if (email != null && !email.isEmpty()) {
            sb.append(", Email: ").append(email);
        }
        if (phone != null && !phone.isEmpty()) {
            sb.append(", Tel: ").append(phone);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %d, Tipo: %s, Documento: %s]",
                name, id, getCustomerType(), getDocument());
    }
}