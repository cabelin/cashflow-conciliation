@startuml

user -> "bff-cashflow-admin": transactions day report request

"bff-cashflow-admin" -> "auth": validate auth request

"auth" -> "bff-cashflow-admin": validate auth response

alt on successful

    "bff-cashflow-admin" -> "cashflow-conciliation": transactions day report request

    "cashflow-conciliation" -> "bff-cashflow-admin": transactions day report response

end

"bff-cashflow-admin" -> user: transactions day report response

@enduml
