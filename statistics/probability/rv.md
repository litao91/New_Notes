# Random Variables
## Introduction


* Link sample spaces and events to data
* Assign a real number to each outcome

---
 
**Definition**: it's a mapping

$$
X:\Omega \rightarrow \mathbb{R}
$$

That assigns a real number $$X(\omega)$$ to each outcome $$omega$$
Note: $$\Omega$$ is the sample space. 

---

Note1:  Sample space is in the background of the random variable. We assign the real number to sample space. 

Note2, my more intuitive understanding: assign each sample to a value, and each sample has some probability to happen, we basically assign each value a probability. When the random variable happen to be some value, it means one or more samples that map to this value occur. 

## Distribution Functions

### CDF 
**Cumulative distribution Function(CDF)**

$$
F_X(x) = \mathbb{P}(X \leq x)
$$

---

### PMF

**Probability Mass Function (PMF)**

Let $$X$$ be discrete (has countably many values).  Definition:

$$
f_X(x) = \mathbb{P}(X=x)
$$

Note: 

$$
F_X(x) = P(X\leq x) = \sum_{x_i \leq x}f_X(x_i)
$$

---

### PDF

R.V. $$X$$ is **Continuous** if there exists a **Probability density function (PDF)**

Let $$f_X(x) \geq 0, \forall x$$

$$
\int_{-\infty}^{\infty}f_X(x)dx = 1
$$

And for every $$ a \leq b$$

$$
\mathbb{P}(a<X<b) = \int_a^b f_X (x) dx
$$


We have that:

$$
F_X(x) = \int_{-\infty}^x f_X(t) dt
$$

and 

$$ 
f_{X} (x) = F'_X \left(x \right) 
$$
