
# Questões/Análises

1. Lançamentos serão realizados manualmente? Ou podemos ter algo automatizado no sentido de vendas online por exemplo? Segue um desenho de uma possível solução de uma automação com microserviços, mas vai depender muito da estrutura de vendas [automated solution design](./automated%20solution%20design.png)

2. O fluxo de caixa é independente e separada da venda? Pois podemos levantar automatizações do fluxo de caixa nesse sentido.

3. Para pagamento em crédito podemos ter lançamentos parcelados? Assumi a criação de lançamentos em que já são geradas as transações futuras.

4. Como fica o status desses lançamentos? Eles serão atualizados automáticamente? Ou será necessário atualizar manualmente? Parti do princípio em que temos uma data esperada e a data de pagamento efetiva, portanto ter a data de pagamento efetiva define o status da transação e foi criado uma api para registrar essa data de pagamento da transação

5. O negócio precisa de categorizar essas entradas e saídas? Entendo que sim para ajudar nas tomadas de decisão, portanto entrei com algumas categorias iniciais, mas que podemos incrementar a medida que necessário.

6. Podemos pensar em uma estrutura para lançamentos em moedas diferentes? Melhoria.
