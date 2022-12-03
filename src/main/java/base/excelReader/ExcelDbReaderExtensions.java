package base.excelReader;

import com.codoid.products.fillo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExcelDbReaderExtensions {
    private static final Logger logger = LoggerFactory.getLogger(ExcelDbReaderExtensions.class);
    public static class UpdateQueryBuilder {
        private Connection connection;
        private Fillo reader;
        private String fileLocation;
        private int result = 0;
        public Optional<Connection> getConnection() {
            try {
                if (this.connection == null)
                    this.connection = this.reader.getConnection(this.fileLocation);
                return Optional.ofNullable(this.connection);
            } catch (Exception exception) {
                printError(exception,"\"get connection error\"");
                return Optional.empty();
            }
        }
        public UpdateQueryBuilder setConnection(String fileLocation) {
            this.fileLocation = fileLocation;
            this.reader = new Fillo();
            return this;
        }
        public UpdateQueryBuilder update(String query) {
            if (this.getConnection().isPresent()) {
                try {
                    this.result = this.getConnection().get().executeUpdate(query);
                } catch (Exception exception) {
                    printError(exception,"update");
                }
            }
            return this;
        }

        public int build() { return this.result; }
    }

    public static class QueryBuilder {
        private Connection connection;
        private Fillo reader;
        private String fileLocation;
        private Recordset recordset;
        private Optional<Connection> getConnection() {
            try {
                if (this.connection == null)
                    this.connection = this.reader.getConnection(this.fileLocation);
                return Optional.ofNullable(this.connection);
            } catch (Exception exception) {
                printError(exception,"\"get connection error\"");
                return Optional.empty();
            }
        }
        public QueryBuilder setConnection(String fileLocation) {
            this.fileLocation = fileLocation;
            this.reader = new Fillo();
            return this;
        }
        public QueryBuilder query(String query) {
            try {
                if (this.getConnection().isPresent()) {
                    recordset = this.getConnection().get().executeQuery(query);
                }
            } catch (Exception exception) {
                printError(exception,"\"query error\"");
            }
            return this;
        }
        public QueryBuilder where(String condition) {
            try {
                if (this.getConnection().isPresent()) {
                    recordset.where(condition);
                }
            } catch (Exception exception) {
                printError(exception,"\"query where error\"");
            }
            return this;
        }

        public Recordset build() { return recordset; }
    }

    protected static void printError(Exception exception, String from) {
        logger.error("Error " + exception + ", from " + from);
    }
    public static List<String> getData(Recordset setQuery, List<String> getFields) {
        List<String> collect = new ArrayList<>();
        try {
            if (setQuery != null) {
                while (setQuery.next()) {
                    for (String field: getFields) {
                        if (setQuery.getField(field) != null) {
                            collect.add(setQuery.getField(field));
                        }
                    }
                }
            }
        } catch (Exception exception) {
            printError(exception,"getData");
        }
        return collect;
    }
}
