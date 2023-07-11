package cu.entumovil.snb.core.models;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String username;
    private String email;
    private String phone;
    private String rol;
    private String name;
    private String lastName;
    private int age;
    private String genre;
    private int count_jewl;
    private String vendor;
    public boolean isBlocked;

    public User(int id, String username, String email, String phone, String rol, String name, String lastName, int age, String genre, int count_jewl, String vendor, boolean isBlocked) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.rol = rol;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.genre = genre;
        this.count_jewl = count_jewl;
        this.vendor = vendor;
        this.isBlocked = isBlocked;
    }

    public User(int id, String username, String email, String phone, String rol, String name, String lastName, int age, String genre, int count_jewl, String vendor) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.rol = rol;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.genre = genre;
        this.count_jewl = count_jewl;
        this.vendor = vendor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}


