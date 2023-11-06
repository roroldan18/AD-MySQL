package Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    private String url;
    private String user;
    private String password;

    public Conexion() {
    }

    public Conexion(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void cargarConfiguracionDesdeArchivo(String archivoConfiguracion) {
        try (FileInputStream fis = new FileInputStream(archivoConfiguracion)) {
            Properties prop = new Properties();
            prop.load(fis);
            this.url = prop.getProperty("url");
            this.user = prop.getProperty("username");
            this.password = prop.getProperty("password");
        } catch (IOException e) {
            // Maneja la excepción apropiadamente (puede registrar o lanzar una excepción más específica).
            e.printStackTrace();
        }
    }

    public Connection conectar() throws SQLException {
        try {
            // Registra el controlador JDBC de MySQL (debes importar la biblioteca JDBC de MySQL)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establece la conexión a la base de datos
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (ClassNotFoundException e) {
            // Maneja la excepción apropiadamente (puede registrar o lanzar una excepción más específica).
            e.printStackTrace();
            return null;
        }
    }
}
