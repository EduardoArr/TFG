package com.example.gestordeinventario.Controlador;

public class Usuario {
    private String email;
    private String password;
    private String esAdmin;
    private String id_lin;

    public String getId_lin() {
        return id_lin;
    }

    public void setId_lin(String id_lin) {
        this.id_lin = id_lin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEsAdmin() {
        return esAdmin;
    }

    public void setEsAdmin(String esAdmin) {
        this.esAdmin = esAdmin;
    }
}
