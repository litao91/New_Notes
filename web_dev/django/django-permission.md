# Django permission

```
https://github.com/lambdalisue/django-permission/
```


## Quick Tutorial

### Usage
In `urls.py`

```python
from django.conf.urls import patterns, include, url
from django.contrib import admin

admin.autodiscover()
# add this line
import permission; permission.autodiscover()

urlpatterns = patterns('',
    url(r'^admin/', include(admin.site.urls)),
    # ...
)
```

In `perm.py`

```python
from permission.logics import AuthorPermissionLogic
from permission.logics import CollaboratorsPermissionLogic

PERMISSION_LOGICS = (
    ('your_app.Article', AuthorPermissionLogic()),
    ('your_app.Article', CollaboratorsPermissionLogic()),
)
```

In `models.py` (applying it)
```python
from django.db import models
from django.contrib.auth.models import User


class Article(models.Model):
    title = models.CharField('title', max_length=120)
    body = models.TextField('body')
    project = models.ForeignKey('permission.Project')

    # this is just required for easy explanation
    class Meta:
        app_label='permission'

class Project(models.Model):
    title = models.CharField('title', max_length=120)
    body = models.TextField('body')
    author = models.ForeignKey(User)

    # this is just required for easy explanation
    class Meta:
        app_label='permission'

# apply AuthorPermissionLogic to Article
from permission import add_permission_logic
from permission.logics import AuthorPermissionLogic
add_permission_logic(Article, AuthorPermissionLogic(
    field_name='project__author',
))
```

### Now it works
```python
user1 = User.objects.create_user(
    username='john',
    email='john@test.com',
    password='password',
)
user2 = User.objects.create_user(
    username='alice',
    email='alice@test.com',
    password='password',
)

art1 = Article.objects.create(
    title="Article 1",
    body="foobar hogehoge",
    author=user1
)
art2 = Article.objects.create(
    title="Article 2",
    body="foobar hogehoge",
    author=user2
)

# You have to apply 'permission.add_article' to users manually because it
# is not an object permission.
from permission.utils.permissions import perm_to_permission
user1.user_permissions.add(perm_to_permission('permission.add_article'))

assert user1.has_perm('permission.add_article') == True
assert user1.has_perm('permission.change_article') == False
assert user1.has_perm('permission.change_article', art1) == True
assert user1.has_perm('permission.change_article', art2) == False

assert user2.has_perm('permission.add_article') == False
assert user2.has_perm('permission.delete_article') == False
assert user2.has_perm('permission.delete_article', art1) == False
assert user2.has_perm('permission.delete_article', art2) == True

#
# You may also be interested in django signals to apply 'add' permissions to the
# newly created users.
# https://docs.djangoproject.com/en/dev/ref/signals/#django.db.models.signals.post_save
#
from django.db.models.signals.post_save
from django.dispatch import receiver
from permission.utils.permissions import perm_to_permission

@receiver(post_save, sender=User)
def apply_permissions_to_new_user(sender, instance, created, **kwargs):
    if not created:
        return
    #
    # permissions you want to apply to the newly created user
    # YOU SHOULD NOT APPLY PERMISSIONS EXCEPT PERMISSIONS FOR 'ADD'
    # in this way, the applied permissions are not object permission so
    # if you apply 'permission.change_article' then the user can change
    # any article object.
    #
    permissions = [
        'permission.add_article',
    ]
    for permission in permissions:
        # apply permission
        # perm_to_permission is a utility to convert string permission
        # to permission instance.
        instance.user_permissions.add(perm_to_permission(permission))
```


Now assume that you add `collabortors` atttribute to store collaborators of the article and you want to give them 
a change permission

=> Apply `CollboratorPermissionLogic` to the `Article` model as follows:


```python
from django.db import models
from django.contrib.auth.models import User


class Article(models.Model):
    title = models.CharField('title', max_length=120)
    body = models.TextField('body')
    author = models.ForeignKey(User)
    collaborators = models.ManyToManyField(User)

    # this is just required for easy explanation
    class Meta:
        app_label='permission'

# apply AuthorPermissionLogic and CollaboratorsPermissionLogic
from permission import add_permission_logic
from permission.logics import AuthorPermissionLogic
from permission.logics import CollaboratorsPermissionLogic
add_permission_logic(Article, AuthorPermissionLogic())
add_permission_logic(Article, CollaboratorsPermissionLogic(
    field_name='collaborators',
    any_permission=False,
    change_permission=True,
    delete_permission=False,
    field_name='project__collaborators',
))
```


### Class, Method or function decorator

Like Django's permission_required but it can be used for object permissions and as a class, method, or function decorator. Also, you don't need to specify a object to this decorator for object permission. This decorator automatically determined the object from request (so you cannnot use this decorator for non view class/method/function but you anyway use user.has_perm in that case).

```python
>>> from permission.decorators import permission_required
>>> # As class decorator
>>> @permission_required('auth.change_user')
>>> class UpdateAuthUserView(UpdateView):
...     pass
>>> # As method decorator
>>> class UpdateAuthUserView(UpdateView):
...     @permission_required('auth.change_user')
...     def dispatch(self, request, *args, **kwargs):
...         pass
>>> # As function decorator
>>> @permission_required('auth.change_user')
>>> def update_auth_user(request, *args, **kwargs):
...     pass
```




## Simple Example
OK. Let say there is an Article model which have 1) title, 2) body, 3) author, 4) published like:

```python
class Article(models.Model):
    title = models.CharField(...)
    body = models.TextField(...)
    author = models.ForeignKey(User, ...)
    published = models.BooleanField(...)
```

And you want to 1) **add**, 2) **change**, 3) **delete**, 4) **publish** the news article. So you need the following permissions.


* `news.add_article` (django's default, Non object permission)
* `news.change_article` (django's default)
* `news.delete_article` (django's default)
* `news.publish_article` (you have to add, Non object permission)



Then, use the followings to do what you want

* `AuthorPermissionLogic`
* `StaffPermissionLogic`

`permission_required` decorator
Like:

```python
class Article(models.Model):
    ...

        from permission import add_permission_logic
        from permission.logics import AuthorPermissionLogic, StaffPermissionLogic
        add_permission_logic(Article, AuthorPermissionLogic(
                    field_name='author',
                    any_permission=False,
                    change_permission=True,
                    delete_permission=True,
                    ))
        add_permission_logic(Article, StaffPermissionLogic(
                    any_permission=False,
                    change_permission=True,
                    delete_permission=True,
                    ))
```


Then use `permission_required` decorator on create, change, delete, publish view like

```python
@permission_required('news.add_article') # It might no be required if anyone can create the article
class ArticleCreateView(CreateView):
    # staff and users (may be everybody?) should have 'news.add_article' permission
    ...

@permission_required('news.change_article')
class ArticleUpdateView(UpdateView):
    # the permission is handled either AuthorPermissionLogic or StaffPermissionLogic
    ...

@permission_required('news.delete_article')
class ArticleDeleteView(DeleteView):
    # the permission is handled either AuthorPermissionLogic or StaffPermissionLogic
    ...

@permission_required('news.publishe_aritcle')
class ArticlePublishView(View):
    # staff users should have 'news.publish_article' permission
    # in the post method, change the 'published' attribute of the specified article
    ....
```
