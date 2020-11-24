package connection;

import com.exactpro.connection.DBConnection;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionTest {

    @Test(expected=RuntimeException.class)
    public void setConnectionDataRuntimeCheck(){
        //All are null
        DBConnection.setConnectionData(null, null, null);
        //Url is null
        DBConnection.setConnectionData(null, "root", "expmysql");
        DBConnection.setConnectionData(null, "root", null);
        DBConnection.setConnectionData(null, null, "expmysql");
        //User is null
        DBConnection.setConnectionData(null, null, "expmysql");
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/HelloWorld", null, "expmysql");
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/HelloWorld", null, null);


        //All are null
        DBConnection.setConnectionData("", "", "");
        //Url is null
        DBConnection.setConnectionData("", "root", "expmysql");
        DBConnection.setConnectionData("", "root", "");
        DBConnection.setConnectionData("", "", "expmysql");
        //User is null
        DBConnection.setConnectionData("", "", "expmysql");
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/HelloWorld", null, "expmysql");
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/HelloWorld", null, null);
    }
    @Test
    public void setConnectionDataAllFine(){
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/HelloWorld", "root", "");
        DBConnection.setConnectionData("jdbc:mysql://localhost:3306/HelloWorld", "root", "password");
    }

    @Test
    public void executeWithResult() throws ClassNotFoundException, SQLException {
        ResultSet data = DBConnection.executeWithResult("SELECT 1 AS col");
        while (data.next()){
            Assert.assertEquals(data.getInt("col"), 1);
        }
    }

    @Test
    public void executeNonResult() throws ClassNotFoundException, SQLException {
        DBConnection.executeNonResult("SELECT 1");
    }
}