import com.google.gson.Gson;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        get("/getBalance/:id", ((request, response) -> {
            response.type("application/json");
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            return new Gson().toJson(account.getBalance());
        }));

        get("/getOperationList/:id/:date", ((request, response) -> {
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            List<String> test = account.getOperationList(new java.sql.Date(System.currentTimeMillis()));
            //Date startDate = (Date) new SimpleDateFormat("dd-MM-yyyy").parse(request.params("date"));
            response.type("application/json");
            return new Gson().toJson(test);
        }));

        post("/putMoney/:id/:amount", ((request, response) -> {
            Account account = new Account(Integer.parseInt(request.params("id")));
            account.putMoney(Integer.parseInt((request.params("amount"))));
            return ("Добавлено " + request.params("amount") + "$ теперь на счету " + account.getBalance() + "$");
        }));

        post("/takeMoney/:id/:amount", ((request, response) -> {
            Account account = new Account(Integer.parseInt(request.params("id")));
            account.takeMoney(Integer.parseInt((request.params("amount"))));
            int status = account.getStatus();
            if (status == 1) {
                return ("Списано " + request.params("amount") + "$ теперь на счету " + account.getBalance() + "$");
            } else {
                return ("Не хватает баланса, на вашем счету " + account.getBalance() + "$");
            }
        }));

    }
}