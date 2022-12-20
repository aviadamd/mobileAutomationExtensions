package base.pdfReader;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Slf4j
public class PdfReaderExtensionsTest {
    private PdfReaderExtensions pdfReaderExtensions;

    private final String path = "C:\\Users\\Lenovo\\IdeaProjects\\mobile.automation.extensions\\src\\test\\resources\\sample.pdf";

    @BeforeClass
    public void init() {
        this.pdfReaderExtensions = new PdfReaderExtensions(path);
    }

    @Test
    public void testPdfReader() {
        PdfReaderExtensions.Actions action = this.pdfReaderExtensions
                .step(actions -> {
                    log.info("pdf text " + actions.getText());
                    actions.writeText("shit is not good");
                    log.info("pdf text " + actions.getText());
                }).build();

        log.info(action.getText());
    }

    @AfterClass
    public void tearDown() {
        this.pdfReaderExtensions.build().close();
    }
}
