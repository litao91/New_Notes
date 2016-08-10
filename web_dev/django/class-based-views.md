
# Class-based Views

Allow you to **strcture your views and reuse code** by harnessing inheritance nad mixins. 

## Basic Examples

### Simple usage in URLconf

Only changing a few simple attributes on a class-based view => pass them into the `as_view` method call

```python
from django.conf.urls import url
from django.views.generic import TemplateView

urlpatterns = [
    url(r'^about/$', TemplateView.as_view(template_name="about.html")),
]
```

Any arguments passed to `as_view()` will override attributes set on the class.

Another example would be `RedirectView`


## Subclassing generic views
Inherit from an existing view and override attributes (e.g., `template_name`) or methods (e.g., `get_context_data`) in your subclass

```python
# some_app/views.py
from django.views.generic import TemplateView

class AboutView(TemplateView):
    template_name = "about.html"
```

Then in urlconf:

```python
# urls.py
from django.conf.urls import url
from some_app.views import AboutView

urlpatterns = [
    url(r'^about/$', AboutView.as_view()),
]
```


## Intro to Class-Based Views

Advantages:

* Organization of code related to specific HTTP methods (GET, POST, etc.) can be addressed by **separate methods** instead of conditional branching.
* Object oriented techniques such as **mixins (multiple inheritance)** can be used to factor code into **reusable components**.

### Using Class-based views

Respond to differet HTTP request methods with differet class instance methods, instead conditionally branching in code

```python

from django.http import HttpResponse

def my_view(request):
    if request.method == 'GET':
		# <view logic>
		return HttpResponse('result')
```

In class-based:

```python
from django.http import HttpResponse
from django.views.generic import View

class MyView(View):
    def get(self, request):
        # <view logic>
        return HttpResponse('result')
```

`as_view()`: the callable entry point to your class. The `as_view` entry point creates an instance of your class and calls its dispatch() method.

#### Inheritance example

```python
from django.http import HttpResponse
from django.views.generic import View

class GreetingView(View):
    greeting = "Good Day"

    def get(self, request):
        return HttpResponse(self.greeting)
```

Override that in a subclass:

```python
class MorningGreetingView(GreetingView):
    greeting = "Morning to ya"
```

Another option is configure in in `as_view()` call:

```python
urlpatterns = [
    url(r'^about/$', GreetingView.as_view(greeting="G'day")),
]
```


### Using Mixins

Behaviors and attributes of multiple parent classes can be combined

For example, in the generic class-based views there is a mixin called `TemplateResponseMixin` whose primary purpose is to define the method `render_to_response()`. When combined with the behavior of the `View` base class, the result is a `TemplateView` class that will dispatch requests to the appropriate matching methods (a behavior defined in the View base class), and that has a `render_to_response()` method that uses a `template_name` attribute to return a `TemplateResponse` object (a behavior defined in the `TemplateResponseMixin`).


### Decorating in URLConf

```python
from django.contrib.auth.decorators import login_required, permission_required
from django.views.generic import TemplateView

from .views import VoteView

urlpatterns = [
    url(r'^about/$', login_required(TemplateView.as_view(template_name="secret.html"))),
    url(r'^vote/$', permission_required('polls.can_vote')(VoteView.as_view())),
]
```

### Decorating the class
The `method_decorator` decorator transform a function decorator into a method decorator so that it can be used on an instance method. 

```python
from django.contrib.auth.decorators import login_required
from django.utils.decorators import method_decorator
from django.views.generic import TemplateView

class ProtectedView(TemplateView):
    template_name = 'secret.html'

    @method_decorator(login_required)
    def dispatch(self, *args, **kwargs):
        return super(ProtectedView, self).dispatch(*args, **kwargs)
```

You can decorate the class instead and pass the name of hte method to be decorated as the keyword argument name:

```python
@method_decorator(login_required, name='dispatch')
class ProtectedView(TemplateView):
    template_name = 'secret.html'
```

## Example of Mixin with generic Views
```python
from django.http import JsonResponse

class JSONResponseMixin(object):
    """
    A mixin that can be used to render a JSON response.
    """
    def render_to_json_response(self, context, **response_kwargs):
        """
        Returns a JSON response, transforming 'context' to make the payload.
        """
        return JsonResponse(
            self.get_data(context),
            **response_kwargs
        )

    def get_data(self, context):
        """
        Returns an object that will be serialized as JSON by json.dumps().
        """
        # Note: This is *EXTREMELY* naive; in reality, you'll need
        # to do much more complex handling to ensure that arbitrary
        # objects -- such as Django model instances or querysets
        # -- can be serialized as JSON.
        return context
```

```python
from django.views.generic import TemplateView

class JSONView(JSONResponseMixin, TemplateView):
    def render_to_response(self, context, **response_kwargs):
        return self.render_to_json_response(context, **response_kwargs)
```

```python
from django.views.generic.detail import BaseDetailView

class JSONDetailView(JSONResponseMixin, BaseDetailView):
    def render_to_response(self, context, **response_kwargs):
        return self.render_to_json_response(context, **response_kwargs)
```
