package database;

import oauth.UserService;

public class DAOFactory {
    private static OauthDAO oauthDAO;
    private static CheckDAO checkDAO;

    private static DAOFactory instance;

    public static DAOFactory getInstance() {
        if (instance == null)
            instance = new DAOFactory();
        return instance;
    }
    
    public OauthDAO getOauthDAO() {
        if (oauthDAO == null)
            oauthDAO = new UserService();
        return oauthDAO;
    }

    public CheckDAO getResultDAO() {
        if (checkDAO == null)
            checkDAO = new CheckService();
        return checkDAO;
    }
}