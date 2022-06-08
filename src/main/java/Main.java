import com.google.gson.Gson;

import java.sql.Date;
import java.util.List;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        get("/checkAccount/:id", ((request, response) -> {
            response.type("application/json");
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            return new Gson().toJson(account.checkAccount());
        }));


        get("/getBalance/:id", ((request, response) -> {
            response.type("application/json");
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            if (account.checkAccount() > 0) {
                return new Gson().toJson(account.getBalance());
            } else {
                return ("Проверьте входящие данные!");
            }

        }));


        post("/putMoney/:id/:amount", ((request, response) -> {
            Account account = new Account(Integer.parseInt(request.params("id")));
            if (account.checkAccount() > 0) {
                account.putMoney(Integer.parseInt((request.params("amount"))));
                return ("Добавлено " + request.params("amount") + "$ теперь на счету " + account.getBalance() + "$");
            } else {
                return ("Проверьте входящие данные!");
            }
        }));


        post("/takeMoney/:id/:amount", ((request, response) -> {
            Account account = new Account(Integer.parseInt(request.params("id")));
            if (account.checkAccount() > 0) {
                account.takeMoney(Integer.parseInt((request.params("amount"))));
                int status = account.getStatus();
                if (status == 1) {
                    return ("Списано " + request.params("amount") + "$ теперь на счету " + account.getBalance() + "$");
                } else {
                    return ("Не хватает баланса, на вашем счету " + account.getBalance() + "$");
                }
            } else {
                return ("Проверьте входящие данные!");
            }


        }));

        post("/transferMoney/:sendId/:receiveId/:amount", ((request, response) -> {
            Account account1 = new Account(Integer.parseInt(request.params("sendId")));
            Account account2 = new Account(Integer.parseInt(request.params("receiveId")));
            if (account1.checkAccount() > 0 && account2.checkAccount() > 0) {
                account1.transferMoney(Integer.parseInt((request.params("receiveId"))), account2.getBalance(), Integer.parseInt((request.params("amount"))));
                int status = account1.getStatus();
                if (status == 1) {
                    return ("Успешно переведено " + request.params("amount") + "$. Теперь на счету " + request.params("sendId") + " " + account1.getBalance() + "$. Теперь на счету " + request.params("receiveId") + " " + account2.getBalance() + "$");
                } else {
                    return ("Не хватает баланса, на вашем счету " + account1.getBalance() + "$");
                }
            } else {
                return ("Проверьте входящие данные!");
            }
        }));


        get("/getOperationList/:id/all", ((request, response) -> {
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            if (account.checkAccount() > 0) {
                List<String> operationsList = account.getOperationListAll();
                response.type("application/json");
                return new Gson().toJson(operationsList);
            } else {
                return ("Проверьте входящие данные!");
            }
        }));


        get("/getOperationList/:id/start/:date", ((request, response) -> {
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            if (account.checkAccount() > 0) {
                Date startDate = Date.valueOf(request.params("date"));
                List<String> operationsList = account.getOperationListStartDate(startDate);
                response.type("application/json");
                return new Gson().toJson(operationsList);
            } else {
                return ("Проверьте входящие данные!");
            }
        }));


        get("/getOperationList/:id/end/:date", ((request, response) -> {
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            if (account.checkAccount() > 0) {
                Date endDate = Date.valueOf(request.params("date"));
                List<String> operationsList = account.getOperationListEndDate(endDate);
                response.type("application/json");
                return new Gson().toJson(operationsList);
            } else {
                return ("Проверьте входящие данные!");
            }
        }));


        get("/getOperationList/:id/:startDate/:endDate", ((request, response) -> {
            int index = Integer.parseInt(request.params("id"));
            Account account = new Account(index);
            if (account.checkAccount() > 0) {
                Date startDate = Date.valueOf(request.params("startDate"));
                Date endDate = Date.valueOf(request.params("endDate"));
                List<String> operationsList = account.getOperationList(startDate, endDate);
                response.type("application/json");
                return new Gson().toJson(operationsList);
            } else {
                return ("Проверьте входящие данные!");
            }
        }));

    }
}