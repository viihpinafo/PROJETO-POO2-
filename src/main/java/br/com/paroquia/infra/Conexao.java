package br.com.paroquia.infra;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexao {

    public static Connection conectar() {
        try {
            String url = "jdbc:mysql://mysql-687974-projeto-paroquia.l.aivencloud.com:10702/defaultdb?sslMode=REQUIRED";
            String usuario = "avnadmin";
            String senha = "AVNS_IiPSxekqFoGBI_1Rgrb";
            return DriverManager.getConnection(url, usuario, senha);
        } catch (Exception e) {
            System.out.println("Erro na conexão: " + e.getMessage());
            return null;
        }
    }

    // só pra testar
    public static void main(String[] args) {
        if (conectar() != null) {
            System.out.println("CONECTOU!");
        } else {
            System.out.println("Não conectou.");
        }
    }
}