# SF_BankAccount

Функции (Классы):

getBalance - получить текущий баланс аккаунта
takeMoney - вывести средства с текущего баланса если будет доступна сумма
putMoney - пополнить средства на текущий баланс

API:

{ID} - ИД банк счета
{AMOUNT} - сумма которую вы хотите внести либо вычесть

GetBalance [GET]
http://localhost:4567/getBalance/{ID}
Получаем баланс подставляя ID счета

PutMoney [POST]
http://localhost:4567/putMoney/{ID}/{AMOUNT}
Вносим сумму на счет

TakeMoney [POST]
http://localhost:4567/takeMoney/{ID}/{AMOUNT}
Снимаем деньги со счета если имеется доступная сумма


![Posgres](https://user-images.githubusercontent.com/78420669/169781013-19c1c2cc-35ce-49c1-9fa3-3fe62d08c123.jpg)
