# Bayesian Inference

## The Bayesian Philosophy

**frequentist (or classical)** mehtods: based on the following postulates:

* Probability refers to limiting relative frequencies. Probabilities are objective properties of the real world. 
* Parameters are fixed, unknown constants. -- they are not fluctuating, so no unseful probability statements can be made about parameters
* Statistical procedures should be designed to have well-defined long run frequency properties.

---

Another approach to inference called **Bayesion Inference**

* Probability describes *degree of believe*, not limiting frequency. 
* We can make probability statements about parameters, even though they are fixed constants.
* We make inference about a parameter $$\theta$$ by producing probability distribution for $$\theta$$.

## The Bayesian Method

1. We can choose a probability $$f(\theta)$$ -- **prior** distribution
    * Our belifs about a parameter $$\theta$$ before we see any data.
* We make inference about a parameter $$\theta$$ by producing a probability distribution for $$\theta$$.
* After observign data $$X_1, \dots, X_n$$ we update our beliefs and calculate the **posterior** distribution $$f(\theta|X_1,\dots, X_n)$$

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

If we have IID observation $$X_1, \dots, X_n$$, we replace $$f(x|\theta)$$ with

$$

f(x_1, \dots, x_n|\theta) = \prod_{i=1}^n f(x_i |\theta ) = \mathcal{L}_n(\theta)

$$

---

_**Notation**_:

Let $$X^n$$ be $$(X_1, \dots, X_n)$$

Let $$x^n$$ be $$(x_1,\dots, x_n)$$

