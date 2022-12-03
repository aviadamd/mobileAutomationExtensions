package base.excelReader;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static base.excelReader.ExcelDbReaderExtensions.getData;

public class ExcelDbReaderExtensionsTest {
    private final Logger logger = LoggerFactory.getLogger(ExcelDbReaderExtensionsTest.class);
    private ExcelDbReaderExtensions.QueryBuilder queryBuilder;
    private ExcelDbReaderExtensions.UpdateQueryBuilder updateQueryBuilder;

    @BeforeClass
    public void init() {
        String path = "C:\\Users\\Lenovo\\IdeaProjects\\mobile.automation.extensions\\src\\test\\resources\\Book1.xlsx";
        this.queryBuilder = new ExcelDbReaderExtensions.QueryBuilder().setConnection(path);
        this.updateQueryBuilder = new ExcelDbReaderExtensions.UpdateQueryBuilder().setConnection(path);
    }

    @AfterClass
    public void tearDown() {
        this.queryBuilder.build().close();
    }

    @Test
    public void readDb_printAllDbByGenericClassPojoObject() {
        List<String> values = getData(this.queryBuilder.query("select * from sheet1").build(), Arrays.asList("id","userName"));
        values.forEach(System.out::println);
    }

    @Test
    public void readDb_printAllDbByClassPojoObject() {
        Recordset getData = this.queryBuilder.query("select * from sheet1").build();
        for (ClassPogo classPogo: this.getClassPogo(getData)) {
            logger.info(classPogo.getId());
            logger.info(classPogo.getUserName());
            logger.info(classPogo.getUserPass());
        }
    }

    @Test
    public void a_readDb_queryTestFromDb() {
        Recordset recordset = this.queryBuilder
                .query("select * from sheet1 WHERE (\"id\"='1') AND (\"userName\"='aviad12345')")
                .where("(\"userPassword\"='ss123456')")
                .build();
        List<String> fields = ExcelDbReaderExtensions.getData(recordset, Arrays.asList("id","userName","userPassword"));
        fields.forEach(logger::info);
    }

    @Test
    public void b_readDb_queryTestFromDb_insert() {
        int recordset = this.updateQueryBuilder
                .update("INSERT INTO sheet1 (id,userName,userPassword) VALUES('22','eli','haviv')")
                .build();
        logger.info("data updated " + recordset);
    }

    private List<ClassPogo> getClassPogo(Recordset getData) {
        ClassPogo classPogo;
        List<ClassPogo> classPogoList = new ArrayList<>();

        try {

            while (getData.next()) {
                classPogo = new ClassPogo(
                        getData.getField("id"),
                        getData.getField("userName"),
                        getData.getField("userPassword"));
                classPogoList.add(classPogo);
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        return classPogoList;
    }
}
