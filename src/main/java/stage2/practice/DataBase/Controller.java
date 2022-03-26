package stage2.practice.DataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
    private Statement stat;

    public Controller(Statement stat) {
        this.stat = stat;
    }

    public void addCenter(int id, String name, int type, int area, int population, int fondYear) throws SQLException {
        try(ResultSet rs = stat.executeQuery("select * from POPULATION_CENTER")){
            rs.moveToInsertRow();
            rs.updateInt(1,id);
            rs.updateString(2,name);
            rs.updateInt(3,type);
            rs.updateInt(4,area);
            rs.updateInt(5,population);
            rs.updateInt(6,fondYear);
            rs.insertRow();
            rs.moveToCurrentRow();
        }
//        return stat.execute("insert into population_center (center_name, id_type, CITY_AREA, POPULATION, FOUNDATION_YEAR)\n" +
//                "values ('" + name + "', " + type + ", " + area + ", " + population + ", " + fondYear + ")");
    }
}
