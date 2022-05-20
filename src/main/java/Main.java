public class Main {
    public static void main(String[] args){

        Account account = new Account(2);
        System.out.println(account.getBalance());
        account.putMoney(200);
        System.out.println(account.getBalance());
        account.takeMoney(100);
        System.out.println(account.getBalance());
    }
}
