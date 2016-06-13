# Bayesian Inference

In short:

$$
f(\theta|x^n) \propto \mathcal{L}(\theta)f(\theta)
$$


* **Prior** -- we have some initial knowledge about the distribution of the parameter $$\theta$$
* **Likelihood** -- $$\mathcal{L}_n(\theta) \equiv f(x_1, ..., x_n|\theta)$$, the probability of drawing the data given the parameter
* **Posterior** -- $$f(\theta|x^n) \propto \mathcal{L}(\theta)f(\theta)$$
 
## The Bayesian Philosophy

**frequentist (or classical)** methods: based on the following postulates:

* Probability refers to limiting relative frequencies. Probabilities are objective properties of the real world. 
* Parameters are fixed, unknown constants. -- they are not fluctuating, so no useful probability statements can be made about parameters
* Statistical procedures should be designed to have well-defined long run frequency properties.

---

Another approach to inference called **Bayesion Inference**

* Probability describes *degree of believe*, not limiting frequency. 
* We can make probability statements about parameters, even though they are fixed constants.
* We make inference about a parameter $$\theta$$ by producing probability distribution for $$\theta$$.

## The Bayesian Method

1. We can choose a probability $$f(\theta)$$ -- **prior** distribution
    * Our beliefs about a parameter $$\theta$$ before we see any data.
* We make inference about a parameter $$\theta$$ by producing a probability distribution for $$\theta$$.
* After observing data $$X_1, ..., X_n$$ we update our beliefs and calculate the **posterior** distribution $$f(\theta|X_1,..., X_n)$$

---

Suppose that $$\theta$$ is discrete.
There is a single, discrete observation $$X$$

Let $$\Theta$$ denote the parameter -- it's a random variable

$$
P(\Theta = \theta | X = x) = \frac{\mathbb{P}(X=x,\Theta = \theta)}{\mathbb{P}(X=x)} =  \frac{\mathbb{P}(X = x|\Theta = \theta)\mathbb{P}(\Theta = \theta)}{\sum_{\theta}\mathbb{P}(X = x|\Theta = \theta)\mathbb{P}(\Theta = \theta)}
$$

---

The version for continuous variables:

$$
f(\theta|x) = \frac{ f(x|\theta)f(\theta) }{ \int{f(x|\theta)f(\theta) d\theta}
}
$$

If we have IID observation $$X_1, ..., X_n$$, we replace $$f(x|\theta)$$ with

$$
f(x_1, ..., x_n|\theta) = \prod_{i=1}^n f(x_i |\theta ) = \mathcal{L}_n(\theta)
$$

---

_**Notation**_:

Let $$X^n$$ be $$(X_1, ..., X_n)$$

Let $$x^n$$ be $$(x_1,..., x_n)$$

Now:

$$
f(\theta|x^n) = \frac{f(x^n|\theta)f(\theta)}{\int f(x^n|\theta)f(\theta)d\theta} = \frac{\mathcal{L}_n(\theta)f(\theta)}{c_n} \propto \mathcal{L}_n(\theta)f(\theta)
$$

Where

$$
c_n = \int \mathcal{L}_n(\theta) f(\theta) d\theta
$$

---
Posterior is proportional to **Likelihood times Prior**

or, in symbols

$$
f(\theta|x^n) \propto \mathcal{L}(\theta)f(\theta)
$$

### What to do with posterior

_**Get a point estimate by summarizing the center of the posterior**_. The posterior mean is

$$
\bar{\theta}_n = \int \theta f(\theta|x^n)d\theta = \frac{\int\theta \mathcal{L}_n(\theta)f(\theta)d\theta}{\int \mathcal{L}_n(\theta) f(\theta) d\theta}
$$

---
_**Obtain a Bayesian interval estimation**_
Find $$a$$ and $$b$$ such that:

$$
\int_{-\infty}^a f(\theta|x^n)d\theta = \int_b^{\infty} f(\theta|x^n)d\theta = \frac{\alpha}{ 2}
$$

Let $$C = (a,b)$$, then

$$
\mathbb{P}(\theta \in C|x^n)=\int_a^b f(\theta | x^n)d\theta = 1-\alpha
$$

## Functions of Parameters

The posterior CDF of $$\tau$$ is:
$$
H(\tau|x^n) = \mathbb{P}(g(\theta) \leq \tau | x^n) = \int_A f(\theta|x^n)d\theta
$$

Where 

$$
A = \{\theta: g(\theta)\leq \tau)\}
$$

The posterior density is:

$$
h(r|x^n) = H'(\tau|x^n)
$$

## Simulation

We draw $$\theta_1, ..., \theta_B \sim P(\theta|x^n)$$

Then a histogram of $$\theta_1, ... \theta_B$$ approximates the posterior density $$p(\theta|x^n)$$

## Large Sample Properties of Bayes' Procedures

**_Theorem_** Let $$\hat{\theta_n}$$ be MLE

$$
    \hat{se} = \frac{ 1 }{ \sqrt{nI(\hat{\theta}_n)}}
$$

$$I$$ -- Fisher information function

Under appropriate regularity conditions, the posterior is approximately Normal with mean $$\hat{\theta}$$ and standard deviation $$\hat{se}$$, Hence

$$\bar{\theta} \approx \hat{\theta}_n$$

If $$\mathcal{C}_n = (\hat{\theta}_n - z_{\alpha/2} \hat{se}, \hat{\theta}_n + z_{\alpha /2 }\hat{se})$$ is asymptotic frequentist $$1-\alpha$$ confidence interval, then $$C_n$$ is also an approximate $$1-\alpha $$ Bayesian posterior interval.

$$
P(\theta\in \mathcal{C}_n |X^n) \rightarrow 1-\alpha
$$

There is also a **Bayesian delta method**. Let $$\tau = g(\theta)$$ then

$$r|X^n \approx N(\hat{r}, \tilde{se}^2)$$

where $$\hat{\tau} = g(\hat{\theta})$$ and $$\tilde{se} = \hat{se}|g'(\hat{\theta})|$$

## Prior

### Improper Prior

**Improper Prior**: e.g., flat prior $$f(\theta) \propto c$$ where $$c > 0$$ is a constant.

Note that $$\int f(\theta) d\theta = \infty$$ ==> improper

Improper prior is not a problem as long as the resulting posterior is a well-defined probability distribution



