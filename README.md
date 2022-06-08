# SF_BankAccount

Функции (Классы):

checkAccount - проверить существует ли такой аккаунт

getStatus - проверить статус хватает ли баланса для списания

getBalance - получить текущий баланс аккаунта

takeMoney - вывести средства с текущего баланса если будет доступна сумма

putMoney - пополнить средства на текущий баланс

transferMoney - перевести деньги с одного аккаунта на другой

getOperationListAll - получить сведения о пополнениях за все время

getOperationListStartDate - получить сведения о пополнениях с входящей даты и все последующие

getOperationListEndDate - получить сведения о пополнениях с входящей даты и все предыдущие

getOperationList - получить сведения о пополнениях с первой входящей даты и по вторую входящую дату

API:

{ID} - ИД банк счета
{ID SENDER} - ИД отправителя
{ID RECEIVER} - ИД получателя
{AMOUNT} - сумма которую вы хотите внести либо вычесть
{DATE} - дата в формате "ГГГГ-ММ-ДД"

GetBalance [GET]
http://localhost:4567/getBalance/{ID}
Получаем баланс подставляя ID счета

PutMoney [POST]
http://localhost:4567/putMoney/{ID}/{AMOUNT}
Вносим сумму на счет

TakeMoney [POST]
http://localhost:4567/takeMoney/{ID}/{AMOUNT}
Снимаем деньги со счета, если имеется доступная сумма

transferMoney [POST]
http://localhost:4567/transferMoney/{ID SENDER}/{ID RECEIVER}/{AMOUNT}/
Отправляем деньги с одного счета на другой, если имеется доступная сумма

getOperationListAll [GET]
http://localhost:4567//getOperationList/{ID}/all
получить сведения о пополнениях за все время

getOperationListStartDate [GET]
http://localhost:4567/getOperationList/{ID}/start/{DATE}
получить сведения о пополнениях с входящей даты и все последующие

getOperationListEndDate [GET]
http://localhost:4567/getOperationList/{ID}/end/{DATE}
получить сведения о пополнениях с входящей даты и все предыдущие

getOperationList [GET]
http://localhost:4567/getOperationList/{ID}/{DATE}/{DATE}
получить сведения о пополнениях с первой входящей даты и по вторую входящую дату

Статусы операций:
1 - пополнение на баланс
2 - снятие с баланса
3 - получение на баланс путем перевода денег
4 - снятие с баланса путем перевода денег

![](../accounts.jpg)

![](../operations.jpg)

![](../transfers.jpg)