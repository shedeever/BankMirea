package Login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Program.Program;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;

import javax.xml.crypto.Data;

public class Admin {
    //Fields
    private String email;
    private String code;
    private final Properties props = new Properties();
    private Session session;
    private Database database = new Database();
    private String sqlquery;

    //Methods
    protected String GetEmail(){
        return this.email;
    }

    protected void SetEmail(String email){
        this.email = email;
    }

    protected String GetCode(){
        return this.code;
    }

    protected void SetCode(String code){
        this.code = code;
    }

        //Main method
    public void ActivateAdmin() {
        Scanner scanner = new Scanner(System.in);
        int key;

        database.connect();

        SessionSettings();
        do {
            System.out.println("0 - Quit");
            System.out.println("1 - Sign in");
            System.out.println("2 - Sign up");
            System.out.println("3 - Forgot password");

            key = scanner.nextInt();

            switch (key) {
                case 1:
                    if (Login()) {
                        sqlquery = "SELECT cardnum FROM cards WHERE email LIKE '" + GetEmail() + "';";
                        Program main_program = new Program(GetEmail(), database.getInfo(sqlquery), this.database);
                        main_program.MainProg();
                    }
                    break;
                case 2:
                    Registration();
                    break;
                case 3:
                    ForgetPass();
                    break;
                default:
                    break;
            }
        } while (key != 0);
    }

        //Usable methods
    private void SessionSettings(){
        props.put("mail.smtp.host", "smtp.yandex.ru");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.user", "mirea.bank");
        props.put("mail.smtp.password", "osmjwxekyhynirgo");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "false");
        props.put("mail.debug", "false");

        session = Session.getInstance(props, null);
    }

    private boolean SendEmail(String email){
        GenerateCode();

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom("mirea.bank@yandex.ru");
            msg.setRecipients(Message.RecipientType.TO,
                    email);
            msg.setSubject("Mirea Bank Code");
            msg.setSentDate(new Date());
            msg.setText("Your permanent code to enter in bank: " + GetCode() + ".\nDon't forget it.");
            Transport.send(msg, "mirea.bank@yandex.ru", "osmjwxekyhynirgo");
            return true;
        } catch (MessagingException mex) {
            System.out.println("Send failed, exception: " + mex);
            return false;
        }
    }

    private boolean checkCode(String input_code){
        if (Objects.equals(input_code, GetCode()))
            return true;
        else
            return false;
    }

    private boolean checkEmail(String input_email){
        sqlquery = "SELECT email FROM users WHERE email LIKE '" + input_email + "';";

        ResultSet resultSet = database.getInfo(sqlquery);

        if (resultSet == null)
            return false;
        else
            return true;
    }

    private boolean checkLogin(String input_email, String input_code){
        sqlquery = "SELECT email, code FROM users WHERE email LIKE '" + input_email + "' AND code LIKE '"+ input_code + "';";

        ResultSet resultSet = database.getInfo(sqlquery);

        if (resultSet == null)
            return false;
        else
            return true;
    }

    private void GenerateCode(){
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        Matcher matcher;
        Random random = new Random();
        String s, buf_code = null;

        for (int i = 0; i < 5; i++){
            boolean flag = random.nextBoolean();
            if (!flag)
                s = String.valueOf(random.nextInt(9));
            else {
                do {
                    s = String.valueOf((char) ('A' + random.nextInt(57)));
                    matcher = pattern.matcher(s);
                } while (!matcher.find());
            }

            if (buf_code == null)
                buf_code = s;
            else
                buf_code = buf_code + s;
        }

        SetCode(buf_code);
    }

    private boolean Login(){
        Scanner scanner = new Scanner(System.in);
        String buf_code, email;
        boolean check;

        System.out.print("Enter your email:");
        email = scanner.nextLine();
        System.out.print("Enter your code:");
        buf_code = scanner.nextLine();


        if (checkLogin(email, buf_code)) {
            SetEmail(email);
            return true;
        }
        else{
            System.out.println("Invalid email or code. Try again");
            return false;
        }
    }

    private void Registration(){
        Scanner scanner = new Scanner(System.in);
        String buf_code, email;
        int i = 0, key;
        boolean check;

        do {
            System.out.print("Enter your email:");
            email = scanner.nextLine();
            check = SendEmail(email);
            if (!check)
                System.out.println("Try again");
        } while(!check);

        do {
            System.out.print("Enter your code:");
            buf_code = scanner.nextLine();
            check = checkCode(buf_code);
            i++;
            if (i == 3){
                System.out.println("Send a new message? (0/1)");
                key = scanner.nextInt();
                if (key == 1){
                    SendEmail(GetEmail());
                }
                i = 0;
            }
            if (!check)
                System.out.println("Try again");
        } while (!check);

        sqlquery = "INSERT INTO users(email, code, role) VALUES ('" + email + "', '" + code + "', " + "'0');";
        database.execute(sqlquery);
    }

    private void ForgetPass(){
        Scanner scanner = new Scanner(System.in);
        String email, buf_code;
        boolean check;

        do {
            System.out.print("Enter your email:");
            email = scanner.nextLine();
            check = checkEmail(email);
            if (check) {
                check = SendEmail(email);
                if (!check)
                    System.out.println("Try again");
            }
            else
                System.out.println("Invalid email");
        } while(!check);

        do {
            System.out.print("Enter your code:");
            buf_code = scanner.nextLine();
            check = checkCode(buf_code);
            if (!check)
                System.out.println("Try again");
        } while (!check);

        sqlquery = "UPDATE users SET code = '"+ GetCode() + "' WHERE email = '" + email + "';";

        database.execute(sqlquery);
    }
}