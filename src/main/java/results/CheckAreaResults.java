package results;

import database.DAOFactory;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class CheckAreaResults implements Serializable {
    private model.XBean XBean;
    private model.YBean YBean;
    private model.RBean RBean;

    private LinkedList<CheckArea> results;

    public CheckAreaResults(String hash) {
        super();
        results = new LinkedList<>();
        try {
            results = new LinkedList<>(DAOFactory.getInstance().getResultDAO().getResults(hash));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public LinkedList<CheckArea> getResults() {
        return results;
    }

    public void setResults(LinkedList<CheckArea> results) {
        this.results = results;
    }

    public void newResult(final double x, final double y, final double r, String hash) {
        System.out.println("Send: " + x + " " + y + " " + r);
        final CheckArea currentResult = new CheckArea();

        final long startExec = System.nanoTime();
        final boolean result = AreaResultChecker.getResult(x, y, r);
        final long endExec = System.nanoTime();
        final long executionTime = endExec - startExec;

        currentResult.setX(x);
        currentResult.setY(y);
        currentResult.setR(r);
        currentResult.setResult(result);
        currentResult.setExecutedAt(LocalDateTime.now());
        currentResult.setExecTime(executionTime);
        currentResult.setUser_hash(hash);
        try {
            DAOFactory.getInstance().getResultDAO().addNewResult(currentResult);
        } catch (SQLException ignored) {}
        results.addFirst(currentResult);
    }

    public void addPoint(double x, double y, double r, boolean hit) {
        System.out.println("TEST: " + x + ", " + y + ", " + r + ", " + hit);
    }

    public void clearResults() throws SQLException {
        DAOFactory.getInstance().getResultDAO().clearResults();
    }

}