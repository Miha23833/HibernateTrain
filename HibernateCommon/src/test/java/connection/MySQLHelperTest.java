package connection;

import com.exactpro.connection.DBConnection;
import com.exactpro.connection.MySQLHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.exactpro.connection.DBHelper.*;

public class MySQLHelperTest {

    MySQLHelper helper = new MySQLHelper();

    String[] columns = new String[]{"TestColumn", "INT"};

    @Before
    public void dbInit() throws ClassNotFoundException, SQLException {
        DBConnection.executeNonResult("CREATE DATABASE `UnitTests`");
        DBConnection.setConnectionData(
                "jdbc:mysql://localhost:3306/UnitTests",
                "root",
                "password"
        );
    }

    @After
    public void dropDb() throws ClassNotFoundException, SQLException {
        DBConnection.executeNonResult("DROP DATABASE `UnitTests`");
        DBConnection.setConnectionData(
                "jdbc:mysql://localhost:3306/HelloWorld",
                "root",
                "password"
        );
    }


    @Test
    public void testTable() throws SQLException, ClassNotFoundException {
        helper.createTable("TestTable", columns);
        helper.dropTable("TestTable");
    }

    @Test
    public void testColumn() throws SQLException, ClassNotFoundException {
        helper.createTable("TestTable", columns);
        helper.addColumn("TestTable", "testAddColumn", "INT");

        helper.dropColumn("testColumn", "TestTable");
        helper.dropTable("TestTable");
    }

    @Test
    public void testTrigger() throws SQLException, ClassNotFoundException {
        helper.createTable("TestTable", columns);
        helper.addTrigger("testTrigger",
                "TestTable",
                ExecTime.BEFORE,
                Operation.INSERT,
                "DO NOTHING");

        helper.dropTrigger("testTrigger");
        helper.dropTable("TestTable");
    }

    @Test
    public void testDataMach() throws SQLException, ClassNotFoundException {
        String[] tableData = new String[]{
                "'Name1', 1",
                "'Name2', 2",
                "'Name3', 3"
        };

        helper.createTable("TestTable", new String[]{"Name", "TEXT", "ID", "INT"});

        // Insert check
        helper.insertData("TestTable", "Name, ID", tableData);

        // Select check
        ResultSet selectResult = helper.selectData(new String[]{"*"}, "TestTable", "");
        selectResult.last();
        Assert.assertEquals(selectResult.getRow(), 3);
        selectResult.close();

        // Update check
        helper.updateData("TestTable", "ID = ID + 10", "");
        ResultSet updateResult = helper.selectData(new String[]{"*"}, "TestTable", "");

        updateResult.last();
        Assert.assertEquals(updateResult.getRow(), 3);
        updateResult.first();

        Assert.assertEquals("Name1", updateResult.getString("Name"));
        Assert.assertEquals(11, updateResult.getInt("ID"));
        updateResult.next();

        Assert.assertEquals("Name2", updateResult.getString("Name"));
        Assert.assertEquals(12, updateResult.getInt("ID"));
        updateResult.next();

        Assert.assertEquals("Name3", updateResult.getString("Name"));
        Assert.assertEquals(13, updateResult.getInt("ID"));
        updateResult.next();

        // Delete check
        helper.deleteData("TestTable", "");
        ResultSet noData = helper.selectData(new String[]{"*"}, "TestTable", "");

        // No data check
        Assert.assertFalse(noData.next());

        helper.dropTable("TestTable");

    }
}