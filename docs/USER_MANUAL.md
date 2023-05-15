# User Manual API

This manual is to facilitate use of this apis

## Create transactions

Samples with some create transactions variations

### Create PAID inbound in one installment

```
Request:

POST /transactions

{
  "description": "Air conditioning",
  "installmentsCount": 1,
  "installmentsType": "MONTH",
  "amount": 2300.29,
  "type": "IN",
  "category": "SALES",
  "expectedPaymentDate": "2023-05-15",
  "paymentDate": "2023-05-15"
}

Response:

[
  {
    "id": 71,
    "relatedTransactionId": "d49712c4-063a-41b9-8ceb-2ba8c3291749",
    "description": "Air conditioning",
    "installment": 1,
    "amount": 2300.29,
    "currency": "BRL",
    "type": "IN",
    "category": "SALES",
    "expectedPaymentDate": "2023-05-15",
    "paymentDate": "2023-05-15",
    "status": "PAID"
  }
]

```


#### Create PAID outbound in one installment

```
Request:

POST /transactions

{
  "description": "Stock replenishment",
  "installmentsCount": 1,
  "installmentsType": "MONTH",
  "amount": 2300.29,
  "type": "OUT",
  "category": "INVENTORY_COSTS",
  "expectedPaymentDate": "2023-05-15",
  "paymentDate": "2023-05-15"
}

Response:

[
  {
    "id": 72,
    "relatedTransactionId": "cba36722-2aa9-435a-a3ce-c8bc88d7180b",
    "description": "Stock replenishment",
    "installment": 1,
    "amount": -2300.29,
    "currency": "BRL",
    "type": "OUT",
    "category": "INVENTORY_COSTS",
    "expectedPaymentDate": "2023-05-15",
    "paymentDate": "2023-05-15",
    "status": "PAID"
  }
]
```

#### Create PAID inbound in three installments
notes: only the first installment is created as paid

```
Request:

POST /transactions

{
  "description": "Air conditioning",
  "installmentsCount": 3,
  "installmentsType": "MONTH",
  "amount": 2300.29,
  "type": "IN",
  "category": "SALES",
  "expectedPaymentDate": "2023-05-15",
  "paymentDate": "2023-05-15"
}

Response:

[
  {
    "id": 73,
    "relatedTransactionId": "e5ffbf3c-6ab1-4425-a9b6-c48dab8c8de7",
    "description": "Air conditioning",
    "installment": 1,
    "amount": 766.77,
    "currency": "BRL",
    "type": "IN",
    "category": "SALES",
    "expectedPaymentDate": "2023-05-15",
    "paymentDate": "2023-05-15",
    "status": "PAID"
  },
  {
    "id": 74,
    "relatedTransactionId": "e5ffbf3c-6ab1-4425-a9b6-c48dab8c8de7",
    "description": "Air conditioning",
    "installment": 2,
    "amount": 766.76,
    "currency": "BRL",
    "type": "IN",
    "category": "SALES",
    "expectedPaymentDate": "2023-06-15",
    "paymentDate": null,
    "status": "PENDING"
  },
  {
    "id": 75,
    "relatedTransactionId": "e5ffbf3c-6ab1-4425-a9b6-c48dab8c8de7",
    "description": "Air conditioning",
    "installment": 3,
    "amount": 766.76,
    "currency": "BRL",
    "type": "IN",
    "category": "SALES",
    "expectedPaymentDate": "2023-07-15",
    "paymentDate": null,
    "status": "PENDING"
  }
]
```

#### Create PAID outbound in two installments
notes: only the first installment is created as paid

```
Request:

POST /transactions

{
  "description": "Stock replenishment",
  "installmentsCount": 2,
  "installmentsType": "MONTH",
  "amount": 2300.29,
  "type": "OUT",
  "category": "INVENTORY_COSTS",
  "expectedPaymentDate": "2023-05-15",
  "paymentDate": "2023-05-15"
}

Response:


[
  {
    "id": 76,
    "relatedTransactionId": "806c585b-2f90-47bd-ba53-95e731e7b20c",
    "description": "Stock replenishment",
    "installment": 1,
    "amount": -1150.15,
    "currency": "BRL",
    "type": "OUT",
    "category": "INVENTORY_COSTS",
    "expectedPaymentDate": "2023-05-15",
    "paymentDate": "2023-05-15",
    "status": "PAID"
  },
  {
    "id": 77,
    "relatedTransactionId": "806c585b-2f90-47bd-ba53-95e731e7b20c",
    "description": "Stock replenishment",
    "installment": 2,
    "amount": -1150.14,
    "currency": "BRL",
    "type": "OUT",
    "category": "INVENTORY_COSTS",
    "expectedPaymentDate": "2023-06-15",
    "paymentDate": null,
    "status": "PENDING"
  }
]
```

#### Create PENDING outbound in two installments
notes: only the first installment is created as paid

```
Request:

POST /transactions

{
  "description": "Stock replenishment",
  "installmentsCount": 2,
  "installmentsType": "MONTH",
  "amount": 2300.29,
  "type": "OUT",
  "category": "INVENTORY_COSTS",
  "expectedPaymentDate": "2023-05-15"
}

Response:


[
  {
    "id": 78,
    "relatedTransactionId": "de45cb6b-2bc7-478b-a056-cf61de89219a",
    "description": "Stock replenishment",
    "installment": 1,
    "amount": -1150.15,
    "currency": "BRL",
    "type": "OUT",
    "category": "INVENTORY_COSTS",
    "expectedPaymentDate": "2023-05-15",
    "paymentDate": null,
    "status": "PENDING"
  },
  {
    "id": 79,
    "relatedTransactionId": "de45cb6b-2bc7-478b-a056-cf61de89219a",
    "description": "Stock replenishment",
    "installment": 2,
    "amount": -1150.14,
    "currency": "BRL",
    "type": "OUT",
    "category": "INVENTORY_COSTS",
    "expectedPaymentDate": "2023-06-15",
    "paymentDate": null,
    "status": "PENDING"
  }
]
```

### Transactions reports

#### Get transaction reports for day 2023-05-15

```
Request:

GET /transactions/reports?type=DAY&date=2023-05-15

Result:

{
  "date": "2023-05-15",
  "amount": -1533.53,
  "paidAmount": -383.38,
  "pendingAmount": -1150.15,
  "transactions": [
    {
      "id": 71,
      "relatedTransactionId": "d49712c4-063a-41b9-8ceb-2ba8c3291749",
      "description": "Air conditioning",
      "installment": 1,
      "amount": 2300.29,
      "currency": "BRL",
      "type": "IN",
      "category": "SALES",
      "expectedPaymentDate": "2023-05-15",
      "paymentDate": "2023-05-15",
      "status": "PAID"
    },
    {
      "id": 72,
      "relatedTransactionId": "cba36722-2aa9-435a-a3ce-c8bc88d7180b",
      "description": "Stock replenishment",
      "installment": 1,
      "amount": -2300.29,
      "currency": "BRL",
      "type": "OUT",
      "category": "INVENTORY_COSTS",
      "expectedPaymentDate": "2023-05-15",
      "paymentDate": "2023-05-15",
      "status": "PAID"
    },
    {
      "id": 73,
      "relatedTransactionId": "e5ffbf3c-6ab1-4425-a9b6-c48dab8c8de7",
      "description": "Air conditioning",
      "installment": 1,
      "amount": 766.77,
      "currency": "BRL",
      "type": "IN",
      "category": "SALES",
      "expectedPaymentDate": "2023-05-15",
      "paymentDate": "2023-05-15",
      "status": "PAID"
    },
    {
      "id": 76,
      "relatedTransactionId": "806c585b-2f90-47bd-ba53-95e731e7b20c",
      "description": "Stock replenishment",
      "installment": 1,
      "amount": -1150.15,
      "currency": "BRL",
      "type": "OUT",
      "category": "INVENTORY_COSTS",
      "expectedPaymentDate": "2023-05-15",
      "paymentDate": "2023-05-15",
      "status": "PAID"
    },
    {
      "id": 78,
      "relatedTransactionId": "de45cb6b-2bc7-478b-a056-cf61de89219a",
      "description": "Stock replenishment",
      "installment": 1,
      "amount": -1150.15,
      "currency": "BRL",
      "type": "OUT",
      "category": "INVENTORY_COSTS",
      "expectedPaymentDate": "2023-05-15",
      "paymentDate": null,
      "status": "PENDING"
    }
  ]
}
```

### Update transactions

#### Update transaction to PAID

```
Request:

PUT /transactions/78/paymentDate

{
    "paymentDate": "2023-05-15"
}

Response:
- (204)

```
