# Decorators

## Introduction
This tutorial builds on concepts introduced in this tutorial. If you are not familiar with decorators at all, or get confused by any of the topics I cover here I would suggest you go through the mentioned tutorial and make sure it makes sense to you.

This tutorial aims to introduce some more interesting uses of decorators. Specifically how decorators can be used on classes, and how to pass extra parameters to your decorator functions.

## Decorators vs. The Decorator Pattern
Python decorators are not an implementation of the decorator pattern. Python decorators **add functionality to functions and methods at definition time**, they are not used to add functionality at run time. The decorator pattern itself can be implemented in Python, but it’a a pretty trivial thing because of the fact that Python is duck-typed.

## A Basic Decorator

This is a really basic example of what a decorator can do. I’ve included it just as a reference point. Please make sure you understand this code completely before continuing. If you need more of an explanation of this example then please check out the basic tutorial before you continue.

```python
def time_this(original_function):      
    def new_function(*args,**kwargs):
        import datetime                 
        before = datetime.datetime.now()                     
        x = original_function(*args,**kwargs)                
        after = datetime.datetime.now()                      
        print "Elapsed Time = {0}".format(after-before)      
        return x                                             
    return new_function                                   
    
@time_this
def func_a(stuff):
    import time
    time.sleep(3)

func_a(1)
```

## Decorators that Take Arguments

Sometimes it is useful for decorators to take in arguments besides the function they are decorating. This kind of technique is used often for things like function registration. A famous example of this is view configuration within the Pyramid web application framework. For example:

```python
@view_config(route_name='home', renderer='templates/mytemplate.pt')
def my_view(request):
    return {'project': 'hello decorators'}
```

Let’s say we have a application that users can log into and interact with a nice gui (graphical user interface). The user’s interactions with the gui trigger events that cause Python functions to get executed. Let’s assume that there are lots of users using this application and that they have a bunch of different permission levels. The execution of different functions requires different permission types. For example consider the following functions:



```python
# assume these functions exist
def current_user_id():
    """
    this function returns the current logged in user id, if the user is not authenticated then return None 
    """
    
def get_permissions(iUserId):
    """
    returns a list of permission strings for the given user. For example ['logged_in','administrator','premium_member']
    """
```


### we need to implment permission checking on these functions
    
```python
def delete_user(iUserId):
   """
   delete the user with the given Id. This function is only accessable to users with administrator permissions
   """
   
def new_game():
    """
    any logged in user can start a new game
    """
    
def premium_checkpoint():
   """
   save the game progress, only accessable to premium members
   """
```

One way to implement these permissions would be to make multiple decorators, for example:

```python
def requires_admin(fn):
    def ret_fn(*args,**kwargs):
        lPermissions = get_permissions(current_user_id())
        if 'administrator' in lPermissions:
            return fn(*args,**kwargs)
        else:
            raise Exception("Not allowed")
    return ret_fn

def requires_logged_in(fn):
    def ret_fn(*args,**kwargs):
        lPermissions = get_permissions(current_user_id())
        if 'logged_in' in lPermissions:
            return fn(*args,**kwargs)
        else:
            raise Exception("Not allowed")
    return ret_fn

def requires_premium_member(fn):
    def ret_fn(*args,**kwargs):
        lPermissions = get_permissions(current_user_id())
        if 'premium_member' in lPermissions:
            return fn(*args,**kwargs)
        else:
            raise Exception("Not allowed")
    return ret_fn

@requires_admin
def delete_user(iUserId):
   """
   delete the user with the given Id. This function is only accessable to users with administrator permissions
   """

@requires_logged_in 
def new_game():
    """
    any logged in user can start a new game
    """

@requires_premium_member
def premium_checkpoint():
   """
   save the game progress, only accessable to premium members
   """
```

But that’s pretty horrible. It requires a lot of copy-paste, and each decorator requires a different name, and if any change is made to how permissions are checked then every decorator has to be updated. Wouldn’t it be great to have one decorator that does the job of all three?

To do this we need a function that returns a decorator:

```python
def requires_permission(sPermission):                            
    def decorator(fn):                                            
        def decorated(*args,**kwargs):                            
            lPermissions = get_permissions(current_user_id())     
            if sPermission in lPermissions:                       
                return fn(*args,**kwargs)                         
            raise Exception("permission denied")                  
        return decorated                                          
    return decorator       
    
    
def get_permissions(iUserId): #this is here so that the decorator doesn't throw NameErrors
    return ['logged_in',]

def current_user_id():        #ditto on the NameErrors
    return 1
```


### and now we can decorate stuff...                                     

```python
@requires_permission('administrator')
def delete_user(iUserId):
   """
   delete the user with the given Id. This function is only accessible to users with administrator permissions
   """

@requires_permission('logged_in')
def new_game():
    """
    any logged in user can start a new game
    """
    
@requires_permission('premium_member')
def premium_checkpoint():
   """
   save the game progress, only accessable to premium members
   """
```

Try calling `delete_user`, new_game and premium_checkpoint and see what happens.

Both premium_checkpoint and delete_user raise an Exception with the message “permission denied”, and new_game executes just fine (but doesn’t do much).

Here is the general form of a decorator with arguments, and an illustration of it’s use:

```python
def outer_decorator(*outer_args,**outer_kwargs):                            
    def decorator(fn):                                            
        def decorated(*args,**kwargs):                            
            do_something(*outer_args,**outer_kwargs)                      
            return fn(*args,**kwargs)                         
        return decorated                                          
    return decorator       
    
@outer_decorator(1,2,3)
def foo(a,b,c):
    print a
    print b
    print c


foo()
This is equivalent to:


def decorator(fn):                                            
    def decorated(*args,**kwargs):                            
        do_something(1,2,3)                      
        return fn(*args,**kwargs)                         
    return decorated                                          
return decorator       
    
@decorator
def foo(a,b,c):
    print a
    print b
    print c


foo()
```

## Decorating Classes

Decorators are not limited to acting on functions, they can act on classes as well. Say for example we have a class that does a lot of very important stuff and we want to time everything it does. Then we could use our `time_this` decorator from before like so:

```python
class ImportantStuff(object):
    @time_this
    def do_stuff_1(self):
        ...
    @time_this
    def do_stuff_2(self):
        ...
    @time_this
    def do_stuff_3(self):
        ...
```

That would work just fine. But it’s quite a few extra lines of code within the class. And what if we write some more class methods and forget to decorate one of them? What if we decide we dont want to time the class any more? There is definately space for human error here. It would be much nicer to write it like this:

```python
@time_all_class_methods
class ImportantStuff:
    def do_stuff_1(self):
        ...
    def do_stuff_2(self):
        ...
    def do_stuff_3(self):
        ...
```

As you know by now, that code is equivalent to:


```pythohn
class ImportantStuff:
    def do_stuff_1(self):
        ...
    def do_stuff_2(self):
        ...
    def do_stuff_3(self):
        ...
ImportantStuff = time_all_class_methods(ImportantStuff)
```

So how would `time_all_class_methods` work?
Firstly, we know it needs to take in a class as an argument, and return a class. We also know that the functions of the returned class should look the same as the functions of the original ImportantStuff class. That is, we still want to be able to do our important stuff, we just want to time it as well. And here is how we will do it:

```python
def time_this(original_function):      
    print "decorating"                      
    def new_function(*args,**kwargs):
        print "starting timer"       
        import datetime                 
        before = datetime.datetime.now()                     
        x = original_function(*args,**kwargs)                
        after = datetime.datetime.now()                      
        print "Elapsed Time = {0}".format(after-before)      
        return x                                             
    return new_function  

def time_all_class_methods(Cls):
    class NewCls(object):
        def __init__(self,*args,**kwargs):
            self.oInstance = Cls(*args,**kwargs)
        def __getattribute__(self,s):
            """
            this is called whenever any attribute of a NewCls object is accessed. This function first tries to 
            get the attribute off NewCls. If it fails then it tries to fetch the attribute from self.oInstance (an
            instance of the decorated class). If it manages to fetch the attribute from self.oInstance, and 
            the attribute is an instance method then `time_this` is applied.
            """
            try:    
                x = super(NewCls,self).__getattribute__(s)
            except AttributeError:      
                pass
            else:
                return x
            x = self.oInstance.__getattribute__(s)
            if type(x) == type(self.__init__): # it is an instance method
                return time_this(x)                 # this is equivalent of just decorating the method with time_this
            else:
                return x
    return NewCls


#now lets make a dummy class to test it out on:

@time_all_class_methods
class Foo(object):
    def a(self):
        print "entering a"
        import time
        time.sleep(3)
        print "exiting a"

oF = Foo()
oF.a()
```

## Conclusion

In this tutorial I’ve shown you a few tricks using Python decorators - I’ve shown you how to pass arguments to your decorators, and how to decorate classes. But this is still just the tip of the ice-burg. There are loads of recipes out there for using decorators in all sorts of weird situations. You can even decorate your decorators (but if you ever get to that point it would probably be a good idea to do a sanity check). Python also has a few built in decorators that are worth knowing about, for example staticmethod and classmethod.

Where to go from here? There usually is no need to do anything more complex with decorators than what I’ve shown you in this tutorial. If you are interested in more ways of altering class functionality then I would suggest reading up on inheritence and general OO design principles. Or if you really want to make them dance then read up on metaclasses (but again, dealing with that stuff is hardly ever necessary).
