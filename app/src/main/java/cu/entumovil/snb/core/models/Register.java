package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class Register implements Serializable {


    private String username;
    private String password;
    private String email;
    private String provider;
    private String phone;
    private String rol;
    private String name;
    private String lastName;
    private int age;
    private String genre;
    private int count_jewl;
    private String vendedor;
    private Boolean isBlocked;

    public Register(String username, String password, String email, String provider, String phone, String rol, String name, String lastName, int age, String genre, int count_jewl, String vendedor, Boolean isBlocked) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.phone = phone;
        this.rol = rol;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.genre = genre;
        this.count_jewl = count_jewl;
        this.vendedor = vendedor;
        this.isBlocked = isBlocked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCount_jewl() {
        return count_jewl;
    }

    public void setCount_jewl(int count_jewl) {
        this.count_jewl = count_jewl;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }
}
