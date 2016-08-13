# The Law of Large Numbers and Centa Limit Theorem
## The Law of large numbers

The sample mean of large sample is close to the mean of the distribution. 

Recall:

* Sample Mean: $$\overline{X}_n = n^{-1}\sum_{i=1}^n X_i$$
* Expectation: $$\mathbb{E}(\overline{X}_n) = \mu$$
* Variance: $$\mathbb{V}(\overline{X_n}) = \sigma^/n$$

---

**The Weak Law of Large Numbers (WLLN)**

If $$X_1,..., X_n$$ are IID

$$
\overline{X}_n \rightarrow^P \mu
$$

Interpretation: the distribution of sample mean $$\overline{X}_n$$ becomes more concentrated around $$\mu$$ as $$n$$ gets large. 

It means that the distribution of $$\overline{X}_n$$ piles up near $$\mu$$.

---

## The Centra Limit Theorem (CLT)
Mean $$\mu$$ and variance $$\sigma^2$$. 

The CLT says that $$\overline{X}_n = n^{-1}\sum_i X_i$$ has a distribution which is approximately Normal 

==> The sample mean follows approximately normal distribution !

---

Definition: Let $$X_1, ... X_n$$ be IID, mean $$\mu$$ variance $$\sigma^2$$. $$\overline{X}_n = n^{-1}\sum_{i=1}^n X_i$$. 

$$
Z_n \equiv \frac{\overline{X}_n - \mu}{\sqrt{\mathbb{V}(\overline{X}_n)}} = \frac{\sqrt{n}(\overline{X}_n - \mu)}{\sigma} \rightsquigarrow Z
$$

Where $$Z\sim N(0,1)$$

In other words

$$
\overline{X}_n \approx N(\mu, \frac{\sigma^2}{n})
$$

## The Delta Method

Basically means

$$
Y_n \approx N\left(\mu, \frac{\sigma^2}{n}\right)
$$

implies that 

$$
g(Y_n) \approx N\left(g(\mu), (g'(\mu))^2 \frac{\sigma^2}{n}\right)
$$
