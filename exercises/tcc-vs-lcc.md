# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

---

## Answer

TTC and LCC are equals when all the methods of a given java class are directly related. This means that every pair of 
methods interacts directly by sharing one or more attributes.

Here is an example :

```java
class BankAccount {
    private double balance;

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }
}
```

---

**LCC cannot be less than TCC because:**
- **TCC** includes all the **directly related pairs** of methods in the class (methods that share at least one attribute directly).
- **LCC** includes all the **directly and indirectly related pairs** of methods in the class (methods linked through shared attributes or intermediary methods).

Thus, by definition:

$$\text{LCC} \geq \text{TCC}$$

and the opposite (LCC < TCC) can never occur.

---
