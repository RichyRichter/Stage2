package stage2.practice.DataBase;

import java.security.PublicKey;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {
    private Statement stat;

    public Controller(Statement stat) throws SQLException {
        this.stat = stat;
    }

    public boolean addStreetToCenter(String centerName, int IdStreet) throws SQLException {
        String queryCenter = String.format("SELECT ID_CENTER from POPULATION_CENTER " +
                "where CENTER_NAME = '%s'", centerName);
        ResultSet set = stat.executeQuery(queryCenter);
        set.next();
        int idTCenter = set.getInt(1);
        return stat.execute(String.format("insert into CENTER_STREET\n" +
                "values (%d, %d);", idTCenter, IdStreet));
    }

    public boolean addCenter(String name, String type, int area, int population, int fondYear) throws SQLException {
        if (area <= 0 || population <= 0 || fondYear <= 0) {
            throw new IllegalArgumentException("Отрицательные значения");
        }
        String query = String.format("SELECT ID_CENTER_TYPE from CENTER_TYPE " +
                "where CENTER_TYPE_NAME = '%s'", type);
        ResultSet set = stat.executeQuery(query);
        set.next();
        int idType = set.getInt(1);
        return stat.execute(String.format("insert into population_center (center_name, id_type, CITY_AREA, POPULATION, " +
                "FOUNDATION_YEAR) values ('%s', %d, %d, %d, %d)", name, idType, area, population, fondYear));
    }

    public boolean deletePlace(String placeName, String placeType) throws SQLException {
        String colum;
        switch (placeType) {
            case "Пункт" -> {
                placeType = "POPULATION_CENTER";
                colum = "CENTER_NAME";
            }
            case "Улица" -> {
                placeType = "STREET";
                colum = "STREET_NAME";
            }
            default -> throw new IllegalStateException("Unexpected value: " + placeType);
        }
        String query = String.format("delete from %s " +
                "where %s = '%s'", placeType, colum, placeName);
        return stat.execute(query);
    }

    public String findAllCenters() throws SQLException {
        StringBuilder res = new StringBuilder("Населенные пункты:\n");
        String query = "select * from centerView";
        try (ResultSet rs = stat.executeQuery(query)) {
            while (rs.next()) {
                res.append(String.format("%s | %s | %s км2 | %s чел. | %s г.\n",
                        rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5)));
            }
        }
        return res.toString();
    }

    public String findAllStreets() throws SQLException {
        StringBuilder res = new StringBuilder("Улицы:\n");
        String query = "select ID_STREET, STREET_NAME, STREET_TYPE_NAME\n" +
                "from STREET\n" +
                "join STREET_TYPE ST on ST.ID_STREET_TYPE = STREET.ID_STREET_TYPE";
        try (ResultSet rs = stat.executeQuery(query)) {
            while (rs.next()) {
                res.append(String.format("%s | %s | %s \n",
                        rs.getString(1), rs.getString(2), rs.getString(3)));
            }
        }
        return res.toString();
    }

    public boolean addStreet(String name, String type) throws SQLException {
        String query = String.format("SELECT ID_STREET_TYPE from STREET_TYPE " +
                "where STREET_TYPE_NAME = '%s'", type);
        ResultSet set = stat.executeQuery(query);
        set.next();
        int idType = set.getInt(1);
        query = String.format("insert into STREET (street_name, id_street_type) " +
                "VALUES ('%s',%d)", name, idType);
        return stat.execute(query);
    }

    public String findStreetInfo(String streetName) throws SQLException {
        StringBuilder res = new StringBuilder();
        String query = "select STREET.id_street, STREET_TYPE_NAME, STREET.street_name\n" +
                "from STREET\n" +
                "         join STREET_TYPE ST on ST.ID_STREET_TYPE = STREET.ID_STREET_TYPE\n" +
                "where STREET_NAME = '" + streetName + "'";
        try (ResultSet rs = stat.executeQuery(query)) {
            while (rs.next()) {
                res.append(String.format("%s | %s | %s \n",
                        rs.getString(1), rs.getString(2), rs.getString(3)));
            }
        }
        return res.toString();
    }

    public String findCenterInfo(String centerName) throws SQLException {
        String query = "select CENTER_NAME, ct.CENTER_TYPE_NAME, CITY_AREA, POPULATION, FOUNDATION_YEAR\n" +
                "from POPULATION_CENTER\n" +
                "join CENTER_TYPE ct on ct.ID_CENTER_TYPE = ID_TYPE\n" +
                "where CENTER_NAME = '" + centerName + "'";
        StringBuilder res = new StringBuilder("По названию (" + centerName + ") найден населенный пункт:\n");
        try (ResultSet rs = stat.executeQuery(query)) {
            while (rs.next()) {
                res.append(String.format("%s | %s | %s км2 | %s чел. | %s г.\n",
                        rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5)));
            }
        }
        res.append("Улицы:\n");
        query = "select STREET_TYPE_NAME, STREET_NAME from centerStreets where CENTER_NAME = '" + centerName + "'";
        try (ResultSet rs = stat.executeQuery(query)) {
            while (rs.next()) {
                res.append(String.format("%s | %s \n",
                        rs.getString(1), rs.getString(2)));
            }
        }
        return res.toString();
    }

    public String findStreetInCenter(String streetName) throws SQLException {
        StringBuilder res = new StringBuilder("Улица '" + streetName + "' есть в следующих населенных пунктах:\n");
        String query = "select STREET_NAME, CENTER_NAME from CENTER_STREET\n" +
                "join STREET S on S.ID_STREET = CENTER_STREET.ID_STREET\n" +
                "join POPULATION_CENTER PC on PC.ID_CENTER = CENTER_STREET.ID_CENTER\n" +
                "where STREET_NAME like '%" + streetName + "%'";
        try (ResultSet rs = stat.executeQuery(query)) {
            while (rs.next()) {
                res.append(String.format("%s | %s \n", rs.getString(2), rs.getString(1)));
            }
        }
        return res.toString();
    }

    public String findCenterByCondition(String parameter, String condition, int num) throws SQLException {
        StringBuilder res = new StringBuilder("Населенные пункты подходящие под условие " +
                "(" + parameter + condition + num + "):\n");
        switch (parameter) {
            case "Население" -> parameter = "population";
            case "Год" -> parameter = "foundation_year";
            case "Площадь" -> parameter = "city_area";
            default -> throw new IllegalStateException("Unexpected value: " + parameter);
        }
        String query = "select CENTER_NAME\n" +
                "from population_center\n" +
                "where " + parameter + condition + num;
        try (ResultSet rs = stat.executeQuery(query)) {
            while (rs.next()) {
                res.append(String.format("%s \n", rs.getString(1)));
            }
        }
        return res.toString();
    }
}
