import com.google.gson.Gson;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/getBalance/:id", ((request, response) -> {
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            return new Gson().toJson(account.getBalance());
        }));

        post("/putMoney/:id/:amount", ((request, response) -> {
            response.type("application/json");

            Account account = new Account(Integer.parseInt(request.params("id")));
            account.putMoney(Integer.parseInt((request.params("amount"))));
            return ("Добавлено " + request.params("amount") + "$ теперь на счету " + account.getBalance() + "$");
        }));

        post("/takeMoney/:id/:amount", ((request, response) -> {
            Account account = new Account(Integer.parseInt(request.params("id")));
            account.takeMoney(Integer.parseInt((request.params("amount"))));
            int status = account.takeMoney(Integer.parseInt((request.params("amount"))));
            if (status == 1) {
                return ("Списано " + request.params("amount") + "$ теперь на счету " + account.getBalance() + "$");
            } else {
                return ("Не хватает баланса, на вашем счету " + account.getBalance() + "$");
            }

        }));

    }
}