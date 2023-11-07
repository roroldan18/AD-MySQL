package Controller;

import Entity.Contacto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class GestionBD {

    public Connection conectar() {
        try{
            Conexion conexion = new Conexion();
            conexion.cargarConfiguracionDesdeArchivo("properties");
            Connection connection = conexion.conectar();
            System.out.println("¡¡Conectado a la base de datos!!");
            return connection;
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
        return null;
    }

    public void desconectar(Connection conexion) {
        try{
            conexion.close();
        } catch (Exception e) {
            System.out.println("Error al desconectar a la base de datos");
        }
    }

    public boolean agregarContacto(Contacto contacto){
            try{
            Connection conexion = conectar();
            String sql = "INSERT INTO contactos (nombre, apellido, email, telefono) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, contacto.getNombre());
            statement.setString(2, contacto.getApellido());
            statement.setString(3, contacto.getEmail());
            statement.setString(4, Integer.toString(contacto.getTelefono()));
            int row = statement.executeUpdate();
            if(row > 0){
                System.out.println("Contacto agregado");
                return true;
            } else {
                System.out.println("No se pudo agregar el contacto");
            }
            desconectar(conexion);
            return true;
        } catch (Exception e) {
            System.out.println("Error al agregar contacto");
            e.printStackTrace();
            return false;
        }
    }

    public void listarContactos(){
        Connection conexion = conectar();

        try{
            String sql = "SELECT * FROM contactos";
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String email = resultSet.getString("email");
                int telefono = resultSet.getInt("telefono");
                System.out.println("ID: " + id + "\nNombre: " + nombre + "\nApellido: " + apellido + "\nEmail: " + email + "\nTeléfono: " + telefono);
                System.out.println("--------------------------------------------------");
            }

            desconectar(conexion);
        } catch (Exception e) {
            System.out.println("Error al listar contactos");
        } finally {
            desconectar(conexion);
        }
    }


    public Contacto buscarContacto(String nombre) {
        Connection conexion = conectar();
        try{
            String sql = "SELECT * FROM contactos WHERE nombre = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, nombre);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("id");
                String nombreEncontrado = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String email = resultSet.getString("email");
                int telefono = resultSet.getInt("telefono");
                Contacto contacto = new Contacto(id, nombreEncontrado, apellido, telefono, email);
                desconectar(conexion);
                return contacto;
            } else {
                desconectar(conexion);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error al buscar contacto");
            e.printStackTrace();
            return null;
        }
        finally {
            desconectar(conexion);
        }
    }

    public void editarContacto(Contacto contacto) {
        Connection conexion = conectar();
        try{
            PreparedStatement ps = conexion.prepareStatement("UPDATE contactos SET nombre = ?, apellido = ?, telefono = ? WHERE id = ?");
            ps.setString(1, contacto.getNombre());
            ps.setString(2, contacto.getApellido());
            ps.setString(3, Integer.toString(contacto.getTelefono()));
            ps.setString(4, Integer.toString(contacto.getId()));
            int row = ps.executeUpdate();
            if(row > 0){
                System.out.println("Contacto actualizado");
            } else {
                System.out.println("No se pudo actualizar el contacto");
            }
        } catch (Exception e){
            System.out.println("Error al actualizar el contacto");
            e.printStackTrace();
        }
        finally {
            desconectar(conexion);
        }

    }

    private void insertUltimoEliminado(Contacto contacto){
        Connection conexion = conectar();
        try{
            // Agrego en la tabla de ultimos eliminados
            String sqlInsert = "INSERT INTO ultimo_registro_eliminado (id_nombre, nombre, apellido, email, telefono, fecha_eliminacion) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conexion.prepareStatement(sqlInsert);
            statement.setString(1, Integer.toString(contacto.getId()));
            statement.setString(2, contacto.getNombre());
            statement.setString(3, contacto.getApellido());
            statement.setString(4, contacto.getEmail());
            statement.setString(5, Integer.toString(contacto.getTelefono()));
            statement.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
            int rowInsert = statement.executeUpdate();

            if(rowInsert > 0){
                System.out.println("Contacto agregado a la tabla de ultimos eliminados");
            } else {
                System.out.println("No se pudo agregar el contacto a la tabla de ultimos eliminados");
            }
        } catch (Exception e){
            System.out.println("Error al insertar en la tabla de ultimos eliminados");
            e.printStackTrace();
        }
        finally {
            desconectar(conexion);
        }
    }

    public void recuperarUltimoEliminado(){
        Connection conexion = conectar();
        try{
            String sql = "SELECT * FROM ultimo_registro_eliminado ORDER BY fecha_eliminacion DESC LIMIT 1";
            Statement statement = conexion.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if(resultSet.next()){
                int id = resultSet.getInt("id_nombre");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String email = resultSet.getString("email");
                int telefono = resultSet.getInt("telefono");
                Contacto contacto = new Contacto(id, nombre, apellido, telefono, email);
                agregarContacto(contacto);
            } else {
                System.out.println("No hay contactos eliminados");
            }
        } catch (Exception e){
            System.out.println("Error al eliminar el contacto");
            e.printStackTrace();
        }
    }

    public void eliminarContacto(Contacto contacto) {
        Connection conexion = conectar();
        try{
            insertUltimoEliminado(contacto); // Inserto en la tabla de ultimos eliminados

            // Elimino de la tabla de contactos
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM contactos WHERE id = ?");
            ps.setString(1, Integer.toString(contacto.getId()));
            int row = ps.executeUpdate();
            if(row > 0){
                System.out.println("Contacto eliminado");
            } else {
                System.out.println("No se pudo eliminar el contacto");
            }
        } catch (Exception e){
            System.out.println("Error al eliminar el contacto");
            e.printStackTrace();
        }
        finally {
            desconectar(conexion);
        }
    }

    public void filtrarContactosLetraNombre(char letra) {
        Connection conexion = conectar();
        try{
            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM contactos WHERE nombre LIKE '" + letra + "%'");
            ResultSet resultSet = ps.executeQuery();

            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String email = resultSet.getString("email");
                int telefono = resultSet.getInt("telefono");
                System.out.println("ID: " + id + "\nNombre: " + nombre + "\nApellido: " + apellido + "\nEmail: " + email + "\nTeléfono: " + telefono);
                System.out.println("--------------------------------------------------");
            }
        } catch (Exception e ){
            System.out.println("Error al filtrar contactos");
            e.printStackTrace();
        }
        finally {
            desconectar(conexion);
        }
    }

    public void eliminarTodosLosDatos() {
        Connection conexion = conectar();
        try{
            // Elimino de la tabla de contactos
            PreparedStatement ps = conexion.prepareStatement("DELETE FROM contactos");
            int row = ps.executeUpdate();
            if(row > 0){
                System.out.println("Contactos eliminados");
            } else {
                System.out.println("No se pudieron eliminar los contactos");
            }

            // Elimino de la tabla de ultimos eliminados
            PreparedStatement ps2 = conexion.prepareStatement("DELETE FROM ultimo_registro_eliminado");
            int row2 = ps2.executeUpdate();
            if(row2 > 0){
                System.out.println("Ultimos eliminados eliminados");
            } else {
                System.out.println("No se pudieron eliminar los ultimos eliminados");
            }
        } catch (Exception e){
            System.out.println("Error al eliminar los contactos");
            e.printStackTrace();
        }
        finally {
            desconectar(conexion);
        }
    }
}
