# Concepts of Statistical Inference
## Introduction

Using data to infer the distribution that generated the data. 

A typical questoin:

Given sample $$X_1, ..., X_n \sim F$$, how do we infer $$F$$

## Parametric and Nonparametric Models

* **Statistical model**: $$\mathfrak{F}$$ is a **set of distributions**.
* **Parametric model**: a set $$\mathfrak{F}$$ that can be *parameterized by a finite number of parameters*. 
* **Nonparametric model**: a set $$\mathfrak{F}$$ that cannot be parameterized by a finite number of parameters. 

---

In general a parametric model takes the form

$$
\mathfrak{F} = \left\{ f(x;\theta): \theta \in \Theta \right\}
$$

Where $$\theta$$ is an unknown parameter that can take values in the **parameter spcae $$\Theta$$**

---

* **nuisance parameters**: the component in the parameter that we are not intrested in. 

---

**Regression, prediction, and classification** We observe a pair of data $$(X_1, Y_1),...(X_n, Y_n)$$. 

e.g., $$X_i$$ is the blood pressure of subject $$i$$ and $$Y_i$$ is how long they live. 

* $$X$$ -- **predicator** or **regressor** or **feature** or **independent variable**  
* $$Y$$ -- **outcome** or **response variable** or **dependent variable**
* $$r(x) = \mathbb{E}(Y|X=x)$$ the **regression function**
    * If $$r\in \mathfrak{F}$$ where $$\mathfrak{F}$$ is finite dimensional => **parametric regression model**. 
    * If $$r\in \mathfrak{F}$$ where $$\mathfrak{F}$$ is not finite dimensional, then we have **nonparameteric regression model**
* Predictional of $$Y$$ based on their $$X$$ value is called **prediction**.
* If $$Y$$ is discrete --> **classification**
* If the goal is to estimate the functoin $$r$$ --> **regression** or **curve estimation**.

---

## Examples

* For $$X_1, ..., X_n \sim \text{Bernoulli}(p)$$ -- estimate the parameter $$p$$
* For $$X_1, ..., X_n \sim N(\mu, \sigma)$$ -- estimate the mean and variance.

---

## Fundamental Concepts in Inference

Notation: $$\mathfrak{F} = {f(x;\theta): \theta \in \Theta}$$ is a parametric model.  Write

$$
\mathbb{P}_\theta (X\in A) = \int_A f(x;\theta) dx
$$


$$
\mathbb{E}_\theta(r(X)) = \int r(x) f(x;\theta) dx
$$

The subscript $$\theta$$ indicates that the probability or expectation is with respect to $$f(x;\theta)$$

---

### Point Estimation

To prove a **single "best guess"** of some quantity of interest. 

We denote a point estimation of $$\theta$$ by $$\hat{\theta}$$ or $$\hat{\theta}_n$$.

Note: 
* $$\theta$$ is a fixed, unkonwn quantity. 
* The estimate $$\hat{\theta}$$ depends on the data so $$\hat{\theta}$$ is a random variable. 

---

Formally: 

Let $$X_1, ..., X_n$$ be IID

$$
\hat{\theta}_n = g(X_1, ..., X_n)
$$

The bias of the estimator:


$$
\text{bias}(\hat{\theta}_n) = \mathbb{E}_{\theta}(\hat{\theta}_n)-\theta
$$

$$\hat{\theta}$$ is **unbiased** if $$\mathbb{E}(\hat{\theta}_n) = \theta$$.

--- 

**Definition**. A point estimator $$\hat{\theta}$$ is **consistent** if $$\hat{\theta} \rightarrow^P \theta$$ 

---

* **Sampling Distribution**: Distribution of $$\hat{\theta}_n$$
* **Standard error**: the standard deviation of $$\hat{\theta}_n$$, denoted by $$se$$
* **Mean Squared Error(MSE)**: $$MSE = \mathbb{E}_{\theta}(\hat{\theta}-\theta)^2 = \text{bias}^2(\hat{\theta}) + \mathbb{V}_{\theta}(\hat{\theta})$$
* An estimator is **astmptotically Normal** if $$\frac{\hat{\theta}-\theta}{se} \rightsquigarrow N(0,1)$$

### Confidence Sets

A $$(1-\alpha)$$ **confidence interval** for a parameter $$\theta$$ is an interval $$C_n = (a,b)$$ where $$ a = a(X_1, ..., X_n)$$ and $$b = b(X_1, ..., X_n)$$ are functions of data such that 

$$
\mathbb{P}_{\theta} (\theta \in C_n) \geq 1-\alpha,  \forall \theta \in \Theta
$$

In words, $$(a,b)$$ traps $$\theta$$ with probability $$(1-\alpha)$$. 
We call $$1-\alpha$$ the **converage* of the confidence interval. 

---

Interpretation: it's not a probability statement about $$\theta$$ since $$\theta$$ is a fixed quantity not a random variable. 

On day 1, you collect data and construct a 95 percent confidence
interval for a parameter $$\theta_1$$ . 

On day 2, you collect new data and construct a 95 percent confidence interval for an unrelated parameter $$\theta_2$$

On day 3, you collect new data and construct a 95 percent confidence interval for an unrelated parameter $$\theta_3$$ . 

You continue this way constructing confidence intervals for a sequence of unrelated parameters $$\theta_1,\theta_2, ... $$. 

Then **95 percent of your intervals will trap the true parameter value.**
