package base.excelReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.*;

public class ExcelDbReaderExtensionsTest {
    private final Logger logger = LoggerFactory.getLogger(ExcelDbReaderExtensionsTest.class);
    private ExcelDbRepository excelDbRepository;

    @BeforeClass
    public void init() {
        String path = "C:\\Users\\Lenovo\\IdeaProjects\\mobile.automation.extensions\\src\\test\\resources\\Book1.xlsx";
        this.excelDbRepository = new ExcelDbRepository(path);
    }
    @AfterClass
    public void tearDown() {
        this.excelDbRepository.close();
    }
    @Test
    public void readDb_printAllDbByClassPojoObject() {
        this.excelDbRepository.setQuery("select * from sheet1");

        List<ClassPogo> pogoObject = this.excelDbRepository.findByAll();
        pogoObject.forEach(obj -> logger.info(obj.toString()));

        Optional<ClassPogo> classPogo = this.excelDbRepository.findBy(predicate -> predicate.getId().equals("6"));
        classPogo.ifPresent(pogo -> logger.info(pogo.toString()));

        this.excelDbRepository.clear();
    }

}
