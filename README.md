# Money transfers

To run application execute:

`./gradlew run`


To add account send http request:

```
POST /accounts HTTP/1.1
Host: localhost:5050
Content-Type: application/json
   
   {
   	"number" :"1239034932014910249430",
   	"balance": "10.0"
   }
```

To get account send http request:

```
GET /accounts/1239034932014910249430 HTTP/1.1
Host: localhost:5050
Content-Type: application/json
```

To add transfer send http request:

```
POST /transfers HTTP/1.1
Host: localhost:5050
Content-Type: application/json

{
	"debitedAccountNumber" :"1239034932014910249430",
	"creditedAccountNumber" :"1239034932014910249435",
	"amount": "3.0"
}
```

To get transfer send http request:
```
GET /transfers/04bd17df-2532-4392-8e4d-8ce9e3159752 HTTP/1.1
Host: localhost:5050
Content-Type: application/json
```
