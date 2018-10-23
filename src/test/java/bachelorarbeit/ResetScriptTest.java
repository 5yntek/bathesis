package bachelorarbeit;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResetScriptTest {

    List<String> lines = List.of("DROP DATABASE `airport`;",
            "CREATE DATABASE `airport`;",
            "use `airport`;",
            "CREATE TABLE `airport`.`country` (",
            "  `CountryISO3166_2LetterCode` char(2) NOT NULL,",
            "  `CountryName` varchar(100) NOT NULL,",
            "  PRIMARY KEY (`CountryISO3166_2LetterCode`)",
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;",
            "INSERT INTO `airport`.`country` VALUES ('CA','Canada')," +
                    "('DE','Germany')," +
                    "('FR','France')," +
                    "('GB','United Kingdom')," +
                    "('GR','Greece')," +
                    "('IT','Italy')," +
                    "('NO','Norway');");

    /**
     * original fromListWithoutStatic method. Does not work like intended.
     *
     * @param lines
     * @return
     */
    private static SQLScript fromListWithoutStatic(List<String> lines) {
        String script = String.join("\n", lines).trim();
        List<String> list = Arrays.stream(script.split("(?<=;)")).map(String::trim).collect(Collectors.toList());
        return new SQLScript(list);
    }

    /**
     * fixed version of fromListWithoutStatic
     *
     * @param lines
     * @return
     */
    private static SQLScript fromListWithoutStaticFixed(List<String> lines) {
        String script = String.join("\n", lines);
        script = script.substring(0, script.lastIndexOf(";") + 1);
        List<String> list = Arrays.stream(script.split("(?<=;)")).map(String::trim).collect(Collectors.toList());
        return new SQLScript(list);
    }

    private void runTest(SQLScript script) {
        for (String sql : script.sql) {
            assertTrue(sql.endsWith(";"), sql);
        }
    }

    @Test
    public void testFixed() {
        List<String> list = new ArrayList<>(lines);
        SQLScript script = fromListWithoutStaticFixed(list);
        runTest(script);
        list.add("-- Dump completed on 2017-05-02 16:56:42");
        script = fromListWithoutStaticFixed(list);
        runTest(script);

    }

    @Test
    public void test() {
        List<String> list = new ArrayList<>(lines);
        SQLScript script = fromListWithoutStatic(list);
        runTest(script);
        list.add("-- Dump completed on 2017-05-02 16:56:42");
        script = fromListWithoutStatic(list);
        runTest(script);
    }

    /**
     * SQLScript mock
     */
    public static class SQLScript {
        public final List<String> sql;

        SQLScript(List<String> sql) {
            this.sql = sql;
        }

        @Override
        public String toString() {
            return String.join("", sql);
        }
    }
}
