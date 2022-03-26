package stage2.demo;

import stage2.practice.DataBase.ConnectionManager;
import stage2.practice.DataBase.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Statement stat = ConnectionManager.getStatement();
        Controller controller = new Controller(stat);
        controller.addCenter(5,"Оренбург", 1, 250, 850000, 1805);

        try (ResultSet rs = stat.executeQuery("select * from POPULATION_CENTER order by ID_CENTER ")) {
            while (rs.next()) {
                System.out.print(rs.getString(1) + "|");
                System.out.print(rs.getString(2) + "\n");
                System.out.print(rs.getString(3) + "\n");
                System.out.print(rs.getString(4) + "\n");
                System.out.print(rs.getString(5) + "\n");
                System.out.print(rs.getString(6) + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ConnectionManager.closeConnection();
    }
}
