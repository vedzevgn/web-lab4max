package database;

import oauth.UserService;

public class DAOFactory {
    private static OauthDAO resultDAO;

    private static DAOFactory instance;

    public static DAOFactory getInstance() {
        if (instance == null)
            instance = new DAOFactory();
        return instance;
    }
    
    public OauthDAO getResultDAO() {
        if (resultDAO == null)
            resultDAO = new UserService();
        return resultDAO;
    }
}