package br.com.bank.system.model.customer;

import java.io.Serializable;
import java.util.Objects;

public class Customer implements Serializable {
    private static final long seriaVersionUID = 1L;

    private final long id;
    private String name;
    private String cpf;

    public Customer(long id, String name, String cpf) {
        this.id = id;
        setName(Objects.requireNonNull(name, "name must not be null"));
        setCpf(Objects.requireNonNull(cpf, "CPF must not be null"));
        if(!isValidCpf(this.cpf)){
            throw new IllegalArgumentException("CPF invalid: " + cpf);
        }
    }

    private static boolean isValidCpf(String cpf) {
        String digits = cpf.replaceAll("\\D", "");
        return digits.length() == 11;
    }

    public long getId() {
        return id;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized String getCpf() {
        return cpf;
    }

    public synchronized void setCpf(String cpf) {
        Objects.requireNonNull(cpf, "CPF must not be null");
        if(!isValidCpf(cpf)){
            throw new IllegalArgumentException("CPF invalid: " + cpf);
        }
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer other)) return false;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Custumer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
