@startuml

user -> "bff-cashflow-admin": create transactions request

"bff-cashflow-admin" -> "auth": validate auth request

"auth" -> "bff-cashflow-admin": validate auth response

alt on successful

    "bff-cashflow-admin" -> "cashflow-conciliation": create transactions request

    "cashflow-conciliation" -> "bff-cashflow-admin": create transactions response

end

"bff-cashflow-admin" -> user: create transactions response

@enduml
