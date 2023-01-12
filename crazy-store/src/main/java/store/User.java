package store;

public class User {

    String emailAddress;
    String password;
    double balance;
    String userName;
    int userId;
    boolean adminMode;
    int[] cart;

    User(){}

    User(String emailAddress,String password,double balance,String userName,int userId,boolean adminMode, int[]cart){
        this.emailAddress = emailAddress;
        this.password = password;
        this.balance = balance;
        this.userName = userName;
        this.userId = userId;
        this.adminMode = adminMode;
        this.cart = cart;
    }

    User(String emailAddress,String password,String userName,int userId,boolean adminMode){
        this.emailAddress = emailAddress;
        this.password = password;
        this.balance = 0;
        this.userName = userName;
        this.userId = userId;
        this.adminMode = adminMode;
        this.cart = null;
    }
    User(String emailAddress,String password,String userName,int userId){
        this.emailAddress = emailAddress;
        this.password = password;
        this.balance = 0;
        this.userName = userName;
        this.userId = userId;
        this.adminMode = false;
        this.cart = null;
    }
}
