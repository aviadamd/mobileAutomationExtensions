package base.excelReader;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
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
        public void close() {
            if (this.recordset != null) this.recordset.close();
        }
    }

    public static <T> MultiValuedMap<Integer, List<String>> queryResultMap(Recordset recordset, Class<T> objectClass) {
        MultiValuedMap<Integer, List<String>> setObjectMap = new ArrayListValuedHashMap<>();
        try {
            int i = 1;
            Field[] fieldsList = objectClass.getDeclaredFields();
            while (recordset.next()) {
                List<String> getFields = Stream.of(fieldsList)
                        .map(field -> {
                            try {
                                return recordset.getField(field.getName());
                            } catch (FilloException e) {
                                return "";
                            }
                        })
                        .collect(Collectors.toList());
                setObjectMap.put(i, getFields);
                i++;
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
        return setObjectMap;
    }

    protected static void printError(Exception exception, String from) {
        logger.error("Error " + exception + ", from " + from);
    }

}
