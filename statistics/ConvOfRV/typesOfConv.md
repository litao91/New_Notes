# Type of Convergence

## Converges in probability
A sequence of random variable converges to another random variable in probability. 

Intuitively it means that the probability that they take different value is very low when $$n$$ comes large => they have the same value concentration in terms of probability of taking the value.

Note that random variable is a mapping from the sample space to real number.

---
**Definition**:

We say $$X_n$$ converges to $$X$$ in probability, written $$X_n \to^P X$$ ,  if for every $$epsilon > 0$$:

$$
\mathbb{P} (|X_n - X|) > \epsilon) \to 0
$$

As $$n \to \infty$$

---

Note that when $$X$$ is a constant $$c$$, it means that the series of random varialbes (distributions) concentrate more and more to the value $$c$$, meaning that it's tails getting shorter and shorter.  

Note again that converges in probability doesn't tell you much about mean and SD. Although the probability of $$X_n$$ and $$X$$ far away is low, but the difference can be **hugh** when it comes. 

They concentrate at the same places, but we know nothing about other places. 

It implies that if $$X_n \to^P b$$ then $$\mathbb{E}(X_n)\to b$$ is **NOT** true. Counter example:

$$
\mathbb{P}(X_n = n^2) = 1/n \text{ and } \mathbb{P}(X_n = 0) = 1-(1/n)
$$

Converge to 0 but the expectation is $$n$$.



## Converges in Distribution

Intuitively, it means that when n comes large, the **CDF** of the series of r.v. and the given r.v. tend to be the same. 

---
**Definition**

Say $$X_n$$ converges to $$X$$ **in distribution**, Written $$X_n \rightsquigarrow X$$, if 

$$
\lim_{n\to\infty}F_n(t) = F(t)
$$

At all $$t$$ for which $$F$$ is continuous

## Converges in quadratic mean

$$
\mathbb{E}(X_n - X)^2 \to 0
$$

## Relationships betwen types of inequality

* quadratic mean => probability
* Probability => distribution
* Only when converges to a fixed real number (point mass), distribution => probability
