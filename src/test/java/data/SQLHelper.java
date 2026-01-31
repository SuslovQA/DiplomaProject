package data;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;


import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLHelper {
    private static String url = System.getProperty("db.url");
    private static String user = System.getProperty("db.user");
    private static String pass = System.getProperty("db.password");


    /**
     * Метод для очистки таблиц ('payment_entity', 'credit_request_entity', 'order_entity')
     * из БД после прогонов тестов
     */
    public static void clearTables() {
        val deletePaymentEntity = "DELETE FROM payment_entity";
        val deleteCreditEntity = "DELETE FROM credit_request_entity";
        val deleteOrderEntity = "DELETE FROM order_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(
                url, user, pass)
        ) {
            runner.update(conn, deletePaymentEntity);
            runner.update(conn, deleteCreditEntity);
            runner.update(conn, deleteOrderEntity);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * Публичный метод для получения статуса кредита из БД.
     * Метод для вызова в тестах
     *
     * @return Возвращает строку со статусом кредита (например, 'APPROVED')
     */
    @SneakyThrows
    public static String getCreditStatus() {
        String status = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        return getCreditStatus(status);
    }

    /**
     * Приватный метод для получения статуса кредита из БД.
     *
     * @return Возвразает строку со статусом кредита (например, 'APPROVED')
     */
    @SneakyThrows
    private static String getCreditStatus(String query) {
        var codeSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        String result = "";
        val runner = new QueryRunner();
        try
                (val conn = DriverManager.getConnection(
                        url, user, pass)
                ) {

            result = runner.query(conn, codeSQL, new ScalarHandler<String>());
            System.out.println(result);
            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * Публичный метод для получения статуса платежа из БД.
     * Метод для вызова в тестах
     *
     * @return Возвращает строку со статусом платежа (например, 'APPROVED')
     */
    @SneakyThrows
    public static String getPaymentStatus() {
        String status = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        return getPayStatus(status);
    }

    /**
     * Приватный метод для получения статуса платежа из БД.
     *
     * @return Возвразает строку со статусом платежа (например, 'APPROVED')
     */
    @SneakyThrows
    private static String getPayStatus(String query) {
        var codeSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        String result = "";
        val runner = new QueryRunner();
        try
                (val conn = DriverManager.getConnection(
                        url, user, pass)
                ) {

            result = runner.query(conn, codeSQL, new ScalarHandler<String>());
            System.out.println(result);
            return result;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }


    /**
     * Публичный метод для получения из БД списанного количества средств при покупке.
     *
     * @return Возвращает строку - количество списанных средств.
     */
    @SneakyThrows
    public static String getAmountFromDb() {
        String amount = "SELECT amount FROM payment_entity ORDER BY created desc LIMIT 1;";
        return getAmount(amount);
    }

    /**
     * Приватный метод для получения из БД списанного количества средств при покупке.
     *
     * @return Возвращает Integer деленный на 100 преобразованный в String.
     * Делениее на 100 необходимо для сравнения в тестах, т.к в БД поле amount хранится в копейках.
     * Преобразование Integer в String необходимо для сравнения в тестах,
     * т.к метод получения цены со страницы возвращает String.
     */
    @SneakyThrows
    private static String getAmount(String query) {
        var codeSQL = "SELECT amount FROM payment_entity ORDER BY created desc LIMIT 1;";
        val runner = new QueryRunner();
        try
                (val conn = DriverManager.getConnection(
                        url, user, pass)
                ) {

            int result = runner.query(conn, codeSQL, new ScalarHandler<Integer>());
            System.out.println(String.valueOf(result / 100));
            return String.valueOf(result / 100);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}