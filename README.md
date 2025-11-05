# Bank System - Controle de Concorrência
<p>Um sistema bancário desenvolvido em Java 17 para praticar lógica de programação, POO (Programação Orientada a Objetos) e Concorrência com ReentrantLock.</p>
<p></p>O projeto permite o <strong>cadastro de clientes</strong>, <strong>criação de contas</strong>, <strong>operações bancárias</strong>(depósito, saque, transferência).</p>

## 🚀 Funcionalidades

✅ Cadastro de clientes <br>
✅ Criação de contas bancárias vinculadas a clientes <br>
✅ Depósito e saque com validação de saldo <br>
✅ Transferência entre contas com controle de concorrência (ReentrantLock) <br>
✅ Salvamento e carregamento dos dados em arquivo .txt <br>
✅ Sequenciamento automático de IDs para clientes e contas <br>

## ⚙️ Controle de Concorrência com ReentrantLock
<p>Em um sistema bancário, onde duas operações podem tentar alterar o mesmo saldo ao mesmo tempo, é essencial garantir consistência dos dados.
Por exemplo: se duas threads tentarem sacar da mesma conta simultaneamente, sem controle, o saldo poderia ficar incorreto.</p>

<p>O ReentrantLock resolve isso ao permitir que apenas uma thread por vez execute um bloco de código protegido pelo lock.
Quando uma thread entra em uma operação crítica (como saque ou transferência), ela adquire o bloqueio, impedindo que outras threads alterem o mesmo recurso até que a operação termine.</p>

<img width="551" height="247" alt="image" src="https://github.com/user-attachments/assets/4ff3b8e2-03ea-4205-a2db-56f641f08645" />

## 🛠️ Tecnologias Utilizadas
- Java 17
- ReentrantLock (Concorrência)
- File I/O para persistência em arquivo txt
