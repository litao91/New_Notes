```python
# myproject/myapp/models.py
class MyModel(models.Model):
    class Meta:
        permissions = (
            ('permission_code', 'Friendly permission description'),
        )
Then you can check a if a user has permission like this:

@user_passes_test(lambda u: u.has_perm('myapp.permission_code'))
def some_view(request):
    # ...
```


Where obj is either a User or Group instance. I think it is simpler to write a function for this:

```python
def has_model_permissions( entity, model, perms, app ):
    for p in perms:
        if not entity.has_perm( "%s.%s_%s" % ( app, p, model.__name__ ) ):
            return False
        return True
```

Where entity is the object to check permissions on (Group or User), model is the instance of a model, perms is a list of permissions as strings to check (e.g. ['read', 'change']), and app is the application name as a string. To do the same check as has_perm above you'd call something like this:

```python
result = has_model_permissions( myuser, mycar, ['read'], 'drivers' )
```
