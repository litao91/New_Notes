# Parametric Inference

Models of the form:

$$\mathfrak{F} = \{f(x; \theta): \theta \in \Theta \}$$

Where:

$$\Theta \subset \mathbb{R}^k$$ -- the parameter space

$$\theta = (\theta_1, ..., \theta_k)$$ -- the parameter


## Parameter of Interest

Often we only interested in some function $$T(\theta)$$

e.g., we only interested in $$\mu$$ for $$X\tilde N(\mu, \sigma^2)$$

* $$\mu = T(\theta)$$ -- **the parameter of interest**
* $$\sigma$$ -- a **nuisance** parameter

## Maximum Likelihood

Let $$X_1, ..., X_n$$ -- IID with PDF $$f(x;\theta)$$

---

**Definition(Likelihood function)**

$$
\mathcal{L}_n(\theta) = \prod_(i=1)^n f(X_i;\theta)
$$

**log-likelihood function**

$$\mathcal{l}_n(\theta) = \log \mathcal{L}_n(\theta)$$

---
**Definition(MLE)** The **maximum likelihood estimator** or MLE, denoted by 
$$\hat{\theta}$$, is the value of $$\theta$$ that maximizes $$\mathcal{L}_n(\theta)$$


