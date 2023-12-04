package database;

import results.CheckArea;

import java.sql.SQLException;
import java.util.Collection;

public interface CheckDAO {
    void addNewResult(CheckArea result) throws SQLException;
    void updateResult(Long bus_id, CheckArea result) throws SQLException;
    CheckArea getResultById(Long result_id) throws SQLException;
    Collection<CheckArea> getResults(String userHash) throws SQLException;
    void deleteResult(CheckArea result) throws SQLException;
    void clearResults() throws SQLException;
}