package Program;

import Login.Database;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

public class Program {
    //Fields
    private String email;
    ResultSet cards;
    private Database database = new Database();
    private String sqlquery;

    private String usingcard;

    //Methods
    public Program(String email, ResultSet cards, Database database){
        this.email = email;
        this.cards = cards;
        this.database = database;
    }

    protected void SetEmail(String email){ this.email = email; }
    protected void SetCards(ResultSet cards){ this.cards = cards; }
    protected void SetUsingCard(String usingcard){ this.usingcard = usingcard; }

    protected String GetEmail(){ return this.email; }
    protected ResultSet GetCards(){ return this.cards; }
    protected String GetUsingCard(){ return this.usingcard; }

    private void CheckCards() {
        sqlquery = "SELECT cardnum FROM cards WHERE email LIKE '" + GetEmail() + "';";
        SetCards(database.getInfo(sqlquery));
        String num;
        try {
            System.out.println("Your cards:");
            while (cards.next()) {
                num = cards.getString("cardnum");
                num = new StringBuilder(num).insert(num.length()-4, " ").insert(num.length()-8, " ").insert(num.length()-12, " ").toString();
                System.out.println(num);
            }
        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    private void CheckUserCard(){

    }

    private void BuyStudy(){

    }

    private void BuyDorm(){

    }

    private boolean AddCard(){
        Scanner scanner = new Scanner(System.in);
        String number;
        int key = 1;

        System.out.println("Enter your card number:");

        do {
            number = scanner.next();

            if (number.length() != 16){
                System.out.println("Invalid card number\nYou want to try again? (0/1)");
                key = scanner.nextInt();
            }
        } while (number.length() != 16 & key != 0);

        sqlquery = "INSERT INTO cards (email, cardnum) VALUES ('" + GetEmail() + "', '" + number + "');";
        database.execute(sqlquery);
        return false;
    }
    
    public void MainProg(){
        Scanner scanner = new Scanner(System.in);
        int key;
        do {
            System.out.println("Successful log in");

            System.out.println("0 - Quit");
            System.out.println("1 - Add new card");
            System.out.println("2 - Check your cards");
            System.out.println("3 - Buy for study");
            System.out.println("4 - Buy for dormitory");

            key = scanner.nextInt();
            switch (key) {
                case 1:
                    AddCard();
                    break;
                case 2:
                    CheckCards();
                    break;
                case 3:
                    BuyStudy();
                    break;
                case 4:
                    BuyDorm();
                    break;
            }
        } while (key != 0);
    }
}
