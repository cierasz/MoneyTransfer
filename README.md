# MoneyTransfer


Using Maven perform clean install.

The standalone file shoulde appear in <i>target/<b>moneytransfer-1.0-SNAPSHOT-jar-with-dependencies.jar</b></i> path.

App start the server on localhost, port 8080.

### Services
| HTTP METHOD | PATH |
| -----------| ------ |
| GET | /account/{accountId} |
| GET | /account/all | 
| PUT | /account/add |
| DELETE | /account/{accountId} |
| POST | /transaction |

Example: <i>http://localhost:8080/account/add</i>

### Json format for
#### Transaction:
```sh
{
	"amount": 2,
	"sourceAccountId": 1,
	"destAccountId": 2
}
```

#### Account (add)
```sh
{
	"emailAddress": "D1",
	"accountBalance": 5
}
```

EmailAddress is unique, it is impossible to create accounts with the same email.
